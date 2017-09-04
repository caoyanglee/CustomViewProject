package io.weimu.www.widget.granule;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
 * @date 2017/9/3 下午8:39
 * @description
 */

public class GranuleGridView extends View {

    private Paint pointP;
    private Paint lineP;

    private ValueAnimator valueAnimator;

    private List<Granule> granuleList = new ArrayList<>();

    public GranuleGridView(Context context) {
        this(context, null);
    }

    public GranuleGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GranuleGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pointP = new Paint();
        pointP.setColor(Color.WHITE);
        pointP.setStyle(Paint.Style.FILL);
        pointP.setAntiAlias(true);

        lineP = new Paint();
        lineP.setStyle(Paint.Style.FILL);
        lineP.setAntiAlias(true);
        lineP.setStrokeWidth(1);
        lineP.setColor(GranuleConfig.lineColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createGranule(w, h);
        beginAnim();
    }

    private void beginAnim() {
        if (valueAnimator != null) valueAnimator.cancel();
        valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(0, 0, 0));

        for (int i = 0; i < granuleList.size(); i++) {
            Granule outItem = granuleList.get(i);
            pointP.setColor(GranuleConfig.granuleColor);
            outItem.update();
            canvas.drawCircle(outItem.getPoint().x, outItem.getPoint().y, dip2px(outItem.getRadius()), pointP);

            for (Granule innerItem : granuleList) {
                double pointDistance = get2pointDistance(outItem, innerItem);

                if (pointDistance < GranuleConfig.minDistance) {
                    lineP.setAlpha(getLineAlpha(pointDistance));
                    canvas.drawLine(outItem.getPoint().x, outItem.getPoint().y, innerItem.getPoint().x, innerItem.getPoint().y, lineP);
                }
            }
        }
    }

    private int getLineAlpha(double distance) {
        return (int) (1 - (distance / GranuleConfig.minDistance) * 255);
    }


    private double get2pointDistance(Granule g1, Granule g2) {
        float i1 = g2.getPoint().x - g1.getPoint().x;
        float i2 = g2.getPoint().y - g1.getPoint().y;
        double pointDistance = Math.sqrt(Math.pow(i1, 2) + Math.pow(i2, 2));
        return pointDistance;
    }


    private void createGranule(int w, int h) {
        for (int i = 0; i < GranuleConfig.totalNumber; i++) {
            granuleList.add(new Granule(w, h));
        }
    }


    //粒子默认配置
    public static class GranuleConfig {
        final static int totalNumber = 25;
        final static float defaultSpeed = 1.0f;
        final static float variantSpeed = 5.0f;
        final static int granuleColor = Color.rgb(241, 219, 132);
        final static int lineColor = Color.rgb(241, 219, 132);
        final static float defaultRadius = 1.0f;
        final static float variantRadius = 1.0f;
        final static float minDistance = 400;
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
