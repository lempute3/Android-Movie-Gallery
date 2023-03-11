package com.example.app.utils;

import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;

public class ValidationUtils {

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_USERNAME_LENGTH = 6;

    public static List<String> isValidUsername(String username) {
        List<String> usernameRequirements = new ArrayList<>();
        String isIllegalChar = "^[a-zA-Z0-9_]*$";

        if (username.length() < MIN_USERNAME_LENGTH)   usernameRequirements.add(String.format("\n- username must be at least %d characters long", MIN_USERNAME_LENGTH));
        if (!username.matches(isIllegalChar))          usernameRequirements.add("\n- username can only contain letters, digits, and underscores (_)");

        return usernameRequirements;
    }

    public static List<String> isValidPassword(String password) {
        List<String> passwordRequirements = new ArrayList<>();
        String isDigit          = "(?=.*[0-9]).*";       // at least 1 digit.
        String isLowerCase      = "(?=.*[a-z]).*";       // at least 1 lower case letter.
        String isUpperCase      = "(?=.*[A-Z]).*";       // at least 1 upper case letter.
        String isSpecialChar    = "(?=.*[@#$%^&+=*]).*"; // at least 1 special character.
        String noWhiteSpace     = "(.*\\S.*)";           // no white spaces.

        if (!password.matches(isDigit))                 passwordRequirements.add("\n- at least 1 digit");
        if (!password.matches(isLowerCase))             passwordRequirements.add("\n- at least 1 lower case letter");
        if (!password.matches(isUpperCase))             passwordRequirements.add("\n- at least 1 upper case letter");
        if (!password.matches(isSpecialChar))           passwordRequirements.add("\n- at least 1 special character");
        if (!password.matches(noWhiteSpace))            passwordRequirements.add("\n- no white spaces");
        if (password.length() < MIN_PASSWORD_LENGTH)    passwordRequirements.add(String.format("\n- password must be at least %d characters!", MIN_PASSWORD_LENGTH));

        return passwordRequirements;
    }

    public static boolean isSearchValid(String query) {
        String isIllegalChar = "^[a-zA-Z0-9_]*$";
        return query.matches(isIllegalChar) ? false : true;
    }

    public static boolean isValidEmail(String email) { return Patterns.EMAIL_ADDRESS.matcher(email).matches(); }
}
