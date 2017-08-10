package io.weimu.www.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import io.weimu.www.R;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/8/10 上午11:30
 * @description
 */

public class SwitchColorBar extends View {
    private int widthSize;
    private int heightSize;


    private Paint lineP;
    private Paint showBarP;


    private RectF viewRect;

    private Rect SrcRect, DesRect;//前景,背景
    private Rect switchSrcRect, switchDesRect;


    private Bitmap circleGreyBitmap;
    private Bitmap colorBarBitmap;
    private Bitmap switchBitmp;


    private float touchX, touchY;

    private double degreeUnit = Math.PI / 180;

    private PorterDuffXfermode mixMode;


    private float startAngle = 123;
    private float endAngle = 417;

    private float currentAngle = 360;

    private int middleCircleRadius = 0;


    Matrix matrix = new Matrix();

    public SwitchColorBar(Context context) {
        this(context, null);
    }

    public SwitchColorBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchColorBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
        initBitmap();
    }

    private void init() {
        setClickable(true);

        lineP = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        lineP.setColor(Color.RED);
        lineP.setStrokeWidth(dip2px(3));
        lineP.setStyle(Paint.Style.STROKE);
        lineP.setAntiAlias(true);

        showBarP = new Paint();
        showBarP.setColor(Color.RED);
        showBarP.setStrokeWidth(dip2px(30));
        showBarP.setStyle(Paint.Style.FILL);
        showBarP.setAntiAlias(true);


        mixMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);


    }

    private void initBitmap() {
        circleGreyBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.img_bg_grey)).getBitmap();
        colorBarBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.img_bg_color_bar)).getBitmap();
        switchBitmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.img_circle_switch)).getBitmap();
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
        setMeasuredDimension(Math.min(widthSize, heightSize), Math.min(widthSize, heightSize));


        viewRect = new RectF(0, 0, widthSize, heightSize);

        SrcRect = new Rect(0, 0, widthSize, heightSize);
        DesRect = new Rect(0, 0, widthSize, heightSize);

        switchSrcRect = new Rect(0, 0, widthSize - dip2px(30), heightSize - dip2px(30));
        switchDesRect = new Rect(dip2px(30), dip2px(30), widthSize - dip2px(30), heightSize - dip2px(30));


        middleCircleRadius = widthSize / 2 - dip2px(15);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(circleGreyBitmap, SrcRect, DesRect, null);
        //save as new layer
        int sc = canvas.saveLayer(viewRect, lineP, Canvas.ALL_SAVE_FLAG);

        canvas.drawArc(viewRect, startAngle, currentAngle - startAngle, true, showBarP);
        canvas.drawCircle(widthSize / 2 + (float) (Math.cos((currentAngle) * degreeUnit) * middleCircleRadius), heightSize / 2 + (float) (Math.sin((currentAngle) * degreeUnit) * middleCircleRadius), dip2px(15), showBarP);
        //再画圆之后 设置mode
        showBarP.setXfermode(mixMode);

        canvas.drawBitmap(colorBarBitmap, 0, 0, showBarP);

        //restore to canvas

        showBarP.setXfermode(null);
        canvas.restoreToCount(sc);

        matrix.postTranslate(-switchBitmp.getWidth() / 2, -switchBitmp.getHeight() / 2);//步骤1
        matrix.postRotate(-(270-startAngle) + (currentAngle - startAngle));///步骤2
        matrix.postTranslate(switchBitmp.getWidth() / 2, switchBitmp.getHeight() / 2);//步骤1
        matrix.postTranslate(dip2px(28), dip2px(28));
        canvas.drawBitmap(switchBitmp, matrix, null);//步骤4
        matrix.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                touchX = event.getX();
                touchY = event.getY();
                computeSelected(touchX, touchY);
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 计算选择
     *
     * @param touchX
     * @param touchY
     */
    private void computeSelected(float touchX, float touchY) {
        //0~180  -180~0 +360 --  180~360
        float currentAngle = (float) (Math.atan2(touchY - viewRect.centerY(), touchX - viewRect.centerX()) / degreeUnit);
        if (currentAngle < 0) {
            currentAngle += 360;
        }
//        Log.e("weimu","currentAngel="+currentAngle);
        if (currentAngle > 0 && currentAngle < 180-startAngle) {
            currentAngle += 360;
        }
        if (currentAngle >= startAngle && currentAngle <= endAngle) {
            this.currentAngle = currentAngle;
            postInvalidate();
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
}
