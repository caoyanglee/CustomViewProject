package io.weimu.www.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.weimu.www.R;
import io.weimu.www.widget.granule.GranuleGridView;

public class GranuleGridActivity extends AppCompatActivity {

    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, GranuleGridActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_granule_grid);
        GranuleGridView ggv = (GranuleGridView) findViewById(R.id.ggv);
        ggv.setGranuleNumber(50);
    }
}
