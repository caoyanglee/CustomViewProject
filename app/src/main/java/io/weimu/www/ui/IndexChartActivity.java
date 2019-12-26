package io.weimu.www.ui;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import io.weimu.www.R;

public class IndexChartActivity extends AppCompatActivity {
    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, IndexChartActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_chart);
    }
}
