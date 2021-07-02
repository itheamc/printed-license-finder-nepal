package com.itheamc.licensefinder.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.itheamc.licensefinder.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private ActivitySplashBinding splashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());

        adoptUserTheme();

        new Handler().postDelayed(this::startMainActivity, 1500);
    }

    // Function to start the MainActivity
    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Creating the function to change app theme as per the user setting
     * Dark Mode
     * or Light mode
     */
    private void adoptUserTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}