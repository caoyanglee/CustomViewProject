package io.weimu.www.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/8/2 下午3:47
 * @description
 */

public class IndexChart extends View {
    private final static int FRACTION_ALL = 100;
    private int FRACTION_SMALL = 10;//份数

    private List<String> indexList;

    private double rad = Math.PI / 180;
    private float colorBarOuterRadius = 0;
    private float colorBarInnerRadius = 0;
    private float textRadius = 0;

    private int widthSize;
    private int heightSize;


    private Paint testP;
    private Paint colorBarP;
    private Paint indexP;
    private Paint circleTextP;
    private Paint indexProgressP;
    private Paint indexPointP;
    private Paint textP;


    private RectF arcRect;//扇形所在的矩形


    private LinearGradient mShader;

    //from deep to shallow
    private int gradientA = Color.argb(180, 215, 255, 120);
    private int gradientB = Color.argb(180, 255, 176, 62);
    private int gradientC = Color.argb(180, 234, 63, 152);
    private int gradientD = Color.argb(180, 51, 56, 245);


    //
    private int padding = 8;//dp
    private int indexBarColorBarSpace = 8;//dp
    private int colorBarWidth = 8;//dp
    private int circleTextSpace = 14;//dp

    //顺时针  -    -|
    private float startAngle = 140;
    private float endAngle = 400;
    private float diffAngle = endAngle - startAngle;


    private float circleTextSize = 10;
    private float centerTextSize = 50;
    private float bottomTextSize = 14;


    //custom parameter
    private int currentIndex = 88;//0~100


    private ValueAnimator valueAnimator;

    public IndexChart(Context context) {
        super(context);
        init();
    }


    public IndexChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndexChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        testP = new Paint();
        testP.setColor(Color.RED);
        testP.setStrokeWidth(dip2px(1));
        testP.setStyle(Paint.Style.STROKE);
        testP.setAntiAlias(true);

        colorBarP = new Paint();
        colorBarP.setColor(Color.RED);
        colorBarP.setStrokeWidth(dip2px(colorBarWidth));
        colorBarP.setStyle(Paint.Style.STROKE);
        colorBarP.setAntiAlias(true);

        arcRect = new RectF();


        indexP = new Paint();
        indexP.setColor(Color.WHITE);
        indexP.setStrokeWidth(dip2px(2));
        indexP.setStyle(Paint.Style.FILL);
        indexP.setAntiAlias(true);


        circleTextP = new Paint();
        circleTextP.setStyle(Paint.Style.FILL);
        circleTextP.setColor(Color.rgb(196, 196, 196));
        circleTextP.setTextSize(sp2px(circleTextSize));
        circleTextP.setTextAlign(Paint.Align.CENTER);
        circleTextP.setAntiAlias(true);


        indexProgressP = new Paint();
        indexProgressP.setColor(Color.rgb(229, 229, 229));
        indexProgressP.setStyle(Paint.Style.STROKE);
        indexProgressP.setStrokeWidth(dip2px(1));
        indexProgressP.setAntiAlias(true);

        indexPointP = new Paint();
        indexPointP.setColor(Color.rgb(253, 123, 144));
        indexPointP.setStrokeWidth(dip2px(4));
        indexPointP.setAntiAlias(true);

        textP = new Paint();
        textP.setColor(Color.rgb(248, 82, 85));
        textP.setAntiAlias(true);
        textP.setStyle(Paint.Style.FILL);
        textP.setTextSize(sp2px(50));
        textP.setTextAlign(Paint.Align.CENTER);
        textP.setAntiAlias(true);


