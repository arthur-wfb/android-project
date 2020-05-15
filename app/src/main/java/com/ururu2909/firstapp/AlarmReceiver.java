package com.ururu2909.firstapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String date = intent.getStringExtra("birthDate");
        int day = Integer.parseInt(date.substring(0,2));
        int month = Integer.parseInt(date.substring(3,5));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (calendar.get(Calendar.MONTH) == month-1 && calendar.get(Calendar.DAY_OF_MONTH) == day){
            String text = intent.getStringExtra("text");
            int contactId = intent.getIntExtra("contactId", -1);
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.putExtra("contactId", contactId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Напоминание о дне рождения")
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(intent.getIntExtra("contactId", -1), builder.build());
        }
    }
}