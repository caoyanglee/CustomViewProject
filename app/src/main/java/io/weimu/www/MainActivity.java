package io.weimu.www;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.weimu.www.bean.BarData;
import io.weimu.www.bean.LineData;
import io.weimu.www.widget.StockChartAView;
import io.weimu.www.widget.WaveView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        WaveView wave = (WaveView) findViewById(R.id.wave);
//        wave.setCurrentProgress(80);

//        StockChartAView stockChartAView = (StockChartAView) findViewById(R.id.stockView);
//        List<LineData> lineDatas = new ArrayList<>();
//        for (int i=0;i<30;i++){
//            lineDatas.add(new LineData(i, i*i));
//        }
//        stockChartAView.setLineDatas(lineDatas);
//
//
//        List<LineData> lineAverageDatas = new ArrayList<>();
//        for (int i=0;i<30;i++){
//            lineAverageDatas.add(new LineData(i, 7*i));
//        }
//        stockChartAView.setLineAverageDatas(lineAverageDatas);
//
//
//        List<BarData> barDatas = new ArrayList<>();
//        for (int i=0;i<30;i++){
//            barDatas.add(new BarData(i, i));
//        }
//        stockChartAView.setBarDatas(barDatas);


    }
}
