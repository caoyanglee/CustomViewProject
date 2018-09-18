package io.weimu.www.widget;

import android.content.Context;
import android.content.res.ColorStateList;
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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
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
    private final static int FRACTION = 10;//刻度尺分10份

    private int widthSize;
    private int heightSize;


    private Paint showBarP;
    private Paint textP;


    private RectF viewRect;

    private Rect SrcRect, DesRect;//前景,背景

    private Bitmap circleGreyBitmap;
    private Bitmap colorBarBitmap;
    private Bitmap switchBitmp;


    private float touchX, touchY;

    private double degreeUnit = Math.PI / 180;

    private PorterDuffXfermode mixMode;


    private float startAngle = 123;//经过计算得出
    private float endAngle = 417;//经过计算得出
    private float angleFactor = (endAngle - startAngle) / FRACTION;

    private float currentAngle = 124;//默认显示1'的bar

    private int middleCircleRadius = 0;


    private Matrix matrix = new Matrix();

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

        showBarP = new Paint();
        showBarP.setColor(Color.RED);
        showBarP.setStrokeWidth(dip2px(30));
        showBarP.setStyle(Paint.Style.FILL);
        showBarP.setAntiAlias(true);

        textP = new Paint();
        textP.setColor(Color.rgb(160, 160, 160));
        textP.setAntiAlias(true);
        textP.setStyle(Paint.Style.FILL);
        textP.setTextSize(sp2px(16));
        textP.setTextAlign(Paint.Align.CENTER);
        textP.setAntiAlias(true);


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

        middleCircleRadius = widthSize / 2 - dip2px(15);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(circleGreyBitmap, SrcRect, DesRect, null);
        //save as new layer
        int sc = canvas.saveLayer(viewRect, showBarP, Canvas.ALL_SAVE_FLAG);
        //color bar
        canvas.drawArc(viewRect, startAngle, currentAngle - startAngle, true, showBarP);
        //color bar's front point
        canvas.drawCircle(widthSize / 2 + (float) (Math.cos((currentAngle) * degreeUnit) * middleCircleRadius), heightSize / 2 + (float) (Math.sin((currentAngle) * degreeUnit) * middleCircleRadius), dip2px(15), showBarP);
        //再画圆之后 设置mode
        showBarP.setXfermode(mixMode);
        canvas.drawBitmap(colorBarBitmap, 0, 0, showBarP);
        //restore to canvas
        showBarP.setXfermode(null);
        canvas.restoreToCount(sc);

        //center circle bar
        matrix.postTranslate(-switchBitmp.getWidth() / 2, -switchBitmp.getHeight() / 2);//步骤1
        matrix.postRotate(-(270 - startAngle) + (currentAngle - startAngle));///步骤2
        matrix.postTranslate(switchBitmp.getWidth() / 2, switchBitmp.getHeight() / 2);//步骤3
        matrix.postTranslate(dip2px(28), dip2px(28));
        canvas.drawBitmap(switchBitmp, matrix, null);//步骤4
        matrix.reset();

        //text

        canvas.drawText("10万", widthSize / 2 + (float) (Math.cos((startAngle) * degreeUnit) * widthSize / 2) - dip2px(24), heightSize / 2 + (float) (Math.sin((startAngle) * degreeUnit) * widthSize / 2) + dip2px(10), textP);
        canvas.drawText("100万", widthSize / 2 + (float) (Math.cos((endAngle) * degreeUnit) * widthSize / 2) + dip2px(24), heightSize / 2 + (float) (Math.sin((endAngle) * degreeUnit) * widthSize / 2) + dip2px(10), textP);


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
        if (currentAngle > 0 && currentAngle < 180 - startAngle) {
            currentAngle += 360;
        }
        if (currentAngle >= startAngle && currentAngle <= endAngle) {
            this.currentAngle = currentAngle;
            float angleDiff = currentAngle - startAngle;
            float index = angleDiff / angleFactor;
            if (valueChangeListener!=null)
                valueChangeListener.onValeChange(Double.valueOf(Math.ceil(index) * 10).intValue());
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


    private ValueChangeListener valueChangeListener;

    public interface ValueChangeListener {
        void onValeChange(int value);
    }

    public void setValueChangeListener(ValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }
}
