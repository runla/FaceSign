package com.example.administrator.facesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.facesign.activity.BaseActivity;
import com.example.administrator.facesign.collector.ActivityCollector;

public class LoadingActivity extends BaseActivity {

    private TextView text_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ActivityCollector.addActivity(this);
        Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
        startActivity(intent);
        //Intent intent
        text_back = (TextView) findViewById(R.id.text_back);
        text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
