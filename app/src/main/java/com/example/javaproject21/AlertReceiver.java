package com.example.javaproject21;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Objects;

public class AlertReceiver extends BroadcastReceiver {
    public static int pendingId;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "onReceive: context for alarm recieved");

        //Intent foreIntent=new Intent(context.getApplicationContext(),AlarmForegroundService.class);
       //// String title=i.getStringExtra("title");
       // foreIntent.setType(i.getType());
        if (intent != null) {
            // this hold information to service
            Intent intentToService = new Intent(context, AlarmForegroundService.class);
            try {
                // getting intent key "intentType"
                String intentType = Objects.requireNonNull(intent.getExtras()).getString("intentType");
                assert intentType != null;
                switch (intentType) {
                    case Utils.ADD_INTENT:
                        // assign pendingId
                        pendingId = intent.getExtras().getInt("PendingId");
                        String title=intent.getStringExtra("title");
                        intentToService.putExtra("AlarmId",pendingId);
                        intentToService.putExtra("title",title);
                        //intentToService.putExtra()
                        intentToService.putExtra("ON_OFF", Utils.ADD_INTENT);

                       // context.startService(intentToService);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(intentToService);
                        } else {
                            context.startService(intentToService);
                        }

                        break;
                    case Utils.OFF_INTENT:
                        // get alarm'id from extras
                        int alarmId = intent.getExtras().getInt("AlarmId");
                        // sending to AlarmService
                        intentToService.putExtra("ON_OFF", Utils.OFF_INTENT);
                        intentToService.putExtra("AlarmId", alarmId);
                        //context.startService(intentToService);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(intentToService);
                        } else {
                            context.startService(intentToService);
                        }

                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*foreIntent.putExtra("title",title);
        startForegroundService(context,foreIntent);*/
    }
}
