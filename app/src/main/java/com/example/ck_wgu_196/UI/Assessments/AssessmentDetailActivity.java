package com.example.ck_wgu_196.UI.Assessments;

import static com.example.ck_wgu_196.R.string.fill_fields;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Assessment;
import com.example.ck_wgu_196.Entity.Course;
import com.example.ck_wgu_196.Entity.Term;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Courses.CourseActivity;
import com.example.ck_wgu_196.UI.Courses.CourseDetailActivity;
import com.example.ck_wgu_196.UI.MainActivity;
import com.example.ck_wgu_196.UI.TermActivity;
import com.example.ck_wgu_196.UI.TermDetailActivity;
import com.example.ck_wgu_196.Util.Receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssessmentDetailActivity extends AppCompatActivity {
    private Repository repo;
    Button saveAssessment;

    int assessmentID;
    String assessmentTitle;
    String assessmentStartDate;
    String assessmentEndDate;
    String assessmentType;
    int courseID_FK;

    Calendar assessmentStartDateCal = Calendar.getInstance();
    Calendar assessmentEndDateCal = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener assessmentStartDateDialog;
    DatePickerDialog.OnDateSetListener assessmentEndDateDialog;

    EditText assessmentTitleEdit;
    EditText assessmentStartDateEdit;
    EditText assessmentEndDateEdit;
    EditText assessmentTypeEdit;

    Assessment selectedAssessment;

    Calendar assessmentCal = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener assessmentDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        // get assessment data
        assessmentID = getIntent().getIntExtra("assessmentID", -1);
        assessmentTitle = getIntent().getStringExtra("title");
        assessmentStartDate = getIntent().getStringExtra("start");
        assessmentEndDate = getIntent().getStringExtra("end");
        assessmentType = getIntent().getStringExtra("kind");
        courseID_FK = getIntent().getIntExtra("courseID_FK", -1);

        // repository instance
        saveAssessment = findViewById(R.id.buttonSaveAssessment);
        repo = new Repository(getApplication());
        List<Assessment> allAssessments = repo.getAllAssessments();

        // Set selected assessment
        for(Assessment assessment : allAssessments){
            if(assessment != null){
                if(assessmentID == assessment.getAssessmentID()) {
                    selectedAssessment = assessment;
                    assessmentTitle = selectedAssessment.getAssessmentTitle();
                    assessmentStartDate = selectedAssessment.getAssessmentStartDate();
                    assessmentEndDate = selectedAssessment.getAssessmentEndDate();
                    assessmentType = selectedAssessment.getAssessmentKind();
                    courseID_FK = selectedAssessment.getCourseID_FK();
                }
            }
        }

        // associate EditText variables with view ID's
        assessmentTitleEdit = findViewById(R.id.assessmentNameEdt);
        assessmentStartDateEdit = findViewById(R.id.assessmentStartDateEdt);
        assessmentEndDateEdit = findViewById(R.id.assessmentEndDateEdt);
        assessmentTypeEdit = findViewById(R.id.assessmentTypeEdt_dt);

        // populate fields with the selected assessments data
        if (assessmentID != -1){
            assessmentTitleEdit.setText(assessmentTitle);
            assessmentStartDateEdit.setText(assessmentStartDate);
            assessmentEndDateEdit.setText(assessmentEndDate);
            assessmentTypeEdit.setText(assessmentType);
        }

        // initialize calendar object for start date
        assessmentStartDateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                assessmentStartDateCal.set(Calendar.YEAR, year);
                assessmentStartDateCal.set(Calendar.MONTH, month);
                assessmentStartDateCal.set(Calendar.DAY_OF_MONTH, day);
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
                showStartDate();
            }

            private void showStartDate() {
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

                assessmentStartDateEdit.setText(sdf.format(assessmentStartDateCal.getTime()));
            }
        };

        assessmentStartDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AssessmentDetailActivity.this, assessmentStartDateDialog, assessmentStartDateCal
                        .get(Calendar.YEAR), assessmentStartDateCal.get(Calendar.MONTH),
                        assessmentStartDateCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // initialize calendar object for end date
        assessmentEndDateDialog = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                assessmentEndDateCal.set(Calendar.YEAR, year);
                assessmentEndDateCal.set(Calendar.MONTH, month);
                assessmentEndDateCal.set(Calendar.DAY_OF_MONTH, day);
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
                showStartDate();
            }

            private void showStartDate() {
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

                assessmentEndDateEdit.setText(sdf.format(assessmentEndDateCal.getTime()));
            }
        };

        assessmentEndDateEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AssessmentDetailActivity.this, assessmentEndDateDialog, assessmentEndDateCal
                        .get(Calendar.YEAR), assessmentEndDateCal.get(Calendar.MONTH),
                        assessmentEndDateCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /**
         *  validate data and update assessment
         */
        saveAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Assessment assessment;
                if (!checkDate(assessmentStartDateEdit.getText().toString(), assessmentEndDateEdit.getText().toString()) ||
                        assessmentTitleEdit.getText().toString().trim().isEmpty() || assessmentStartDateEdit.getText().toString().trim().isEmpty() ||
                        assessmentEndDateEdit.getText().toString().trim().isEmpty() ||
                        assessmentTypeEdit.getText().toString().isEmpty()){
                    Toast.makeText(AssessmentDetailActivity.this, getString(fill_fields), Toast.LENGTH_LONG).show();
                    return;
                }
