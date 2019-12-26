package io.weimu.www.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import io.weimu.www.R;


/**
 * Author:你需要一台永动机
 * Date:2018/3/8 18:41
 * Description:横向自动滚动的背景图
 */

public class HorizontalAutoScrollBgView extends View {

    private int mWidth, mHeight;

    private Paint mBackPaint;

    private Bitmap bitmapBg;
    private BitmapShader mShader;
    private Matrix matrix;
    private ValueAnimator valueAnimator;
    private boolean isPlaying = false;

    public HorizontalAutoScrollBgView(Context context) {
        this(context, null);
    }

    public HorizontalAutoScrollBgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalAutoScrollBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.HorizontalAutoScrollBgView, defStyleAttr, -1);
        final BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(R.styleable.HorizontalAutoScrollBgView_src);
        if (drawable == null) throw new IllegalStateException("need set attribute src");
        drawable.getBitmap();

        //bitmapBg = BitmapFactory.decodeResource(getResources(), d);
        bitmapBg = drawable.getBitmap();
        matrix = new Matrix();
        mBackPaint = new Paint();
        // use the bitamp to create the shader
        mShader = new BitmapShader(bitmapBg, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mBackPaint.setShader(mShader);

        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, bitmapBg.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mWidth = w;
        mHeight = h;
        startAnim();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 指定图片绘制区域(左上角的四分之一)
        canvas.drawRect(0, 0, mWidth, mHeight, mBackPaint);
        //canvas.drawBitmap(bitmapBg, matrix, null);
    }


    private void beginHorizontalAnim() {
        //matrix.reset();
        if (isPlaying) return;
        if (valueAnimator != null) valueAnimator.cancel();
        valueAnimator = ValueAnimator.ofFloat(0, bitmapBg.getWidth());
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                //float fraction = animation.getAnimatedFraction();
                changeShaderStatus(value);
            }
        });
        valueAnimator.start();
        isPlaying = true;
    }

    public void startAnim() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                beginHorizontalAnim();
            }
        }, 400);
    }

    public void stopAnim() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            isPlaying = false;
            valueAnimator.cancel();
        }
    }

    private void changeShaderStatus(float fraction) {
        matrix.postTranslate(2, 0);
        //matrix.setTranslate(dx, 0);
        mShader.setLocalMatrix(matrix);
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) valueAnimator.cancel();
        if (bitmapBg != null && !bitmapBg.isRecycled()) bitmapBg.recycle();
    }
}
