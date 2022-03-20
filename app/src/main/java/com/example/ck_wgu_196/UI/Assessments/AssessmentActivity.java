package com.example.ck_wgu_196.UI.Assessments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Courses.AddCourseActivity;
import com.example.ck_wgu_196.UI.Courses.CourseActivity;
import com.example.ck_wgu_196.UI.Instructors.InstructorActivity;
import com.example.ck_wgu_196.UI.TermActivity;
import com.example.ck_wgu_196.UI.TermAdapter;

public class AssessmentActivity extends AppCompatActivity {
    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        // repository instance
        repository = new Repository(getApplication());
        repository.getAllAssessments();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Assessments");
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.my_toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(AssessmentActivity.this, TermActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.view_courses:
                        Intent intent2 = new Intent(AssessmentActivity.this, CourseActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.view_instructors:
                        Intent intent3 = new Intent(AssessmentActivity.this, InstructorActivity.class);
                        startActivity(intent3);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });

        // populate recycler view with assessment data
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(repository.getAllAssessments());
    }

    // inflate toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_toolbar, menu);

        return true;
    }

    /**
     *  OnClick listener for Add Assessment button
     * @param view
     */
    public void AddAssessment(View view) {
        Intent intent = new Intent(AssessmentActivity.this, AddAssessmentActivity.class);
        startActivity(intent);
    }

    /**
     *  OnClick listener for Edit Assessment button
     * @param view
     */
    public void EditAssessment(View view) {
        Intent intent = new Intent(AssessmentActivity.this, AssessmentDetailActivity.class);
        startActivity(intent);
    }
}