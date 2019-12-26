package io.weimu.www.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import static io.weimu.www.widget.WaveView.BORDER.CIRCLE;

/**
 * @author 艹羊
 * @project YouduoAndroid
 * @date 2017/8/1 下午4:33
 * @description
 */

public class TextViewWithBorder extends AppCompatTextView {


    private int widthSize;
    private int heightSize;

    private Paint paint;


    public TextViewWithBorder(Context context) {
        this(context, null);
    }

    public TextViewWithBorder(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewWithBorder(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPadding(dip2px(13), dip2px(13), dip2px(13), dip2px(13));

        paint = new Paint();
        paint.setColor(Color.rgb(255, 168, 170));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(dip2px(1));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        widthSize = w;
        heightSize = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 0, dip2px(13), 0, paint);
        canvas.drawLine(0, 0, 0, dip2px(26), paint);


        canvas.drawLine(widthSize, heightSize, widthSize - dip2px(13), heightSize, paint);
        canvas.drawLine(widthSize, heightSize, widthSize, heightSize - dip2px(26), paint);
    }

    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
