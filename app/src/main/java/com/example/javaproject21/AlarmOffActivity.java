package com.example.javaproject21;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.skyfishjy.library.RippleBackground;


public class AlarmOffActivity extends AppCompatActivity {

    TextView txtTitle,btnStop;
    DataBaseManager dataBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_off);
        btnStop=findViewById(R.id.alarm_stop_tv);
        txtTitle= findViewById(R.id.txtTitle);
        RippleBackground rippleBackground = findViewById(R.id.stopAlarm);
        rippleBackground.startRippleAnimation();
        dataBaseManager= new DataBaseManager(this);
        String title = getIntent().getStringExtra("title");
        final int alarmId =  getIntent().getIntExtra("AlarmId",0);
        Log.d("Alarmid", "AlarmId  =  "+ alarmId);
        if(title!= null)
        {
            txtTitle.setText(title);
        }


        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(),AlarmForegroundService.class);
                intent.putExtra("stop",true);
                intent.putExtra("intentType", Utils.OFF_INTENT);
                // intent.putExtra("AlarmId", (int) alarm.getId());
                getApplicationContext().stopService(intent);
                dataBaseManager.delete(alarmId);
                finish();

            }
        });
    }


}