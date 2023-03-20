package com.example.app;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.app.utils.ValidationUtils;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseRepository firebaseHelper = FirebaseRepository.getInstance();

    private EditText mUsernameInput, mEmailInput, mPasswordInput;
    private Button mLoginSectionBtn, mRegisterBtn;
    private CheckBox mShowPasswordCheck;
    private TextView mForgotPwd;
    private ProgressBar mProgressBar;

    private UIActivitySwitcher mLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*ACTIVITIES*/
        mLoginActivity = new UIActivitySwitcher(this, LoginActivity.class);

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
        mLoginSectionBtn.setOnClickListener(this);
        mShowPasswordCheck.setOnCheckedChangeListener(new UIPasswordToggler(mPasswordInput));
        mRegisterBtn.setOnClickListener(this);

    }

    private void registerUser(String username, String email, String password) {

        /*Validate user register info*/
        if (!validate(username, email, password)) return;

        /*Starts registration process*/
        mProgressBar.setVisibility(View.VISIBLE);
        firebaseHelper.registerUser(email, username, password, new OnTaskCompletionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Registration Successful!", Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
                mLoginActivity.setScene();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    private boolean validate(String username, String email, String password) {

        List<String> usernameRequirements = ValidationUtils.isValidUsername(username);
        if (!usernameRequirements.isEmpty()) {
            String message = "Your username is missing: " + TextUtils.join(", ", usernameRequirements);
            mUsernameInput.setError(message);
            mUsernameInput.requestFocus();
            return false;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            mEmailInput.setError("Please provide valid email!");
            mEmailInput.requestFocus();
            return false;
        }

        List<String> passwordRequirements = ValidationUtils.isValidPassword(password);
        if (!passwordRequirements.isEmpty()) {
            String message = "Your password is missing: " + TextUtils.join(", ", passwordRequirements);
            mPasswordInput.setError(message);
            mPasswordInput.requestFocus();
            return false;
        }

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
            case R.id.r_login_section_btn:
                mLoginActivity.setScene();
                break;

            case R.id.r_register_btn:
                registerUser(username, email, password);
                break;

            default:
                break;
        }
    }
}