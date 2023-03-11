package com.example.app.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;

public class RememberMe {

    private static final String PREFS_NAME = "appPrefFile";
    private static final String KEY_REMEMBER_ME = "rememberMe";
    private static final String KEY_AUTH_TOKEN = "userAuthToken";

    private SharedPreferences mUserDetails;
    private String mEmail, mPassword;

    public RememberMe() {}

    public RememberMe(Context context, CheckBox checkBox) {
        mUserDetails = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked1) -> saveState(isChecked1));
        checkBox.setChecked(getState());
    }

    private void saveUser(String authToken) {
        mUserDetails.edit()
                .putString(KEY_AUTH_TOKEN, authToken)
                .apply();
    }

    private void removeUser() {
        mUserDetails.edit()
                .remove(KEY_AUTH_TOKEN)
                .apply();
    }

    private void saveState(boolean isChecked) {
        mUserDetails.edit()
                .putBoolean(KEY_REMEMBER_ME, isChecked)
                .apply();
    }

    public String getUserToken() {
        return mUserDetails.getString(KEY_AUTH_TOKEN, null);
    }

    private boolean getState() {
        return mUserDetails.getBoolean(KEY_REMEMBER_ME, false);
    }

}
