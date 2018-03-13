package io.weimu.www.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import io.weimu.www.R

/**
 * Author:你需要一台永动机
 * Date:2018/1/15 17:02
 * Description:
 */
class BlockLayout : FrameLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //获取测量模式
        val wMode = View.MeasureSpec.getMode(widthMeasureSpec)
        if (wMode==MeasureSpec.EXACTLY) Log.e("weimu", "BlockLayout onMeasure 模式：EXACTLY")
        if (wMode==MeasureSpec.AT_MOST) Log.e("weimu", "BlockLayout onMeasure 模式：AT_MOST")
        if (wMode==MeasureSpec.UNSPECIFIED) Log.e("weimu", "BlockLayout onMeasure 模式：UNSPECIFIED")

        //获取测量大小
        val wSize = View.MeasureSpec.getSize(widthMeasureSpec)

//        Log.e("weimu", "onMeasure   模式=$wMode 宽度=$wSize")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}