package io.weimu.www.ui;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.weimu.www.R;
import io.weimu.www.bean.BarData;
import io.weimu.www.bean.LineData;
import io.weimu.www.widget.StockChartAView;

public class StockChartActivity extends AppCompatActivity {
    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, StockChartActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_chart);

        StockChartAView stockChartAView = (StockChartAView) findViewById(R.id.stockView);
        List<LineData> lineDatas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            lineDatas.add(new LineData(i, i * i));
        }
        stockChartAView.setLineDatas(lineDatas);


        List<LineData> lineAverageDatas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            lineAverageDatas.add(new LineData(i, 7 * i));
        }
        stockChartAView.setLineAverageDatas(lineAverageDatas);


        List<BarData> barDatas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            barDatas.add(new BarData(i, i));
        }
        stockChartAView.setBarDatas(barDatas);
    }
}
