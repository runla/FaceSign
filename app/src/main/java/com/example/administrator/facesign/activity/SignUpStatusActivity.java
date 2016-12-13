package com.example.administrator.facesign.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.facesign.R;
import com.example.administrator.facesign.adapter.ViewPagerAdapter;
import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.entity.MyLocation;
import com.example.administrator.facesign.fragment.SignUpStatusFragment;
import com.example.administrator.facesign.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;


public class SignUpStatusActivity extends BaseActivity implements SignUpStatusFragment.OnFragmentInteractionListener{


    /**
     * viewPager适配器
     */
    private ViewPagerAdapter mViewPagerAdapter;

    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    private CourseInfo courseInfo;

    private MyLocation myLocation;

    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_status);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            courseInfo = (CourseInfo) bundle.getSerializable("courseInfo");
            myLocation = bundle.getParcelable("myLocation");
        }


        initView();
        initBackButton();
        initViewPager();
    }

    //启动activity
    public static void actionStart(Context mContext, CourseInfo courseInfo, MyLocation myLocation){
        Intent intent = new Intent(mContext,SignUpStatusActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("courseInfo",courseInfo);
        bundle.putParcelable("myLocation",myLocation);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("签到");
        mViewPager = (ViewPager) findViewById(R.id.vp_main_content);
        mTabLayout = (TabLayout) findViewById(R.id.tl_main_tabs);
    }
    //返回上一个activity
    private void initBackButton(){
        ImageView btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initViewPager(){
        final List<String> titleStr = new ArrayList<>();
        //实例化viewpager适配器
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        int currentWeek = TimeUtil.getCurrentWeek(courseInfo.getEduTerm().getStartDate());
        int singleOrDouble = currentWeek % 2 == 1 ? 1:2;//单周为1，双周为2
        int day = TimeUtil.getDay();//今天是星期几
        //初始化viewpager适配器
        for (Course course : courseInfo.getCourseList()) {
            Log.d(TAG,course.getCourseName());
            if (currentWeek <= course.getTotalWeeks()&&
                    singleOrDouble + course.getSingleOrDouble()!=3&&
                    day == course.getDay()) {
                titleStr.add(course.getCourseName());
                mViewPagerAdapter.addFragment(SignUpStatusFragment.newInstance(course,courseInfo.getStudent(),courseInfo.getEduTerm()),course.getStartSection()+" - "+(course.getStartSection()+course.getTotalSection()-1));
            }
        }

        //viewPager设置适配器
        mViewPager.setAdapter(mViewPagerAdapter);

        //tabLayout绑定viewpager
        mTabLayout.setupWithViewPager(mViewPager);

        if (titleStr.size() ==0){
            tv_title.setText("今天没有课");
        }
        else {
            tv_title.setText(titleStr.get(0));
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
               tv_title.setText(titleStr.get(position));
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
