package com.example.ck_wgu_196.UI.Courses;

import static com.example.ck_wgu_196.R.string.fill_fields;
import static java.security.AccessController.getContext;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Course;
import com.example.ck_wgu_196.Entity.Term;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Assessments.AddAssessmentActivity;
import com.example.ck_wgu_196.UI.Assessments.AssessmentActivity;
import com.example.ck_wgu_196.UI.Instructors.InstructorActivity;
import com.example.ck_wgu_196.UI.TermActivity;
import com.example.ck_wgu_196.UI.TermAdapter;
import com.example.ck_wgu_196.UI.TermDetailActivity;
import com.example.ck_wgu_196.UI.TermViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddCourseActivity extends AppCompatActivity {
    private Button mBtnAddCourse;
    private EditText courseTitleEdit, courseStartEdit,
            courseEndEdit, courseStatusEdit, courseNotesEdit;
    static int id;

    private Button saveCourseBtn;
    Course course = new Course();
    Term termAssocCourse;
    String dateFormat= "MM/dd/yyyy";

    Calendar courseStartCal = Calendar.getInstance();
    Calendar courseEndCal = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener courseStartDate;
    DatePickerDialog.OnDateSetListener courseEndDate;
    List<Course> allCourses;
    List<Term> allTerms;

    int termID;

    private Repository repo;
    int courseID;
    String courseTitle;
    String courseStart;
    String courseEnd;
    String courseStatus;
    String courseNotes;
    Date end;
    Date start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        saveCourseBtn = findViewById(R.id.saveCourseBtn);

        // get course data
        courseID = getIntent().getIntExtra("id", -1);
        courseTitle = getIntent().getStringExtra("title");
        courseStatus = getIntent().getStringExtra("status");
        courseStart = getIntent().getStringExtra("start");
        courseEnd = getIntent().getStringExtra("end");
        courseNotes = getIntent().getStringExtra("notes");
        termID = getIntent().getIntExtra("termID", -1);

        //start repository instance
        repo = new Repository(getApplication());
        allCourses = repo.getAllCourses();
        allTerms = repo.getAllTerms();

        /**
         *  Save course to database
         */
        saveCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course c;
                if (!checkDate(courseStartEdit.getText().toString(), courseEndEdit.getText().toString()) ||
                        courseTitleEdit.getText().toString().trim().isEmpty() ||
                        courseStatusEdit.getText().toString().isEmpty() ||
                        courseStartEdit.getText().toString().trim().isEmpty() ||
                        courseEndEdit.getText().toString().trim().isEmpty() ||
                        courseNotesEdit.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddCourseActivity.this, getString(fill_fields), Toast.LENGTH_LONG).show();
                    return;
                }
                else /*(courseID != -1) */{
/*                    List<Course> allCourses = repo.getAllCourses();
                    courseID = allCourses.get(allCourses.size() - 1).getCourseID();*/
                    c = new Course(++courseID, courseTitleEdit.getText().toString(),
                            courseStatusEdit.getText().toString(),
                            courseStartEdit.getText().toString(), courseEndEdit.getText().toString(),
                            courseNotesEdit.getText().toString(), false, termID);

                }
/*                else {
                    c = new Course(courseID, courseTitleEdit.getText().toString(),
                            courseStatusEdit.getText().toString(),
                            courseStartEdit.getText().toString(), courseEndEdit.getText().toString(),
                            courseNotesEdit.getText().toString(), false, termID);

                }*/
                repo.insert(c);
                Intent intent = new Intent(AddCourseActivity.this, CourseActivity.class);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
            }
        });

        //setup Toolbar options
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.my_toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setTitle("Add Course");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(AddCourseActivity.this, TermActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.view_assessments:
                        Intent intent2 = new Intent(AddCourseActivity.this, AssessmentActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.view_instructors:
                        Intent intent3 = new Intent(AddCourseActivity.this, InstructorActivity.class);
                        startActivity(intent3);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });

        // associating EditText variables with view ID's
        courseTitleEdit = findViewById(R.id.courseNameEdt);
        courseStartEdit = findViewById(R.id.courseStartDateEdt);
        courseEndEdit = findViewById(R.id.courseEndDateEdt);
        courseStatusEdit = findViewById(R.id.courseStatusEdt);
        courseNotesEdit = findViewById(R.id.courseNotesEdt);

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

                courseStartEdit.setText(sdf.format(courseStartCal.getTime()));
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

                courseEndEdit.setText(sdf.format(courseEndCal.getTime()));
            }
        };

        courseStartEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddCourseActivity.this, courseStartDate, courseStartCal
                        .get(Calendar.YEAR), courseStartCal.get(Calendar.MONTH),
                        courseStartCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        courseEndEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddCourseActivity.this, courseEndDate, courseEndCal
                        .get(Calendar.YEAR), courseEndCal.get(Calendar.MONTH),
                        courseEndCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    public void addCourse(View view) {

    }

    // inflate toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_toolbar, menu);
        return true;
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


    public void addCourseToTerm(View view) {

    }

}