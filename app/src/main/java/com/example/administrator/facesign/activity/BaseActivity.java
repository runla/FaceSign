package com.example.administrator.facesign.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG,getClass().getSimpleName());
    }
}
