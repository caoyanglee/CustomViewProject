package io.weimu.www;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gelitenight.waveview.library.WaveView;

import io.weimu.www.helper.WaveHelper;
import io.weimu.www.ui.CircleProgressBarActivity;

public class TestAreaActivity extends AppCompatActivity {
    private WaveView waveView;
    private WaveHelper waveHelper;



    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, TestAreaActivity.class);
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_area);
        waveView = (WaveView) findViewById(R.id.wave);
        initWaweView();
    }

    /**
     * 初始化WaveView
     */
    private void initWaweView() {
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        waveView.setAmplitudeRatio(0.2f);
        waveView.setWaveShiftRatio(0.5f);
        waveView.setWaterLevelRatio(0.25f);
        waveView.setWaveColor(Color.argb(10, 255, 255, 255), Color.argb(15, 255, 255, 255));
        waveHelper = new WaveHelper(waveView);
    }


    @Override
    public void onResume() {
        super.onResume();
        waveHelper.start();
    }


    @Override
    public void onPause() {
        super.onPause();
        waveHelper.cancel();
    }
}
