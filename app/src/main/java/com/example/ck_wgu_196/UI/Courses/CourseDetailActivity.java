package com.example.ck_wgu_196.UI.Courses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Assessment;
import com.example.ck_wgu_196.Entity.Course;
import com.example.ck_wgu_196.Entity.Instructor;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Assessments.AddAssessmentActivity;
import com.example.ck_wgu_196.UI.Assessments.AssessmentAdapter;
import com.example.ck_wgu_196.UI.Assessments.AssessmentDetailActivity;
import com.example.ck_wgu_196.UI.Instructors.AddInstructorActivity;
import com.example.ck_wgu_196.UI.Instructors.InstructorAdapter;
import com.example.ck_wgu_196.UI.MainActivity;
import com.example.ck_wgu_196.Util.Receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CourseDetailActivity extends AppCompatActivity {
    private Repository repo;
    Course course;
    int courseID;
    String courseTitle;
    String courseStatus;
    String courseStart;
    String courseEnd;
    String courseNotes;
    Boolean courseAlert;
    int termID_FK;
    public static int numAssessments;
    public static int numInstructors;
    Course selectedCourse;
    public static int alertNumber;
    Button saveCourse;
    EditText editCourseTitle;
    EditText editCourseStatus;
    EditText editCourseStart;
    EditText editCourseEnd;
    EditText editCourseNotes;
    String sdf;
    String start;
    Calendar courseStartCal = Calendar.getInstance();
    Calendar courseEndCal = Calendar.getInstance();
    String dateFormat= "MM/dd/yyyy";
    DatePickerDialog.OnDateSetListener courseStartDate;
    DatePickerDialog.OnDateSetListener courseEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        //course = (Course) getIntent().getSerializableExtra("Course");
        saveCourse = findViewById(R.id.saveCourseBtn);

        // start repository instance
        repo = new Repository(getApplication());
        List<Course> allCourses = repo.getAllCourses();

        // get current courseID
        courseID = getIntent().getIntExtra("courseID", -1);

        // aet selected course details
        for(Course course : allCourses){
            if(course.getCourseID() == courseID){
                selectedCourse = course;
                courseTitle = selectedCourse.getCourseTitle();
                courseStatus = selectedCourse.getCourseStatus();
                courseStart = selectedCourse.getCourseStart();
                courseEnd = selectedCourse.getCourseEnd();
                courseNotes = selectedCourse.getCourseNotes();
                courseAlert = false;
                termID_FK = selectedCourse.getTermID_FK();
            }
        }

        // associating EditText variables with view ID's
        editCourseTitle = findViewById(R.id.courseNameEdt);
        editCourseStatus = findViewById(R.id.courseStatusEdt);
        editCourseStart = findViewById(R.id.courseStartDateEdt);
        editCourseEnd = findViewById(R.id.courseEndDateEdt);
        editCourseNotes = findViewById(R.id.courseNotesEdt);

        // populate fields with the selected courses data
        if(courseID != -1){
            editCourseTitle.setText(courseTitle);
            editCourseStatus.setText(courseStatus);
            editCourseStart.setText(courseStart);
            editCourseEnd.setText(courseEnd);
            editCourseNotes.setText(courseNotes);
        }

        /**
         *  validate data and update course
         */
        saveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Course course;
                if (!checkDate(editCourseStart.getText().toString(), editCourseEnd.getText().toString()) || editCourseTitle.getText().toString().trim().isEmpty() || editCourseStatus.getText().toString().trim().isEmpty() ||
                        editCourseStart.getText().toString().isEmpty() || editCourseEnd.getText().toString().trim().isEmpty() ||
                        editCourseNotes.getText().toString().trim().isEmpty()){
                    Toast.makeText(CourseDetailActivity.this, getString(R.string.fill_out_fields_date_check), Toast.LENGTH_LONG).show();
                    return;
                }
/*        if (courseID != -1)
            course = new Course(courseID, editCourseTitle.getText().toString(), editCourseStatus.getText().toString(),
                    editCourseStart.getText().toString(), editCourseEnd.getText().toString(), editCourseNotes.getText().toString(),
                    false, termID_FK);*/
                else {
/*            List<Course> allCourses = repo.getAllCourses();
            courseID = allCourses.get(allCourses.size() - 1).getCourseID();*/
                    course = new Course(courseID, editCourseTitle.getText().toString(), editCourseStatus.getText().toString(),
                            editCourseStart.getText().toString(), editCourseEnd.getText().toString(), editCourseNotes.getText().toString(),
                            false, termID_FK);
                }
                repo.update(course);
                Toast.makeText(getApplicationContext(), "Course Updated!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CourseDetailActivity.this, CourseActivity.class);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
            }
        });

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_menu);
        toolbar.setTitle("Course Details");
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.settings_menu_course);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @SuppressLint({"NonConstantResourceId", "SimpleDateFormat"})
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_course:
                        if (numAssessments == 0 || numInstructors == 0) {
                            Course course = new Course(courseID, editCourseTitle.getText().toString(),
                                    editCourseStatus.getText().toString(), editCourseStart.getText().toString(),
                                    editCourseEnd.getText().toString(), editCourseNotes.getText().toString(), courseAlert, termID_FK);
                            repo.delete(course);
                            Toast.makeText(getApplicationContext(), "Course deleted successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CourseDetailActivity.this, CourseActivity.class);
                            intent.putExtra("courseID", courseID);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Cannot delete course with associated assessments or instructors", Toast.LENGTH_LONG).show();
                        }
                        return true;

                    case R.id.share:
                        Intent intentShare = new Intent();
                        intentShare.setAction(Intent.ACTION_SEND);
                        intentShare.putExtra(Intent.EXTRA_TEXT, courseNotes);
                        intentShare.putExtra(Intent.EXTRA_TITLE, "My Course Notes");
                        intentShare.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(intentShare, "Share notes to...");
                        startActivity(shareIntent);
                        return true;

                    case R.id.startAlert:
                        String assessmentDate = editCourseStart.getText().toString();
                        String assessmentTitle = editCourseTitle.getText().toString();
                        String dateFormat = "MM/dd/yy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                        Date date = null;
                        try {
                            date=simpleDateFormat.parse(assessmentDate);
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                        assert date != null;
                        long trigger = date.getTime();
                        Intent intent1 = new Intent(CourseDetailActivity.this , Receiver.class);
                        intent1.putExtra("key", assessmentTitle + " begins today: " + assessmentDate);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(CourseDetailActivity.this, ++MainActivity.alertNumber,intent1,0 );
                        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger,pendingIntent);
                        return true;
                    case R.id.endAlert:
                        String assessmentEndDate = editCourseEnd.getText().toString();
                        String assessmentTitle2 = editCourseTitle.getText().toString();
                        String dateFormat2 = "MM/dd/yy";
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(dateFormat2, Locale.US);
                        Date date2 = null;
                        try {
                            date2 = simpleDateFormat2.parse(assessmentEndDate);
                        } catch (ParseException e){
                            e.printStackTrace();
                        }
                        assert date2 != null;
                        long trigger2 = date2.getTime();
                        Intent intent2 = new Intent(CourseDetailActivity.this , Receiver.class);
                        intent2.putExtra("key", assessmentTitle2 + " ends today: " + assessmentEndDate);
                        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(CourseDetailActivity.this, ++MainActivity.alertNumber,intent2,0 );
                        AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                        alarmManager2.set(AlarmManager.RTC_WAKEUP, trigger2,pendingIntent2);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });

        // initialize calendar object for start date
        courseStartDate = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                courseStartCal.set(Calendar.YEAR, year);
                courseStartCal.set(Calendar.MONTH, month);
                courseStartCal.set(Calendar.DAY_OF_MONTH, day);
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
                showStartDate();
            }

            private void showStartDate() {
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

                editCourseStart.setText(sdf.format(courseStartCal.getTime()));
            }
        };

        // initialize calendar object for end date
        courseEndDate = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                courseEndCal.set(Calendar.YEAR, year);
                courseEndCal.set(Calendar.MONTH, month);
                courseEndCal.set(Calendar.DAY_OF_MONTH, day);
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
                showEndDate();
            }

            private void showEndDate() {
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

                editCourseEnd.setText(sdf.format(courseEndCal.getTime()));
            }
        };

        editCourseStart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CourseDetailActivity.this, courseStartDate, courseStartCal
                        .get(Calendar.YEAR), courseStartCal.get(Calendar.MONTH),
                        courseStartCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editCourseEnd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CourseDetailActivity.this, courseEndDate, courseEndCal
                        .get(Calendar.YEAR), courseEndCal.get(Calendar.MONTH),
                        courseEndCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


