package com.mnn.nnn.qqdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by mnn on 2016/12/9.
 */

public class DragLayout extends FrameLayout {

    private View mViewRed;
    private View mViewBlue;

    public DragLayout(Context context) {
        super(context);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    ViewDragHelper mDragHelper;
//    Scroller mScroller;

    private void init() {
        mDragHelper = ViewDragHelper.create(this, mCallback);
//        mScroller = new Scroller(getContext());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //让ViewDragHelper帮忙判断是否应该拦截
        boolean intercept = mDragHelper.shouldInterceptTouchEvent(ev);

        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件给mDragHelper处理
        mDragHelper.processTouchEvent(event);

        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mViewRed = getChildAt(0);
        mViewBlue = getChildAt(1);
    }

    //继承自FrameLayout
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        /*int viewMeasureSpec = MeasureSpec.makeMeasureSpec(mViewRed.getLayoutParams().width, MeasureSpec.EXACTLY);
//        mViewRed.measure(viewMeasureSpec, viewMeasureSpec);
//        mViewBlue.measure(viewMeasureSpec, viewMeasureSpec);*/
//
//        //方便的测量方式 没有特殊的对子View测量需求
//        measureChild(mViewBlue, widthMeasureSpec, heightMeasureSpec);
//        measureChild(mViewRed, widthMeasureSpec, heightMeasureSpec);
//
//    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int top = getPaddingTop();
        int left = getPaddingLeft() + getMeasuredWidth() / 2 - mViewRed.getMeasuredWidth() / 2;
//        int left = getPaddingLeft();

        mViewRed.layout(left, top, left + mViewRed.getMeasuredWidth(), top + mViewRed.getMeasuredHeight());

        mViewBlue.layout(left, mViewRed.getBottom(), left + mViewBlue.getMeasuredWidth(),
                mViewRed.getBottom() + mViewBlue.getMeasuredHeight());

    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        /**
         * 用处判断是否捕获当前Child的触摸事件
         * @param child 当前触摸的子View
         * @param pointerId
         * @return true 捕获并解析 false 不补货
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mViewBlue || child == mViewRed;
        }

        /**
         * 当前VIew被开始捕获解析的回调
         * @param capturedChild 当前被捕获的子View
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
//            Log.e("ee", "ee");
        }

        /**
         * 获取view 水平方向拖拽范围
         * 控制不了范围 但是最好不要返回 0
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return super.getViewVerticalDragRange(child);
        }

        /**
         * 控制view在水平方向的移动
         * @param child
         * @param left 触摸的View的left加上dx : child.getLeft()+dx
         * @param dx 触摸的便宜量
         * @return 表示想让left改变的量
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left < 0) {
                left = 0;
            }

            if (left > getMeasuredWidth() - child.getMeasuredWidth()) {
                left = getMeasuredWidth() - child.getMeasuredWidth();
            }

            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top < 0) {
                top = 0;
            }

            if (top > getMeasuredHeight() - child.getMeasuredHeight()) {
                top = getMeasuredHeight() - child.getMeasuredHeight();
            }

            return top;
        }

        /**
         * 当view的位置改变的时候执行
         * @param changedView 当前view
         * @param left 最新的left
         * @param top 最新的top
         * @param dx 本次水平移动的距离
         * @param dy 本次垂直移动的距离
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == mViewBlue) {
                mViewRed.layout(mViewRed.getLeft() + dx, mViewRed.getTop() + dy,
                        mViewRed.getRight() + dx, mViewRed.getBottom() + dy);

            } else if (changedView == mViewRed) {
                mViewBlue.layout(mViewBlue.getLeft() + dx, mViewBlue.getTop() + dy,
                        mViewBlue.getRight() + dx, mViewBlue.getBottom() + dy);
            }

            float fraction = changedView.getLeft()*1f/(getMeasuredWidth()-changedView.getMeasuredWidth());
            exectuteAnim(fraction);
        }

        /**
         * 手指抬起的时候
         * @param releasedChild 当前view
         * @param xvel x方向的速度 正 向右移动
         * @param yvel y方向的速度 正 向下移动
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            int centerLeft = getMeasuredWidth() / 2 - releasedChild.getMeasuredWidth() / 2;

            if (releasedChild.getLeft() < centerLeft) {
                //在左半边
                mDragHelper.smoothSlideViewTo(releasedChild, 0, releasedChild.getTop());
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);//刷新
            } else {
                //在右半边
                mDragHelper.smoothSlideViewTo(releasedChild,
                        getMeasuredWidth() - releasedChild.getMeasuredWidth(), releasedChild.getTop());
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);

            }

        }
    };

    /**
     * 执行动画
     * @param fraction 0--1
     */
    private void exectuteAnim(float fraction) {
        Log.e("tag", fraction+"");
//        mViewRed.setScaleX(1+fraction);
//        mViewRed.setScaleY(1+fraction);
//        mViewRed.setRotation(720*fraction);
//        mViewRed.setRotationY(720*fraction);
        mViewRed.setTranslationX(80*fraction);

        mViewRed.setBackgroundColor((Integer) ColorUtil.evaluateColor(fraction, Color.RED, Color.GREEN));
    }

    @Override
    public void computeScroll() {
        /*if (mScroller.computeScrollOffset()){//没有完成
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();//刷新
        }*/
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(DragLayout.this);
        }
    }
}
