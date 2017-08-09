package io.weimu.www.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/7/6 上午9:39
 * @description
 */

public class GradientProgressBar extends View {
    /*圆弧线宽*/
    private float circleBorderWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
    /*内边距*/
    private float circlePadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
    /*字体大小*/
    private float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 50, getResources().getDisplayMetrics());
    /*绘制圆周的画笔*/
    private Paint backCirclePaint;
    /*绘制圆周白色分割线的画笔*/
    private Paint linePaint;
    /*绘制文字的画笔*/
    private Paint textPaint;
    /*百分比*/
    private int percent = 60;
    /*渐变圆周颜色数组*/
    private int[] gradientColorArray = new int[]{Color.GREEN, Color.parseColor("#fe751a"), Color.parseColor("#13be23"), Color.GREEN};
    private Paint gradientCirclePaint;

    public GradientProgressBar(Context context) {
        super(context);
        init();
    }

    public GradientProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backCirclePaint = new Paint();
        backCirclePaint.setStyle(Paint.Style.STROKE);
        backCirclePaint.setAntiAlias(true);
        backCirclePaint.setColor(Color.LTGRAY);
        backCirclePaint.setStrokeWidth(circleBorderWidth);
//        backCirclePaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.OUTER));

        gradientCirclePaint = new Paint();
        gradientCirclePaint.setStyle(Paint.Style.STROKE);
        gradientCirclePaint.setAntiAlias(true);
        gradientCirclePaint.setColor(Color.LTGRAY);
        gradientCirclePaint.setStrokeWidth(circleBorderWidth);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(5);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.BLACK);

//        setBackgroundColor(Color.GREEN);
    }


    private RectF backRect;
    private LinearGradient gradientShader;
    private RectF gradientRect;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(measureWidth, measureHeight), Math.min(measureWidth, measureHeight));
        initAllRect();
    }

    private void initAllRect() {
        backRect = new RectF(circlePadding * 2, circlePadding * 2, getMeasuredWidth() - circlePadding * 2, getMeasuredHeight() - circlePadding * 2);
        gradientShader = new LinearGradient(circlePadding, circlePadding,
                getMeasuredWidth() - circlePadding,
                getMeasuredHeight() - circlePadding,
                gradientColorArray, null, Shader.TileMode.MIRROR);

        gradientRect = new RectF(circlePadding * 2, circlePadding * 2, getMeasuredWidth() - circlePadding * 2, getMeasuredHeight() - circlePadding * 2);
    }

    private boolean isTest = true;

    private Paint paint = new Paint();

    private Path path  = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1.绘制灰色背景圆环
        canvas.drawArc(backRect, -90, 360, false, backCirclePaint);

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);


        //2.绘制颜色渐变圆环
        gradientCirclePaint.setShader(gradientShader);
        gradientCirclePaint.setShadowLayer(10, 10, 10, Color.RED);
        canvas.drawArc(gradientRect, -90, (float) (percent / 100.0) * 360, false, gradientCirclePaint);





        //半径
        float radius = (getMeasuredWidth() - circlePadding * 3) / 2;
        //X轴中点坐标
        int centerX = getMeasuredWidth() / 2;


        //3.绘制100份线段，切分空心圆弧
        for (float i = 0; i < 360; i += 3.6) {
            //1°=π/180 rad
            double rad = i * Math.PI / 180;
            float startX = (float) (centerX + (radius - circleBorderWidth) * Math.sin(rad));
            float startY = (float) (centerX + (radius - circleBorderWidth) * Math.cos(rad));

            float stopX = (float) (centerX + radius * Math.sin(rad));
            float stopY = (float) (centerX + radius * Math.cos(rad));

            canvas.drawLine(startX, startY, stopX, stopY, linePaint);
        }

//        if (isTest) return;

        //4.绘制文字
        float textWidth = textPaint.measureText(percent + "%");
        int textHeight = (int) (Math.ceil(textPaint.getFontMetrics().descent - textPaint.getFontMetrics().ascent) + 2);
        canvas.drawText(percent + "%", centerX - textWidth / 2, centerX + textHeight / 4, textPaint);

    }

    /**
     * 设置百分比
     *
     * @param percent
     */
    public void setPercent(int percent) {
        if (percent < 0) {
            percent = 0;
        } else if (percent > 100) {
            percent = 100;
        }

        this.percent = percent;
        invalidate();
    }
}