/**
 *  populate recycler view with associated assessments (based on courseID)
 */
        repo = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.associatedAssessments);
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Assessment> associateAssessments = new ArrayList<>();
        List<Assessment> allAssessments = repo.getAllAssessments();
        for(Assessment a : repo.getAllAssessments()){
            if(a.getCourseID_FK()==courseID){
                associateAssessments.add(a);
            }
        }
        adapter.setAssessments(associateAssessments);
        numAssessments = associateAssessments.size();

/**
 *  populate recycler view with associated instructors (based on courseID)
 */
        repo  = new Repository(getApplication());
        RecyclerView rv = findViewById(R.id.associatedInstructors);
        final InstructorAdapter adapter2 = new InstructorAdapter(this);
        rv.setAdapter(adapter2);
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<Instructor> associatedInstructors = new ArrayList<>();
        //List<Instructor> allInstructors = repo.getAllInstructors();
        for (Instructor i : repo.getAllInstructors()){
            if(i.getCourseID() == courseID){
                associatedInstructors.add(i);
            }
        }
        adapter2.setInstructors(associatedInstructors);
        numInstructors = associatedInstructors.size();

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
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date endD = sdf.parse(endDate);
            Date startD = sdf.parse(startDate);
            if(endD.after(startD))
                return true;
            else
                return false;
        }
        catch (Exception e){
            return false;

        }
    }

    /**
     *  OnClick listener for Add Assessment button
     * @param view
     */
    public void AddAssessment(View view) {
        Intent intent = new Intent(CourseDetailActivity.this, AddAssessmentActivity.class);
        intent.putExtra("courseID", courseID);
        startActivity(intent);
    }

    /**
     * OnClick listener for Add Instructor button
     * @param view
     */
    public void AddInstructor(View view) {
        Intent intent = new Intent(CourseDetailActivity.this, AddInstructorActivity.class);
        //intent.putExtra("courseID", courseID);
        intent.putExtra("courseID", selectedCourse.getCourseID());
        startActivity(intent);
    }
}