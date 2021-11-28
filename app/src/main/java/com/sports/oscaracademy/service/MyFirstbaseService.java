package com.sports.oscaracademy.service;
//
//public class MyFirebaseMessaginfService {
//}

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sports.oscaracademy.Dashboard;
import com.sports.oscaracademy.MainActivity;
import com.sports.oscaracademy.R;
import com.sports.oscaracademy.homeActivities.news_feeds;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyFirstbaseService extends FirebaseMessagingService {

    boolean isFeed = false;

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("TAG", "onMessageReceived: " + remoteMessage.getData());
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        sendNotification(notification.getTitle(), notification.getBody(), notification.getTag());
        Log.e("TAG", "onMessageReceived: tag " + remoteMessage.getNotification().getTag());
//        Log.e("TAG", "onMessageReceived: " + notification.getBodyLocalizationArgs());
    }

    SharedPreferences preferences;

    private void sendNotification(String title, String messageBody, String tag) {
        isFeed = tag.equals("feed");

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName()
                .equalsIgnoreCase(this.getPackageName())) {
            isActivityFound = true;
            Log.e("TAG", "sendNotification: activity found or not = " + isActivityFound);
        }
        Intent intent;
        if (isActivityFound) {
            if (isFeed) {
                Log.e("TAG", "sendNotification: 1");
                intent = new Intent(this, news_feeds.class);
                intent.putExtra("notification", false);
            } else {
                Log.e("TAG", "sendNotification: 2");
                intent = new Intent(this, Dashboard.class);
                intent.removeExtra("notification");
                intent.putExtra("notification", true);
            }
        } else {

            if (isFeed) {
                Log.e("TAG", "sendNotification: 3");
                intent = new Intent(this, news_feeds.class);
                intent.putExtra("notification", false);
            } else {
                intent = new Intent(this, MainActivity.class);
                Log.e("TAG", "sendNotification: 4");
                intent.putExtra("notification", true);
            }
            // write your code to build a notification.
            // return the notification you built here
        }


//        Log.e("Notification ", "sendNotification: " + isFeed);
//
//        preferences = getSharedPreferences("tokenFile", MODE_PRIVATE);
//
//
//
//        if(MainActivity.isAppOpened){
//            if(isFeed){
//                Log.e("TAG", "sendNotification: 1" );
//                intent = new Intent(this,news_feeds.class);
//            }else{
//                Log.e("TAG", "sendNotification: 2" );
//                intent = new Intent(this,Dashboard.class);
//                intent.putExtra("notification", true);
//                intent.putExtra("isFeed", false);
//            }
//        }else{
//            intent = new Intent(this, MainActivity.class);
//            if(isFeed){
//                Log.e("TAG", "sendNotification: 3" );
//                intent = new Intent(this,news_feeds.class);
//                intent.putExtra("isFeed", true);
//                intent.putExtra("notification", false);
//            }else{
//                Log.e("TAG", "sendNotification: 4" );
//                intent.putExtra("notification", true);
//                intent.putExtra("isFeed", false);
//            }
//        }
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

//        String channelId = getString(R.string.default_notification_channel_id);
        String channelId = "1";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_send)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "General",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}