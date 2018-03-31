package com.mnn.nnn.swipedelete;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mLv;
    private ArrayList<String> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLv = (ListView) findViewById(R.id.lv);

        for (int i = 0; i < 30; i++) {
            mArrayList.add("name" + i);
        }

        mLv.setAdapter(new MyAdapter());


        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //如果是触摸滚动状态 垂直滑动 垂直的话 listView才可能接收到事件
                    //水平滑动被swipeLayout消费掉了
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int i, int i1, int i2) {

            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup group) {
            if (view == null) {
                view = View.inflate(MainActivity.this, R.layout.item, null);
            }

            ViewHolder viewHolder = ViewHolder.getViewHolder(view);
            viewHolder.tv_name.setText(mArrayList.get(i));

            viewHolder.mSwipeLayout.setTag(i);//传递当前的position 给外界
            viewHolder.mSwipeLayout.setOnSwipeTouchListener(new SwipeLayout.OnSwipeTouchListener() {
                @Override
                public void onOpen(Object tag) {
                    Log.e("tag", "open" + (int) tag);
                }

                @Override
                public void onClose(Object tag) {
                    Log.e("tag", "close" + (int) tag);

                }
            });

            return view;
        }
    }

    static class ViewHolder {
        TextView tv_name, tv_delete;
        SwipeLayout mSwipeLayout;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            mSwipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_layout);
        }

        public static ViewHolder getViewHolder(View view) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }
            return viewHolder;
        }
    }
}
