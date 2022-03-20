package com.example.ck_wgu_196.UI.Instructors;

import static com.example.ck_wgu_196.R.string.fill_fields;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Instructor;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Assessments.AddAssessmentActivity;
import com.example.ck_wgu_196.UI.Assessments.AssessmentActivity;
import com.example.ck_wgu_196.UI.Courses.AddCourseActivity;
import com.example.ck_wgu_196.UI.Courses.CourseActivity;
import com.example.ck_wgu_196.UI.TermActivity;

public class AddInstructorActivity extends AppCompatActivity {

    int instructorID;
    String instructorName;
    String instructorEmail;
    String instructorPhone;
    int courseID;

    Button saveInstructor;

    private Repository repo;

    private EditText instructorNameEdit, instructorPhoneEdit,
    instructorEmailEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instructor);
        saveInstructor = findViewById(R.id.saveButton);

        // get instructor data
        instructorID = getIntent().getIntExtra("instructorID", -1);
        instructorName = getIntent().getStringExtra("name");
        instructorEmail = getIntent().getStringExtra("email");
        instructorPhone = getIntent().getStringExtra("phone");
        courseID = getIntent().getIntExtra("courseID", -1);

        // start repository instance
        repo = new Repository(getApplication());

        // associating EditText variables with view ID's
        instructorNameEdit = findViewById(R.id.instructorNameEdt);
        instructorEmailEdit = findViewById(R.id.instructorEmailEdt);
        instructorPhoneEdit = findViewById(R.id.instructorPhoneEdt);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.my_toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setTitle("Add Instructor");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(AddInstructorActivity.this, TermActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.view_courses:
                        Intent intent2 = new Intent(AddInstructorActivity.this, CourseActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.view_assessments:
                        Intent intent3 = new Intent(AddInstructorActivity.this, AssessmentActivity.class);
                        startActivity(intent3);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });


/**
 *  Add instructor
 */
        saveInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instructor instructor;
                if(instructorNameEdit.getText().toString().isEmpty() ||
                        instructorEmailEdit.getText().toString().isEmpty() ||
                instructorPhoneEdit.getText().toString().isEmpty()){
                    Toast.makeText(AddInstructorActivity.this,"Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    instructor = new Instructor(++instructorID, instructorNameEdit.getText().toString(),
                            instructorEmailEdit.getText().toString(), instructorPhoneEdit.getText().toString(), courseID);
                }
                repo.insert(instructor);
                Intent intent = new Intent(AddInstructorActivity.this, InstructorActivity.class);
                startActivity(intent);

            }
        });
    }

    // inflate toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_toolbar, menu);
        return true;
    }

}