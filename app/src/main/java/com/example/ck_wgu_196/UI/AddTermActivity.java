package com.example.ck_wgu_196.UI;

import static com.example.ck_wgu_196.R.string.fill_fields;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Term;
import com.example.ck_wgu_196.R;
import com.example.ck_wgu_196.UI.Assessments.AssessmentActivity;
import com.example.ck_wgu_196.UI.Courses.AddCourseActivity;
import com.example.ck_wgu_196.UI.Courses.CourseActivity;
import com.example.ck_wgu_196.UI.Instructors.InstructorActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTermActivity extends AppCompatActivity {
    private EditText termNameEdt, termStartEdt, termEndEdt;
    private Button addTermBtn;
    Term term = new Term();
    Calendar termStartCal = Calendar.getInstance();
    Calendar termEndCal = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener termStartDate;
    DatePickerDialog.OnDateSetListener termEndDate;
    String dateFormat = "MM/dd/yyyy";


    private Repository Repo;

    int termId;
    String termName;
    String termStart;
    String termEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        // get term data
        termId = getIntent().getIntExtra("termID", -1);
        addTermBtn = findViewById(R.id.addTermBtn);

        //start repository instance
        Repo = new Repository(getApplication());

        /**
         *  validate data and add term
         */
        addTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Term term;
                if (!checkDate(termStartEdt.getText().toString(), termEndEdt.getText().toString()) || termNameEdt.getText().toString().trim().isEmpty() ||
                        termStartEdt.getText().toString().trim().isEmpty() || termEndEdt.getText().toString().isEmpty()) {
                    Toast.makeText(AddTermActivity.this, getString(fill_fields), Toast.LENGTH_LONG).show();
                    return;
                }
/*                else if (termId != -1) {
                    term = new Term(termId, termNameEdt.getText().toString(), termStartEdt.getText().toString(),
                            termEndEdt.getText().toString());
                }*/
                else {
/*                    List<Term> allTerms = Repo.getAllTerms();
                    termId = allTerms.get(allTerms.size() - 1).getTermID();*/
                    term = new Term(++termId, termNameEdt.getText().toString(), termStartEdt.getText().toString(),
                            termEndEdt.getText().toString());
                }
                Repo.insert(term);

                Intent intent = new Intent(AddTermActivity.this, TermActivity.class);
                startActivity(intent);
            }
        });

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
                        Intent intent = new Intent(AddTermActivity.this, TermActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.view_courses:
                        Intent intent2 = new Intent(AddTermActivity.this, CourseActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.view_assessments:
                        Intent intent3 = new Intent(AddTermActivity.this, AssessmentActivity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.view_instructors:
                        Intent intent4 = new Intent(AddTermActivity.this, InstructorActivity.class);
                        startActivity(intent4);
                        return true;
                    default:
                        //default intent
                        return true;
                }
            }
        });


        // associating EditText variables with view ID's
        termNameEdt = findViewById(R.id.termName);
        termStartEdt = findViewById(R.id.termStartDate);
        termEndEdt = findViewById(R.id.termEndDate);

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

                termStartEdt.setText(sdf.format(termStartCal.getTime()));
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

                termEndEdt.setText(format.format(termEndCal.getTime()));
            }

        };

        termStartEdt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddTermActivity.this, termStartDate, termStartCal
                        .get(Calendar.YEAR), termStartCal.get(Calendar.MONTH),
                        termStartCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        termEndEdt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddTermActivity.this, termEndDate, termEndCal
                        .get(Calendar.YEAR), termEndCal.get(Calendar.MONTH),
                        termEndCal.get(Calendar.DAY_OF_MONTH)).show();
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


    // validate Dates
    public static boolean checkDate(String startDate, String endDate){
        try
        {
            String dateFormat= "MM/dd/yyyy";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date endD = sdf.parse(endDate);
            Date startD = sdf.parse(startDate);
            assert endD != null;
            return endD.after(startD);
        }
        catch (Exception e){
            return false;

        }
    }


}