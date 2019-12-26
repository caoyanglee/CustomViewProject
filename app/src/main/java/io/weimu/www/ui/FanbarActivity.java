package io.weimu.www.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.weimu.www.R;

public class FanbarActivity extends AppCompatActivity {
    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, FanbarActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanbar);
    }
}
