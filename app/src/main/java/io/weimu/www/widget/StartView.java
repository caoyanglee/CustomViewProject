package io.weimu.www.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/7/10 下午5:18
 * @description
 */

public class StartView extends View {
    RectF startRect = new RectF();
    Paint startPaint = new Paint();


    public StartView(Context context) {
        this(context, null);
    }

    public StartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public StartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        startPaint.setAntiAlias(true);
        startPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        startPaint.setColor(Color.rgb(254, 221, 50));
        startPaint.setStrokeWidth(5);
    }

    List<PointX> points = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.e("weimu", "widthMode=" + widthMode + " heightMode=" + heightMode + " widthSize=" + widthSize + " heightSize=" + heightSize);
        startRect.left = 0;
        startRect.top = 0;
        startRect.right = getMeasuredWidth();
        startRect.bottom = getMeasuredHeight();

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        int radius = centerX;

        canvas.drawColor(Color.rgb(220, 44, 31));
        canvas.drawPoint(centerX, centerY, startPaint);

//        canvas.drawCircle(centerX, centerY, radius, startPaint);
        points.clear();
        for (float i = 36; i < 396; i += 72) {
            double rad = i * Math.PI / 180;
            float startX = (float) (centerX + radius * Math.sin(rad));
            float startY = (float) (centerY + radius * Math.cos(rad));
//            canvas.drawPoint(startX,startY,startPaint);
            points.add(new PointX(startX, startY));
        }
//        startPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawCircle(centerX,centerY,centerX,startPaint);

        canvas.drawLine(points.get(0).getX(), points.get(0).getY(), points.get(2).getX(), points.get(2).getY(), startPaint);
        canvas.drawLine(points.get(2).getX(), points.get(2).getY(), points.get(4).getX(), points.get(4).getY(), startPaint);
        canvas.drawLine(points.get(4).getX(), points.get(4).getY(), points.get(1).getX(), points.get(1).getY(), startPaint);
        canvas.drawLine(points.get(1).getX(), points.get(1).getY(), points.get(3).getX(), points.get(3).getY(), startPaint);
        canvas.drawLine(points.get(3).getX(), points.get(3).getY(), points.get(0).getX(), points.get(0).getY(), startPaint);

    }

    public class PointX {
        float x;
        float y;

        public PointX(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
