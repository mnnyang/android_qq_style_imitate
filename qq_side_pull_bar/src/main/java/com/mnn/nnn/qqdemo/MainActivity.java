package com.mnn.nnn.qqdemo;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ListView mMenuList;
    private ImageView head;
    private MyLinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        head = (ImageView) findViewById(R.id.iv_head);
        mLayout = (MyLinearLayout) findViewById(R.id.main_layout);
        mLayout.setSlideMenu(((SlideMenu) findViewById(R.id.activity_main)));

        ((SlideMenu) findViewById(R.id.activity_main)).setOnDragStateChangleListener(new SlideMenu.OnDragStateChangeListener() {
            @Override
            public void onOpen() {
                System.out.println("open");
                mMenuList.smoothScrollToPosition(new Random().nextInt(mMenuList.getCount()));
            }

            @Override
            public void onClose() {
                System.out.println("close");

                head.animate()
                        .translationXBy(15)
                        .setInterpolator(new CycleInterpolator(4))
                        .setDuration(500)
                        .start();
            }

            @Override
            public void onDraging(float fraction) {
                Log.e("tag", "ing");
                head.setAlpha(1-fraction);
            }
        });

        //以下是设置数据
        mMenuList = (ListView) findViewById(R.id.menu_listview);
        ListView mainList = (ListView) findViewById(R.id.main_listview);
        mainList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Constant.NAMES) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                //先缩小view
                tv.setScaleX(0.5f);
                tv.setScaleY(0.5f);

//                ViewPropertyAnimator.animate()
                ObjectAnimator.ofFloat(tv, "scaleX", 1).setDuration(400).start();
                ObjectAnimator.ofFloat(tv, "scaleY", 1).setDuration(400).start();

                return tv;
            }
        });

        mMenuList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Constant.sCheeseStrings) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.WHITE);
                return tv;
            }
        });


    }
}
