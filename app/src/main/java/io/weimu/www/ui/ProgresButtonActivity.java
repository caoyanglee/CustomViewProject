package io.weimu.www.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import io.weimu.www.R;
import io.weimu.www.TestAreaActivity;
import io.weimu.www.widget.ProgressButton;

public class ProgresButtonActivity extends AppCompatActivity {

    //跳转
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, ProgresButtonActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progres_button);

        ProgressButton pb = (ProgressButton) findViewById(R.id.pt);
        pb.runAnimation();


        pb.setOnAnimationFinishListener(new ProgressButton.OnAnimationFinishListener() {
            @Override
            public void onFinish() {
                Toast.makeText(ProgresButtonActivity.this, "结束了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
