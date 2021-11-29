package com.pucmm.csti.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pucmm.csti.activity.LoginActivity;
import com.pucmm.csti.model.Product;
import com.pucmm.csti.model.Userr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public static final String CARTS = "carts";
    public static final String KEY_QTY = "qty";

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

    public JsonArray getCart() {
        return sortedCart(sharedPreferences.getString(CARTS, "[]"));
    }

    /*
    *         // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER, new Gson().toJson(user));
        // commit changes
        editor.commit();
    * */
    public void addToCart(final Product product, final int qty) {
        new Gson().toJson(product);
    }

    private JsonArray sortedCart(String jsonArrStr) {

        JsonArray jsonArr = new Gson().fromJson(jsonArrStr, JsonArray.class);
        JsonArray sortedJsonArray = new JsonArray();


        List<JsonElement> jsonValues = new ArrayList<JsonElement>();
        for (int i = 0; i < jsonArr.size(); i++) {
            jsonValues.add(jsonArr.get(i));
        }
        Collections.sort(jsonValues, new Comparator<JsonElement>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "itemCode";

            @Override
            public int compare(JsonElement a, JsonElement b) {

                return a.getAsString().compareTo(b.toString());
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < jsonArr.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }

        return sortedJsonArray;

    }
}
