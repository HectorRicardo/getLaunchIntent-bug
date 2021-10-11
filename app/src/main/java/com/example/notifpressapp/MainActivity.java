package com.example.notifpressapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

  private static final String CHANNEL_ID = "channelId";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Log.d("MyApp-Debug", "MainActivity.onCreate");
    postNotification();
  }

  private void postNotification() {
    // Create a notification channel if API level >= 26
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      getSystemService(NotificationManager.class).createNotificationChannel(
          new NotificationChannel(CHANNEL_ID, "Channel", NotificationManager.IMPORTANCE_DEFAULT));
    }

    // create a notification
    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentIntent(createNotificationIntent())
        .build();

    // post it in the notification manager
    NotificationManagerCompat.from(this).notify(0, notification);
  }

  /** Create the intent that will be run when the notification is pressed */
  private PendingIntent createNotificationIntent() {
    Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
    int pendingIntentFlags = VERSION.SDK_INT < VERSION_CODES.M ? 0 : PendingIntent.FLAG_IMMUTABLE;
    return PendingIntent.getActivity(this, 0, intent, pendingIntentFlags);
  }
}
