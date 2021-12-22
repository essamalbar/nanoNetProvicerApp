package com.crazyiter.nanonetprovicerapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseCloudMessagingService extends FirebaseMessagingService {

    private static final String TOKEN_NAME = "nanonet_provider_fcm";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        SharedPreferences.Editor sharedPreferences = getSharedPreferences(TOKEN_NAME, Context.MODE_PRIVATE).edit();
        sharedPreferences.putString("fcm", s);
        sharedPreferences.apply();

    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TOKEN_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("fcm", null);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sendNotification(
                remoteMessage.getData().get("title"),
                remoteMessage.getData().get("body"),
                remoteMessage.getData().get("ticketId")
        );
    }

    private void sendNotification(String title, String body, String ticketId) {
        Intent intent = new Intent(getApplicationContext(), TicketsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ticketId", ticketId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Tickets", "Tickets", importance);
            channel.setDescription(body);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Tickets");
        builder
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_round_notifications_active)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setChannelId("Tickets")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(new Random().nextInt(), builder.build());
    }

}
