package io.weimu.www

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.weimu.www.widget.SimpleButton
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

        simpleButton.setOnClickListener {
            Toast.makeText(this@TestAreaActivity,"测试信号",Toast.LENGTH_SHORT).show()
        }
    }


}
