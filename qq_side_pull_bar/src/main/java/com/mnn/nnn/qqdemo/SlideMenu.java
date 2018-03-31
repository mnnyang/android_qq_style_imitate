package com.mnn.nnn.qqdemo;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by mnn on 2016/12/9.
 */

public class SlideMenu extends FrameLayout {

    ViewDragHelper drawHelper;
    FloatEvaluator mFloatEvaluator;
    IntEvaluator mIntEvaluator;
    private View mMenuView;
    private View mMainView;
    private int mWidth;
    /**
     * 拖拽范围
     */
    private int mDragRange;

    public enum DragState {
        OPEN, CLOSE
    }

    public DragState getCurrentDragState() {
        return currentDragState;
    }

    private DragState currentDragState = DragState.CLOSE;

    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 2) {
            throw new IllegalArgumentException("SlideMenu is only have 2 children !");
        }

        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    private void init() {
        drawHelper = ViewDragHelper.create(this, callback);
        mFloatEvaluator = new FloatEvaluator();
        mIntEvaluator = new IntEvaluator();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMainView.getMeasuredWidth();
        mDragRange = (int) (mWidth * 0.6f);
    }

    //Callback
    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mMainView || child == mMenuView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mMainView) {
                if (left < 0) left = 0;
                if (left > mDragRange) {
                    left = mDragRange;
                }
            }
            //在此处让menu不动会导致changed获取不到dx
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            if (mMenuView == changedView) {
                mMenuView.layout(0, 0, mMenuView.getMeasuredWidth(), mMenuView.getMeasuredHeight());

                //限制mainView的范围
                int newLeft = mMainView.getLeft() + dx;
                if (newLeft < 0) {
                    newLeft = 0;
                }
                if (newLeft > mDragRange) {
                    newLeft = mDragRange;
                }

                mMainView.layout(newLeft, mMainView.getTop() + dy,
                        mMainView.getRight() + dx, mMainView.getBottom() + dy);
            }

            float fraction = (mMainView.getLeft()) * 1f / mDragRange;

            executeAnim(fraction);

            System.out.println("left:" + mMainView.getLeft() + "dragRange:" + mDragRange + "fr:"
                    + fraction + "state:" + currentDragState);

            if (fraction == 1f && currentDragState != DragState.OPEN) {
                currentDragState = DragState.OPEN;
                if (mListener != null) {
                    mListener.onOpen();
                }

            } else if (fraction == 0f && currentDragState != DragState.CLOSE) {
                currentDragState = DragState.CLOSE;
                if (mListener != null) {
                    mListener.onClose();
                }
            }

            if (mListener != null) {
                mListener.onDraging(fraction);
            }

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (mMainView.getLeft() < mDragRange / 2) {
                //main在左半边
                close();
            } else {
                open();
            }

            if (xvel>200&& currentDragState == DragState.CLOSE){
                open();
            }else if (xvel<-200 &&currentDragState==DragState.OPEN){
                close();
            }
        }
    };

    public void open() {
        drawHelper.smoothSlideViewTo(mMainView, mDragRange, mMainView.getTop());
        ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
    }

    public void close() {
        drawHelper.smoothSlideViewTo(mMainView, 0, mMainView.getTop());
        ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
    }

    private void executeAnim(float fraction) {
        System.out.println(fraction);
        //1-0.2f*fraction
        float scale = mFloatEvaluator.evaluate(fraction, 1f, 0.8f);
        mMainView.setScaleX(scale);
        mMainView.setScaleY(scale);

        mMenuView.setTranslationX(mIntEvaluator.evaluate(fraction, -mMenuView.getMeasuredWidth() / 2, 0));
        mMenuView.setScaleX(mFloatEvaluator.evaluate(fraction, 0.5f, 1f));
        mMenuView.setScaleY(mFloatEvaluator.evaluate(fraction, 0.5f, 1f));
        mMenuView.setAlpha(mFloatEvaluator.evaluate(fraction, 0.4f, 1f));

        //给背景设置颜色蒙层
        getBackground().setColorFilter((Integer) ColorUtil.evaluateColor(
                fraction, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    @Override
    public void computeScroll() {
        if (drawHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
        }
    }

    private OnDragStateChangeListener mListener;


    /**
     * 设置拖拽状态监听
     *
     * @param mListener
     */
    public void setOnDragStateChangleListener(OnDragStateChangeListener mListener) {
        this.mListener = mListener;
    }

    interface OnDragStateChangeListener {
        void onOpen();

        void onClose();

        void onDraging(float fraction);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return drawHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        drawHelper.processTouchEvent(event);
        return true;
    }


}
