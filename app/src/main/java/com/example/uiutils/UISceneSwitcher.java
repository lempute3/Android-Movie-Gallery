package com.example.uiutils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class UISceneSwitcher implements View.OnClickListener {
    private Activity mCurrentActivity;
    private Class mTargetActivity;

    public UISceneSwitcher(Activity currentActivity, Class targetActivity) {
        mCurrentActivity = currentActivity;
        mTargetActivity = targetActivity;
    }

    public void setActive() {
        Intent intent = new Intent(mCurrentActivity, mTargetActivity);
        mCurrentActivity.startActivity(intent);
        mCurrentActivity.finish();
    }

    @Override
    public void onClick(View view) { setActive(); }
}
