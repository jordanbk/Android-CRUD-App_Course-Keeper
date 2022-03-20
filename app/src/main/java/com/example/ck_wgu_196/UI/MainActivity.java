package com.example.ck_wgu_196.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.example.ck_wgu_196.Database.Repository;
import com.example.ck_wgu_196.Entity.Course;
import com.example.ck_wgu_196.Entity.Term;
import com.example.ck_wgu_196.R;

public class MainActivity extends AppCompatActivity {

    public static int alertNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // start repository instance
        Repository repo = new Repository(getApplication());

    }

    public void termActivity(View view) {
        Intent intent = new Intent(MainActivity.this, TermActivity.class);
        startActivity(intent);

    }

}