        List<String> indexList = new ArrayList<>();
        indexList.add("0");
        indexList.add("低");
        indexList.add("20");
        indexList.add("极低");
        indexList.add("40");
        indexList.add("中");
        indexList.add("60");
        indexList.add("较高");
        indexList.add("80");
        indexList.add("高");
        indexList.add("100");
        setIndexList(indexList);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(widthSize, heightSize), Math.min(widthSize, heightSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        widthSize = w;
        heightSize = h;
        mShader = new LinearGradient(0, 0, widthSize, heightSize, new int[]{gradientA, gradientB, gradientC, gradientD}, null, Shader.TileMode.MIRROR);

        colorBarOuterRadius = widthSize / 2 - dip2px(padding) - dip2px(indexBarColorBarSpace);
        colorBarInnerRadius = widthSize / 2 - dip2px(padding) - dip2px(indexBarColorBarSpace) - dip2px(colorBarWidth);
        textRadius = colorBarInnerRadius - dip2px(circleTextSpace);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //indexBar
        arcRect.left = dip2px(padding);
        arcRect.top = dip2px(padding);
        arcRect.right = widthSize - dip2px(padding);
        arcRect.bottom = heightSize - dip2px(padding);

        //小刻度尺--背景
        canvas.drawArc(arcRect, startAngle, diffAngle, false, indexProgressP);

        indexProgressP.setShader(mShader);
        //指示点
        float currentDegree = (float) (rad * (startAngle + currentIndex * (diffAngle / FRACTION_ALL)));
        canvas.drawArc(arcRect, startAngle, (float) (currentIndex * (diffAngle / FRACTION_ALL)), false, indexProgressP);

        canvas.drawCircle(widthSize / 2 + (float) (Math.cos(currentDegree) * arcRect.width() / 2), heightSize / 2 + (float) (Math.sin(currentDegree) * arcRect.width() / 2), dip2px(2), indexPointP);


        //colorBar
        float colorBarPosition = padding + colorBarWidth / 2 + indexBarColorBarSpace;
        arcRect.left = dip2px(colorBarPosition);
        arcRect.top = dip2px(colorBarPosition);
        arcRect.right = widthSize - dip2px(colorBarPosition);
        arcRect.bottom = heightSize - dip2px(colorBarPosition);

        colorBarP.setShader(mShader);
        canvas.drawArc(arcRect, startAngle, diffAngle, false, colorBarP);


        //white divider & circle text
        for (int i = 0; i < FRACTION_SMALL + 1; i++) {
            //angle
            double degree = rad * (startAngle + i * diffAngle / FRACTION_SMALL);
            //divider
            canvas.drawLine(widthSize / 2 + (float) (Math.cos(degree) * colorBarInnerRadius), heightSize / 2 + (float) (Math.sin(degree) * colorBarInnerRadius), widthSize / 2 + (float) (Math.cos(degree) * colorBarOuterRadius), heightSize / 2 + (float) (Math.sin(degree) * colorBarOuterRadius), indexP);
            //circleText
            canvas.rotate(90 + startAngle + (i * diffAngle / FRACTION_SMALL), widthSize / 2 + (float) (Math.cos(degree) * textRadius), heightSize / 2 + (float) (Math.sin(degree) * textRadius));
            canvas.drawText(indexList.get(i), widthSize / 2 + (float) (Math.cos(degree) * textRadius), heightSize / 2 + (float) (Math.sin(degree) * textRadius), circleTextP);
            canvas.rotate(-(90 + startAngle + (i * diffAngle / FRACTION_SMALL)), widthSize / 2 + (float) (Math.cos(degree) * textRadius), heightSize / 2 + (float) (Math.sin(degree) * textRadius));
        }
        //center Text
        textP.setTextSize(sp2px(centerTextSize));
        canvas.drawText(currentIndex + "", widthSize / 2, heightSize / 2 + textP.getTextSize() / 3, textP);
        //bottom Text
        textP.setTextSize(sp2px(bottomTextSize));
        canvas.drawText("波动值" + getFluctuateStr(currentIndex), widthSize / 2, heightSize * 3 / 4, textP);

        indexProgressP.setShader(null);
        colorBarP.setShader(null);

    }

    /**
     * 获取波动提示字符串
     *
     * @param currentIndex
     * @return
     */
    public String getFluctuateStr(int currentIndex) {
        if (currentIndex < 0 || currentIndex > FRACTION_ALL) return "null";
        float value = ((float) currentIndex);
        int currentItem = (int) Math.ceil(value);
        Log.e("weimu", "value=" + value + " currentItem=" + currentItem);
        if (currentItem <= 20) {
            return "低";
        } else if (currentItem <= 40) {
            return "较低";
        } else if (currentItem <= 60) {
            return "中";
        } else if (currentItem <= 80) {
            return "较高";
        } else {
            return "高";
        }
    }


    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //字体大小设置
    public int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }

    //计算文本的BaseLine
    private float computeBaseLine(RectF rectf, Paint.FontMetricsInt fontMetrics) {
        float baseline = rectf.top + (rectf.bottom - rectf.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        return baseline;
    }

    public void setIndexList(List<String> indexList) {
        this.indexList = indexList;
        if (indexList != null) {
            FRACTION_SMALL = indexList.size() - 1;
            postInvalidate();
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        runAnim();
    }

    private void runAnim() {
        valueAnimator = ValueAnimator.ofInt(0, currentIndex);
        valueAnimator.setDuration(2000);
        valueAnimator.setStartDelay(300);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentIndex = (int) animation.getAnimatedValue();

                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }

    private void stopAnim() {
        valueAnimator.cancel();
    }
}
