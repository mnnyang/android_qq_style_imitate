package com.mnn.nnn.themesdemo;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by nnn on 2017/2/1/001.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
