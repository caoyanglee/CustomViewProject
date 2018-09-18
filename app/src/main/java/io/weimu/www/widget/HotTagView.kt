package io.weimu.www.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import io.weimu.www.R

/**
 * Author:你需要一台永动机
 * Date:2017/11/30 10:20
 * Description:热门标签
 */
class HotTagView : View {
    val POSITION_LEFT = 1;
    val POSITION_RIGHT = 2;

    private var viewWidth: Float = 0f
    private var tagWidth = 0f//标签的宽度
    private var tagPath: Path = Path()
    private var tagPaint: Paint = Paint()
    private var textPaint: Paint = Paint()


    //自定义属性
    var tagText = "限时免费"
    var tegTextColor = Color.WHITE
    var tagBgColor = Color.RED
    var position = POSITION_LEFT
    var triangle = false//是否为三角形


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        //获取自定义属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.HotTagView, defStyle, 0)
        tagText = (a.getString(R.styleable.HotTagView_tagText)) ?: "限时免费"
        tegTextColor = a.getColor(R.styleable.HotTagView_tagTextColor, Color.WHITE)
        tagBgColor = a.getColor(R.styleable.HotTagView_tagBgColor, Color.RED)
        position = a.getInt(R.styleable.HotTagView_position, POSITION_LEFT)
        triangle = a.getBoolean(R.styleable.HotTagView_triangle, false)
        a.recycle()

        //初始化画笔
        tagPaint.isAntiAlias = true
        tagPaint.style = Paint.Style.FILL
        tagPaint.color = tagBgColor

        textPaint.isAntiAlias = true
        textPaint.style = Paint.Style.FILL
        textPaint.color = tegTextColor
        textPaint.textAlign = Paint.Align.CENTER
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        //获取测量模式
        val wMode = View.MeasureSpec.getMode(widthMeasureSpec)
        //获取测量大小
        var wSize = View.MeasureSpec.getSize(widthMeasureSpec)

        if (wMode == MeasureSpec.AT_MOST)
            wSize = dip2px(80f).toInt()

        viewWidth = wSize.toFloat()
        //计算得出标签的宽度
        tagWidth = Math.sqrt(Math.pow((viewWidth / 2).toDouble(), 2.0) / 2).toFloat()

        if (triangle) {
            when (tagText.length) {
                1 -> {
                    textPaint.textSize = tagWidth
                }
                2 -> {
                    textPaint.textSize = tagWidth * 3 / 4
                }
                3 -> {
                    textPaint.textSize = tagWidth * 3 / 4
                }
                4 -> {
                    textPaint.textSize = tagWidth / 2
                }
                else -> {
                    textPaint.textSize = tagWidth / 2
                }
            }
        } else {
            textPaint.textSize = tagWidth / 2
        }


        //强制设置view为方形
        setMeasuredDimension(wSize, wSize)
        //获取测量模式
        if (wMode == MeasureSpec.EXACTLY) Log.e("weimu", "HotTagView onMeasure 模式：EXACTLY")
        if (wMode == MeasureSpec.AT_MOST) Log.e("weimu", "HotTagView onMeasure 模式：AT_MOST")
        if (wMode == MeasureSpec.UNSPECIFIED) Log.e("weimu", "HotTagView onMeasure 模式：UNSPECIFIED")
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val center = (viewWidth / 2).toFloat();
        tagPath.reset()

        if (triangle) {
            //三角形
            if (position == POSITION_LEFT) {
                tagPath.moveTo(viewWidth, 0f)
                tagPath.lineTo(0f, 0f)
                tagPath.lineTo(0f, viewWidth)
                tagPath.close()

                canvas?.drawPath(tagPath, tagPaint)
                canvas?.rotate(-45f, center, center)
                canvas?.drawText(tagText, center, center - tagWidth / 3, textPaint)
                canvas?.rotate(45f, center, center)
            } else {

                tagPath.moveTo(0f, 0f)
                tagPath.lineTo(0f, viewWidth)
                tagPath.lineTo(viewWidth, viewWidth)
                tagPath.close()

                canvas?.drawPath(tagPath, tagPaint)
                canvas?.rotate(45f, center, center)
                canvas?.drawText(tagText, center, center - tagWidth / 3, textPaint)
                canvas?.rotate(-45f, center, center)
            }
        } else {
            //条形
            if (position == POSITION_LEFT) {
                tagPath.moveTo(viewWidth, 0f)
                tagPath.lineTo(center, 0f)
                tagPath.lineTo(0f, center)
                tagPath.lineTo(0f, viewWidth)
                tagPath.close()

                canvas?.drawPath(tagPath, tagPaint)
                canvas?.rotate(-45f, center, center)
                canvas?.drawText(tagText, center, center - tagWidth / 3, textPaint)
                canvas?.rotate(45f, center, center)
            } else {
                tagPath.moveTo(0f, 0f)
                tagPath.lineTo(center, 0f);
                tagPath.lineTo(viewWidth, center)
                tagPath.lineTo(viewWidth, viewWidth)
                tagPath.close()

                canvas?.drawPath(tagPath, tagPaint)
                canvas?.rotate(45f, center, center)
                canvas?.drawText(tagText, center, center - tagWidth / 3, textPaint)
                canvas?.rotate(-45f, center, center)
            }
        }


    }


    //dp2px
    fun dip2px(dpValue: Float) = (dpValue * context.resources.displayMetrics.density + 0.5f)

    //sp2px
    fun sp2px(spValue: Float) = (spValue * context.resources.displayMetrics.density + 0.5f)
}