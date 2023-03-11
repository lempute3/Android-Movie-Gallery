package com.example.app.uiutils;

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
        mFragmentManager.beginTransaction().replace(mContainerViewId, mTargetFragment).commit();
    }
}
