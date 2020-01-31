package io.weimu.www.ui.curve

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Interpolator
import kotlin.math.min

/**
 * Author:你需要一台永动机
 * Date:2020-01-31 20:24
 * Description:
 */
class CurveView : View {

    private var widthSize = 0
    private var widthFactor = 0//宽度因子 总宽度度/5

    private var heightSize = 0
    private var heightFactor = 0//高度因子 总高度/5

    var interpolator: Interpolator? = null
        set(value) {
            field = value
            postInvalidate()
        }

    private val borderPaint: Paint by lazy {
        Paint().apply {
            this.isAntiAlias = true
            this.style = Paint.Style.FILL
            this.color = Color.BLACK
            this.strokeWidth = dip2px(1f).toFloat()
        }
    }

    private val curvePaint: Paint by lazy {
        Paint().apply {
            this.isAntiAlias = true
            this.style = Paint.Style.FILL
            this.color = Color.BLACK
            this.strokeWidth = dip2px(4f).toFloat()
        }
    }

    private val textPaint: Paint by lazy {
        Paint().apply {
            this.isAntiAlias = true
            this.color = Color.BLACK
            this.textSize = dip2px(14f).toFloat()
            this.textAlign = Paint.Align.RIGHT
        }
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        resolveWidthHeight(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        resolveWidthHeight(w, h)
    }

    //处理高度宽度问题
    private fun resolveWidthHeight(w: Int, h: Int) {
        widthSize = w
        widthFactor = widthSize / 5
        heightSize = h
        heightFactor = heightSize / 5
        setMeasuredDimension(min(widthSize, heightSize), min(widthSize, heightSize))
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return

        //画基础先
        borderPaint.color = Color.BLACK
        canvas.drawLine(0f, 0f, 0f, heightSize.toFloat(), borderPaint)
        canvas.drawLine(
            0f,
            heightSize.toFloat(),
            widthSize.toFloat(),
            heightSize.toFloat(),
            borderPaint
        )

        //画网格
        borderPaint.color = Color.argb(30, 0, 0, 0)
        for (idx in 1 until 5) {
            val curX = widthFactor * idx
            //竖线
            canvas.drawLine(curX.toFloat(), 0f, curX.toFloat(), heightSize.toFloat(), borderPaint)

            val curY = heightFactor * idx
            //横线
            canvas.drawLine(0f, curY.toFloat(), widthSize.toFloat(), curY.toFloat(), borderPaint)
        }

        //画字
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("时间", widthSize.toFloat(), heightSize.toFloat() - dip2px(8f), textPaint)

        textPaint.textAlign = Paint.Align.LEFT
        val text2X= dip2px(16f).toFloat()
        val text2Y=dip2px(32f).toFloat()
        canvas.rotate(-90f, text2X, text2Y)
        canvas.drawText("进度", text2X, text2Y, textPaint)
        canvas.rotate(90f, text2X, text2Y)


        //画底线
        curvePaint.color = Color.argb(85, 0, 0, 0)
        canvas.drawLine(0f, heightSize.toFloat(), widthSize.toFloat(), 0f, curvePaint)

        if (interpolator != null) {
            //画具体的曲线
            curvePaint.color = Color.BLACK
            for (idx in 0..widthSize) {
                val x = idx.toFloat()
                val y = interpolator!!.getInterpolation(idx.toFloat() / widthSize) * heightSize
                canvas.drawPoint(x, heightSize - y, curvePaint)
                Log.e("pmm", "x=$x y=$y")
            }
        }


    }


    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}