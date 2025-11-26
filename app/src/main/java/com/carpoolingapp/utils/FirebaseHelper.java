package com.carpoolingapp.utils;

// File: CarpoolingApp/app/src/main/java/com/carpooling/app/utils/FirebaseHelper.java

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public DatabaseReference getDatabase() {
        return mDatabase;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public String getCurrentUserId() {
        FirebaseUser user = getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }

    // Database References
    public DatabaseReference getUsersRef() {
        return mDatabase.child("users");
    }

    public DatabaseReference getRidesRef() {
        return mDatabase.child("rides");
    }

    public DatabaseReference getBookingsRef() {
        return mDatabase.child("bookings");
    }

    public DatabaseReference getMessagesRef() {
        return mDatabase.child("messages");
    }

    public DatabaseReference getRatingsRef() {
        return mDatabase.child("ratings");
    }

    public DatabaseReference getUserRef(String userId) {
        return getUsersRef().child(userId);
    }

    public DatabaseReference getRideRef(String rideId) {
        return getRidesRef().child(rideId);
    }

    public DatabaseReference getBookingRef(String bookingId) {
        return getBookingsRef().child(bookingId);
    }
}