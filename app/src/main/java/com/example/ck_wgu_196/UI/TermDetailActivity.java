package com.example.ck_wgu_196.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Course;
import com.example.ck_wgu_196.Entity.Term;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Courses.AddCourseActivity;
import com.example.ck_wgu_196.UI.Courses.CourseActivity;
import com.example.ck_wgu_196.UI.Courses.CourseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TermDetailActivity extends AppCompatActivity {

    private Repository repo;

    int termID;
    String termName;
    String termStart;
    String termEnd;
    private static int numCourses;
    EditText updateName;
    EditText updateStart;
    EditText updateEnd;

    Term termSelected;

    Calendar termStartCal = Calendar.getInstance();
    Calendar termEndCal = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener termStartDate;
    DatePickerDialog.OnDateSetListener termEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        // get term data
        termID = getIntent().getIntExtra("termID", -1);

        // repository instance
        repo = new Repository(getApplication());
        List<Term> allTerms = repo.getAllTerms();

        // get selected term
        for(Term term : allTerms) {
            if(term.getTermID() == termID) {
                termSelected = term;
                termName = termSelected.getTermName();
                termStart = termSelected.getTermStart();
                termEnd = termSelected.getTermEnd();
            }
        }

        // associate editText variables with view ID's
        updateName = findViewById(R.id.termNameDetailEdt);
        updateStart = findViewById(R.id.termStartDateEdt);
        updateEnd = findViewById(R.id.termEndDateEdt);

        // populate fields with selected terms data
        if(termID!=-1) {
            updateName.setText(termName);
            updateStart.setText(termStart);
            updateEnd.setText(termEnd);
        }

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_menu);
        toolbar.setTitle("Terms");
        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.settings_menu);
        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_term:
                        if (numCourses == 0) {
                            Term term = new Term(termID, updateName.getText().toString(),
                                    updateStart.getText().toString(),updateEnd.getText().toString());
                            repo.delete(term);
                            Intent intent = new Intent(TermDetailActivity.this, TermActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Cannot delete terms with associated courses", Toast.LENGTH_LONG).show();
                        }
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });

        // initialize calendar object for start date
        termStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                termStartCal.set(Calendar.YEAR, year);
                termStartCal.set(Calendar.MONTH, month);
                termStartCal.set(Calendar.DAY_OF_MONTH, day);
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);

                showStartDate();
            }

            private void showStartDate() {
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

                updateStart.setText(sdf.format(termStartCal.getTime()));
            }

        };

        // initialize calendar object for end date
        termEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                termEndCal.set(Calendar.YEAR, year);
                termEndCal.set(Calendar.MONTH, month);
                termEndCal.set(Calendar.DAY_OF_MONTH, day);
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);

                showEndDate();
            }

            private void showEndDate() {
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);

                updateEnd.setText(format.format(termEndCal.getTime()));
            }

        };

        updateStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(TermDetailActivity.this, termStartDate, termStartCal
                        .get(Calendar.YEAR), termStartCal.get(Calendar.MONTH),
                        termStartCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        updateEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(TermDetailActivity.this, termEndDate, termEndCal
                        .get(Calendar.YEAR), termEndCal.get(Calendar.MONTH),
                        termEndCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // populate recyclerview with associated courses
        repo = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.associatedCourses);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> associateCourses = new ArrayList<>();
        List<Course> allCourses = repo.getAllCourses();
        for(Course c : repo.getAllCourses()){
            if(c.getTermID_FK() == termID){
                associateCourses.add(c);
            }
        }
        courseAdapter.setCourses(associateCourses);
        numCourses = associateCourses.size();
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

    // inflate toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    /**
     *  validate data and update term
     * @param view
     */
    public void saveTerm(View view) {
        Term term;
        if (!checkDate(updateStart.getText().toString(), updateEnd.getText().toString()) ||
                updateStart.getText().toString().trim().isEmpty() ||
                updateEnd.getText().toString().trim().isEmpty() || updateEnd.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields and make the start date is before the end date", Toast.LENGTH_LONG).show();
            return;
        }/*
        else if (termID != -1)
            term = new Term(termID, updateName.getText().toString(),
                    updateStart.getText().toString(),updateEnd.getText().toString());*/
        else {
/*            List<Term> allTerms = repo.getAllTerms();
            termID = allTerms.get(allTerms.size() - 1).getTermID();*/
            term = new Term(termID, updateName.getText().toString(), updateStart.getText().toString(),
                    updateEnd.getText().toString());
        }

        repo.update(term);
        Toast.makeText(getApplicationContext(), "Term updated!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(TermDetailActivity.this,TermActivity.class);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }


    /**
     *  OnClick listener for Add Term button
     * @param view
     */
    public void AddCourse(View view) {
        Intent intent = new Intent(TermDetailActivity.this, AddCourseActivity.class);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }
}