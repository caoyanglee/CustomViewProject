package io.weimu.www.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/7/5 下午5:55
 * @description
 */

public class TestView extends View {
    Point startPoint = new Point(100, 100);//起始点
    Point assistPoint = new Point(100, 200);//控制点
    Point endPoint = new Point(200, 100);//结束点

    Paint mPaint = new Paint();
    Path mPath = new Path();

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.RED); //画笔颜色
        mPaint.setStrokeWidth(5); //画笔宽度
        mPaint.setStyle(Paint.Style.STROKE);

        mPath.reset();
        //起点
        mPath.moveTo(startPoint.x, startPoint.y);
        //mPath
        mPath.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
        //画path
        canvas.drawPath(mPath, mPaint);
        //画控制点
//        canvas.drawPoint(assistPoint.x, assistPoint.y, mPaint);
        //画线
//        canvas.drawLine(startPoint.x, startPoint.y, assistPoint.x, assistPoint.y, mPaint);
//        canvas.drawLine(endPoint.x, endPoint.y, assistPoint.x, assistPoint.y, mPaint);

        canvas.drawCircle(100,100,20,mPaint);


    }
}
