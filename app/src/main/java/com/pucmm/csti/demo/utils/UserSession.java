package com.pucmm.csti.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.pucmm.csti.demo.activity.LoginActivity;
import com.pucmm.csti.demo.model.Userr;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class UserSession {

    // Shared Preferences
    private SharedPreferences sharedPreferences;
    // Editor for Shared preferences
    private SharedPreferences.Editor editor;
    // Context
    private Context context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // SharedPreferences file name
    private static final String PREF_NAME = "userSessionPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "isLoggedIn";

    // User  (make variable public to access from outside)
    private static final String USER = "user";

    public UserSession(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(final Userr user) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER, new Gson().toJson(user));
        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     */
    public Userr getUserSession() {
        final String json = sharedPreferences.getString(USER, "{}");

        return new Gson().fromJson(json, Userr.class);
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent intent = new Intent(context, LoginActivity.class);
            // Closing all the Activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(intent);
        }
    }

    /**
     * Quick check for logout
     **/
    public void logout() {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, false);

        editor.putString(USER, null);
        // commit changes
        editor.commit();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.putBoolean(IS_LOGIN, false);
        editor.clear();
        editor.commit();
    }
}
