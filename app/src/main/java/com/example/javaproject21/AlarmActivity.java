package com.example.javaproject21;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AlarmActivity extends AppCompatActivity implements AlarmAdapter.CallBack,PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    AlarmAdapter alarmAdapter;
    DataBaseManager dataBaseManager;
    FloatingActionButton btnCreateReminder;
    ImageButton settings,backButton;
    Calendar timeCalender, finalCalender, dateCalender;
    TextView remainderName,dateR,timeR;
    ImageButton dateBtn,timeBtn;
    String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        recyclerView = findViewById(R.id.alarmrecyclerview);
        btnCreateReminder = findViewById(R.id.btnCreateReminder);
        settings = findViewById(R.id.settingBtn);
        backButton= findViewById(R.id.btnBack);
        timeCalender = Calendar.getInstance();
        finalCalender = Calendar.getInstance();
        dateCalender = Calendar.getInstance();
        dataBaseManager = new DataBaseManager(this);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context wrapper = new ContextThemeWrapper(AlarmActivity.this, R.style.popupMenuStyle);
                PopupMenu popup = new PopupMenu(wrapper, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_example, popup.getMenu());
                popup.show();
            }
        });
        initView();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onBackPressed();
            }
        });
       /* btnCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CreateAlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });*/

        btnCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AlarmActivity.this);
                View view = inflater.inflate(R.layout.create_alarm_dialog, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(AlarmActivity.this)
                        .setView(view)
                        .create();
                remainderName = view.findViewById(R.id.txtRemainder);
                dateBtn = view.findViewById(R.id.dateId);
                timeBtn = view.findViewById(R.id.timeId);
                 dateR = view.findViewById(R.id.dateTv);
                timeR = view.findViewById(R.id.timeTv);
                Button addBtn= view.findViewById(R.id.btnAddAlarm);
                Button cancelBtn=view.findViewById(R.id.btnCancel);

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(remainderName.getText().toString().length()<1)
                        {
                            remainderName.setError("Can't be empty");
                        }
                        else if(dateR.getText().toString()=="Date of remainder")
                        {
                            Toast.makeText(AlarmActivity.this, "Please select the date.", Toast.LENGTH_SHORT).show();
                        }
                        else if(timeR.getText().toString()=="Time of remainder")
                        {
                            Toast.makeText(AlarmActivity.this, "Please select the time.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            finalCalender.set(Calendar.HOUR, timeCalender.get(Calendar.HOUR));
                            finalCalender.set(Calendar.MINUTE, timeCalender.get(Calendar.MINUTE));
                            finalCalender.set(Calendar.DATE, dateCalender.get(Calendar.DATE));
                            finalCalender.set(Calendar.MONTH, dateCalender.get(Calendar.MONTH));
                            finalCalender.set(Calendar.YEAR, dateCalender.get(Calendar.YEAR));
                            Calendar test = Calendar.getInstance();
                            if (finalCalender.before(test)) {
                                Toast.makeText(AlarmActivity.this, "Sorry! Can not set alarm for past time", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String name = remainderName.getText().toString();

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


                            alarm.schedule(AlarmActivity.this);
                            dataBaseManager.insert(alarm);
                            Toast.makeText(AlarmActivity.this, "Reminder created", Toast.LENGTH_SHORT).show();
                            updateData();
                            alertDialog.dismiss();

                        }
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                timeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar1 = Calendar.getInstance();
                        int HOUR = calendar1.get(Calendar.HOUR_OF_DAY);
                        int MINUTE = calendar1.get(Calendar.MINUTE);
                        int am_pm = calendar1.get(Calendar.AM_PM);
                        boolean is24HourFormat = DateFormat.is24HourFormat(AlarmActivity.this);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {


                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {


                                timeCalender.set(Calendar.HOUR_OF_DAY, hour);
                                timeCalender.set(Calendar.MINUTE, minute);
                                String startTime = DateFormat.format("h:mm a", timeCalender).toString();



                                timeR.setText(startTime);
                            }
                        }, HOUR, MINUTE, is24HourFormat);

                        timePickerDialog.show();
                    }
                });

                timeR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar1 = Calendar.getInstance();
                        int HOUR = calendar1.get(Calendar.HOUR_OF_DAY);
                        int MINUTE = calendar1.get(Calendar.MINUTE);
                        int am_pm = calendar1.get(Calendar.AM_PM);
                        boolean is24HourFormat = DateFormat.is24HourFormat(AlarmActivity.this);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {


                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {


                                timeCalender.set(Calendar.HOUR_OF_DAY, hour);
                                timeCalender.set(Calendar.MINUTE, minute);
                                String startTime = DateFormat.format("h:mm a", timeCalender).toString();



                                timeR.setText(startTime);
                            }
                        }, HOUR, MINUTE, is24HourFormat);

                        timePickerDialog.show();
                    }
                });
                dateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar1 = Calendar.getInstance();
                        int YEAR = calendar1.get(Calendar.YEAR);
                        int MONTH = calendar1.get(Calendar.MONTH);
                        int DATE = calendar1.get(Calendar.DATE);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(AlarmActivity.this, new DatePickerDialog.OnDateSetListener() {


                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                                //Calendar calendar1 = Calendar.getInstance();
                                dateCalender.set(Calendar.YEAR, year);
                                dateCalender.set(Calendar.MONTH, month);
                                dateCalender.set(Calendar.DATE, date);
                                String dateText = DateFormat.format("EEEE, MMM d, yyyy", dateCalender).toString();
                                dateR.setText(dateText);
                            }
                        }, YEAR, MONTH, DATE);

                        datePickerDialog.show();
                    }
                });

                dateR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar1 = Calendar.getInstance();
                        int YEAR = calendar1.get(Calendar.YEAR);
                        int MONTH = calendar1.get(Calendar.MONTH);
                        int DATE = calendar1.get(Calendar.DATE);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(AlarmActivity.this, new DatePickerDialog.OnDateSetListener() {


                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                                //Calendar calendar1 = Calendar.getInstance();
                                dateCalender.set(Calendar.YEAR, year);
                                dateCalender.set(Calendar.MONTH, month);
                                dateCalender.set(Calendar.DATE, date);
                                String dateText = DateFormat.format("EEEE, MMM d, yyyy", dateCalender).toString();
                                dateR.setText(dateText);
                            }
                        }, YEAR, MONTH, DATE);

                        datePickerDialog.show();
                    }
                }

                );

                alertDialog.show();
            }
        }

        );




    }




    private void initView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        importData();
        // set adapter for recycle view

    }

    private void importData() {
        // if alarmAdapter null it's means data have not imported, yet or database is empty
        if (alarmAdapter == null) {
            // initialize database manager
            dataBaseManager = new DataBaseManager(this);
            // get Alarm ArrayList from database
            ArrayList<Alarm> arrayList = dataBaseManager.getAlarmList();
            Log.d("AlarmMainActivity", "importData: " + arrayList.toString());
            // create Alarm adapter to display detail through RecyclerView
            alarmAdapter = new AlarmAdapter(arrayList, this);

        }
        recyclerView.setAdapter(alarmAdapter);
    }

    private void updateData() {
        ArrayList<Alarm> arrayList = dataBaseManager.getAlarmList();

        alarmAdapter.update(arrayList);
    }

    @Override
    public void deleteAlarm(Alarm alarm) {
        // if user click delete or cancel alarm the pendingIntent also to be canceled by AlarmManager
        // this PendingIntent is canceled based on alarm's ID was set for it, the pendingIntent is
        // going to be canceled must be same with the one was made based on it'id and intent also
        // where the context is.
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // get alarm id
        int alarmId = (int) alarm.getId();
        // create intent
        Intent intent = new Intent(AlarmActivity.this, AlertReceiver.class);
        // this retrieve the pendingIntent was set
        PendingIntent alarmIntent = PendingIntent.getBroadcast(AlarmActivity.this, alarmId, intent, 0);
        // cancel this pendingIntent
        assert alarmManager != null;
        alarmManager.cancel(alarmIntent);
        dataBaseManager.delete(alarmId);
        updateData();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.auto_alarmID:
                return true;
            case R.id.arekta_kichuId:

                return true;
            default:
                return false;
        }
    }
}