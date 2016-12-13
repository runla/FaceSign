package com.example.administrator.facesign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.example.administrator.facesign.MyLocationService;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.adapter.ViewPagerAdapter;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.entity.MyLocation;
import com.example.administrator.facesign.fragment.CourseTableFragment;
import com.example.administrator.facesign.fragment.Fragment_3;
import com.example.administrator.facesign.fragment.PersonalInfoFragment;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

public class MainActivity1 extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private ViewPager mViewPage;
    private ViewPagerAdapter mAdapter;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private CourseInfo courseInfo;

   // private TextView show_location;

    private String locationDes;
    //位置服务
  //  public LocationService locationService;

    //当前的位置信息
    private MyLocation myLocation = new MyLocation();

    private int[] color=new int[3];

    private DataCallBack dataCallBack;

    public DataCallBack getDataCallBack() {
        return dataCallBack;
    }

    public void setDataCallBack(DataCallBack dataCallBack) {
        this.dataCallBack = dataCallBack;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        courseInfo = (CourseInfo) getIntent().getSerializableExtra("courseInfo");

        if (savedInstanceState != null&&courseInfo == null) {
            courseInfo = (CourseInfo) savedInstanceState.getSerializable("courseInfo");
        }
        //注册服务


     }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initViewPager();
        initAction();
        MyLocationService.getInstance(getApplicationContext()).start(mListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("courseInfo",courseInfo);
    }

    private void initView(){
        mViewPage = (ViewPager) findViewById(R.id.vp_main_content);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
   //     show_location = (TextView) findViewById(R.id.show_location);
        //   linearLayout_table = (LinearLayout) findViewById(R.id.linear_table);
        toolbar.setTitle("课程表");
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity1.this,R.color.md_green_500));

    }
    private void initViewPager(){
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        CourseTableFragment tableFragment = new CourseTableFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("courseInfo",courseInfo);
        bundle.putParcelable("myLocation",myLocation);
        tableFragment.setArguments(bundle);
        mAdapter.addFragment(tableFragment,"课程表");

        Fragment_3 fragment_3 = new Fragment_3();
        mAdapter.addFragment(fragment_3,"考勤");


        PersonalInfoFragment personalInfoFragment = new PersonalInfoFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("student",courseInfo.getStudent());
        personalInfoFragment.setArguments(bundle3);
        mAdapter.addFragment(personalInfoFragment,"我");

        mViewPage.setAdapter(mAdapter);

        int[] image = {R.drawable.ic_content_paste, R.drawable.ic_book_black_24dp,
                R.drawable.ic_person_outline};

         int[] color = {ContextCompat.getColor(this, R.color.md_green_700), ContextCompat.getColor(this, R.color.md_light_green_700),
                ContextCompat.getColor(this, R.color.md_lime_700)};

        this.color = color;
        bottomNavigationView.setUpWithViewPager(mViewPage, color, image);

    }

    private void initAction(){
        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                switch (index){
                    case 0:
                        toolbar.setTitle("课程表");
                        toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity1.this,R.color.md_green_500));
                        break;
                    case 1:
                        toolbar.setTitle("考勤");
                        toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity1.this,R.color.md_light_green_500));
                        break;
                    case 2:
                        toolbar.setTitle("个人信息");
                        toolbar.setBackgroundColor(ContextCompat.getColor(MainActivity1.this,R.color.md_lime_500));
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.location_item:
                Intent intent = new Intent(MainActivity1.this, LocationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("myLocation",myLocation);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE);
              //  LocationActivity.actionStart(this,myLocation);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取从现实地理信息的activity获取信息
        if (requestCode == REQUEST_CODE && resultCode == LocationActivity.RESULT_CODE){
            Bundle bundle = data.getExtras();
            myLocation = bundle.getParcelable("myLocation");
            dataCallBack.onDataChange(myLocation);
        }
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());

                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\nProvince : ");//省
                sb.append(location.getProvince());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\nbuildingId : ");
                sb.append(location.getBuildingID());
                sb.append("\nbuildingName : ");
                sb.append(location.getBuildingName());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息

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
       //         show_location.setText(myLocation.getLocationdescribe());
                MyLocationService.getInstance(getApplicationContext()).stop(mListener);
                dataCallBack.onDataChange(myLocation);

            }
        }

    };


    public interface DataCallBack{
        void onDataChange(MyLocation myLocation);
    }
}
