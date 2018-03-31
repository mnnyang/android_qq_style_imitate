package com.mnn.nnn.themesdemo;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.WindowManager;

public class MainActivity2 extends AppCompatActivity {

    boolean isNight;
    private View mNightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonClick(View v) {
        //获取应用当前的主题
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            // Night mode is not active, we're in day time
            case Configuration.UI_MODE_NIGHT_NO:
//                getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                break;

            // Night mode is active, we're at night!
            case Configuration.UI_MODE_NIGHT_YES:
//                getDelegate().setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                break;

            // We don't know what mode we're in, assume notnight
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
//                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                System.out.println("其他模式:" + currentNightMode);
                break;

        }
        recreate();
    }

    public void add(View v) {
        if (!isNight) {
            isNight = true;
            //第二中方式，只是提供一种思路，逻辑没有去实习
            WindowManager.LayoutParams mNightViewParam = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);

            WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            mNightView = new View(this);
            mWindowManager.addView(mNightView, mNightViewParam);
            mNightView.setBackgroundResource(R.color.night_mask);
        }else {
            //第二中方式，只是提供一种思路，逻辑没有去实习
            WindowManager.LayoutParams mNightViewParam = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);

            WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            mWindowManager.removeViewImmediate(mNightView);
            /*mWindowManager.addView(mNightView, mNightViewParam);
            mNightView.setBackgroundResource(R.color.night_mask2);*/

            isNight = false;
        }
    }
}
