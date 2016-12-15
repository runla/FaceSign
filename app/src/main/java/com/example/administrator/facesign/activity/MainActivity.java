package com.example.administrator.facesign.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.administrator.facesign.MyLocationService;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.entity.MyLocation;
import com.example.administrator.facesign.entity.Student;
import com.example.administrator.facesign.fragment.CourseTableFragment;
import com.example.administrator.facesign.service.LongRunningService;
import com.example.administrator.facesign.util.ImageUtil;
import com.example.administrator.facesign.util.UrlUtil;
import com.example.administrator.facesign.vollery.AppController;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE = 1;
    private Toolbar toolbar;
    private CourseInfo courseInfo;
    private Student student;
    private String locationDes;
    //当前的位置信息
    private MyLocation myLocation = new MyLocation();

    private MainActivity.DataCallBack dataCallBack;

    private CircleImageView nav_image;

    private TextView nav_name;

    private TextView nav_department;

    public MainActivity.DataCallBack getDataCallBack() {
        return dataCallBack;
    }

    public void setDataCallBack(MainActivity.DataCallBack dataCallBack) {
        this.dataCallBack = dataCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        courseInfo = (CourseInfo) getIntent().getSerializableExtra("courseInfo");

        if (savedInstanceState != null&&courseInfo == null) {
            courseInfo = (CourseInfo) savedInstanceState.getSerializable("courseInfo");
        }



        //启动后台服务
        Intent intent = new Intent(MainActivity.this, LongRunningService.class);
        startService(intent);
    }


    protected void onStart() {
        super.onStart();
        initView();
        initFragment();
/*
        MyLocationService.getInstance(getApplicationContext()).start(mListener);
*/
        MyLocationService.getInstance(getApplicationContext()).getMyLocation(new MyLocationService.LocationCallbackListener() {
            @Override
            public void succssReceive(MyLocation location) {
                myLocation = location;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 初始化控件
     */
    private void initView(){
        student = courseInfo.getStudent();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        nav_image = (CircleImageView) headerView.findViewById(R.id.nav_image);
        nav_name = (TextView) headerView.findViewById(R.id.nav_tv_name);
        nav_department = (TextView) headerView.findViewById(R.id.nav_tv_department);

        nav_name.setText(student.getName());
        nav_department.setText(student.getMajor());
        //查看本地是否存在个人图片,有则从本地获取并显示，没有则从服务器中获取
        if (ImageUtil.isBitmapExist(ImageUtil.getPersonalImagePath(student.getStudentid()))){
            byte[] array = ImageUtil.imageProcessing(ImageUtil.getPersonalImagePath(student.getStudentid()));
            Bitmap bitmap_head = BitmapFactory.decodeByteArray(array,0,array.length);
            nav_image.setImageBitmap(bitmap_head);
        }
        else{
            getImage();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("课程表");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

     //   NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_localtion) {
            Intent intent = new Intent(MainActivity.this, LocationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("myLocation",myLocation);
            intent.putExtras(bundle);
            startActivityForResult(intent,REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //主页
        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_person) {//个人信息
            PersonalInfoActivity.actionStart(MainActivity.this,student);
        } else if (id == R.id.nav_sign) {//签到
            SignUpStatusActivity.actionStart(MainActivity.this,courseInfo,myLocation);
        } else if (id == R.id.nav_statistics) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initFragment(){

        FragmentManager fragmentManager = getSupportFragmentManager();
        CourseTableFragment tableFragment = new CourseTableFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("courseInfo",courseInfo);
        bundle.putParcelable("myLocation",myLocation);
        tableFragment.setArguments(bundle);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame_content,tableFragment);
        ft.commit();

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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("courseInfo",courseInfo);
    }

    public interface DataCallBack{
        void onDataChange(MyLocation myLocation);
    }

    /**
     * 从网络服务器中获取个人图片
     */
    private void getImage(){
        String urlPath = UrlUtil.getDownloadImageUrl(student.getStudentid());
        ImageRequest imageRequest = new ImageRequest(urlPath, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                nav_image.setImageBitmap(response);
                ImageUtil.saveBitmap(MainActivity.this,response,student.getStudentid());
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(imageRequest);
    }



}
