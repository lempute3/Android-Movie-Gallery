package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dbconn.FirebaseHelper;
import com.example.dbconn.OnTaskCompletionListener;
import com.example.uiutils.UISceneSwitcher;
import com.example.uiutils.UIValidationUtils;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseHelper firebaseHelper = FirebaseHelper.getInstance();

    private EditText mEmailInput;
    private Button mSendBtn;
    private TextView mBackBtn;

    private UISceneSwitcher mRegisterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        /*ACTIVITIES*/
        mRegisterActivity = new UISceneSwitcher(this, LoginActivity.class);

        /*INPUTS*/
        mEmailInput = findViewById(R.id.res_email_input);

        /*BUTTONS*/
        mSendBtn = findViewById(R.id.res_send_btn);
        mBackBtn = findViewById(R.id.res_back_btn);

        /*EVENT LISTENERS*/
        mBackBtn.setOnClickListener(mRegisterActivity);
        mSendBtn.setOnClickListener(this);
    }

    private void resetPassword(String email) {
        firebaseHelper.resetPassword(email, new OnTaskCompletionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Check your email!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate(String email) {

        if (email.isEmpty()) {
            mEmailInput.setError("Email is required");
            mEmailInput.requestFocus();
            return false;
        }

        if (!UIValidationUtils.isValidEmail(email)) {
            mEmailInput.setError("Please provide valid email!");
            mEmailInput.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {

        /*Parses user email from input fields*/
        String email = mEmailInput.getText().toString().trim();

        if (view.getId() == R.id.res_send_btn) {
            resetPassword(email);
        }
    }
}