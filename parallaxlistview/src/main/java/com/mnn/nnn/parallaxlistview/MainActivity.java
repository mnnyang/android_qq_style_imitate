package com.mnn.nnn.parallaxlistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ParallaxListView mLv;
    private String[] indexArr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLv = (ParallaxListView) findViewById(R.id.lv);

        View view = View.inflate(this, R.layout.head, null);
        ImageView iv = (ImageView) view.findViewById(R.id.head);

        mLv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, indexArr));
        mLv.addHeaderView(view);
        mLv.setParallaxImageView(iv);
        mLv.setOverScrollMode(ListView.OVER_SCROLL_NEVER);//滑动超出的时候不显示蓝色的阴影
    }
}
