package com.itheamc.licensefinder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class StorageUtil {
    private static final String TAG = "StorageUtil";
    private static StorageUtil instance;
    private Activity activity;
    private final SharedPreferences sharedPreferences;

    // Constructor
    public StorageUtil(@NonNull Activity activity) {
        this.activity = activity;
        this.sharedPreferences = activity.getSharedPreferences("LicenseFinder", Context.MODE_PRIVATE);
    }

    // Instance
    public static StorageUtil getInstance(@NonNull Activity activity) {
        if (instance == null) {
            instance = new StorageUtil(activity);
        }
        return instance;
    }

    // Function to store date
    public void storeDate(long _time_in_milliseconds) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("past_time", _time_in_milliseconds);
        editor.apply();
    }

    // Function to store date
    public long getDate() {
        return sharedPreferences.getLong("past_time", 0);
    }


    // Function to set active status
    public void setActive(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("active_status", status);
        editor.apply();
    }

    // Function to store app status
    public boolean isActive() {
        return sharedPreferences.getBoolean("active_status", false);
    }
}
