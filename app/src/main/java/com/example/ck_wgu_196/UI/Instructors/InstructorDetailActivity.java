package com.example.ck_wgu_196.UI.Instructors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.ck_wgu_196.UI.Assessments.AssessmentActivity;
import com.example.ck_wgu_196.UI.Courses.CourseActivity;
import com.example.ck_wgu_196.UI.TermActivity;

import java.util.List;

public class InstructorDetailActivity extends AppCompatActivity {

    int instructorID;
    String instructorName;
    String instructorEmail;
    String instructorPhone;
    int courseID;
    private Repository repo;
    Button saveInstructorDT;
    Instructor selectedInstructor;

    EditText instructorNameEdit, instructorEmailEdit, instructorPhoneEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_detail);

        // get instructor data
        instructorID = getIntent().getIntExtra("id", -1);
        instructorName = getIntent().getStringExtra("name");
        instructorEmail = getIntent().getStringExtra("email");
        instructorPhone = getIntent().getStringExtra("phone");
        courseID = getIntent().getIntExtra("courseID_FK", -1);

        // repository instance
        repo = new Repository(getApplication());
        List<Instructor> allInstructors = repo.getAllInstructors();

        // associate EditText variables with view ID's
        instructorNameEdit = findViewById(R.id.instructorNameEdt_dt);
        instructorEmailEdit = findViewById(R.id.instructorEmailEdt_dt);
        instructorPhoneEdit = findViewById(R.id.instructorPhoneEdt_dt);
        saveInstructorDT = findViewById(R.id.saveInstructor_dt);

        // get the selected instructor
        for (Instructor instructor : allInstructors) {
            if (instructor != null) {
                if(instructorID == instructor.getInstructorID()) {
                    selectedInstructor = instructor;
                    instructorName = selectedInstructor.getInstructorName();
                    instructorEmail = selectedInstructor.getInstructorEmail();
                    instructorPhone = selectedInstructor.getInstructorPhone();
                    courseID = selectedInstructor.getCourseID();
                }
            }
        }

        // populate fields with the selected instructors data
        if (instructorID != -1) {
            instructorNameEdit.setText(instructorName);
            instructorEmailEdit.setText(instructorEmail);
            instructorPhoneEdit.setText(instructorPhone);
        }

        //Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_menu);
        toolbar.setTitle("Instructors");
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.settings_menu_instructor);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_instructor:
                        repo.delete(selectedInstructor);
                        Toast.makeText(getApplicationContext(), "Instructor deleted successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(InstructorDetailActivity.this, InstructorActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });

        /**
         * validate data and update instructor
         */
        saveInstructorDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Instructor instructor;
                if(instructorNameEdit.getText().toString().isEmpty() ||
                        instructorEmailEdit.getText().toString().isEmpty() ||
                        instructorPhoneEdit.getText().toString().isEmpty()){
                    Toast.makeText(InstructorDetailActivity.this,"Please fill our all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    instructor = new Instructor(instructorID, instructorNameEdit.getText().toString(),
                            instructorEmailEdit.getText().toString(), instructorPhoneEdit.getText().toString(), courseID);
                }
                repo.update(instructor);
                Toast.makeText(getApplicationContext(), "Instructor Updated!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(InstructorDetailActivity.this, InstructorActivity.class);
                startActivity(intent);
            }
        });
    }

    //Inflate Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);

        return true;
    }

}