package io.weimu.www.widget;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/8/14 上午11:24
 * @description
 */

public class ProgressButton extends View {

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int ANMATION_TIME = 3000;

    private int circleColor = Color.rgb(244, 84, 89);
    private int circleWith = dip2px(2);

    private int foregroundColor = Color.BLACK;

    private int textColor = Color.WHITE;


    private Paint foregroundP;
    private Paint circleP;
    private TextPaint textP;

    private RectF viewRect;


    private int currentAngle = 0;


    private ValueAnimator mAnimator;

    private OnAnimationFinishListener animationFinishListener;

    private void init() {
        foregroundP = new Paint();
        foregroundP.setColor(foregroundColor);
        foregroundP.setStyle(Paint.Style.FILL);
        foregroundP.setAntiAlias(true);

        circleP = new Paint();
        circleP.setColor(circleColor);
        circleP.setStyle(Paint.Style.FILL);
        circleP.setAntiAlias(true);


        textP = new TextPaint();
        textP.setColor(textColor);
        textP.setAntiAlias(true);
        textP.setStyle(Paint.Style.FILL);
        textP.setTextAlign(Paint.Align.CENTER);
        textP.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(widthSize, heightSize), Math.min(widthSize, heightSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int widthSize = w;
        int heightSize = h;
        viewRect = new RectF(0, 0, widthSize, heightSize);
        textP.setTextSize(px2dip(heightSize));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float radius = getWidth() / 2;

        //circle
        canvas.drawArc(viewRect, -90, currentAngle, true, circleP);

        //foreground
        canvas.drawCircle(centerX, centerY, radius - circleWith, foregroundP);

        //垂直居中
        float baseLine = computeBaseLine(viewRect, textP);
        //text
        canvas.drawText("跳过", viewRect.centerX(), baseLine, textP);

    }

    public void runAnimation() {
        // 运行之前，先取消上一次动画
        cancelAnimation();
        mAnimator = ValueAnimator.ofObject(new IntEvaluator(), 0, 360);
        // 设置差值器
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                currentAngle = value;
                if (animationFinishListener != null && value == 360) {
                    cancelAnimation();
                    animationFinishListener.onFinish();
                }
                postInvalidate();
            }

        });
        mAnimator.setDuration(ANMATION_TIME);
        mAnimator.start();
    }

    /**
     * 取消动画
     */
    public void cancelAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnimation();
    }

    public interface OnAnimationFinishListener {
        void onFinish();
    }

    public void setOnAnimationFinishListener(OnAnimationFinishListener animationFinishListener) {
        this.animationFinishListener = animationFinishListener;
    }


    //计算文本的BaseLine
    private float computeBaseLine(RectF rectf, TextPaint textPaint) {
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        float baseline = rectf.top + (rectf.bottom - rectf.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        return baseline;
    }


    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }


    public int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public int px2sp(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().density;
        return (int) (spValue / fontScale + 0.5f);
    }

}
