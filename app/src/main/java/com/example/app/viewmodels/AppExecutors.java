package com.example.app.viewmodels;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    private static final int THREAD_POOL_SIZE = 3;
    private static AppExecutors instance;

    private final ScheduledExecutorService mNetworkIO;

    private AppExecutors() {
        mNetworkIO = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
    }

    public static AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    public ScheduledExecutorService getNetworkIO() { return mNetworkIO; }
}
