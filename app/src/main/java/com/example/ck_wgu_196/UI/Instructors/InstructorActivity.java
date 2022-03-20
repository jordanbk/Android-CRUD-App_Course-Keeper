package com.example.ck_wgu_196.UI.Instructors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Instructor;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Assessments.AssessmentActivity;
import com.example.ck_wgu_196.UI.Courses.CourseActivity;
import com.example.ck_wgu_196.UI.TermActivity;

import java.util.List;

public class InstructorActivity extends AppCompatActivity {
    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor);

        //start repository instance
        repository = new Repository(getApplication());
        repository.getAllInstructors();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        // set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Instructors");
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.my_toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(InstructorActivity.this, TermActivity.class);
                        startActivity(intent);
                        // start activity 1
                        return true;
                    case R.id.view_courses:
                        Intent intent2 = new Intent(InstructorActivity.this, CourseActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.view_assessments:
                        Intent intent3 = new Intent(InstructorActivity.this, AssessmentActivity.class);
                        startActivity(intent3);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });
        // populate recycler view with instructor data
        final InstructorAdapter adapter = new InstructorAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setInstructors(repository.getAllInstructors());
    }

    //inflate toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_toolbar, menu);

        return true;
    }

    /**
     *  OnClick listener for Edit Instructor button
     * @param view
     */
    public void EditInstructor(View view) {
        Intent intent = new Intent(InstructorActivity.this, InstructorDetailActivity.class);
        startActivity(intent);
    }

}