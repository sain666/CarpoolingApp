package com.carpoolingapp.utils;

// File: CarpoolingApp/app/src/main/java/com/carpooling/app/utils/ValidationHelper.java
import android.util.Patterns;

public class ValidationHelper {

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.length() >= 10;
    }

    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static String getEmailError(String email) {
        if (!isNotEmpty(email)) {
            return "Email is required";
        }
        if (!isValidEmail(email)) {
            return "Invalid email address";
        }
        return null;
    }

    public static String getPasswordError(String password) {
        if (!isNotEmpty(password)) {
            return "Password is required";
        }
        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters";
        }
        return null;
    }

    public static String getNameError(String name) {
        if (!isNotEmpty(name)) {
            return "Name is required";
        }
        return null;
    }
}