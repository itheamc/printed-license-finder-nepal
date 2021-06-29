package com.itheamc.licensefinder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class StorageUtility {

    public static String getBirthDate(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("LICENSE", Context.MODE_PRIVATE);
        return sharedPreferences.getString("dob_ad", null);
    }

    public static String getLicenseNumber(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("LICENSE", Context.MODE_PRIVATE);
        return sharedPreferences.getString("license_number", null);
    }

    public static void setLicenseNumber(Activity activity, String licNo) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("LICENSE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("license_number", licNo);
        editor.apply();
    }

    public static void setBirthDate(Activity activity, int y, int m, int d) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("LICENSE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String month = String.valueOf(m);
        String day = String.valueOf(d);

        if (m < 10) {
            month = "0" + m;
        }
        if (d < 10) {
            day = "0" + d;
        }
        editor.putString("dob_ad", String.format(Locale.ENGLISH,"%d-%s-%s", y, month, day));
        editor.apply();
    }
}
