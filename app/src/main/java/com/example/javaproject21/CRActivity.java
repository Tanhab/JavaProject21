package com.example.javaproject21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CRActivity extends AppCompatActivity {
    private static final String TAG = "CRActivity";
    private Button btnCreateRoutine,btnCreateExam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_r);
        btnCreateRoutine=findViewById(R.id.btnCreateRoutine);
        btnCreateExam=findViewById(R.id.btnCreateExamRoutine);
        btnCreateExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateExamRoutineActivity.class));
            }
        });
        btnCreateRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateClassRoutineActivity.class));
            }
        });


    }
}
