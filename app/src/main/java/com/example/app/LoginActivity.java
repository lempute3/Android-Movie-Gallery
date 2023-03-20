package com.example.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.firebase.FirebaseRepository;
import com.example.app.firebase.OnTaskCompletionListener;
import com.example.app.uiutils.UIActivitySwitcher;
import com.example.app.uiutils.UIPasswordToggler;
import com.example.app.utils.RememberMe;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseRepository mFirebaseHelper;

    private EditText mEmailInput, mPasswordInput;
    private Button mRegisterSectionBtn, mLoginBtn;
    private CheckBox mShowPasswordCheck, mRememberMeCheck;
    private TextView mForgotPasswordBtn;
    private ProgressBar mProgressBar;

    private UIActivitySwitcher mMainActivity, mResetActivity, mRegisterActivity;
    private RememberMe mAutoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseHelper = FirebaseRepository.getInstance();

        /*ACTIVITIES*/
        mMainActivity = new UIActivitySwitcher(this, MainActivity.class);
        mResetActivity = new UIActivitySwitcher(this, ResetActivity.class);
        mRegisterActivity = new UIActivitySwitcher(this, RegisterActivity.class);

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
        mRegisterSectionBtn.setOnClickListener(this);
        mForgotPasswordBtn.setOnClickListener(this);
        mShowPasswordCheck.setOnCheckedChangeListener(new UIPasswordToggler(mPasswordInput));

        /*TEMP*/
        mEmailInput.setText("admin@gmail.com");
        mPasswordInput.setText("Admin*409");
    }

    void loginUser(String email, String password) {

        /*Validate user login info*/
        if (!validate(email, password)) return;

        /*Starts login process*/
        mProgressBar.setVisibility(View.VISIBLE);
        mFirebaseHelper.loginUser(email, password, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
                mMainActivity.setScene();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private boolean validate(String email, String password) {

        if (email.isEmpty()) {
            mEmailInput.setError("Email is required!");
            mEmailInput.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            mPasswordInput.setError("Password is required!");
            mPasswordInput.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {

        /*Parses user info from input fields*/
        String email, password;
        email = mEmailInput.getText().toString().trim();
        password = mPasswordInput.getText().toString().trim();

        switch (view.getId()) {
            case R.id.l_register_section_btn:
                mRegisterActivity.setScene();
                break;

            case R.id.l_forgot_pwd:
                mResetActivity.setScene();
                break;

            case R.id.l_login_btn:
                loginUser(email, password);
                break;

            default:
                break;
        }

    }
}