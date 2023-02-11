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

public class LoginActivity extends AppCompatActivity {

    private EditText uidInput, pwdInput;
    private Button registerSectionBtn, loginBtn;
    private CheckBox showPwdCheck, rememberMeCheck;
    private TextView forgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*INPUTS*/
        uidInput = findViewById(R.id.l_uidInput);
        pwdInput = findViewById(R.id.l_pwdInput);
        forgotPwd = findViewById(R.id.l_forgotPwd);

        /*CHECKS*/
        showPwdCheck = findViewById(R.id.l_showPwdCheck);
        rememberMeCheck = findViewById(R.id.l_rememberMeCheck);

        /*BUTTONS*/
        registerSectionBtn = findViewById(R.id.l_registerSectionBtn);
        loginBtn = findViewById(R.id.l_loginBtn);

        /*EVENT LISTENERS*/
        registerSectionBtn.setOnClickListener(new UISceneSwitcher(this, RegisterActivity.class));
        showPwdCheck.setOnCheckedChangeListener(new UIPasswordToggler(pwdInput));
    }

}