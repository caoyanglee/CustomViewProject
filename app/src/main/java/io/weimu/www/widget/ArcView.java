package io.weimu.www.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/9/21 上午10:07
 * @description 半弧View  利用贝塞尔二阶曲线
 * 函数=B(t)=(1-t)^2*P0+2t*(1-t)*P1+t^2*P2,t=[0,1]
 */

public class ArcView extends View {

    public ArcView(Context context) {
        this(context, null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ValueAnimator valueAnimator;

    private Paint testP;

    private Path mPath;

    private Point P0,P1,P2;

    private void init() {
        testP = new Paint();
        testP.setColor(Color.rgb(80, 188, 252));
        testP.setStrokeWidth(dip2px(1));
        testP.setStyle(Paint.Style.FILL);
        testP.setAntiAlias(true);

        mPath = new Path();

        P0 = new Point();
        P1 = new Point();
        P2 = new Point();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        P0.set(0,0);
        P1.set(w / 2, h * 2);
        P2.set(w,0);
        beginAnim();
    }

    private void beginAnim() {
        if (valueAnimator != null) valueAnimator.cancel();
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                P1.set(getMeasuredWidth() / 2, (int) (getMeasuredHeight() * 2 * value));
                postInvalidate();
            }
        });
        valueAnimator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();


        mPath.reset();
        //起点
        mPath.moveTo(P0.x, P0.y);
        //mPath
        //经计算
        mPath.quadTo(P1.x, P1.y, P2.x, P2.y);

        mPath.close();

        //画path
        canvas.drawPath(mPath, testP);

    }


    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public void cancelAnim() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
    }

    public void startAnim() {
        if (valueAnimator != null && !valueAnimator.isRunning()) {
            valueAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnim();
    }
}

