package io.weimu.www.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.weimu.www.MainActivity;
import io.weimu.www.bean.FanData;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/8/3 下午5:43
 * @description 扇形图
 */

public class FanBar extends View {
    private List<FanData> dataList;
    private double degreeUnit = Math.PI / 180;
    private float degreeFactor = 360 / 100f;

//    private int widthSize;
//    private int heightSize;


    private float bigFanRadius = 0;
    private float smallFanRadius = 0;
    private float circleBackRadius = 0;


    private RectF bigFanRect;
    private RectF smallFanRect;

    private Paint fanP;
    private Paint borderP;
    private Paint dividerP;
    private Paint circleBackP;
    private TextPaint centerTextP;
    private float touchX;
    private float touchY;


    private int chooseItem = 0;

    public FanBar(Context context) {
        this(context, null);
    }

    public FanBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FanBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);

        bigFanRect = new RectF();
        smallFanRect = new RectF();

        fanP = new Paint();
        fanP.setColor(Color.RED);
        fanP.setStrokeWidth(dip2px(1));
        fanP.setStyle(Paint.Style.FILL);
        fanP.setAntiAlias(true);

        borderP = new Paint();
        borderP.setColor(Color.WHITE);
        borderP.setStrokeWidth(dip2px(3));
        borderP.setStyle(Paint.Style.STROKE);
        borderP.setAntiAlias(true);

        dividerP = new Paint();
        dividerP.setColor(Color.WHITE);
        dividerP.setStrokeWidth(dip2px(2));
        dividerP.setStyle(Paint.Style.FILL);
        dividerP.setAntiAlias(true);

        circleBackP = new Paint();
        circleBackP.setColor(Color.WHITE);
        circleBackP.setStrokeWidth(dip2px(1));
        circleBackP.setStyle(Paint.Style.FILL);
        circleBackP.setAntiAlias(true);

        centerTextP = new TextPaint();
        centerTextP.setColor(Color.rgb(250, 133, 63));
        centerTextP.setAntiAlias(true);
        centerTextP.setStyle(Paint.Style.FILL);
        centerTextP.setTextSize(sp2px(16));
        centerTextP.setTextAlign(Paint.Align.CENTER);
        centerTextP.setAntiAlias(true);

        List<FanData> datas = new ArrayList<>();
        datas.add(new FanData(Color.rgb(242, 219, 156), "拍拍贷", 5));
        datas.add(new FanData(Color.rgb(237, 230, 150), "什么贷", 5));
        datas.add(new FanData(Color.rgb(225, 229, 131), "要什么", 5));
        datas.add(new FanData(Color.rgb(193, 226, 131), "自行车", 5));
        datas.add(new FanData(Color.rgb(152, 216, 111), "长者", 10));
        datas.add(new FanData(Color.rgb(112, 229, 112), "呵呵哒", 10));
        datas.add(new FanData(Color.rgb(89, 211, 115), "你谁谁", 10));
        datas.add(new FanData(Color.rgb(76, 204, 128), "有多", 10));
        datas.add(new FanData(Color.rgb(61, 193, 120), "车财多", 10));
        datas.add(new FanData(Color.rgb(49, 175, 127), "聚募", 30));
        setDataList(datas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//        switch (widthMode){
//            case MeasureSpec.EXACTLY:
//                Log.e("weimu","widthMode=EXACTLY");
//                break;
//            case MeasureSpec.AT_MOST:
//                Log.e("weimu","widthMode=AT_MOST");
//                break;
//            case MeasureSpec.UNSPECIFIED:
//                Log.e("weimu","widthMode=UNSPECIFIED");
//                break;
//            default:
//                Log.e("weimu","widthMode=NULL");
//                break;
//        }
//
//        switch (heightMode){
//            case MeasureSpec.EXACTLY:
//                Log.e("weimu","heightMode=EXACTLY");
//                break;
//            case MeasureSpec.AT_MOST:
//                Log.e("weimu","heightMode=AT_MOST");
//                break;
//            case MeasureSpec.UNSPECIFIED:
//                Log.e("weimu","heightMode=UNSPECIFIED");
//                break;
//            default:
//                Log.e("weimu","heightMode=NULL");
//                break;
//        }
//
//
//        Log.e("weimu", "onMeasure  widthMode=" + widthMode + " heightMode=" + heightMode + " widthSize=" + widthSize + " heightSize=" + heightSize);


        setMeasuredDimension(Math.min(widthSize, heightSize), Math.min(widthSize, heightSize));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("weimu", "onSizeChanged");
        int widthSize = w;
        int heightSize = h;

        bigFanRadius = widthSize / 2;
        smallFanRadius = bigFanRadius - dip2px(8);
        circleBackRadius = smallFanRadius * 2 / 3;

        bigFanRect.left = 0;
        bigFanRect.top = 0;
        bigFanRect.right = widthSize;
        bigFanRect.bottom = heightSize;

        smallFanRect.left = dip2px(8);
        smallFanRect.top = dip2px(8);
        smallFanRect.right = widthSize - dip2px(8);
        smallFanRect.bottom = heightSize - dip2px(8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        //fan
        float startDegree = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (i != 0) {
                startDegree += dataList.get(i - 1).getShare() * degreeFactor;
            }
            FanData currentData = dataList.get(i);
            fanP.setColor(currentData.getColor());
            canvas.drawArc(smallFanRect, startDegree, currentData.getShare() * degreeFactor, true, fanP);
        }

        //white border
        canvas.drawCircle(width / 2, height / 2, smallFanRadius - dip2px(4), borderP);
        //choose fan
        if (chooseItem != -1) {
            float bigStartDegree = 0;
            for (int i = 0; i < dataList.size(); i++) {
                if (i != 0) {
                    bigStartDegree += dataList.get(i - 1).getShare() * degreeFactor;
                }
                if (i == chooseItem) {
                    FanData currentData = dataList.get(i);
                    fanP.setColor(currentData.getColor());
                    canvas.drawArc(bigFanRect, bigStartDegree, currentData.getShare() * degreeFactor, true, fanP);
                }
            }
        }

        //divider
        float dividerDegree = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (i != 0) {
                dividerDegree += dataList.get(i - 1).getShare() * degreeFactor;
            }
            canvas.drawLine(width / 2 + (float) (Math.cos(dividerDegree * degreeUnit) * smallFanRadius), height / 2 + (float) (Math.sin(dividerDegree * degreeUnit) * smallFanRadius), width / 2 + (float) (Math.cos(dividerDegree * degreeUnit) * circleBackRadius), height / 2 + (float) (Math.sin(dividerDegree * degreeUnit) * circleBackRadius), dividerP);
        }


        //circle background
        canvas.drawCircle(width / 2, height / 2, circleBackRadius, circleBackP);

        if (chooseItem != -1) {

            //center text
            String title = dataList.get(chooseItem).getTitle();
            String share = dataList.get(chooseItem).getShare() + "%";
            int color = dataList.get(chooseItem).getColor();
            centerTextP.setColor(color);

            StaticLayout layout = new StaticLayout(title + "\r\n" + share, centerTextP, 300, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);

            Paint.FontMetrics fontMetrics = centerTextP.getFontMetrics();
            float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
            float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
            canvas.save();
            canvas.translate(width / 2, height / 2 - (bottom - top));
            layout.draw(canvas);
            canvas.restore();//别忘了restore
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


    public void setDataList(List<FanData> dataList) {
        this.dataList = dataList;
        postInvalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                highLightX = event.getX();
//                highLightY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                highLightX = event.getX();
//                highLightY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                touchX = event.getX();
                touchY = event.getY();
                //利用圆的方程
                if (Math.sqrt(Math.pow(Math.abs((touchX - bigFanRect.centerX())), 2) + Math.pow(Math.abs((touchY - bigFanRect.centerY())), 2)) > circleBackRadius) {
                    //Log.e("weimu", "is out of circle background");
                    computeWhichFanSelected(touchX, touchY);
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    private void computeWhichFanSelected(float touchX, float touchY) {
        //0~180  -180~0 +360 --  180~360
        float current = (float) (Math.atan2(touchY - bigFanRect.centerY(), touchX - bigFanRect.centerX()) / degreeUnit);
        if (current < 0) {
            current += 360;
        }
        float endDegree = 0;
        for (int i = 0; i < dataList.size(); i++) {
            endDegree += dataList.get(i).getShare() * degreeFactor;
            if (current < endDegree) {
                //chooseItem
                if (i == chooseItem) {
                    chooseItem = -1;
                } else {
                    chooseItem = i;
                }
                postInvalidate();
                break;
            }
        }
    }
}
