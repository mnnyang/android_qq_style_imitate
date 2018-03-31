package com.mnn.nnn.listindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mnn on 2016/12/10.
 */

public class QuickIndexBar extends View {

    private Paint mPaint;
    private int width;
    //格子的高度
    private float cellHeight;

    private String[] indexArr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};


    public QuickIndexBar(Context context) {
        super(context);
        init();
    }


    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(18);
        mPaint.setTextAlign(Paint.Align.CENTER);//设置字体的中心位置为字体边框底边的中心

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        cellHeight = getMeasuredHeight() * 1f / indexArr.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int x = width / 2;

        for (int i = 0; i < indexArr.length; i++) {
            float y = cellHeight / 2 + getTextHeight(indexArr[i]) / 2 + cellHeight * i;

            mPaint.setColor(i == lastIndex ? Color.RED : Color.BLACK);//根据当前按下的变色
            canvas.drawText(indexArr[i], x, y, mPaint);
        }

    }

    private int lastIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                float y = event.getY();
                int index = (int) (y / cellHeight);//得到对应字母的索引

                if (index != lastIndex && index < indexArr.length && index >= 0) {
//                    System.out.println(indexArr[index]);
                    if (mListener != null) {
                        mListener.onTouchListener(indexArr[index]);
                    }
                }
                lastIndex = index;

                break;
            case MotionEvent.ACTION_UP:
                lastIndex = -1;//重置一下
                break;
        }

        invalidate();
        return true;
    }

    public void setOnTouchLetterListener(OnTouchLetterListener listener) {
        mListener = listener;
    }

    private OnTouchLetterListener mListener;

    public interface OnTouchLetterListener {
        void onTouchListener(String letter);
    }

    /**
     * 获取字符串的高度
     *
     * @param s
     * @return
     */
    private int getTextHeight(String s) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(s, 0, s.length(), bounds);

        return bounds.height();
    }
}
