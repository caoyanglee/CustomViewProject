package io.weimu.www

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
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


        siv_city.setOnClickListener {
            Toast.makeText(baseContext,"开始选择城市",Toast.LENGTH_SHORT).show()
        }

        btn_test.setOnClickListener {
            Toast.makeText(baseContext,"测试信号",Toast.LENGTH_SHORT).show()
            val account = siv_account.content
            val pwd = siv_pwd.content
            val nickname = siv_nickname.content
            val city = siv_city.content

            val stb = StringBuilder()
            stb.append("$account ")
            stb.append("$pwd ")
            stb.append("$nickname ")
            stb.append("$city ")

            Log.e("weimu", "获取的数据=" + stb.toString())
        }


    }


}
