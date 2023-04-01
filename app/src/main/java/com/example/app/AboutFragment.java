package com.example.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app.repositories.firebase.FirebaseRepository;
import com.example.app.repositories.firebase.FirebaseTempUserModel;
import com.example.app.repositories.firebase.OnDataFetchListener;

public class AboutFragment extends Fragment {

    /*DATA*/
    private FirebaseRepository mFirebaseRepository = FirebaseRepository.getInstance();

    private Button mGenTempUserBtn;
    private EditText mGenTempUserURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mGenTempUserBtn = view.findViewById(R.id.gen_temp_user_btn);
        mGenTempUserURL = view.findViewById(R.id.gen_temp_user_url);

        mGenTempUserBtn.setOnClickListener(view1 -> {
            mFirebaseRepository.generateTempAccessKey(new OnDataFetchListener() {
                @Override
                public void onFetchSuccess(Object obj) {
                    FirebaseTempUserModel tempUser = (FirebaseTempUserModel) obj;
                    mGenTempUserURL.setText(tempUser.getAccess_key());
                }

                @Override
                public void onFetchFailure(String message) {
                    Toast.makeText(getContext(), "Failed to generate user: " + message, Toast.LENGTH_LONG).show();
                }
            });
        });

        return view;
    }
}