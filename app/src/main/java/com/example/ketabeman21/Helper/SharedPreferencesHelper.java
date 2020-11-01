package com.example.ketabeman21.Helper;

import android.content.Context;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;

public class SharedPreferencesHelper {



    static {
        System.loadLibrary("native-lib");
    }

    public native String stringFromJNI();


    private EncryptedPreferences encryptedPreferences;
    private static SharedPreferencesHelper instance;
    private static final String PREFS_NAME = "default_preferences";

    public synchronized static SharedPreferencesHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    private SharedPreferencesHelper(Context context) {
        encryptedPreferences = new EncryptedPreferences.Builder(context).withEncryptionPassword(stringFromJNI()).build();
    }

    public String getString(String key) {
        return encryptedPreferences.getString(key, null);
    }

    public void putString(String key, String value) {
        encryptedPreferences.edit()
                .putString(key, value)
                .apply();
    }

    public void setBoolean(String key, boolean value) {
        encryptedPreferences.edit()
                .putBoolean(key, value)
                .apply();
    }

    public void clearAll() {
        encryptedPreferences.edit().clear().apply();
    }


    public boolean isLoggedIn() {
        return encryptedPreferences.getBoolean("LOGGED", false);
//        return true;
    }

    public void setLoggedIn(boolean value) {
//        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
//                .edit().putBoolean("LOGGED", value).apply();

        encryptedPreferences.edit().putBoolean("LOGGED", value).apply();
    }
}
