package com.mnn.nnn.biti;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by mnn on 2016/12/11.
 */

public class BiTiView extends View {

    private Paint mPaint;
    private float fixationRadius = 20;
    private float dragRadius = 20;

    private PointF fixationCenter = new PointF(340, 400);
    private PointF dragCenter = new PointF(340, 400);

    private PointF center = new PointF(470, 400);

    private PointF[] fixationPoint = {new PointF(fixationCenter.x, fixationCenter.y - fixationRadius), new PointF(fixationCenter.x, fixationCenter.y + fixationRadius)};
    private PointF[] dragPoint = {new PointF(dragCenter.x, dragCenter.y - dragRadius), new PointF(dragCenter.x, dragCenter.y + dragRadius)};

    private double lineK;

    public BiTiView(Context context) {
        super(context);
        init();
    }

    public BiTiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BiTiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);


    }

    //最大距离
    private int maxDistance = 120;

    /**
     * 球固定于的半径
     *
     * @return
     */
    private float getFixationRadius() {
        float radius;

        //求出两个圆心之间的距离
        float centerDistance = GeometryUtil.getDistanceBetween2Points(dragCenter, fixationCenter);
        float fraction = centerDistance / maxDistance;
//        fraction = fraction > 1 ? 1 : fraction;

        radius = GeometryUtil.evaluateValue(fraction, 20f, 4f);//计算百分比后的值
        return radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        fixationRadius = getFixationRadius();//根据拖动情况动态改变半径

        //状态栏 画布偏移
        canvas.translate(0f, -Utils.getStatusBarHeight(getResources()));
        //动态根据drag的圆心 设置切点


        //获取圆心相对值
        float offsetX = dragCenter.x - fixationCenter.x;
        float offsetY = dragCenter.y - fixationCenter.y;


        if (offsetX != 0f) {
            lineK = offsetY / offsetX;
        }
        dragPoint = GeometryUtil.getIntersectionPoints(dragCenter, dragRadius, lineK);
        fixationPoint = GeometryUtil.getIntersectionPoints(fixationCenter, fixationRadius, lineK);

        //中心
        center = GeometryUtil.getPointByPercent(dragCenter, fixationCenter, 0.618f);

        canvas.drawCircle(dragCenter.x, dragCenter.y, dragRadius, mPaint);


        if (!dragOutOfRange) {
            canvas.drawCircle(fixationCenter.x, fixationCenter.y, fixationRadius, mPaint);
            Path path = new Path();
            path.moveTo(fixationPoint[0].x, fixationPoint[0].y);
            path.quadTo(center.x, center.y, dragPoint[0].x, dragPoint[0].y);
            path.lineTo(dragPoint[1].x, dragPoint[1].y);
            path.quadTo(center.x, center.y, fixationPoint[1].x, fixationPoint[1].y);
            path.close();
            canvas.drawPath(path, mPaint);
        }

        //绘制外圆
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(fixationCenter.x, fixationCenter.y, maxDistance, mPaint);
        mPaint.setStyle(Paint.Style.FILL);

    }

    private boolean dragOutOfRange = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dragOutOfRange = false;
                dragCenter.set(event.getRawX(), event.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                dragCenter.set(event.getRawX(), event.getRawY());

                if (GeometryUtil.getDistanceBetween2Points(dragCenter, fixationCenter) > maxDistance) {
                    //超出范围 不在绘制贝塞尔曲线 断掉
                    dragOutOfRange = true;

                }
                break;
            case MotionEvent.ACTION_UP:

                if (GeometryUtil.getDistanceBetween2Points(dragCenter, fixationCenter) > maxDistance) {
                    dragCenter.set(fixationCenter.x, fixationCenter.y);
                }else {
                    if (dragOutOfRange){
                        //如果曾经出去
                    dragCenter.set(fixationCenter.x, fixationCenter.y);
                    }else {
                        //动画形式缩回去
                        ValueAnimator animator = ValueAnimator.ofFloat(1);
                        final PointF startPoint = new PointF(dragCenter.x, dragCenter.y);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                float animatedFraction = animator.getAnimatedFraction();

                                Log.e("tag", animatedFraction+"");

                                PointF newPoint = GeometryUtil.getPointByPercent(startPoint, fixationCenter, animatedFraction);
                                dragCenter.set(newPoint);
                                invalidate();
                            }
                        });

                        animator.setDuration(180);
                        animator.setInterpolator(new OvershootInterpolator(3));
                        animator.start();
                    }
                }
                break;
        }

        invalidate();

        return true;
    }
}
