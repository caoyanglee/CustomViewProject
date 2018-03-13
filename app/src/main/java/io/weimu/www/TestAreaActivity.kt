package io.weimu.www

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_test_area.*

/**
 * 测试 区域 代码
 * Test Area Code
 */
class TestAreaActivity : AppCompatActivity() {

    companion object {
        //跳转
        fun newIntent(context: Context): Intent {
            return Intent(context, TestAreaActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_area)

        val params = view_back.layoutParams
        Log.e("caoyanglee", "宽度=${params.width} 高度=${params.height}")
    }


}
