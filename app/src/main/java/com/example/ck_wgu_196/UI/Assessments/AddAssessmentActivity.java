package com.example.ck_wgu_196.UI.Assessments;

import static com.example.ck_wgu_196.R.string.fill_fields;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Assessment;
import com.example.ck_wgu_196.Entity.Course;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Courses.AddCourseActivity;
import com.example.ck_wgu_196.UI.Courses.CourseActivity;
import com.example.ck_wgu_196.UI.Instructors.InstructorActivity;
import com.example.ck_wgu_196.UI.TermActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddAssessmentActivity extends AppCompatActivity {
    EditText assessmentNameEdit, assessmentStartDateEdit, assessmentEndDateEdit, assessmentTypeEdit;
    private Button saveAssessment;

    Calendar assessmentStartDateCal = Calendar.getInstance();
    Calendar assessmentEndDateCal = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener assessmentStartDateDialog;
    DatePickerDialog.OnDateSetListener assessmentEndDateDialog;

    List<Course> allCourses;
    List<Assessment> allAssessments;

    int courseID;
    int assessmentID;
    String assessmentName;
    String assessmentStartDate;
    String assessmentEndDate;
    String assessmentType;

    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        saveAssessment = findViewById(R.id.addButton_Assessment);

        // get assessment data
        assessmentID = getIntent().getIntExtra("assessmentID", -1);
        assessmentName = getIntent().getStringExtra("title");
        assessmentStartDate = getIntent().getStringExtra("start");
        assessmentEndDate = getIntent().getStringExtra("end");
        assessmentType = getIntent().getStringExtra("kind");
        courseID = getIntent().getIntExtra("courseID", -1);

        // repository instance
        repo = new Repository(getApplication());
        allAssessments = repo.getAllAssessments();
        allCourses = repo.getAllCourses();

        // associate EditText variables with view ID's
        assessmentNameEdit = findViewById(R.id.assessmentNameEdt);
        assessmentStartDateEdit = findViewById(R.id.assessmentDateEdt);
        assessmentEndDateEdit = findViewById(R.id.assessmentDateEdt2);
        assessmentTypeEdit = findViewById(R.id.assessmentTypeEdt);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.my_toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setTitle("Add Assessment");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(AddAssessmentActivity.this, TermActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.view_courses:
                        Intent intent2 = new Intent(AddAssessmentActivity.this, CourseActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.view_instructors:
                        Intent intent3 = new Intent(AddAssessmentActivity.this, InstructorActivity.class);
                        startActivity(intent3);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });

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
                new DatePickerDialog(AddAssessmentActivity.this, assessmentStartDateDialog, assessmentStartDateCal
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
                new DatePickerDialog(AddAssessmentActivity.this, assessmentEndDateDialog, assessmentEndDateCal
                        .get(Calendar.YEAR), assessmentEndDateCal.get(Calendar.MONTH),
                        assessmentEndDateCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /**
         * Save Assessment to database
         */
        saveAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Assessment assessment = new Assessment();
                if (!checkDate(assessmentStartDateEdit.getText().toString(), assessmentEndDateEdit.getText().toString()) ||
                        assessmentNameEdit.getText().toString().isEmpty() ||
                        assessmentStartDateEdit.getText().toString().isEmpty() ||
                        assessmentEndDateEdit.getText().toString().isEmpty() ||
                        assessmentTypeEdit.getText().toString().isEmpty()) {
                    Toast.makeText(AddAssessmentActivity.this, getString(fill_fields), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    assessment = new Assessment(++assessmentID, assessmentNameEdit.getText().toString(),
                            assessmentStartDateEdit.getText().toString(), assessmentEndDateEdit.getText().toString(),
                            courseID, assessmentTypeEdit.getText().toString());
                }
                repo.insert(assessment);
                Intent intent = new Intent(AddAssessmentActivity.this, AssessmentActivity.class);
                startActivity(intent);

            }
        });

}

    /**
     *  Validate start and end date
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

    // inflate toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_toolbar, menu);
        return true;
    }


}