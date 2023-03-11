package com.example.app.uiutils;

import android.app.Activity;
import android.content.Intent;

public class UIActivitySwitcher {
    private Activity mCurrentActivity;
    private Class mTargetActivity;

    public UIActivitySwitcher(Activity currentActivity, Class targetActivity) {
        mCurrentActivity = currentActivity;
        mTargetActivity = targetActivity;
    }

    public void setScene() {
        if (mCurrentActivity == null || mTargetActivity == null)  return;
        Intent intent = new Intent(mCurrentActivity, mTargetActivity);
        mCurrentActivity.startActivity(intent);
        mCurrentActivity.finish();
    }
}
