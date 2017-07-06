package com.fizzer.doraemon.countdownview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private CountDownView mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (CountDownView) findViewById(R.id.button);
        mButton.setCountDownListener(new CountDownView.CountDownListener() {
            @Override
            public void countDown(float progress) {
                Log.e("Fizzer",progress+"");
            }
        });
        mButton.start();
    }
}
