package io.weimu.www.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collections;
import java.util.List;

import io.weimu.www.bean.BarData;
import io.weimu.www.bean.LineData;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/7/26 上午10:19
 * @description
 */

public class StockChartAView extends View {
    private final static int FRACTION = 30;//份数
    private final static int FRACTION_GAP = 4;//划分4大份
    private final static int LINE_HEIGHT_GAP = 4;//折线图 高度划分4份
    private final static int BAR_HEIGHT_GAP = 2;//柱状图 高度划分2份


    private List<LineData> lineDatas;
    private List<LineData> lineAverageDatas;
    private List<BarData> barDatas;

    private Paint borderP;//边界画笔
    private Paint gridP;//网格画笔
    private Paint lineP;//折线图画笔
    private Paint lineAverageP;//折线图【平均线】
    private Paint barP;//柱状图画笔
    private Paint highLightP;//高亮线画笔
    private Paint highLightBackP;//高亮背景【黑】
    private Paint highTextP;//高亮文字【白】

    private Paint bottomTextP;//底部文字
    private Paint leftTextP;//左侧文字

    //折线图底部Y  柱状图的顶部Y
    private float lineCharEndY;

    private float barCharStartY;
    private float barChartEndY;

    //整个视图的高度因子
    private float heightFactor = 0;//顶部3份，底部1份,中间32dp  底部文字18dp
    private float widthFactor = 0;//9:30~15:00共240份  第一份60 第二份60 60 第四份60


    //折线图
    private double lineDiff = -1;
    private float lineHeightFactor = -1;

    //柱状图
    private double barDiff = -1;
    private float barHeightFactor = -1;


    //高亮
    private float highLightX = -1;
    private float highLightY = -1;
    private RectF rectLeftRight;
    private RectF rectBottom;


    //自定义属性
    private float barViewGap = 1f;//柱状图与柱状图之间的距离


    public StockChartAView(Context context) {
        this(context, null);
    }

    public StockChartAView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StockChartAView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setClickable(true);
        //border
        borderP = new Paint();
        initNormalPaint(borderP);
        borderP.setStyle(Paint.Style.STROKE);

        //grid
        gridP = new Paint();
        initNormalPaint(gridP);
        gridP.setColor(Color.GRAY);
        gridP.setStrokeWidth(1);

        //lineChart
        lineP = new Paint();
        initNormalPaint(lineP);
        lineAverageP = new Paint();
        initNormalPaint(lineAverageP);
        lineAverageP.setColor(Color.MAGENTA);

        //barChart
        barP = new Paint();
        initNormalPaint(barP);
        barP.setColor(Color.rgb(13, 103, 14));
        barP.setStyle(Paint.Style.FILL);
        //highLight
        highLightP = new Paint();
        initNormalPaint(highLightP);
        //rect
        rectLeftRight = new RectF();
        rectBottom = new RectF();
        //rect-back
        highLightBackP = new Paint();
        initNormalPaint(highLightBackP);
        highLightBackP.setStyle(Paint.Style.FILL);
        //rect-text
        highTextP = new Paint(Paint.ANTI_ALIAS_FLAG);
        initNormalPaint(highTextP);
        highTextP.setColor(Color.WHITE);
        highTextP.setTextSize(dip2px(10));
        highTextP.setTextAlign(Paint.Align.CENTER);

        //bottom text
        bottomTextP = new Paint();
        initNormalPaint(bottomTextP);
        bottomTextP.setTextAlign(Paint.Align.LEFT);
        bottomTextP.setTextSize(dip2px(14));

