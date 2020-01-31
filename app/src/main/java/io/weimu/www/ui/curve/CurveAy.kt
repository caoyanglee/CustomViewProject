package io.weimu.www.ui.curve

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.weimu.www.R
import kotlinx.android.synthetic.main.activity_curve_ay.*

class CurveAy : AppCompatActivity() {

    companion object {
        //跳转
        fun newIntent(context: Context): Intent {
            return Intent(context, CurveAy::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curve_ay)


        mCurve.interpolator = EaseCubicInterpolator(1f, 0f, 1f, 0f)

        btnTest.setOnClickListener {
            mCurve.interpolator = EaseCubicInterpolator(
                Math.random().toFloat(),
                Math.random().toFloat(),
                Math.random().toFloat(),
                Math.random().toFloat()
            )
        }
    }
}
