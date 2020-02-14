package com.app.badoli.firebase;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.badoli.R;
import com.app.badoli.activities.HomePageActivites.HomePageActivity;
import com.app.badoli.utilities.LoginPre;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static int UNIQUE_REQUEST_CODE = 0;
   // private NotificationManager notifManager;
    private NotificationChannel mChannel;
    private String TAG=MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(@NotNull String s) {
        super.onNewToken(s);
        Log.e(TAG + "--->NEW_TOKEN: ", s);
        LoginPre.getActiveInstance(getApplicationContext()).setDevice_token(s);
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "OHH--->Called");
        Log.e(TAG, Objects.requireNonNull(remoteMessage.getFrom()));


        String body = new Gson().toJson(remoteMessage.getData());
        try {
            JSONObject object = new JSONObject(body);
            //JSONObject result = object.getJSONObject("result");
            String title = object.getString("result");
            showNotification(title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Notification Response--", body+"\n"
                +new Gson().toJson(remoteMessage.getData())
                +new Gson().toJson(remoteMessage.getNotification()));

       // showChatNotification(to,title, message, data, username, uid, fcm_token,recieverId);
        //Gson gson = new Gson();
        //NotificationResponse response = gson.fromJson(body, NotificationResponse.class);

       /* if (remoteMessage.getData().containsKey("type_notification")) {
            String type =remoteMessage.getData().get("type_notification");
            if (type!=null&&type.equals("chat")){
                String to =remoteMessage.getData().get("to");
                String title =remoteMessage.getData().get("title");
                String message =remoteMessage.getData().get("message");
                String data =remoteMessage.getData().get("data");
                String username =remoteMessage.getData().get("username");
                String uid =remoteMessage.getData().get("uid");
                String fcm_token =remoteMessage.getData().get("fcm_token");
                String recieverId = remoteMessage.getData().get("reciever_id");
                if(!applicationInForeground()) {
                    showChatNotification(to,title, message, data, username, uid, fcm_token,recieverId);
                }else {
                    showChatNotification(to,title, message, data, username, uid, fcm_token,recieverId);
                    sendBroadcastMessage(to,title, message, data, username, uid, fcm_token,recieverId);
                }
            }
        }else {
         //   displayNotification(body);
        }*/
    }

    private void sendBroadcastMessage(String to, String title, String message, String data,
                                      String username, String uid, String fcm_token, String recieverId) {
        Intent broadCastIntent = new Intent("com.wm.welldone.foreground_notification");
        broadCastIntent.putExtra("title_notification",title);
        broadCastIntent.putExtra("message_notification",message);
        broadCastIntent.putExtra("visitUserId",uid);
        broadCastIntent.putExtra("visitUserId",uid);
        broadCastIntent.putExtra("userName",username);
        broadCastIntent.putExtra("deviceToken",to);
        broadCastIntent.putExtra("SenderdeviceToken",fcm_token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent);
    }
    private void showNotification(String title) {


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIDICATION_CHANNEL_ID = "com.app.badoli";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIDICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(getString(R.string.app_name));
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            if (manager != null) {
                manager.createNotificationChannel(notificationChannel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIDICATION_CHANNEL_ID);
        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL | NotificationCompat.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis())
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.logo)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                .setContentInfo("Info")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
       // manager.notify(UNIQUE_REQUEST_CODE++, notificationBuilder.build());
    //    if (manager != null) {
            manager.notify(new Random().nextInt(), builder.build());
      //  }
       // callBuilder();
    }


    private boolean applicationInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = null;
        if (activityManager != null) {
            services = activityManager.getRunningAppProcesses();
        }
        boolean isActivityFound = false;

        if (services.get(0).processName
                .equalsIgnoreCase(getPackageName()) && services.get(0)
                .importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            isActivityFound = true;
        }

        return isActivityFound;
    }



    private void showChatNotification(String to, String title, String message, String data, String username,
                                      String uid, String fcm_token, String recieverId) {
      /*  Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("visitUserId",uid);
        intent.putExtra("userName",username);
        intent.putExtra("deviceToken",to);
        intent.putExtra("SenderdeviceToken",fcm_token);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(new Intent(this, HomePageActivity.class ));
        stackBuilder.addNextIntent(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            PendingIntent pendingIntent;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (mChannel == null) {
                mChannel = new NotificationChannel("chat_101", "chat_channel", importance);
                // mChannel.setDescription(message);//
                mChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableVibration(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

//                notifManager.createNotificationChannel(mChannel);
            }
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
//            pendingIntent = stackBuilder.getPendingIntent(
//                            0,
//                            PendingIntent.FLAG_UPDATE_CURRENT
//                    );

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "chat_101");
            builder.setContentTitle(title)
                    .setDefaults(NotificationCompat.DEFAULT_ALL| NotificationCompat.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(UNIQUE_REQUEST_CODE++, builder.build());

          /*  Notification notification = builder.build();
            notifManager.notify(UNIQUE_REQUEST_CODE++, notification);*/
        } else {
            PendingIntent pendingIntent;
                pendingIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_ONE_SHOT);
          /*  pendingIntent = stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );*/

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"chat_101")
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(UNIQUE_REQUEST_CODE++, notificationBuilder.build());
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("chat_101", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent backIntent = new Intent(this, MainActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("visitUserId",uid);
        intent.putExtra("userName",username);
        intent.putExtra("deviceToken",to);
        intent.putExtra("SenderdeviceToken",fcm_token);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        *//*final PendingIntent pendingIntent = new AtomicReference<>(PendingIntent.getActivities(this, UNIQUE_REQUEST_CODE++,
                new Intent[]{backIntent, intent}, PendingIntent.FLAG_ONE_SHOT));*//*



        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"chat_101")
                .setSmallIcon(R.drawable.chat)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notificationBuilder.build());*/
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logo : R.drawable.logo;
    }

}