        //left text
        leftTextP = new Paint();
        initNormalPaint(leftTextP);
        leftTextP.setTextAlign(Paint.Align.LEFT);
        leftTextP.setTextSize(dip2px(14));
    }


    private void initNormalPaint(Paint paint) {
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        heightFactor = ((h - dip2px(40) - dip2px(18)) / 4f);
        widthFactor = w / (float) FRACTION;

        lineCharEndY = heightFactor * 3;
        barChartEndY = getHeight() - dip2px(18);
        barCharStartY = barChartEndY - heightFactor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorder(canvas);
        drawGrid(canvas);
        drawLineChart(canvas, lineDatas, lineP);
        drawLineChart(canvas, lineAverageDatas, lineAverageP);
        drawBarChart(canvas);
        drawHighLight(canvas);
        drawLineChartLeftText(canvas);
        drawBarChartBottomText(canvas);

    }


    private void drawBorder(Canvas canvas) {
        //border-line chart
        canvas.drawRect(0, 0, getWidth(), lineCharEndY, borderP);
        //border-bar chart
        canvas.drawRect(0,barCharStartY,getWidth(),barChartEndY,borderP);
    }


    private void drawGrid(Canvas canvas) {
        //grid-vertical
        for (int i = 0; i < FRACTION_GAP - 1; i++) {
            //lineChart
            canvas.drawLine(widthFactor * FRACTION / FRACTION_GAP * (i + 1), 0, widthFactor * FRACTION / FRACTION_GAP * (i + 1), lineCharEndY, gridP);
            //barChart
            canvas.drawLine(widthFactor * FRACTION / FRACTION_GAP * (i + 1), barCharStartY, widthFactor * FRACTION / FRACTION_GAP * (i + 1), barChartEndY, gridP);
        }
        //grid-horizontal
        float lineChartHeightFactor = lineCharEndY / LINE_HEIGHT_GAP;//divide into multiply fraction
        float barChartHeightFactor = heightFactor / BAR_HEIGHT_GAP;//divide into multiply fraction
        //grid-horizontal-lineChart
        for (int i = 0; i < LINE_HEIGHT_GAP - 1; i++) {
            canvas.drawLine(0, lineChartHeightFactor * (i + 1), getWidth(), lineChartHeightFactor * (i + 1), gridP);
        }
        for (int i = 0; i < BAR_HEIGHT_GAP - 1; i++) {
            canvas.drawLine(0, barCharStartY + barChartHeightFactor * (i + 1), getWidth(), barCharStartY + barChartHeightFactor * (i + 1), gridP);
        }
    }

    private void drawLineChart(Canvas canvas, List<LineData> data, Paint paint) {
        if (lineDiff != -1) {
            float lastX = -1;
            float lastY = -1;
            lineHeightFactor = (float) (heightFactor * 3 / lineDiff);
            for (int i = 1; i <= data.size(); i++) {
                float x = widthFactor * (i - 1) + widthFactor / 2;
                float y = (float) (lineCharEndY - (data.get(i - 1).getValueY() * lineHeightFactor));
                if (lastX == -1 && lastY == -1) {
                    lastX = x;
                    lastY = y;
                }
                canvas.drawLine(lastX, lastY, x, y, paint);
                lastX = x;
                lastY = y;
            }
        }
    }

    private void drawBarChart(Canvas canvas) {
        //barChart
        if (barDiff != -1) {
            barHeightFactor = (float) (heightFactor / barDiff);
            for (int i = 1; i <= barDatas.size(); i++) {
                float x = widthFactor * (i - 1) + widthFactor / 2;
                float y = (float) (barChartEndY - (barDatas.get(i - 1).getValueY() * barHeightFactor));
                if (i % 2 == 0) {
                    barP.setColor(Color.rgb(13, 103, 14));
                } else {
                    barP.setColor(Color.RED);
                }
                canvas.drawRect(x - widthFactor / 2 + barViewGap, y, x + widthFactor / 2 - barViewGap, barChartEndY, barP);
            }
        }
    }


    private void drawHighLight(Canvas canvas) {
        if (highLightX != -1 && highLightY != -1) {
            int cell = (int) Math.ceil(highLightX * FRACTION / getWidth());
            try {
                if (lineDatas.get(cell - 1) == null) return;
            } catch (Exception e) {
                if (e instanceof IndexOutOfBoundsException) return;
            }
            if (cell >= 0 && cell < FRACTION) {
                Log.e("weimu", "cell=" + cell);
                //horizontal
                float x = widthFactor * (cell - 1) + widthFactor / 2;
                float y = (float) (lineCharEndY - (lineDatas.get(cell - 1).getValueY() * lineHeightFactor));
                canvas.drawLine(0, y, getWidth(), y, highLightP);
                //vertical
                canvas.drawLine(x, 0, x, lineCharEndY, highLightP);
                canvas.drawLine(x, barCharStartY, x, barChartEndY, highLightP);

                String lineValue = lineDatas.get(cell - 1).getValueY() + "";
                String cellValue = lineDatas.get(cell - 1).getTimeX() + "";

                float textX = 0;
                float textY = 0;


                if (highLightX > getWidth() / 2) {
                    //显示左边

                    rectLeftRight.left = 0;
                    rectLeftRight.top = y - 25;
                    rectLeftRight.right = 100;
                    rectLeftRight.bottom = y + 25;

                    textX = 50;
                    textY = computeBaseLine(rectLeftRight, highTextP.getFontMetricsInt());
                } else {
                    //显示右边
                    rectLeftRight.left = getWidth() - 100;
                    rectLeftRight.top = y - 25;
                    rectLeftRight.right = getWidth();
                    rectLeftRight.bottom = y + 25;

                    textX = getWidth() - 50;
                    textY = computeBaseLine(rectLeftRight, highTextP.getFontMetricsInt());
                }
                //左右两边文字的高亮
                canvas.drawRect(rectLeftRight, highLightBackP);
                canvas.drawText(lineValue, textX, textY, highTextP);

                //底部文字高亮
                rectBottom.left = x - 50;
                rectBottom.top = barChartEndY - 50;
                rectBottom.right = x + 50;
                rectBottom.bottom = barChartEndY;

                canvas.drawRect(rectBottom, highLightBackP);

                float bottomTextX = x;
                float bottomTextY = computeBaseLine(rectBottom, highTextP.getFontMetricsInt());
                canvas.drawText(cellValue, bottomTextX, bottomTextY, highTextP);
            }


        }
    }

    private void drawBarChartBottomText(Canvas canvas) {
        bottomTextP.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("09:30", 0, getHeight() - dip2px(2), bottomTextP);
        bottomTextP.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("11:30", getWidth() / 2, getHeight() - dip2px(2), bottomTextP);
        bottomTextP.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("15:00", getWidth(), getHeight() - dip2px(2), bottomTextP);
    }

    private void drawLineChartLeftText(Canvas canvas) {
        if (lineDatas == null || lineDatas.size() == 0) return;
        LineData max = Collections.max(lineDatas);
        LineData min = Collections.min(lineDatas);

        canvas.drawText(max.getValueY() + "", dip2px(2), dip2px(14), leftTextP);
        canvas.drawText(max.getValueY() / 2 + "", dip2px(2), lineCharEndY / 2 + dip2px(4), leftTextP);
        canvas.drawText(min.getValueY() + "", dip2px(2), lineCharEndY - dip2px(4), leftTextP);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                highLightX = event.getX();
                highLightY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                highLightX = event.getX();
                highLightY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                highLightX = -1;
                highLightY = -1;
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public void setLineDatas(List<LineData> lineDatas) {
        this.lineDatas = lineDatas;
        lineDiff = computeLineChartMinMaxDiff(lineDatas);
        postInvalidate();
    }


    public void setLineAverageDatas(List<LineData> lineAverageDatas) {
        this.lineAverageDatas = lineAverageDatas;
        barDiff = computeLineChartMinMaxDiff(lineAverageDatas);
        postInvalidate();
    }

    public void setBarDatas(List<BarData> barDatas) {
        this.barDatas = barDatas;
        barDiff = computeBarChartMinMaxDiff(barDatas);
        postInvalidate();
    }

    //计算【柱状图】差值=最大值-最小值
    private double computeBarChartMinMaxDiff(List<BarData> barDatas) {
        BarData max = Collections.max(barDatas);
        BarData min = Collections.min(barDatas);
        double diff = max.getValueY() - min.getValueY();
        return diff;
    }

    //计算【折线图】差值=最大值-最小值
    private double computeLineChartMinMaxDiff(List<LineData> lineDatas) {
        LineData max = Collections.max(lineDatas);
        LineData min = Collections.min(lineDatas);
        double diff = max.getValueY() - min.getValueY();
        return diff;
    }

    //计算文本的BaseLine
    private float computeBaseLine(RectF rectf, Paint.FontMetricsInt fontMetrics) {
        float baseline = rectf.top + (rectf.bottom - rectf.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        return baseline;
    }


    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
