package com.example.pillremind.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, @NonNull Intent intent) {
        String pillName = intent.getStringExtra("content");
        Log.d("AlarmReceiver", "onReceive: " + pillName);
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            final String CHANNEL_ID = "201";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Đã đến giờ uống thuốc");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("PillRemind")
                    .setContentText("Đã đến giờ uống thuốc")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM);
            notificationManager.notify(getNotificationID(), builder.build());
            Intent serviceIntent = new Intent(context, AlarmService.class);
            context.startService(serviceIntent);
        }catch (Exception e){
            Log.e("AlarmReceiverError", e.getMessage());
        }
    }

    private int getNotificationID() {
        return (int) System.currentTimeMillis();
    }
}
