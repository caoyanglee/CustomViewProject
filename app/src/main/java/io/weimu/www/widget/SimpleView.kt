package io.weimu.www.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import io.weimu.www.R

/**
 * Author:你需要一台永动机
 * Date:2018/9/17 10:04
 * Description:可变化的TextView,为了减少drawable的使用
 */
class SimpleView : TextView {


    //获取对应的属性值 Android框架自带的属性 attr
    private val pressed = android.R.attr.state_pressed
    private val window_focused = android.R.attr.state_window_focused
    private val focused = android.R.attr.state_focused
    private val selected = android.R.attr.state_selected
    private val activited = android.R.attr.state_activated
    private val enabled = android.R.attr.state_enabled


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.SimpleView, defStyle, 0)
        //background
        val backgroundColor = attr.getColor(R.styleable.SimpleView_wm_backgroundColor, Color.TRANSPARENT)
        val backgroundPressColor = attr.getColor(R.styleable.SimpleView_wm_backgroundPressColor, backgroundColor)
        val backgroundUnEnableColor = attr.getColor(R.styleable.SimpleView_wm_backgroundUnEnableColor, backgroundColor)
        //stroke
        val strokeWidth = attr.getDimension(R.styleable.SimpleView_wm_strokeWidth, 0f)
        val strokeColor = attr.getColor(R.styleable.SimpleView_wm_strokeColor, Color.TRANSPARENT)
        val strokePressColor = attr.getColor(R.styleable.SimpleView_wm_strokePressColor, strokeColor)
        val strokeUnEnableColor =  attr.getColor(R.styleable.SimpleView_wm_strokePressColor, strokeColor)
        //corner
        val cornerRadius = attr.getDimension(R.styleable.SimpleView_wm_cornerRadius, -1f)
        val cornerRadius_TL = attr.getDimension(R.styleable.SimpleView_wm_cornerRadius_TL, 0f)
        val cornerRadius_TR = attr.getDimension(R.styleable.SimpleView_wm_cornerRadius_TR, 0f)
        val cornerRadius_BL = attr.getDimension(R.styleable.SimpleView_wm_cornerRadius_BL, 0f)
        val cornerRadius_BR = attr.getDimension(R.styleable.SimpleView_wm_cornerRadius_BR, 0f)
        //textPressColor
        val textColor = attr.getColor(R.styleable.SimpleView_wm_textColor, Color.BLACK)
        val textPressColor = attr.getColor(R.styleable.SimpleView_wm_textPressColor, textColor)
        val textUnEnableColor = attr.getColor(R.styleable.SimpleView_wm_textUnEnableColor, textColor)
        setTextColor(createColorStateList(textColor, textPressColor, textPressColor, textUnEnableColor, textPressColor))

        //bg-default
        val bg = GradientDrawable()
        //background
        bg.setColor(backgroundColor)
        //stroke
        bg.setStroke(dip2px(strokeWidth).toInt(), strokeColor)
        //corner
        bg.cornerRadii = floatArrayOf(
                dip2px(cornerRadius_TL), dip2px(cornerRadius_TL),
                dip2px(cornerRadius_TR), dip2px(cornerRadius_TR),
                dip2px(cornerRadius_BR), dip2px(cornerRadius_BR),
                dip2px(cornerRadius_BL), dip2px(cornerRadius_BL)
        )
        if (cornerRadius != -1f) bg.cornerRadius = dip2px(cornerRadius)

        //bg-select
        val selectBg = GradientDrawable()
        //background
        selectBg.setColor(backgroundPressColor)
        //stroke
        selectBg.setStroke(dip2px(strokeWidth).toInt(), strokePressColor)
        //corner
        selectBg.cornerRadii = floatArrayOf(
                dip2px(cornerRadius_TL), dip2px(cornerRadius_TL),
                dip2px(cornerRadius_TR), dip2px(cornerRadius_TR),
                dip2px(cornerRadius_BR), dip2px(cornerRadius_BR),
                dip2px(cornerRadius_BL), dip2px(cornerRadius_BL)
        )
        if (cornerRadius != -1f) selectBg.cornerRadius = dip2px(cornerRadius)


        //bg-uneable
        val unEnablebg = GradientDrawable()
        //background
        unEnablebg.setColor(backgroundUnEnableColor)
        //stroke
        unEnablebg.setStroke(dip2px(strokeWidth).toInt(), strokeUnEnableColor)
        //corner
        unEnablebg.cornerRadii = floatArrayOf(
                dip2px(cornerRadius_TL), dip2px(cornerRadius_TL),
                dip2px(cornerRadius_TR), dip2px(cornerRadius_TR),
                dip2px(cornerRadius_BR), dip2px(cornerRadius_BR),
                dip2px(cornerRadius_BL), dip2px(cornerRadius_BL)
        )
        if (cornerRadius != -1f) unEnablebg.cornerRadius = dip2px(cornerRadius)

        //复制给背景
        background = createSelector(bg, selectBg, bg, unEnablebg, selectBg)

        //gravity
        gravity = Gravity.CENTER


    }


    /**
     * 设置文本Selector
     */
    private fun createColorStateList(normal: Int, pressed: Int, focused: Int, unable: Int, activited: Int): ColorStateList {
//        val colors = intArrayOf(pressed, focused, normal, focused, unable, activited, normal)
        val colors = intArrayOf(pressed, unable, activited, normal)
        val states = arrayOfNulls<IntArray>(colors.size)
        states[0] = intArrayOf(this.enabled, this.pressed)
//        states[1] = intArrayOf(enabled, this.focused)
//        states[2] = intArrayOf(enabled)
//        states[3] = intArrayOf(this.focused)
        states[1] = intArrayOf(-this.enabled, -this.activited)
        states[2] = intArrayOf(-this.enabled, this.activited)
        states[3] = intArrayOf()
        return ColorStateList(states, colors)
    }

    /**
     * 设置背景的Selector
     */
    fun createSelector(normal: Drawable, pressed: Drawable, focused: Drawable, unable: Drawable, activited: Drawable): StateListDrawable {
        val bg = StateListDrawable()
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(intArrayOf(this.enabled,this.pressed), pressed)
//        // View.ENABLED_FOCUSED_STATE_SET
//        bg.addState(intArrayOf(this.enabled, this.focused), focused)
//        // View.ENABLED_STATE_SET
//        bg.addState(intArrayOf(this.enabled), normal)
//        // View.FOCUSED_STATE_SET
//        bg.addState(intArrayOf(this.focused), focused)
//        // View.WINDOW_FOCUSED_STATE_SET
//        bg.addState(intArrayOf(this.window_focused), unable)
        //unable
        bg.addState(intArrayOf(-this.enabled, -this.activited), unable)
        //activited
        bg.addState(intArrayOf(-this.enabled, this.activited), activited)
        // default
        bg.addState(intArrayOf(), normal)
        return bg
    }


    //dp2px
    fun dip2px(dpValue: Float) = (dpValue * context.resources.displayMetrics.density + 0.5f)

    //sp2px
    fun sp2px(spValue: Float) = (spValue * context.resources.displayMetrics.density + 0.5f)


}