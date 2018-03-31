package com.mnn.nnn.qqdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by mnn on 2016/12/9.
 */

public class MyLinearLayout extends LinearLayout {

    private SlideMenu mSlideMenu;

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSlideMenu(SlideMenu slideMenu) {
        mSlideMenu = slideMenu;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mSlideMenu != null && mSlideMenu.getCurrentDragState() == SlideMenu.DragState.OPEN) {
            return true;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSlideMenu != null && mSlideMenu.getCurrentDragState() == SlideMenu.DragState.OPEN) {
            if (event.getAction()==MotionEvent.ACTION_UP){
                mSlideMenu.close();
            }
            return true;
        }

        return super.onTouchEvent(event);
    }
}
