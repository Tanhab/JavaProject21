package com.example.javaproject21;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * The class for firebase messaging service.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingServ";
    DataBaseManager dataBaseManager;

    /**
     * The String for Admin channel id.
     */
private final String ADMIN_CHANNEL_ID ="admin_channel";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        dataBaseManager= new DataBaseManager(this);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt(3000);
        Map<String, String> extraData = remoteMessage.getData();

        String category = extraData.get("category");
        // if the notification is for alarm set then only set alarm
        if("alarm".equals(category))
        {
            Log.d(TAG, "alarms detected");
            String s = extraData.get("alarm");
            assert s != null;
            String[] ss = s.split("%%%%");
            Log.d(TAG, "string array =" + Arrays.toString(ss));
            long l = Long.parseLong(ss[1]);
            int id = Integer.parseInt(ss[0]);
            int started = Integer.parseInt(ss[3]);
            Calendar calendar = Calendar.getInstance();
            if(calendar.getTimeInMillis()< l- 5*60*1000) // checking if the alarm time is not in the past
            {
                l= l- 5*60*1000; // setting alarms for 5 min beforehand
                Alarm alarm= new Alarm(id,l,ss[2],started,ss[4]);
                alarm.schedule(this);
                dataBaseManager.insert(alarm);
            }


        }else
        {
            /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels(notificationManager);
            }
            Intent intent;
            intent = new Intent(this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.classroom);
            String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
            String body = remoteMessage.getNotification().getBody();

            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.classroom)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent);

            //Set notification color to match your app color template
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            assert notificationManager != null;
            notificationManager.notify(notificationID, notificationBuilder.build());

        }


    }

    /**
     * This method sets up channels for sending notification.
     *
     * @param notificationManager the notification manager
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to device notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
