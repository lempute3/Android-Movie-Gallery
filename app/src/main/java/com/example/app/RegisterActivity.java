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
import com.example.uiutils.UIValidationUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

    private EditText mUsernameInput, mEmailInput, mPasswordInput;
    private Button mLoginSectionBtn, mRegisterBtn;
    private CheckBox mShowPasswordCheck;
    private TextView mForgotPwd;
    private ProgressBar mProgressBar;

    private UISceneSwitcher mLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*ACTIVITIES*/
        mLoginActivity = new UISceneSwitcher(this, LoginActivity.class);

        /*VIEWS*/
        mProgressBar = findViewById(R.id.r_progress_bar);

        /*INPUTS*/
        mUsernameInput = findViewById(R.id.r_username_input);
        mEmailInput = findViewById(R.id.r_email_input);
        mPasswordInput = findViewById(R.id.r_pwd_input);

        /*CHECKS*/
        mShowPasswordCheck = findViewById(R.id.r_show_pwd_check);

        /*BUTTONS*/
        mLoginSectionBtn = findViewById(R.id.r_login_section_btn);
        mRegisterBtn = findViewById(R.id.r_register_btn);

        /*EVENT LISTENERS*/
        mLoginSectionBtn.setOnClickListener(mLoginActivity);
        mShowPasswordCheck.setOnCheckedChangeListener(new UIPasswordToggler(mPasswordInput));
        mRegisterBtn.setOnClickListener(this);

    }

    private void registerUser(String username, String email, String password) {

        /*Validate user info*/
        if (!validate(username, email, password)) { return; }

        /*Starts registration process*/
        mProgressBar.setVisibility(View.VISIBLE);
        firebaseHelper.registerUser("Users", email, username, password, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
                mLoginActivity.setActive();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    private boolean validate(String username, String email, String password) {

        if (username.isEmpty()) {
            mUsernameInput.setError("Username is required!");
            mUsernameInput.requestFocus();
            return false;
        }

        if (!UIValidationUtils.isUsernameLongEnough(username)) {
            mUsernameInput.setError(String.format("Min username length should be %d characters!", UIValidationUtils.MIN_USERNAME_LENGTH));
            mUsernameInput.requestFocus();
            return false;
        }

        if (!UIValidationUtils.isValidUsername(username)) {
            mUsernameInput.setError("Please provide valid username!");
            mUsernameInput.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            mEmailInput.setError("Email is required!");
            mEmailInput.requestFocus();
            return false;
        }

        if (!UIValidationUtils.isValidEmail(email)) {
            mEmailInput.setError("Please provide valid email!");
            mEmailInput.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            mPasswordInput.setError("Password is required!");
            mPasswordInput.requestFocus();
            return false;
        }

        if (!UIValidationUtils.isPasswordLongEnough(password)) {
            mPasswordInput.setError(String.format("Min password length should be %d characters!", UIValidationUtils.MIN_PASSWORD_LENGTH));
            mPasswordInput.requestFocus();
            return false;
        }

        /*TODO: Not working*/
        /*if (!UIValidationUtils.isValidPassword(password)) {
            mPwdInput.setError("Password needs to contain at least one digit, one lowercase letter, ");
            mPwdInput.requestFocus();
            return false;
        }*/

        return true;
    }

    @Override
    public void onClick(View view) {

        /*Parses user info from input fields*/
        String username, email, password;
        username = mUsernameInput.getText().toString().trim();
        email = mEmailInput.getText().toString().trim();
        password = mPasswordInput.getText().toString().trim();

        switch (view.getId()) {
            case R.id.r_register_btn:
                registerUser(username, email, password);
                break;
            default:
                break;
        }
    }
}