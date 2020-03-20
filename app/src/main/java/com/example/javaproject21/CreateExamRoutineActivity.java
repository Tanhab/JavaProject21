package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class CreateExamRoutineActivity extends AppCompatActivity {
    private static final String TAG = "CreateExamRoutineActivi";
    private EditText edtExamName,edtExamSyllabus,edtExamResource;
    private TextView txtExamDate,txtExamTime;
    private ImageButton btnExamDate,btnExamTime;
    private Button btnSubmit;
    String startTime,startDate;
    long priority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam_routine);
        edtExamName=findViewById(R.id.txtExamNameInput);
        edtExamSyllabus=findViewById(R.id.txtExamSyllabusInput);
        edtExamResource=findViewById(R.id.txtExamResourcesInput);
        txtExamDate=findViewById(R.id.txtExamDate);
        txtExamTime=findViewById(R.id.txtExamTime);
        btnExamDate=findViewById(R.id.btnExamDate);
        btnExamTime=findViewById(R.id.btnExamTime);
        btnSubmit=findViewById(R.id.btnExamRoutineSubmit);
        btnExamTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });
        btnExamDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startTime.length()<1)
                {
                    Toast.makeText(CreateExamRoutineActivity.this, "Please select exam starting time.", Toast.LENGTH_SHORT).show();
                }else if(startDate.length()<1)
                {
                    Toast.makeText(CreateExamRoutineActivity.this, "Please select exam starting date.", Toast.LENGTH_SHORT).show();
                }else if(edtExamName.getText().toString().trim().length()<1){
                    edtExamName.setError("Exam name can not be empty");
                    edtExamName.setFocusable(true);
                }else if(edtExamSyllabus.getText().toString().trim().length()<1){
                    edtExamSyllabus.setError("Exam Syllabus can not be empty");
                    edtExamSyllabus.setFocusable(true);
                }else{
                    createExamRoutine();
                }
            }
        });


    }

    private void createExamRoutine() {
        Log.d(TAG, "createExamRoutine: started");
        String name=edtExamName.getText().toString().trim();
        String syllabus=edtExamSyllabus.getText().toString().trim();
        String resources=edtExamResource.getText().toString().trim();
        ExamRoutine examRoutine= new ExamRoutine(name,startDate,startTime,syllabus,resources,priority);
        FirebaseFirestore.getInstance().collection("cse18").document("ExamRoutines").collection("ExamRoutines")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(examRoutine).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CreateExamRoutineActivity.this, "Exam Schedule Updated.", Toast.LENGTH_SHORT).show();
                edtExamName.setText("");
                edtExamResource.setText("");
                edtExamSyllabus.setText("");
                txtExamDate.setText("Set new Date");
                txtExamTime.setText("Set new Time");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateExamRoutineActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                long a=year*100+month;
                a=a*100+date;
                priority=a;

                startDate = DateFormat.format("dd.MM.yy", calendar1).toString();

                txtExamDate.setText(startDate);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();




    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                startTime = DateFormat.format("h:mm a", calendar1).toString();
                txtExamTime.setText(startTime);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }
}
