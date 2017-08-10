package io.weimu.www.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.weimu.www.R;
import io.weimu.www.widget.CircleProgressBarView;

public class CircleProgressBarActivity extends AppCompatActivity {


    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, CircleProgressBarActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress_bar);

        //手动开启动画
        final CircleProgressBarView cir01 = (CircleProgressBarView) findViewById(R.id.crv_01);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cir01.runAnimation();
            }
        }, 2000);
    }
}
