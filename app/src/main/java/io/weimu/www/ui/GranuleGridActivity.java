package io.weimu.www.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.weimu.www.R;

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
    }
}
