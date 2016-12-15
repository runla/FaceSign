package com.example.administrator.facesign.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.adapter.RecyclerAdapter;
import com.example.administrator.facesign.entity.AttentionInfo;
import com.example.administrator.facesign.util.RecyclerItemClickListener;
import com.example.administrator.facesign.util.UrlUtil;
import com.example.administrator.facesign.util.Utility;
import com.example.administrator.facesign.vollery.AppController;

import java.util.ArrayList;
import java.util.List;

public class AttentionActivity extends BaseActivity {

    private ImageView btn_back;

    private TextView tv_title;

    private TextView tv_sure;
    //适配器
    private RecyclerAdapter mRecyclerAdapter;
    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    //数据
    private List<AttentionInfo> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
      //  ActivityCollector.addActivity(this);

        mData = new ArrayList<>();
        initView();
        initData();
        initEvent();

    }

    public static void actionStart(Context mContext){
        Intent intent = new Intent(mContext,AttentionActivity.class);
        mContext.startActivity(intent);
    }
    private void initView(){
        btn_back = (ImageView) findViewById(R.id.btn_back);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("考勤统计");
        tv_sure.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        //设置RecyclerView的样式,这里使用线性显示，功能类似ListView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AttentionActivity.this));
        //设置拉下刷新滚动条颜色
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    private void initData(){

        //初始化适配器
        mRecyclerAdapter = new RecyclerAdapter(AttentionActivity.this,mData);
        //为recyclerView设置适配器
        mRecyclerView.setAdapter(mRecyclerAdapter);
        if (mData.size() == 0) {
            mSwipeRefreshLayout.setRefreshing(true);
            getData();
        }
    }


    private void initEvent() {
        //RecyclerView item点击事件
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(AttentionActivity.this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               // AttentionInfo data = mData.get(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getData(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AttentionActivity.this);
        String studentId = preferences.getString("studentId","");
        String urlPath = UrlUtil.getAttentionInfoUrl(studentId);

        StringRequest stringRequest = new StringRequest(urlPath,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "onResponse: "+response);
                    mData = Utility.handleAttentionInfoList(response);

                    if (mRecyclerAdapter == null) {
                        //初始化适配器
                        mRecyclerAdapter = new RecyclerAdapter(AttentionActivity.this,mData);
                        //为recyclerView设置适配器
                        mRecyclerView.setAdapter(mRecyclerAdapter);
                    }
                    else{
                        //通知适配器更新数据
                        mRecyclerAdapter.refreshData(mData);
                    }

                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AttentionActivity.this, "网络连接有误", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, error.getMessage(), error);
                }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // ActivityCollector.removeActivity(this);
    }
}
