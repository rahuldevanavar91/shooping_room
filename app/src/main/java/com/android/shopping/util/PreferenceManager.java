package com.android.shopping.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    public static PreferenceManager mInstance;
    private static final Object lock = new Object();
    private final SharedPreferences mUserPref;

    public static PreferenceManager getInstance(Context applicationContext) {
        if (mInstance == null) {
            synchronized (lock) {
                if (mInstance == null) {
                    mInstance = new PreferenceManager(applicationContext);
                }
            }
        }
        return mInstance;
    }


    public static void setUseLogIn(Context context, boolean value) {
        PreferenceManager.getInstance(context).mUserPref.edit().putBoolean("is_user_login", value).apply();
    }

    public static boolean isUserLoggedIn(Context context) {
        return PreferenceManager.getInstance(context).mUserPref.getBoolean("is_user_login", false);
    }

    private PreferenceManager(Context applicationContext) {
        mUserPref = applicationContext.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
    }

    public static void setDataLoad(Context context) {
        PreferenceManager.getInstance(context).mUserPref.edit().putBoolean("is_data_loaded", true).apply();
    }

    public static boolean getIsDataLoaded(Context context) {
        return PreferenceManager.getInstance(context).mUserPref.getBoolean("is_data_loaded", false);
    }


}
