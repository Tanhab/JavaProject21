package com.example.javaproject21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CRActivity extends AppCompatActivity {
    private static final String TAG = "CRActivity";
    private CardView cardCreateRoutine,cardCreateExam,cardChangeInvitationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_r);
        cardCreateRoutine=findViewById(R.id.cardCreateClass);
        cardCreateExam=findViewById(R.id.cardCreateExam);
        cardCreateExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateExamRoutineActivity.class));
            }
        });
        cardCreateRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateClassRoutineActivity.class));
            }
        });



    }
}
