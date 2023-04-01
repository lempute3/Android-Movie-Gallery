package com.example.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.repositories.firebase.OnTaskCompletionListener;
import com.example.app.repositories.firebase.FirebaseRepository;
import com.example.app.uiutils.UIActivitySwitcher;
import com.example.app.utils.ValidationUtils;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseRepository firebaseHelper = FirebaseRepository.getInstance();

    private EditText mEmailInput;
    private Button mSendBtn;
    private TextView mBackBtn;

    private UIActivitySwitcher mRegisterActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        /*ACTIVITIES*/
        mRegisterActivity = new UIActivitySwitcher(this, LoginActivity.class);

        /*INPUTS*/
        mEmailInput = findViewById(R.id.res_email_input);

        /*BUTTONS*/
        mSendBtn = findViewById(R.id.res_send_btn);
        mBackBtn = findViewById(R.id.res_back_btn);

        /*EVENT LISTENERS*/
        mBackBtn.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
    }

    private void resetPassword(String email) {

        if (!validate(email)) return;

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

        if (!ValidationUtils.isValidEmail(email)) {
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

        switch (view.getId()) {
            case R.id.res_back_btn:
                mRegisterActivity.setScene();
                break;

            case R.id.res_send_btn:
                resetPassword(email);
                break;

            default:
                break;
        }
    }
}