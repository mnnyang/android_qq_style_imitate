package com.mnn.nnn.swipedelete;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by mnn on 2016/12/10.
 */

public class SwipeLayout extends FrameLayout {

    private View contentView;
    private View deleteView;

    //宽度
    private int contentHeight;
    private int contentWidth;

    private int deleteHeight;
    private int deleteWidth;

    public enum SwipeState {
        Open, Close
    }

    //当前状态
    public SwipeState currentState = SwipeState.Close;

    private ViewDragHelper mDragHelper;


    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deleteView = getChildAt(1);
    }


    //onMeasure 已经执行完了
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentHeight = contentView.getMeasuredHeight();
        contentWidth = contentView.getMeasuredWidth();

        deleteHeight = deleteView.getMeasuredHeight();
        deleteWidth = deleteView.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        contentView.layout(0, 0, contentWidth, contentHeight);
        deleteView.layout(contentView.getRight(), 0, contentView.getRight() + deleteWidth, deleteHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果当前又打开的, 则需要拦截交给onTOuch处理
        if (!SwipeLayoutManager.getInstance().isShouldSwipe(this)) {

            //关闭打开的
            SwipeLayoutManager.getInstance().closeCurrentLayout();
            return true;
        }

        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    private int startX, startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //如果当前又打开的, 则下面的代码不能执行
        if (!SwipeLayoutManager.getInstance().isShouldSwipe(this)) {
            requestDisallowInterceptTouchEvent(true);
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                startY = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();

                if (Math.abs(moveX - startX) > Math.abs(moveY - startY)) {
                    //偏向于x
                    requestDisallowInterceptTouchEvent(true);//请求父控件不要动
                } else {
                    //偏向于y

                }

                startY = moveY;
                startX = moveX;

                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        mDragHelper.processTouchEvent(event);
        return true;
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == deleteView || child == contentView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return deleteWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == contentView) {
                if (left > 0) left = 0;
                if (left < -deleteWidth) left = -deleteWidth;
            } else if (child == deleteView) {
                if (left > contentView.getRight() + deleteWidth)
                    left = contentView.getRight() + deleteWidth;
                if (left < contentWidth - deleteWidth) left = contentWidth - deleteWidth;
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == contentView) {
                deleteView.layout(deleteView.getLeft() + dx, deleteView.getTop() + dy,
                        deleteView.getRight() + dx, deleteView.getBottom() + dy);
            } else if (changedView == deleteView) {
                contentView.layout(contentView.getLeft() + dx, contentView.getTop() + dy,
                        contentView.getRight() + dx, contentView.getBottom() + dy);
            }

            if (contentView.getLeft() == 0 && currentState != SwipeState.Close) {
                currentState = SwipeState.Close;
                //清空记录
                SwipeLayoutManager.getInstance().clearCurrentLayout();

                if (mListener != null) {
                    mListener.onClose(getTag());
                }

            } else if (contentView.getLeft() == -deleteWidth && currentState != SwipeState.Open) {
                currentState = SwipeState.Open;
                //记录当前
                SwipeLayoutManager.getInstance().setSwipeLayout(SwipeLayout.this);

                if (mListener != null) {
                    mListener.onOpen(getTag());
                }
            }

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (contentView.getLeft() < -deleteWidth / 2) {
                //应该打开
                open();
            } else {
                //应该关闭
                close();
            }
        }

    };

    public void close() {
        mDragHelper.smoothSlideViewTo(contentView, 0, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    /**
     * 打开的方法
     */
    public void open() {
        mDragHelper.smoothSlideViewTo(contentView, -deleteWidth, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


    public void setOnSwipeTouchListener(OnSwipeTouchListener listener) {
        mListener = listener;
    }

    private OnSwipeTouchListener mListener;

    public interface OnSwipeTouchListener {
        void onOpen(Object tag);

        void onClose(Object tag);
    }

}
