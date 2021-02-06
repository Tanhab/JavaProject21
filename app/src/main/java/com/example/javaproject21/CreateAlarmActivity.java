package com.example.javaproject21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class CreateAlarmActivity extends AppCompatActivity {
    private static final String TAG = "CreateAlarmActivity";
    Button btnDate, btnTime, btnAdd, btnNotification;
    EditText txtAlarmName;
    TextView txtDate, txtTime;
    String s = "";
    Calendar timeCalender, finalCalender, dateCalender;
    private DataBaseManager dataBaseManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        btnAdd = findViewById(R.id.btnAddAlarm);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtAlarmName = findViewById(R.id.edtAlarmName);
        timeCalender = Calendar.getInstance();
        finalCalender = Calendar.getInstance();
        dateCalender = Calendar.getInstance();
        dataBaseManager = new DataBaseManager(this);


        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlarm();
            }
        });

    }

    private void handleDateButton() {
        Calendar calendar1 = Calendar.getInstance();
        int YEAR = calendar1.get(Calendar.YEAR);
        int MONTH = calendar1.get(Calendar.MONTH);
        int DATE = calendar1.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                //Calendar calendar1 = Calendar.getInstance();
                dateCalender.set(Calendar.YEAR, year);
                dateCalender.set(Calendar.MONTH, month);
                dateCalender.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", dateCalender).toString();
                Log.d(TAG, "onDateSet: " + dateText);
                txtDate.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();


    }

    private void handleTimeButton() {
        Calendar calendar1 = Calendar.getInstance();
        int HOUR = calendar1.get(Calendar.HOUR_OF_DAY);
        int MINUTE = calendar1.get(Calendar.MINUTE);
        int am_pm = calendar1.get(Calendar.AM_PM);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {


            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + " : " + minute);

                timeCalender.set(Calendar.HOUR_OF_DAY, hour);
                timeCalender.set(Calendar.MINUTE, minute);
                String startTime = DateFormat.format("h:mm a", timeCalender).toString();


                Log.d(TAG, "onTimeSet: " + startTime);
                txtTime.setText(startTime);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

    }

    private void createAlarm() {
        finalCalender.set(Calendar.HOUR, timeCalender.get(Calendar.HOUR));
        finalCalender.set(Calendar.MINUTE, timeCalender.get(Calendar.MINUTE));
        finalCalender.set(Calendar.DATE, dateCalender.get(Calendar.DATE));
        finalCalender.set(Calendar.MONTH, dateCalender.get(Calendar.MONTH));
        finalCalender.set(Calendar.YEAR, dateCalender.get(Calendar.YEAR));
        Calendar test = Calendar.getInstance();
        if (finalCalender.before(test)) {
            Toast.makeText(this, "Sorry! Can not set alarm for past time", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = txtAlarmName.getText().toString();
        if (name.length() == 0) {
            Toast.makeText(this, "Name can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        int MONTH = finalCalender.get(Calendar.MONTH);
        int DATE = finalCalender.get(Calendar.DATE);
        int HOUR = finalCalender.get(Calendar.HOUR);
        int MINUTE = finalCalender.get(Calendar.MINUTE);
        int alarmId = MONTH;
        alarmId = alarmId * 100 + DATE;
        alarmId = alarmId * 100 + HOUR;
        alarmId = alarmId * 100 + MINUTE;

        long l = finalCalender.getTimeInMillis();
        s = DateFormat.format("EEEE, MMM d, yyyy", finalCalender).toString() + " " + DateFormat.format("h:mm a", finalCalender).toString();

        Alarm alarm = new Alarm(alarmId, l, name, 1, s);

        Log.d(TAG, "createAlarm: creating " + s);
        alarm.schedule(this);
        dataBaseManager.insert(alarm);
        Toast.makeText(this, "Reminder created", Toast.LENGTH_SHORT).show();
        txtAlarmName.setText("");
        txtDate.setText("");
        txtTime.setText("");

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,AlarmActivity.class));
    }
}
