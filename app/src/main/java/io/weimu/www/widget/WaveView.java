package io.weimu.www.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import static io.weimu.www.widget.WaveView.BORDER.CIRCLE;
import static io.weimu.www.widget.WaveView.BORDER.SQUARE;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/7/31 下午5:57
 * @description 正余弦函数方程式：y = Asin(wx+b)+h   w影响周期，A影响振幅，h影响y位置，b为初相；
 */

public class WaveView extends View {

    public enum BORDER {
        CIRCLE, SQUARE
    }

    // y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 20;
    private static final int OFFSET_Y = 0;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private RectF viewRect;


    private ValueAnimator valueAnimator;


    private int mXOneOffset = 0;
    private float heightFactor = 0;


    //paint
    private Paint mWavePaint;
    private Paint mBackPaint;
    private Paint mBorderPaint;
    private Paint mtextPaint;

    private PorterDuffXfermode mixMode;
    private LinearGradient mShader;


    //custom field
    private int currentProgress = 0;//0~100

    //-border
    private BORDER borderStyle = CIRCLE;
    private int borderColor = Color.rgb(255, 168, 170);
    private int borderWidth = 1;//dp unit

    //-background
    private int backgroundColor = Color.rgb(251, 251, 250);;


    //-wave
    private boolean isUseGradient = true;//是否使用过度颜色
    private Path firstWavePath;
    private Path secondWavePath;
    private int firstWaveSpeed = 3;//3
    private int secondWaveSpeed = 5;//5
    private int firstWaveColor = Color.argb(10, 255, 255, 255);
    private int secondWaveColor = Color.argb(15, 255, 255, 255);
    //from top to bottom
    private int[] gradientArray = new int[]{Color.argb(128, 255, 201, 151), Color.argb(128, 255, 150, 142), Color.argb(128, 234, 100, 142)};
    //text
    private boolean isShowText = true;//是否显示文字


    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //background
        mBackPaint = new Paint();
        mBackPaint.setStrokeWidth(dip2px(1));
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setColor(backgroundColor);


        // wave
        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);

        firstWavePath = new Path();
        secondWavePath = new Path();

        //border
        mBorderPaint = new Paint();
        mBorderPaint.setStrokeWidth(dip2px(borderWidth));
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(borderColor);

        //text
        mtextPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mtextPaint.setColor(Color.WHITE);
        mtextPaint.setStrokeWidth(dip2px(2));
        mtextPaint.setTextSize(sp2px(30));
        mtextPaint.setTextAlign(Paint.Align.CENTER);


        mixMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);


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
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;

        viewRect = new RectF(0, 0, mTotalWidth, mTotalHeight);

        if (borderStyle == CIRCLE) {
            int min = Math.min(mTotalWidth, mTotalHeight);
            mTotalWidth = min;
            mTotalHeight = min;
        }


        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        heightFactor = mTotalHeight / 100;


        if (valueAnimator != null) valueAnimator.cancel();
        valueAnimator = ValueAnimator.ofInt(0, mTotalWidth);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mXOneOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        valueAnimator.cancel();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //save as new layer
        int sc = canvas.saveLayer(viewRect, mBorderPaint, Canvas.ALL_SAVE_FLAG);


        if (borderStyle == CIRCLE) {
            //background
            canvas.drawCircle(mTotalWidth / 2, mTotalWidth / 2, mTotalWidth / 2, mBackPaint);
            mWavePaint.setXfermode(mixMode);
            mBorderPaint.setXfermode(mixMode);
            mtextPaint.setXfermode(mixMode);
        } else {
            canvas.drawRect(0, 0, mTotalWidth, mTotalHeight, mBackPaint);
        }


        //wave1
        firstWavePath.reset();
        firstWavePath.moveTo(0, mTotalHeight);

        //wave2
        secondWavePath.reset();
        secondWavePath.moveTo(0, mTotalHeight);

        for (int i = 0; i < mTotalWidth; i++) {
            firstWavePath.lineTo(i, mTotalHeight - currentProgress * heightFactor - STRETCH_FACTOR_A + (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * (i) + mCycleFactorW * mXOneOffset * firstWaveSpeed) + OFFSET_Y));
            if (i == mTotalWidth - 1) {
                firstWavePath.lineTo(mTotalWidth, mTotalHeight);
                firstWavePath.close();
            }

            secondWavePath.lineTo(i, mTotalHeight - currentProgress * heightFactor - STRETCH_FACTOR_A + (float) (STRETCH_FACTOR_A * Math.sin(Math.PI + mCycleFactorW * (i) + mCycleFactorW * mXOneOffset * secondWaveSpeed) + OFFSET_Y));
            if (i == mTotalWidth - 1) {
                secondWavePath.lineTo(mTotalWidth, mTotalHeight);
                secondWavePath.close();
            }
        }
        if (isUseGradient) {
            mShader = new LinearGradient(mTotalWidth / 2, mTotalHeight - currentProgress * heightFactor, mTotalWidth / 2, mTotalHeight, gradientArray, null, Shader.TileMode.CLAMP);
            mWavePaint.setShader(mShader);
            canvas.drawPath(firstWavePath, mWavePaint);
            canvas.drawPath(secondWavePath, mWavePaint);
        } else {
            mWavePaint.setColor(firstWaveColor);
            canvas.drawPath(firstWavePath, mWavePaint);
            mWavePaint.setColor(secondWaveColor);
            canvas.drawPath(secondWavePath, mWavePaint);
        }


        //border
        if (borderWidth != 0) {
            switch (borderStyle) {
                case CIRCLE:
                    canvas.drawCircle(mTotalWidth / 2, mTotalWidth / 2, mTotalWidth / 2, mBorderPaint);
                    break;
                case SQUARE:
                    canvas.drawRect(0, 0, mTotalWidth, mTotalHeight, mBorderPaint);
                    break;
            }
        }

        //text
        if (isShowText) {
            canvas.drawText(currentProgress + "%", mTotalWidth / 2, mTotalHeight / 8 * 7, mtextPaint);
        }


        mWavePaint.setXfermode(null);
        mBorderPaint.setXfermode(null);
        mtextPaint.setXfermode(null);

        //restore to canvas
        canvas.restoreToCount(sc);

    }


    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 字体大小设置
     *
     * @param spValue
     * @return
     */
    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }


    public void setCurrentProgressWithAnim(int currentProgress) {
        if (currentProgress > 100) currentProgress = 100;
        if (currentProgress < 0) currentProgress = 0;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, currentProgress);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WaveView.this.currentProgress = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    public void setCurrentProgress(int currentProgress) {
        WaveView.this.currentProgress = currentProgress;
        postInvalidate();
    }


    public void setBorderStyle(BORDER borderStyle) {
        this.borderStyle = borderStyle;
    }

    public void startAnim() {
        valueAnimator.start();
    }

    public void stopAnim() {
        if (valueAnimator.isRunning())
            valueAnimator.cancel();
    }


    public void setUseGradient(boolean useGradient) {
        isUseGradient = useGradient;
    }

    public void setShowText(boolean showText) {
        isShowText = showText;
    }


}
