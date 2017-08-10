package io.weimu.www.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.weimu.www.R;
import io.weimu.www.widget.SwitchColorBar;

public class SwitchColorBarActivity extends AppCompatActivity {

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
