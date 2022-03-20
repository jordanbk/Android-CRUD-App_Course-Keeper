package com.example.ck_wgu_196.UI.Courses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Instructor;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.AddTermActivity;
import com.example.ck_wgu_196.UI.Assessments.AssessmentActivity;
import com.example.ck_wgu_196.UI.Instructors.InstructorActivity;
import com.example.ck_wgu_196.UI.TermActivity;

public class CourseActivity extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        // repository instance
        repository = new Repository(getApplication());
        repository.getAllCourses();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Courses");
        toolbar.inflateMenu(R.menu.my_toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(CourseActivity.this, TermActivity.class);
                        startActivity(intent);
                        // start activity 1
                        return true;
                    case R.id.view_assessments:
                        Intent intent2 = new Intent(CourseActivity.this, AssessmentActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.view_instructors:
                        Intent intent3 = new Intent(CourseActivity.this, InstructorActivity.class);
                        startActivity(intent3);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });

        // populate recycler view with course data
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(repository.getAllCourses());
    }

    /**
     *  OnClick listener for Add Course button
     * @param view
     */
    public void AddCourse(View view) {
        Intent intent = new Intent(CourseActivity.this, AddCourseActivity.class);
        startActivity(intent);
    }

    /**
     *  OnClick listener for Edit Course button
     * @param view
     */
    public void editCourse(View view) {
        Intent intent = new Intent(CourseActivity.this, CourseDetailActivity.class);
        startActivity(intent);
    }
}