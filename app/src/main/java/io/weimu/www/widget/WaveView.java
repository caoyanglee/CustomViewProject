package io.weimu.www.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;


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
    private static final float STRETCH_FACTOR_A = 30;
    private static final int OFFSET_Y = 0;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;


    private ValueAnimator valueAnimator;


    private float heightFactor = 0;


    //paint
    private Paint mWavePaint;
    private Paint mBackPaint;
    private Paint mBorderPaint;
    private Paint mtextPaint;


    //custom field
    private int targetProgress = 1;
    private int currentProgress = 1;//0~100

    //-border
    private BORDER borderStyle = BORDER.SQUARE;
    private int borderColor = Color.rgb(255, 168, 170);
    private int borderWidth = 1;//dp unit

    //-background
    private int backgroundColor = Color.rgb(86, 44, 122);

    //-wave
    private Path firstWavePath;
    private Path secondWavePath;
    private int firstWaveSpeed = 3;
    private int secondWaveSpeed = 7;
    private int firstWaveColor = Color.argb(128, 217, 106, 91);
    private int secondWaveColor = Color.argb(200, 161, 213, 242);
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
        setClickable(true);
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
        mWavePaint.setColor(Color.rgb(255, 255, 255));

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


        if (borderStyle == BORDER.CIRCLE) {
            int min = Math.min(mTotalWidth, mTotalHeight);
            mTotalWidth = min;
            mTotalHeight = min;
        }


        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        heightFactor = mTotalHeight / 100;


        createShader();
        createShader2();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                beginHorizontalAnim();
                beginVerticalAnim();
            }
        }, 400);
    }


    private void beginHorizontalAnim() {
        if (valueAnimator != null) valueAnimator.cancel();
        valueAnimator = ValueAnimator.ofInt(0, mTotalWidth);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mShaderMatrix.postTranslate(firstWaveSpeed, 0);
                mShaderMatrix2.postTranslate(secondWaveSpeed, 0);
                mWaveShader.setLocalMatrix(mShaderMatrix);
                mWaveShader2.setLocalMatrix(mShaderMatrix2);
                postInvalidate();
            }
        });
        valueAnimator.start();
    }


    private void beginVerticalAnim() {
        mShaderMatrix.reset();
        mShaderMatrix2.reset();
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, getHeight());
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                WaveView.this.currentProgress = (int) Math.floor(value / heightFactor);
                if (value >= heightFactor * targetProgress) {
                    valueAnimator.cancel();
                    return;
                }
                mShaderMatrix.postTranslate(0, -heightFactor);
                mShaderMatrix2.postTranslate(0, -heightFactor);
                mWaveShader.setLocalMatrix(mShaderMatrix);
                mWaveShader2.setLocalMatrix(mShaderMatrix2);
            }
        });
        valueAnimator.start();
    }

    private BitmapShader mWaveShader;
    private Matrix mShaderMatrix;

    private void createShader() {
        mShaderMatrix = new Matrix();
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(backgroundColor);
        //wave1
        firstWavePath.reset();
        firstWavePath.moveTo(0, mTotalHeight);

        mTotalWidth = mTotalWidth + 1;
        for (int i = 0; i < mTotalWidth; i++) {
            firstWavePath.lineTo(i, mTotalHeight - 1 * heightFactor - STRETCH_FACTOR_A + (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * (i)) + OFFSET_Y));
            if (i == mTotalWidth - 1) {
                firstWavePath.lineTo(mTotalWidth, mTotalHeight);
                firstWavePath.close();
            }
        }
        mWavePaint.setColor(firstWaveColor);
        canvas.drawPath(firstWavePath, mWavePaint);
        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mBackPaint.setShader(mWaveShader);
    }


    private BitmapShader mWaveShader2;
    private Matrix mShaderMatrix2;

    private void createShader2() {
        mShaderMatrix2 = new Matrix();
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //wave2
        secondWavePath.reset();
        secondWavePath.moveTo(0, mTotalHeight);

        mTotalWidth = mTotalWidth + 1;
        for (int i = 0; i < mTotalWidth; i++) {
            secondWavePath.lineTo(i, mTotalHeight - 1 * heightFactor - STRETCH_FACTOR_A + (float) (STRETCH_FACTOR_A * Math.sin(Math.PI + mCycleFactorW * (i)) + OFFSET_Y));
            if (i == mTotalWidth - 1) {
                secondWavePath.lineTo(mTotalWidth, mTotalHeight);
                secondWavePath.close();
            }
        }
        mWavePaint.setColor(secondWaveColor);
        canvas.drawPath(secondWavePath, mWavePaint);

        // use the bitamp to create the shader
        mWaveShader2 = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mWavePaint.setShader(mWaveShader2);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        valueAnimator.cancel();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (borderWidth != 0) {
            switch (borderStyle) {
                case CIRCLE:
                    //wave
                    canvas.drawCircle(mTotalWidth / 2, mTotalWidth / 2, mTotalWidth / 2, mBackPaint);
                    canvas.drawCircle(mTotalWidth / 2, mTotalWidth / 2, mTotalWidth / 2, mWavePaint);
                    //border
                    canvas.drawCircle(mTotalWidth / 2, mTotalWidth / 2, mTotalWidth / 2 - 2, mBorderPaint);
                    break;
                case SQUARE:
                    //wave
                    canvas.drawRect(0, 0, mTotalWidth, mTotalHeight, mBackPaint);
                    canvas.drawRect(0, 0, mTotalWidth, mTotalHeight, mWavePaint);
                    //border
                    canvas.drawRect(0, 0, mTotalWidth, mTotalHeight, mBorderPaint);
                    break;
            }
        }
        //text
        if (isShowText) {
            canvas.drawText(currentProgress + "%", mTotalWidth / 2, mTotalHeight / 8 * 7, mtextPaint);
        }
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


    public void setCurrentProgress(int currentProgress) {
        if (currentProgress > 100) currentProgress = 100;
        if (currentProgress < 0) currentProgress = 0;
        WaveView.this.targetProgress = currentProgress;
        if (mShaderMatrix != null && mShaderMatrix2 != null)
            beginVerticalAnim();
    }


    public void setBorderStyle(BORDER borderStyle) {
        this.borderStyle = borderStyle;
    }


    public void setShowText(boolean showText) {
        isShowText = showText;
    }


}
