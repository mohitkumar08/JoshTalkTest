package com.bit.joshtalktest;

import android.app.Application;

import com.bit.joshtalktest.data.local.database.AppDatabase;
import com.facebook.stetho.Stetho;

public class BaseApplication extends Application {
    private static AppDatabase appDatabaseContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabaseContext = AppDatabase.getInstance(this);
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    public static AppDatabase getAppDatabaseContext() {
        return appDatabaseContext;
    }
}
