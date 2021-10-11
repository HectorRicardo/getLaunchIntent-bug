package com.example.notifpressapp;

import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Assume that, for other reasons, we cannot upgrade to target API Level 31.
 * That means we can't use the new SplashScreen API, so we need to create
 * the splash screen this way
 */
public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d("MyApp-Debug", "SplashActivity.onCreate");

    // Simulate loading for 1.5 seconds
    new Thread(() -> {
      try {
        Thread.sleep(1500);
        startActivity(new Intent(this, MainActivity.class));
        finish();
      } catch (InterruptedException ignored) {
      }
    }).start();
  }
}
