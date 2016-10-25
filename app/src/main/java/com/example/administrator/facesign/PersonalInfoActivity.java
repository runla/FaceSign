package com.example.administrator.facesign;

import android.os.Bundle;

import com.example.administrator.facesign.activity.BaseActivity;
import com.example.administrator.facesign.collector.ActivityCollector;

public class PersonalInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal_info);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
