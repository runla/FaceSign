package com.example.administrator.facesign.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.facesign.MyLocationService;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.entity.MyLocation;


public class LocationActivity extends BaseActivity implements View.OnClickListener{

    public static final int RESULT_CODE = 2;
    private MyLocation myLocation;
    private TextView showLocation;


    private ImageView img_back;
    private TextView tv_title;
    private TextView tv_sure;

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        intent = getIntent();
        if (intent!=null) {
            Bundle bundle = intent.getExtras();
            myLocation = bundle.getParcelable("myLocation");
        }

        initView();
        initData();
    }
    private void initView(){
        showLocation = (TextView) findViewById(R.id.textView);
        img_back = (ImageView) findViewById(R.id.btn_back);
        tv_sure = (TextView) findViewById(R.id.tv_sure);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("位置");
        tv_sure.setText("刷新");
        img_back.setOnClickListener(this);
        tv_sure.setOnClickListener(this);

    }
    private void initData(){
        if (myLocation != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(myLocation.getAddr()+"\n");
            sb.append(myLocation.getLocationdescribe()+"\n");
            showLocation.setText(sb.toString());
        }
        else{
            showLocation.setText("还没有位置信息");
        }
    }
    public static void actionStart(Context context, MyLocation myLocation){
        Intent intent = new Intent(context, LocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myLocation",myLocation);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public void actionStartForResult(Context context, MyLocation myLocation, int request_code){
        Intent intent = new Intent(context, LocationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("myLocation",myLocation);
        intent.putExtras(bundle);
        startActivityForResult(intent,request_code);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_back:
                /*Intent intent = new Intent(LocationActivity.this, MainActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("myLocation",myLocation);
                intent.putExtras(bundle);*/
                setResult(RESULT_CODE,intent);
                finish();

                break;

            //刷新
            case R.id.tv_sure:
                showLocation.setText("重新定位中......");

                //获取当前位置信息
                MyLocationService.getInstance(getApplicationContext()).getMyLocation(new MyLocationService.LocationCallbackListener() {
                    @Override
                    public void succssReceive(MyLocation location) {
                        myLocation = location;
                        initData();
                    }
                });

                break;
        }
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
   /* private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                *//**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 *//*
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());

                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息

                //

                myLocation.setTime(location.getTime());
                myLocation.setErrorCode(location.getLocType());
                myLocation.setLatitude(location.getLatitude());
                myLocation.setLongitude(location.getLongitude());
                myLocation.setCity(location.getCity());
                myLocation.setDistrict(location.getDistrict());
                myLocation.setStreet(location.getStreet());
                myLocation.setLocationdescribe(location.getLocationDescribe());
                myLocation.setProvince(location.getProvince());
                myLocation.setBuildingID(location.getBuildingID());
                myLocation.setBuildingName(location.getBuildingName());
                myLocation.setFloor(location.getFloor());
                myLocation.setAddr(location.getAddrStr());

                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                    myLocation.setDescribe("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果

                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                    myLocation.setDescribe("网络定位成功");

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                    myLocation.setDescribe("离线定位成功，离线定位结果也是有效的");

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    myLocation.setDescribe("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                    myLocation.setDescribe("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    myLocation.setDescribe("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                Log.d("BaiduLocationApiDem",sb.toString());
                initData();
                MyLocationService.getInstance(getApplicationContext()).stop(mListener);
            }
        }

    };*/
}
