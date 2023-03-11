package com.example.app.uiutils;

import android.text.method.PasswordTransformationMethod;
import android.widget.CompoundButton;
import android.widget.EditText;

public class UIPasswordToggler implements CompoundButton.OnCheckedChangeListener {

    private EditText mPwdInput;

    public UIPasswordToggler(EditText pwdInput) {
        mPwdInput = pwdInput;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isPwdChecked) {
        if (isPwdChecked) { mPwdInput.setTransformationMethod(null); }
        else { mPwdInput.setTransformationMethod(new PasswordTransformationMethod()); }
    }
}
