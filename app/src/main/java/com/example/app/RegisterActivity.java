package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import uiutils.UIPasswordToggler;
import uiutils.UISceneSwitcher;

public class RegisterActivity extends AppCompatActivity {

    private EditText uidInput, pwdInput;
    private Button loginSectionBtn, registerBtn;
    private CheckBox showPwdCheck;
    private TextView forgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*INPUTS*/
        uidInput = findViewById(R.id.r_uidInput);
        pwdInput = findViewById(R.id.r_pwdInput);

        /*CHECKS*/
        showPwdCheck = findViewById(R.id.r_showPwdCheck);

        /*BUTTONS*/
        loginSectionBtn = findViewById(R.id.r_loginSectionBtn);
        registerBtn = findViewById(R.id.r_registerBtn);

        /*EVENT LISTENERS*/
        loginSectionBtn.setOnClickListener(new UISceneSwitcher(this, LoginActivity.class));
        showPwdCheck.setOnCheckedChangeListener(new UIPasswordToggler(pwdInput));

    }
}