package com.example.javaproject21;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity implements AlarmAdapter.CallBack {

    RecyclerView recyclerView;
    AlarmAdapter alarmAdapter;
    DataBaseManager dataBaseManager;
    Button btnCreateReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        recyclerView = findViewById(R.id.alarmrecyclerview);
        btnCreateReminder= findViewById(R.id.btnCreateReminder);
        btnCreateReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(),CreateAlarmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        initView();
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
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}