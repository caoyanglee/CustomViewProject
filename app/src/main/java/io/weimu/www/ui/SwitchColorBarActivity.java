package io.weimu.www.ui;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.weimu.www.R;
import io.weimu.www.widget.SwitchColorBar;

public class SwitchColorBarActivity extends AppCompatActivity {
    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, SwitchColorBarActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_color_bar);

        SwitchColorBar sw = (SwitchColorBar) findViewById(R.id.switchBar);
        final TextView tv = (TextView) findViewById(R.id.tv);
        sw.setValueChangeListener(new SwitchColorBar.ValueChangeListener() {
            @Override
            public void onValeChange(int value) {
                tv.setText("金额" + value + "万元");
            }
        });
    }
}
