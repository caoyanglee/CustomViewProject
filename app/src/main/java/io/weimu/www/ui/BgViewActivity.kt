package io.weimu.www.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.weimu.www.R
import kotlinx.android.synthetic.main.activity_bg_view.*
import kotlin.system.measureTimeMillis

class BgViewActivity : AppCompatActivity() {

    companion object {
        //跳转
        fun newIntent(context: Context): Intent {
            return Intent(context, BgViewActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bg_view)
        bgview.setOnClickListener {
            startActivity(HotTagActivity.newIntent(this))
        }
    }

    override fun onRestart() {
        super.onRestart()
        bgview.startAnim()
        Log.e("weimu", "startAnim")
    }

    override fun onStop() {
        super.onStop()
        bgview.stopAnim()
        Log.e("weimu", "stopAnim")
    }
}
