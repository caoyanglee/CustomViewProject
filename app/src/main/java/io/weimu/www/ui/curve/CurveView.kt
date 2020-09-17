package io.weimu.www.ui.curve

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import kotlin.math.min


/**
 * Author:你需要一台永动机
 * Date:2020-01-31 20:24
 * Description:曲线视图
 */
class CurveView : View {

    private var widthSize = 0
    private var widthFactor = 0//宽度因子 总宽度度/5

    private var heightSize = 0
    private var heightFactor = 0//高度因子 总高度/5

    private var padding = dip2px(4f)

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
            this.style = Paint.Style.STROKE
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


    private val path = Path()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

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
        val minWidth = min(w, h)
        widthSize = minWidth - padding * 2
        widthFactor = widthSize / 5
        heightSize = minWidth - padding * 2
        heightFactor = heightSize / 5
        setMeasuredDimension(minWidth, minWidth)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return

        val ltX = padding.toFloat()
        val ltY = padding.toFloat()

        val lbX = padding.toFloat()
        val lbY = heightSize + padding.toFloat()

        val rtX = widthSize + padding.toFloat()
        val rtY = padding.toFloat()

        val rbX = widthSize + padding.toFloat()
        val rbY = heightSize + padding.toFloat()


        val paddingGap = padding.toFloat()

        //画基础线
        borderPaint.color = Color.BLACK
        canvas.drawLine(ltX, ltY, lbX, lbY, borderPaint)
        canvas.drawLine(
            lbX,
            lbY,
            rbX,
            rbY,
            borderPaint
        )

        //画网格
        borderPaint.color = Color.argb(30, 0, 0, 0)
        for (idx in 1 until 5) {
            val curX = widthFactor * idx
            //竖线
            canvas.drawLine(
                paddingGap + curX.toFloat(),
                paddingGap,
                paddingGap + curX.toFloat(),
                paddingGap + heightSize.toFloat(),
                borderPaint
            )

            val curY = heightFactor * idx
            //横线
            canvas.drawLine(
                paddingGap,
                paddingGap + curY.toFloat(),
                paddingGap + widthSize.toFloat(),
                paddingGap + curY.toFloat(),
                borderPaint
            )
        }

        //画字
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            "时间",
            paddingGap + widthSize.toFloat(),
            paddingGap + heightSize.toFloat() - dip2px(8f),
            textPaint
        )

        textPaint.textAlign = Paint.Align.LEFT
        val text2X = paddingGap + dip2px(16f).toFloat()
        val text2Y = paddingGap + dip2px(32f).toFloat()
        canvas.rotate(-90f, text2X, text2Y)
        canvas.drawText("进度", text2X, text2Y, textPaint)
        canvas.rotate(90f, text2X, text2Y)


        //画底线
        curvePaint.color = Color.argb(85, 0, 0, 0)
        canvas.drawLine(lbX, lbY, rtX, rtY, curvePaint)

        if (interpolator != null) {
            //画具体的曲线
            curvePaint.color = Color.BLACK
            path.reset()
            for (idx in 0..widthSize) {
                val x = idx.toFloat()
                val y = interpolator!!.getInterpolation(idx.toFloat() / widthSize) * heightSize

                if (idx == 0)
                    path.moveTo(x + paddingGap, heightSize - y + paddingGap)
                else
                    path.lineTo(x + paddingGap, heightSize - y + paddingGap)
                //Log.e("pmm", "x=$x y=$y")
            }
            canvas.drawPath(path, curvePaint)
        }

        //起点和结束点画圆
        canvas.drawCircle(lbX, lbY, dip2px(2f).toFloat(), curvePaint)
        canvas.drawCircle(rtX, rtY, dip2px(2f).toFloat(), curvePaint)

    }


    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}