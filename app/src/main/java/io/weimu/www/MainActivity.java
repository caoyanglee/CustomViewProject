package io.weimu.www;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.weimu.www.bean.BarData;
import io.weimu.www.bean.LineData;
import io.weimu.www.helper.StatusBarUtil;
import io.weimu.www.ui.CircleProgressBarActivity;
import io.weimu.www.ui.FanbarActivity;
import io.weimu.www.ui.GranuleGridActivity;
import io.weimu.www.ui.IndexChartActivity;
import io.weimu.www.ui.ProgresButtonActivity;
import io.weimu.www.ui.StockChartActivity;
import io.weimu.www.ui.SwitchColorBarActivity;
import io.weimu.www.ui.TextViewWithBorderActivity;
import io.weimu.www.ui.WaveActivity;
import io.weimu.www.widget.CircleProgressBarView;
import io.weimu.www.widget.StockChartAView;
import io.weimu.www.widget.SwitchColorBar;
import io.weimu.www.widget.TextViewWithBorder;
import io.weimu.www.widget.WaveView;
import io.weimu.www.widget.granule.Granule;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setColor(this, Color.RED);
    }


    public void itemClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test://测试专用
                startActivity(TestAreaActivity.newIntent(this));
                break;
            case R.id.btn_circle_progress_bar:
                startActivity(CircleProgressBarActivity.newIntent(this));
                break;
            case R.id.btn_fan_bar:
                startActivity(FanbarActivity.newIntent(this));
                break;
            case R.id.btn_index_hart:
                startActivity(IndexChartActivity.newIntent(this));
                break;
            case R.id.btn_stock_chart:
                startActivity(StockChartActivity.newIntent(this));
                break;
            case R.id.btn_switch_color_bar:
                startActivity(SwitchColorBarActivity.newIntent(this));
                break;
            case R.id.btn_text_with_border:
                startActivity(TextViewWithBorderActivity.newIntent(this));
                break;
            case R.id.btn_wave:
                startActivity(WaveActivity.newIntent(this));
                break;
            case R.id.btn_progress_button:
                startActivity(ProgresButtonActivity.newIntent(this));
                break;
            case R.id.btn_granule://粒子连线
                startActivity(GranuleGridActivity.newIntent(this));
                break;
        }
    }
}
