package com.example.app.uiutils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class UIFragmentSwitcher {

    private int mContainerViewId;
    private FragmentManager mFragmentManager;
    private Fragment mTargetFragment;

    public UIFragmentSwitcher(int containerViewId, FragmentManager fragmentManager, Fragment targetFragment) {
        mFragmentManager = fragmentManager;
        mContainerViewId = containerViewId;
        mTargetFragment = targetFragment;
    }
    public void setFragment() {
        if (mFragmentManager == null || mTargetFragment == null) return;

        mFragmentManager.beginTransaction().replace(mContainerViewId, mTargetFragment)
                .addToBackStack(null)
                .commit();
    }

    public void setBundle(Bundle bundle) {
        if (mFragmentManager == null || mTargetFragment == null) return;
        mTargetFragment.setArguments(bundle);
    }
}
