package com.mnn.nnn.listindex;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mnn on 2016/12/10.
 */
public class MyAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<Friend> mFriends = new ArrayList<>();

    public MyAdapter(Context context, ArrayList<Friend> friends) {
        mContext = context;
        mFriends = friends;
    }


    @Override
    public int getCount() {
        return mFriends.size();
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
            view = View.inflate(mContext, R.layout.item, null);
        }

        ViewHolder holder = ViewHolder.getViewHolder(view);

        holder.tvName.setText(mFriends.get(i).getName());

        String thisWord = mFriends.get(i).getPinYin().charAt(0) + "";
        if (i > 0) {
            String lastWord = mFriends.get(i - 1).getPinYin().charAt(0) + "";
            if (lastWord.equals(thisWord)) {
                holder.tvFirstWord.setVisibility(View.GONE);

            } else {
                holder.tvFirstWord.setVisibility(View.VISIBLE);
                holder.tvFirstWord.setText(thisWord);

            }

        } else {
            holder.tvFirstWord.setVisibility(View.VISIBLE);
            holder.tvFirstWord.setText(thisWord);
        }


        return view;
    }

    private static class ViewHolder {
        TextView tvName, tvFirstWord;

        ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.name);
            tvFirstWord = (TextView) view.findViewById(R.id.first_word);
        }

        static ViewHolder getViewHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
            }
            return viewHolder;

        }
    }
}
