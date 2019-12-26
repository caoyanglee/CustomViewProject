package io.weimu.www.widget.sectionitem

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import io.weimu.www.R

/**
 * Author:你需要一台永动机
 * Date:2018/9/27 14:44
 * Description:key-value的输入视图
 */
class SectionItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var etValue: EditText? = null
    private var tvValue: TextView? = null

    private var canInput = true//可以输入

    //获取内容
    var content: String = ""
        get() {
            if (canInput) {
                return etValue!!.text.toString().trim { it <= ' ' }
            } else {
                return tvValue!!.text.toString().trim { it <= ' ' }
            }

        }
        //设置
        set(value) {
            if (canInput) {
                if (etValue != null) {
                    field = value
                    etValue?.setText(value)
                }
            } else {
                if (tvValue != null) {
                    field = value
                    tvValue?.setText(value)
                }
            }

        }


    init {
        setWillNotDraw(false)//重写onDraw方法,需要调用这个方法来清除flag
        clipChildren = false
        clipToPadding = false
        init(context, attrs, defStyleAttr)
    }


    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        //attrs
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.SectionItemView, defStyleAttr, 0)
        var key = a.getString(R.styleable.SectionItemView_wm_key)
        if (TextUtils.isEmpty(key)) key = "这是Key："
        var hint = a.getString(R.styleable.SectionItemView_wm_hint)
        if (TextUtils.isEmpty(hint)) hint = "这是Hint："
        val content = a.getString(R.styleable.SectionItemView_wm_content)
        val isImportant = a.getBoolean(R.styleable.SectionItemView_wm_important, true)
        val showArrow = a.getBoolean(R.styleable.SectionItemView_wm_showArrow, true)
        canInput = a.getBoolean(R.styleable.SectionItemView_wm_canInput, true)
        val inputType = a.getInt(R.styleable.SectionItemView_wm_inputType, 0)
        a.recycle()


        orientation = LinearLayoutCompat.VERTICAL

        //linearLayout
        val linearLayout = LinearLayoutCompat(getContext())
        //linearLayout.setBackgroundResource(R.drawable.bg_trade_pwd);
        linearLayout.layoutParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, dip2px(52f))
        linearLayout.gravity = Gravity.CENTER_VERTICAL
        linearLayout.orientation = LinearLayoutCompat.HORIZONTAL
        linearLayout.setPadding(dip2px(10f), 0, dip2px(15f), 0)

        //是否为必填的视图
        val tvImport = TextView(getContext())
        val tvImportParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        tvImportParams.setMargins(0, 0, dip2px(2f), 0)
        tvImport.layoutParams = tvImportParams
        tvImport.text = "*"
        tvImport.gravity = Gravity.CENTER
        tvImport.setTextColor(Color.argb(255, 252, 70, 74))
        tvImport.textSize = 15f
        linearLayout.addView(tvImport)

        if (isImportant) {
            tvImport.visibility = View.VISIBLE
        } else {
            tvImport.visibility = View.INVISIBLE
        }

        //key
        val tvKey = TextView(getContext())
        tvKey.layoutParams = LinearLayoutCompat.LayoutParams(dip2px(80f), LinearLayoutCompat.LayoutParams.MATCH_PARENT)
        tvKey.text = key
        tvKey.gravity = Gravity.CENTER_VERTICAL
        tvKey.setTextColor(Color.rgb(57, 57, 57))
        tvKey.textSize = 15f
        linearLayout.addView(tvKey)

        //value
        if (canInput) {
            etValue = EditText(getContext())
            etValue?.layoutParams = LinearLayoutCompat.LayoutParams(0, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 1.0f)
            etValue?.setText(content)
            etValue?.hint = hint
            etValue?.background = null
            etValue?.inputType = EditorInfo.TYPE_CLASS_TEXT
            etValue?.gravity = Gravity.CENTER_VERTICAL
            etValue?.setTextColor(Color.rgb(57, 57, 57))
            etValue?.setHintTextColor(Color.rgb(184, 184, 184))
            etValue?.textSize = 15f

            when (inputType) {
                1//密码
                -> etValue?.inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
            }
            linearLayout.addView(etValue)
        } else {
            tvValue = TextView(getContext())
            tvValue?.layoutParams = LinearLayoutCompat.LayoutParams(0, LinearLayoutCompat.LayoutParams.MATCH_PARENT, 1.0f)
            tvValue?.setText(content)
            tvValue?.hint = hint
            tvValue?.background = null
            tvValue?.inputType = EditorInfo.TYPE_CLASS_TEXT
            tvValue?.gravity = Gravity.CENTER_VERTICAL
            tvValue?.setTextColor(Color.rgb(57, 57, 57))
            tvValue?.setHintTextColor(Color.rgb(184, 184, 184))
            tvValue?.textSize = 15f

            when (inputType) {
                1//密码
                -> tvValue?.inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
            }
            linearLayout.addView(tvValue)
        }


        //ImageView
        val ivArrow = ImageView(getContext())
        ivArrow.setImageResource(R.drawable.universal_item_more_black)
        ivArrow.layoutParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT)
        if (showArrow) linearLayout.addView(ivArrow)


        //!!!
        addView(linearLayout)

        val divider = View(getContext())
        val dividerParams = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, dip2px(1f))
        dividerParams.setMargins(dip2px(20f), 0, 0, 0)
        divider.layoutParams = dividerParams
        divider.setBackgroundColor(0x26000000)

        addView(divider)


    }

    //根据手机的分辨率从 dp 的单位 转成为 px(像素)
    private fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


}
