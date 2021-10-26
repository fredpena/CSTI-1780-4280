package com.pucmm.csti.clazz;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class PrefManager {

    // Shared Preferences
    private SharedPreferences sharedPreferences;
    // Editor for Shared preferences
    private SharedPreferences.Editor editor;
    // Context
    private Context context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // SharedPreferences file name
    private static final String PREF_NAME = "Pref.Manager";


    public PrefManager(Context context) {
        this.context = context;

        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void putStringSet(String key, Set<String> values) {
        editor.putStringSet(key, values);
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
    }

    public void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
    }

    public <T> T get(String key) {
        return (T) sharedPreferences.getString(key, null);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
    }

    public void commit() {
        editor.commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }
}
