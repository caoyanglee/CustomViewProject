package io.weimu.www;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.gelitenight.waveview.library.WaveView;

import io.weimu.www.helper.WaveHelper;
import io.weimu.www.ui.CircleProgressBarActivity;
import io.weimu.www.widget.ProgressButton;

public class TestAreaActivity extends AppCompatActivity {


    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, TestAreaActivity.class);
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_area);

        ProgressButton pb = (ProgressButton) findViewById(R.id.pt);
        pb.runAnimation();


        pb.setOnAnimationFinishListener(new ProgressButton.OnAnimationFinishListener() {
            @Override
            public void onFinish() {
                Toast.makeText(TestAreaActivity.this,"结束了",Toast.LENGTH_SHORT).show();
            }
        });
    }


}