package com.mnn.nnn.parallaxlistview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by mnn on 2016/12/10.
 * --
 */

public class ParallaxListView extends ListView {

    //头布局的iv
    private ImageView ivhead;
    private int maxHeight;
    private int orignalHeight;

    public void setParallaxImageView(final ImageView ivhead) {
        this.ivhead = ivhead;

        //这样获取高度是为了防止图片的高度小于imageView的高度的到时候
        ivhead.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivhead.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                orignalHeight = ivhead.getHeight();
                int drawableHeight = ivhead.getDrawable().getIntrinsicHeight();//获取图片真是的最大高度

                //如果图片高度小于imageView的高度
                maxHeight = orignalHeight > drawableHeight ? orignalHeight * 2 : drawableHeight;

            }
        });

    }

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param deltaX         继续滑动x方向的距离
     * @param deltaY         继续滑动y方向的距离 负 表示顶部到头  正 表示 底部到头
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX x方向最大可滑动距离
     * @param maxOverScrollY y方向最大可滑动距离
     * @param isTouchEvent   true:是手指拖动 false:惯性移动
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                   int maxOverScrollY, boolean isTouchEvent) {

        if (deltaY < 0 && isTouchEvent) {
            //表示顶部到头并且是手动拖动的情况
            if (ivhead != null) {
                int newHeight = ivhead.getHeight() - deltaY/3;//视差效果
                if (newHeight > maxHeight) newHeight = maxHeight;
                ivhead.getLayoutParams().height = newHeight;
                ivhead.requestLayout();//同样是时生效
            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP){
            //处理抬起手之后的回弹
            ValueAnimator animator = ValueAnimator.ofInt(ivhead.getHeight(), orignalHeight);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int value = (int) animator.getAnimatedValue();
                    ivhead.getLayoutParams().height = value;
                    ivhead.requestLayout();//同样是时生效
                }
            });

            animator.setInterpolator(new OvershootInterpolator(5));//弹性
            animator.setDuration(500);
            animator.start();
        }

        return super.onTouchEvent(ev);
    }
}
