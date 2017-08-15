package io.weimu.www.widget;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.weimu.www.R;


/**
 * @author caoyang～ [WeiMu Studio]
 */
public class CircleProgressBarView extends View {
    private Context mContext;

    //圆的半径
    private float mRadius;

    //总体大小
    private int mHeight;
    private int mWidth;

    //圆心坐标
    private float viewX;
    private float viewY;


    //****属性定义****
    private int mBackGroundColor;//背景颜色，不能为透明
    private int mProgressBarColor;//进度条的颜色
    private int mProgressBarBackgroundColor;//进度条背景颜色
    private float mProgressbarWidth;//进度条的宽度
    private int mProgressValue; //进度值
    private boolean isShowAnimation = true;//是否需要动画
    private boolean isAutoAnimation = true;//是否需要自动播放动画【若用于recycerView中，应设置此属性为false，手动调用动画最佳】
    private int mAnimationTime;//动画时间【毫秒】
    private float mTextSize;//文本大小
    private boolean isShowText = true;//是否显示文本
    private int mTextColor;//文本颜色o
    private boolean isFanShaped = false;//是否扇形
    //****属性定义****

    //动画位置百分比进度
    private int mCurrentProgressValue = 0;//用于动画
    //要画的弧度
    private int mEndAngle;


    private ValueAnimator mAnimator;

    public CircleProgressBarView(Context context) {
        this(context, null);
    }

    public CircleProgressBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView, defStyleAttr, 0);

        mProgressValue = a.getInteger(R.styleable.CircleProgressBarView_progressValue, 0);
        if (mProgressValue > 100) {
            this.mProgressValue = 100;
        }
        mBackGroundColor = a.getColor(R.styleable.CircleProgressBarView_backgroundColor, 0xffffffff);//背景颜色
        mProgressBarColor = a.getColor(R.styleable.CircleProgressBarView_progressBarColor, 0xff33ccff);
        mProgressBarBackgroundColor = a.getColor(R.styleable.CircleProgressBarView_progressBarBackgroundColor, 0xfff6f6f6);
        mProgressbarWidth = a.getDimension(R.styleable.CircleProgressBarView_progressBarWidth, dip2px(30));
        isShowAnimation = a.getBoolean(R.styleable.CircleProgressBarView_isShowAnimation, true);
        isAutoAnimation = a.getBoolean(R.styleable.CircleProgressBarView_isAutoAnimation, true);
        mAnimationTime = a.getInteger(R.styleable.CircleProgressBarView_animationTime, 1000);
        mTextSize = a.getDimension(R.styleable.CircleProgressBarView_textSize, dip2px(16));
        isShowText = a.getBoolean(R.styleable.CircleProgressBarView_isShowText, true);
        mTextColor = a.getColor(R.styleable.CircleProgressBarView_textColor, mProgressBarColor);
        isFanShaped = a.getBoolean(R.styleable.CircleProgressBarView_isFanShaped, false);
        a.recycle();
    }


    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mRadius = widthSize / 2;
            viewX = widthSize / 2;
            viewY = heightSize / 2;
            mWidth = widthSize;
            mHeight = heightSize;
        }

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            mWidth = (int) (mRadius * 2);
            mHeight = (int) (mRadius * 2);
            viewX = mRadius;
            viewY = mRadius;

        }

        setMeasuredDimension(mWidth, mHeight);
        rect = new RectF(0, 0, mWidth, mHeight);
    }

    Paint bigCirclePaint = new Paint();
    Paint grayPaint = new Paint();
    Paint sectorPaint = new Paint();
    Paint smallCirclePaint = new Paint();
    RectF rect;

    @Override
    protected void onDraw(Canvas canvas) {
        //是否需要动画
        if (isShowAnimation) {
            mEndAngle = (int) (mProgressValue == 0 ? 0 : mCurrentProgressValue * 3.6);
        } else {
            mEndAngle = (int) (mProgressValue * 3.6);
        }

        //背景图
        bigCirclePaint.setAntiAlias(true);
        bigCirclePaint.setColor(mBackGroundColor);
        canvas.drawCircle(viewX, viewY, mRadius, bigCirclePaint);


        //进度条背景
        if (!isFanShaped) {
            grayPaint.setColor(mProgressBarBackgroundColor);
            grayPaint.setAntiAlias(true);
            canvas.drawArc(rect, 270, 360, true, grayPaint);
        }

        //进度条
        sectorPaint.setColor(mProgressBarColor);
        sectorPaint.setAntiAlias(true);
        canvas.drawArc(rect, 270, mEndAngle, true, sectorPaint);

        //前景图
        if (mProgressbarWidth < mRadius && !isFanShaped) {
            smallCirclePaint.setAntiAlias(true);
            smallCirclePaint.setColor(mBackGroundColor);
            canvas.drawCircle(viewX, viewY, mRadius - mProgressbarWidth, smallCirclePaint);
        }


        if (isShowText) {
            //绘制文本
            Paint textPaint = new Paint();
            String text = mCurrentProgressValue + "%";

            textPaint.setTextSize(mTextSize);
            float textLength = textPaint.measureText(text);

            textPaint.setColor(mTextColor);
            canvas.drawText(text, viewX - textLength / 2, viewY + mTextSize / 2, textPaint);
        }

    }


    /**
     * 开始执行动画
     */
    public void runAnimation() {
        if (!isShowAnimation) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                actionAnimation();
            }
        }, 500);
    }

    private void actionAnimation() {
        // 运行之前，先取消上一次动画
        cancelAnimation();
        mCurrentProgressValue = 0;
        mAnimator = ValueAnimator.ofObject(new FloatEvaluator(), 0, mProgressValue);
        // 设置差值器
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mCurrentProgressValue = (int) value;
                postInvalidate();
            }
        });

        mAnimator.setDuration(mAnimationTime);
        mAnimator.start();
    }

    /**
     * 取消动画
     */
    public void cancelAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    /**
     * onAttachedToWindow是在第一次onDraw前调用的。也就是我们写的View在没有绘制出来时调用的，但只会调用一次。
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isShowAnimation && isAutoAnimation) {
            runAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mProgressValue > mCurrentProgressValue) {
            postInvalidate();
            cancelAnimation();
        }

    }

    public void setProgressValue(int mTotalPercent) {
        if (mTotalPercent > 100) {
            this.mProgressValue = 100;
        } else {
            this.mProgressValue = mTotalPercent;
        }
        postInvalidate();
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }


    public void setmProgressbarWidth(float mProgressbarWidth) {
        this.mProgressbarWidth = mProgressbarWidth;
    }

    public void setmProgressBarColor(int mProgressBarColor) {
        this.mProgressBarColor = mProgressBarColor;
    }

    public void setmProgressBarBackgroundColor(int mProgressBarBackgroundColor) {
        this.mProgressBarBackgroundColor = mProgressBarBackgroundColor;
    }

    public void setmBackGroundColor(int mBackGroundColor) {
        this.mBackGroundColor = mBackGroundColor;
    }

    public void setShowText(boolean showText) {
        isShowText = showText;
    }

    public void setAutoAnimation(boolean autoAnimation) {
        isAutoAnimation = autoAnimation;
    }

    public void setShowAnimation(boolean showAnimation) {
        isShowAnimation = showAnimation;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

}