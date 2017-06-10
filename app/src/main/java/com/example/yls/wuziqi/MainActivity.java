package com.example.yls.wuziqi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
private  WuziqiPanel mWuziqiPanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWuziqiPanel= (WuziqiPanel) findViewById(R.id.wuziqi);
    }


}
