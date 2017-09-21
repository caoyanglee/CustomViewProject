package io.weimu.www.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/9/21 上午10:07
 * @description 半弧View  利用贝塞尔二阶曲线
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


    private Paint testP;

    private Path mPath;

    private void init() {
        testP = new Paint();
        testP.setColor(Color.rgb(80, 188, 252));
        testP.setStrokeWidth(dip2px(1));
        testP.setStyle(Paint.Style.FILL);
        testP.setAntiAlias(true);

        mPath = new Path();
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

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();

        canvas.drawPoint(0,0,testP);
        canvas.drawPoint(measuredWidth/2,measuredHeight,testP);
        canvas.drawPoint(measuredWidth,0,testP);

        mPath.reset();
        //起点
        mPath.moveTo(0, 0);
        //mPath
        mPath.quadTo(measuredWidth/2, measuredHeight*2, measuredWidth, 0);

        mPath.close();

        //画path
        canvas.drawPath(mPath, testP);

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
}
