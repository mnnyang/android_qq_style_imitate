package com.mnn.nnn.themesdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    public int current;
    private boolean isNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        if (isNight){
            WindowManager.LayoutParams mNightViewParam = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);

            WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            View   mNightView = new View(this);
            mWindowManager.addView(mNightView, mNightViewParam);
            mNightView.setBackgroundResource(R.color.night_mask);
        }else {
            WindowManager.LayoutParams mNightViewParam = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);

            WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            View   mNightView = new View(this);
            mWindowManager.addView(mNightView, mNightViewParam);
            mNightView.setBackgroundResource(R.color.night_mask2);
        }
    }

    public void buttonClick(View v) {
        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        System.out.println("启动前的主题"+currentNightMode);
        startActivity(new Intent(MainActivity.this, MainActivity2.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        int currentNightMode2 = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (current!= currentNightMode2){
            AppCompatDelegate.setDefaultNightMode(currentNightMode2);
            //第二中方式，只是提供一种思路，逻辑没有去实习
            recreate();

            isNight = true;
        }
        System.out.println("重新开始的主题"+currentNightMode2);
    }
}
