package com.example.uiutils;

import android.util.Patterns;

public class UIValidationUtils {

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_USERNAME_LENGTH = 6;

    public static boolean isValidUsername(String username) {
        String pattern = "^[a-zA-Z0-9._-]+$";
        return username.matches(pattern);
    }

    public static boolean isValidPassword(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }

    public static boolean isValidEmail(String email) { return Patterns.EMAIL_ADDRESS.matcher(email).matches(); }
    public static boolean isPasswordLongEnough(String password) { return password.length() >= MIN_PASSWORD_LENGTH; }
    public static boolean isUsernameLongEnough(String username) { return username.length() >= MIN_USERNAME_LENGTH; }

}
