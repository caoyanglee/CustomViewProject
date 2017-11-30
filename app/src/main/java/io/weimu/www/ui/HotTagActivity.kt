package io.weimu.www.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.weimu.www.R

class HotTagActivity : AppCompatActivity() {

    companion object {
        //跳转
        fun newIntent(context: Context): Intent {
            return Intent(context, HotTagActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_tag)
    }
}
