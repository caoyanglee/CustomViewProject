package io.weimu.www;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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


    }


}
