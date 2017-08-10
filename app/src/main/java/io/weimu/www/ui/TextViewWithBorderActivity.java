package io.weimu.www.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.weimu.www.R;

public class TextViewWithBorderActivity extends AppCompatActivity {
    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, TextViewWithBorderActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_view_with_border);
    }
}
