package com.example.javaproject21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;

public class MyClassroomActivity extends AppCompatActivity {
    private static final String TAG = "MyClassroomActivity";

    private CardView cardLeave,cardClassInfo,cardClassmates,cardTeachers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classroom);
        cardClassInfo=findViewById(R.id.cardClassInfo);
        cardClassmates=findViewById(R.id.cardClassmates);
        cardLeave=findViewById(R.id.cardLeave);
        cardTeachers=findViewById(R.id.cardTeachers);
        cardTeachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cardLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cardClassmates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cardClassInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
