package com.example.javaproject21;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class AlarmForegroundService extends Service {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private String channelId="Notification_go";
    private PowerManager.WakeLock wl;
    private AlertReceiver AlarmReceiver;
    DataBaseManager dataBaseManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(sound ==null){
            sound =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }
        dataBaseManager= new DataBaseManager(this);


        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myapp:mywakelocktag");

        mediaPlayer= MediaPlayer.create(this, sound);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(100,100);

        vibrator= (Vibrator) getSystemService(VIBRATOR_SERVICE);
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Class_Update_Alarm",
                    NotificationManager.IMPORTANCE_HIGH);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO: processing on and off ringtone
        // get string from intent
        String on_Off = Objects.requireNonNull(intent.getExtras()).getString("ON_OFF");
        assert on_Off != null;
        switch (on_Off) {
            case Utils.ADD_INTENT: // if string like this set start media
                // this is system default alarm alert uri
                String title= intent.getStringExtra("title");
                int alarmId= intent.getIntExtra("AlarmId",0);
                Intent notificationIntent= new Intent(this, AlarmOffActivity.class);
                notificationIntent.putExtra("title",title);
                notificationIntent.putExtra("AlarmId",alarmId);
                PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);


                wl.acquire(60*1000L /*1 minutes*/);

                Notification notification= new NotificationCompat.Builder(this,channelId)
                        .setContentTitle(title)
                        .setContentText("Starts in 10 minutes")
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .build();
                mediaPlayer.start();

                long[] pattern={0 , 100 , 1000};
                vibrator.vibrate(pattern,0);

                startForeground(1,notification);

                break;
            case Utils.OFF_INTENT:
                // this check if user pressed cancel
                // get the alarm cancel id to check if equal the
                // pendingIntent'trigger id(pendingIntent request code)
                // the AlarmReceiver.pendingIntentId is taken from AlarmReceiver
                // when one pendingIntent trigger
                int aalarmId = intent.getExtras().getInt("AlarmId");
                // check if mediaPlayer created or not and if media is playing and id of
                // alarm and trigger pendingIntent is same  then stop music and reset it
                if (mediaPlayer != null && mediaPlayer.isPlaying() && aalarmId == AlertReceiver.pendingId) {
                    // stop media
                    mediaPlayer.stop();
                    // reset it
                    mediaPlayer.reset();
                    vibrator.cancel();
                    wl.release();
                    dataBaseManager.delete(AlertReceiver.pendingId);

                }
                break;


        }


        return  START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        vibrator.cancel();
        wl.release();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to device notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(channelId, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
