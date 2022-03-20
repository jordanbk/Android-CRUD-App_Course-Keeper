package com.example.ck_wgu_196.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Assessment;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Assessments.AssessmentActivity;
import com.example.ck_wgu_196.UI.Courses.CourseActivity;
import com.example.ck_wgu_196.UI.Instructors.InstructorActivity;

public class TermActivity extends AppCompatActivity {
    private Repository repository;
    Button button;
    int termID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        // repository instance
        repository = new Repository(getApplication());
        repository.getAllTerms();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("Terms");
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.my_toolbar);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent = new Intent(TermActivity.this, TermActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.view_courses:
                        Intent intent2 = new Intent(TermActivity.this, CourseActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.view_assessments:
                        Intent intent3 = new Intent(TermActivity.this, AssessmentActivity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.view_instructors:
                        Intent intent4 = new Intent(TermActivity.this, InstructorActivity.class);
                        startActivity(intent4);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });

        // populate recycler view with term data
        final TermAdapter adapter = new TermAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setTerms(repository.getAllTerms());

    }

    // inflate toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_toolbar, menu);

        return true;
    }

    /**
     *  OnClick listener for Add Term button
     * @param view
     */
    public void addTerm(View view) {
        Intent intent = new Intent(TermActivity.this, AddTermActivity.class);
        startActivity(intent);
    }

    /**
     *  OnClick listener for Edit Term button
     * @param view
     */
    public void editTerm(View view) {
        Intent intent = new Intent(TermActivity.this, TermDetailActivity.class);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }
}