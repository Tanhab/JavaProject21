package com.example.javaproject21;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class Alarm {
    private static final String TAG = "Alarm";
    private int Id, isStarted;
    private String title,timeText;
    private long alarmTime;

    public Alarm(int id, long alarmTime , String title, int isStarted, String timeText) {
        Id = id;
        this.isStarted = isStarted;
        this.title = title;
        this.alarmTime = alarmTime;
        this.timeText=timeText;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public Alarm() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(int isStarted) {
        this.isStarted = isStarted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }

    @Override
    public String toString() {
        return Id +
                "%%%%" + alarmTime +
                "%%%%" + title +
                "%%%%" + isStarted +
                "%%%%" + timeText;
    }
    public void schedule(Context context)
    {
        Log.d(TAG, "schedule called for"+title);
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent= new Intent(context, AlertReceiver.class);
        intent.putExtra("title",title);
        // put intent type to check which intent trigger add or cancel
        intent.putExtra("intentType", Utils.ADD_INTENT);
        // put id to intent
        intent.putExtra("PendingId", getId());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,getId(),intent,0);
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(alarmTime);

        Log.d(TAG, "schedule: compare :"+calendar.getTimeInMillis() +"  " +System.currentTimeMillis());
        if(calendar.getTimeInMillis()<=System.currentTimeMillis())
        {
            //do something for ager time
            Toast.makeText(context, "Sorry! Can not set alarm for past time."+ DateFormat.format("h:mm a", calendar).toString(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "schedule: past time! sorry cant execute");
        }else
        {
            assert alarmManager != null;
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),pendingIntent),pendingIntent);
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            Log.d(TAG, "schedule: "+"Alarm set for "+ DateFormat.format("h:mm a", calendar).toString());
            //Toast.makeText(context, "Alarm set for "+ DateFormat.format("h:mm a", calendar).toString(), Toast.LENGTH_SHORT).show();
        }


    }

    public void cancelAlarm(Context context)
    {
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent= new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(context, Id,intent,0);
        alarmManager.cancel(pendingIntent);
        this.isStarted=0;

        Toast.makeText(context, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }
    public void sendIntent(Context context)
    {
        // intent1 to send to AlarmReceiver
        Intent intent1 = new Intent(context, AlertReceiver.class);
        // put intent type Constants.ADD_INTENT or Constants.OFF_INTENT
        intent1.putExtra("intentType", Utils.OFF_INTENT);
        // put alarm'id to compare with pendingIntent'id in AlarmService
        intent1.putExtra("AlarmId", Id);
        // this sent broadCast right a way
        context.sendBroadcast(intent1);
    }
}
