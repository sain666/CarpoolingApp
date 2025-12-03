package com.carpoolingapp.utils;

// File: CarpoolingApp/app/src/main/java/com/carpooling/app/utils/SharedPrefsHelper.java
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {
    private static final String PREF_NAME = "CarpoolingAppPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_USER_RATING = "rating";
    private static final String KEY_USER_TOTAL_RIDES = "totalRides";
    private static final String KEY_USER_PHONE = "phone";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPrefsHelper(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Save user data
    public void saveUserData(String userId, String userName, String userEmail, String rating, String totalRides, String phone) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_RATING, rating);
        editor.putString(KEY_USER_TOTAL_RIDES, totalRides);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Get user data
    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "User");
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public String getUserRating() { return prefs.getString(KEY_USER_RATING, "0.0"); }
    public String getUserTotalRides() { return prefs.getString(KEY_USER_TOTAL_RIDES, "0"); }
    public String getUserPhone() { return prefs.getString(KEY_USER_PHONE, "XX-XXX-XXXX"); }



    public String getUserType() {
        return prefs.getString(KEY_USER_TYPE, "rider");
    }

    public void setUserType(String userType) {
        editor.putString(KEY_USER_TYPE, userType);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Clear all data (logout)
    public void clearUserData() {
        editor.clear();
        editor.apply();
    }
}