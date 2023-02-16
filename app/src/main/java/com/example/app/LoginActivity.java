package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbconn.FirebaseHelper;
import com.example.dbconn.OnTaskCompletionListener;
import com.example.uiutils.UIPasswordToggler;
import com.example.uiutils.UISceneSwitcher;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

    private EditText mEmailInput, mPasswordInput;
    private Button mRegisterSectionBtn, mLoginBtn;
    private CheckBox mShowPasswordCheck, mRememberMeCheck;
    private TextView mForgotPasswordBtn;
    private ProgressBar mProgressBar;

    private UISceneSwitcher mMainActivity, mResetActivity, mRegisterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*ACTIVITIES*/
        mMainActivity = new UISceneSwitcher(this, MainActivity.class);
        mResetActivity = new UISceneSwitcher(this, ResetActivity.class);
        mRegisterActivity = new UISceneSwitcher(this, RegisterActivity.class);

        /*VIEWS*/
        mProgressBar = findViewById(R.id.l_progress_bar);

        /*INPUTS*/
        mEmailInput = findViewById(R.id.l_email_input);
        mPasswordInput = findViewById(R.id.l_pwd_input);
        mForgotPasswordBtn = findViewById(R.id.l_forgot_pwd);

        /*CHECKS*/
        mShowPasswordCheck = findViewById(R.id.l_show_pwd_check);
        mRememberMeCheck = findViewById(R.id.l_remember_me_check);

        /*BUTTONS*/
        mRegisterSectionBtn = findViewById(R.id.l_register_section_btn);
        mLoginBtn = findViewById(R.id.l_login_btn);

        /*EVENT LISTENERS*/
        mLoginBtn.setOnClickListener(this);
        mRegisterSectionBtn.setOnClickListener(mRegisterActivity);
        mForgotPasswordBtn.setOnClickListener(mResetActivity);
        mShowPasswordCheck.setOnCheckedChangeListener(new UIPasswordToggler(mPasswordInput));
    }

    void loginUser(String email, String password) {

        /*Starts registration process*/
        mProgressBar.setVisibility(View.VISIBLE);
        firebaseHelper.loginUser(email, password, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
                mMainActivity.setActive();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {

        /*Parses user info from input fields*/
        String email, password;
        email = mEmailInput.getText().toString().trim();
        password = mPasswordInput.getText().toString().trim();

        if (view.getId() == R.id.l_login_btn) {
            mMainActivity.setActive();
            /*loginUser(email, password);*/
        }
    }
}