package com.mnn.nnn.listindex;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private QuickIndexBar mQuickIndexBar;
    private ListView lv;
    private ArrayList<Friend> friends = new ArrayList<Friend>();
    private TextView currentWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuickIndexBar = (QuickIndexBar) findViewById(R.id.quick_bar);
        lv = (ListView) findViewById(R.id.list_view);
        currentWord = (TextView) findViewById(R.id.currentWord);


        currentWord.animate().scaleX(0).scaleY(0).start();
        //填充数据
        fillList();
        //排序数据
        Collections.sort(friends);
        lv.setAdapter(new MyAdapter(this, friends));

        mQuickIndexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchListener(String letter) {
                for (int i = 0; i < friends.size(); i++) {
                    String firstWord = friends.get(i).getPinYin().charAt(0) + "";
                    if (letter.equals(firstWord)) {
                        lv.setSelection(i);
                        break;//防止重复遍历
                    }
                }

                showCurrentWord(letter);
            }
        });

    }

    private Handler mHandler = new Handler();

    private boolean isScale = false;

    private void showCurrentWord(String letter) {
        currentWord.setText(letter);
//        currentWord.setVisibility(View.VISIBLE);
        if (!isScale) {
            isScale = true;
            currentWord.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setInterpolator(new OvershootInterpolator())
                    .setDuration(450)
                    .start();
        }

        //先移除之前的任务 防止按住不放时也执行消去任务
        mHandler.removeCallbacksAndMessages(null);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                currentWord.setVisibility(View.INVISIBLE);
                currentWord.animate()
                        .scaleX(0f)
                        .scaleY(0f)
                        .setInterpolator(new OvershootInterpolator())
                        .setDuration(450)
                        .start();

                isScale = false;
            }
        }, 800);
    }

    private void fillList() {
        // 虚拟数据
        friends.add(new Friend("李伟"));
        friends.add(new Friend("张三"));
        friends.add(new Friend("阿三"));
        friends.add(new Friend("阿四"));
        friends.add(new Friend("段誉"));
        friends.add(new Friend("段正淳"));
        friends.add(new Friend("张三丰"));
        friends.add(new Friend("陈坤"));
        friends.add(new Friend("林俊杰1"));
        friends.add(new Friend("陈坤2"));
        friends.add(new Friend("王二a"));
        friends.add(new Friend("林俊杰a"));
        friends.add(new Friend("张四"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("王二"));
        friends.add(new Friend("王二b"));
        friends.add(new Friend("赵四"));
        friends.add(new Friend("杨坤"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("杨坤1"));
        friends.add(new Friend("李伟1"));
        friends.add(new Friend("宋江"));
        friends.add(new Friend("宋江1"));
        friends.add(new Friend("李伟3"));
    }
}