/*        if (assessmentID != -1)
            assessment = new Assessment(assessmentID, assessmentTitleEdit.getText().toString(), assessmentStartDateEdit.getText().toString(),
                    assessmentEndDateEdit.getText().toString(),
                    courseID_FK, assessmentTypeEdit.getText().toString());*/
                else {
/*            List<Assessment> allAssessments = repo.getAllAssessments();
            assessmentID = allAssessments.get(allAssessments.size() - 1).getAssessmentID();*/
                    assessment = new Assessment(assessmentID, assessmentTitleEdit.getText().toString(), assessmentStartDateEdit.getText().toString(),
                            assessmentEndDateEdit.getText().toString(), courseID_FK, assessmentTypeEdit.getText().toString());
                }
                repo.update(assessment);
                Toast.makeText(getApplicationContext(), "Assessment Updated!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AssessmentDetailActivity.this, AssessmentActivity.class);
                startActivity(intent);
            }
        });

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_menu);
        toolbar.setTitle("Assessment Details");
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.settings_menu_assessment);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_assessment_dt:
                            repo.delete(selectedAssessment);
                            Toast.makeText(getApplicationContext(), "Assessment deleted successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AssessmentDetailActivity.this, AssessmentActivity.class);
                            startActivity(intent);
                        return true;
                    case R.id.setAssessmentStartAlert:
                        String assessmentDate = assessmentStartDateEdit.getText().toString();
                        String assessmentTitle = assessmentTitleEdit.getText().toString();
                        String dateFormat = "MM/dd/yy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                        Date date = null;
                        try {
                            date=simpleDateFormat.parse(assessmentDate);
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                        Long trigger = date.getTime();
                        Intent intent1 = new Intent(AssessmentDetailActivity.this , Receiver.class);
                        intent1.putExtra("key", assessmentTitle + " begins today!");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AssessmentDetailActivity.this, ++MainActivity.alertNumber,intent1,0 );
                        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger,pendingIntent);
                        return true;
                    case R.id.setAssessmentEndAlert:
                        String assessmentEndDate = assessmentEndDateEdit.getText().toString();
                        String assessmentEndTitle = assessmentTitleEdit.getText().toString();
                        String dateFormat2 = "MM/dd/yy";
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(dateFormat2, Locale.US);
                        Date date2 = null;
                        try {
                            date2 = simpleDateFormat2.parse(assessmentEndDate);
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                        Long trigger2 = date2.getTime();
                        Intent intent2 = new Intent(AssessmentDetailActivity.this , Receiver.class);
                        intent2.putExtra("key", assessmentEndTitle + " ends today!");
                        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(AssessmentDetailActivity.this, ++MainActivity.alertNumber, intent2,0 );
                        AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        alarmManager2.set(AlarmManager.RTC_WAKEUP, trigger2,pendingIntent2);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });

    }

    /**
     *  Validate start and end dates
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean checkDate(String startDate, String endDate){
        try
        {
            String dateFormat= "MM/dd/yyyy";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date endD = sdf.parse(endDate);
            Date startD = sdf.parse(startDate);
            return endD.after(startD);
        }
        catch (Exception e){
            return false;

        }
    }
}