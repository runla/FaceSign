package com.example.administrator.facesign.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.facesign.MyLocationService;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.activity.ShowActivity;
import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.EduTerm;
import com.example.administrator.facesign.entity.MyLocation;
import com.example.administrator.facesign.entity.SignStatus;
import com.example.administrator.facesign.entity.Student;
import com.example.administrator.facesign.util.SignUpSharePreference;
import com.example.administrator.facesign.util.TimeUtil;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpStatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpStatusFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "course";
    private static final String ARG_PARAM2 = "student";
    private static final String ARG_PARAM3 = "eduTerm";

    // TODO: Rename and change types of parameters
    private Course course;
    private Student student;
    private EduTerm eduTerm;//开学日期和学期结束日期

    private OnFragmentInteractionListener mListener;

    private View view;

    //显示课程名、星期几
    private TextView tv_course;

    //确定按钮
    private TextView btn_sure;

    //------------第一次 上课签到-------------//
    //显示上课时间（签到后添加签到时间）
    private TextView tv_up_time;
    //显示签到后的位置
    private TextView tv_up_location;
    //显示签到后的状态（正常、迟到、早退、旷课......）
    private TextView tv_up_status;
    //显示目前获取到的位置信息
    private TextView tv_up_current_loaction;
    //进行签到的按钮
    private Button btn_up_sign;
    //位置刷新按钮
    private Button btn_up_refresh;
    //签到前显示签到按钮及当前位置的LinearLayout
    private LinearLayout line_up_action;
    //显示数据上传的状态
    private TextView tv_up_upload;
    //显示人脸识别的状态
    private TextView tv_up_recognize;

    //------------第二次 下课签到-------------//
    //显示上课时间（签到后添加签到时间）
    private TextView tv_down_time;
    //显示签到后的位置
    private TextView tv_down_location;
    //显示签到后的状态（正常、迟到、早退、旷课......）
    private TextView tv_down_status;
    //显示目前获取到的位置信息
    private TextView tv_down_current_loaction;
    //进行签到的按钮
    private Button btn_down_sign;
    //位置刷新按钮
    private Button btn_down_refresh;
    //签到前显示签到按钮及当前位置的LinearLayout
    private LinearLayout line_down_action;
    //显示数据上传的状态
    private TextView tv_down_upload;
    //显示人脸识别的状态
    private TextView tv_down_recognize;

    //秒表计时器
    private Timer timerSecond;

    //定时任务的计时器
    private Timer myTimer;

    //第一次签到的位置
    private MyLocation firstLoctation=new MyLocation();
    //第二次签到的位置
    private MyLocation secondLocation =new MyLocation();
    //当前位置
    private MyLocation currentLoction=new MyLocation();

    private static final int FIRST_REQUEST_CODE = 123;
    private static final int SECOND_REQUEST_CODE = 124;



    private SignUpSharePreference mSignUpSharePreference;
    /**
     * 保存第一次签到的状态
     */
    private SignStatus firstSignStatusInfo;
    /**
     * 保存第二次签到的状态
     */
    private SignStatus secondSignStatusInfo;
    /**
     * 现在是第几次签到
     */
    private int currentSignTimes;

    public SignUpStatusFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SignUpStatusFragment newInstance(Course course, Student student, EduTerm eduTerm) {
        SignUpStatusFragment fragment = new SignUpStatusFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, course);
        args.putSerializable(ARG_PARAM2, student);
        args.putSerializable(ARG_PARAM3,eduTerm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course = (Course) getArguments().getSerializable(ARG_PARAM1);
            student = (Student) getArguments().getSerializable(ARG_PARAM2);
            eduTerm = (EduTerm) getArguments().getSerializable(ARG_PARAM3);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //恢复销毁前的状态
        if (savedInstanceState != null) {
            firstLoctation = savedInstanceState.getParcelable("firstLocation");
            secondLocation = savedInstanceState.getParcelable("secondLocation");
        }

        view = inflater.inflate(R.layout.activity_sign_up_status_content, container, false);

        mSignUpSharePreference = new SignUpSharePreference(getActivity(),TimeUtil.getCurrentWeek(eduTerm.getStartDate()),course.getDay(),course.getStartSection());
        currentSignTimes = mSignUpSharePreference.getSignTimes();
        //Toast.makeText(getActivity(), ""+currentSignTimes, Toast.LENGTH_SHORT).show();
        initView();

        initShowData();
        //定时器
       // startTimerSecond();

        startMyTimer();
        initAction();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("firstLocation",firstLoctation);
        outState.putParcelable("secondLocation", secondLocation);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initView(){
        TextView tv_course;
        //tv_course = (TextView) view.findViewById(R.id.tv_course_info);
        btn_sure = (TextView) getActivity().findViewById(R.id.tv_sure);
        //------------第一次 上课签到-------------//
        tv_up_time = (TextView) view.findViewById(R.id.tv_up_time);
        tv_up_location = (TextView) view.findViewById(R.id.tv_up_location);
        tv_up_status = (TextView) view.findViewById(R.id.tv_up_status);
        tv_up_current_loaction = (TextView) view.findViewById(R.id.tv_up_current_loaction);
        btn_up_sign = (Button) view.findViewById(R.id.btn_up);
        btn_up_refresh = (Button) view.findViewById(R.id.btn_up_refresh);
        line_up_action = (LinearLayout) view.findViewById(R.id.line_up_action);
        tv_up_upload = (TextView) view.findViewById(R.id.tv_up_upload);
        tv_up_recognize = (TextView) view.findViewById(R.id.tv_up_recognize);

        tv_down_time = (TextView) view.findViewById(R.id.tv_down_time);
        tv_down_location = (TextView) view.findViewById(R.id.tv_down_location);
        tv_down_status = (TextView) view.findViewById(R.id.tv_down_status);
        tv_down_current_loaction = (TextView) view.findViewById(R.id.tv_down_current_loaction);
        btn_down_sign = (Button) view.findViewById(R.id.btn_down);
        btn_down_refresh = (Button) view.findViewById(R.id.btn_down_refresh);
        line_down_action = (LinearLayout) view.findViewById(R.id.line_down_action);
        tv_down_upload = (TextView) view.findViewById(R.id.tv_down_upload);
        tv_down_recognize = (TextView) view.findViewById(R.id.tv_down_recognize);

    }

    /**
     * 初始化显示数据
     */
    public void initShowData() {
        btn_up_sign.setText("上课签到\n"+TimeUtil.getCurrentTime());
        btn_down_sign.setText("下课签到\n"+TimeUtil.getCurrentTime());


        signUpShow();
        signDownShow();
    }

    private Handler hander = new Handler(Looper.getMainLooper());
    /**
     * 启动秒表计时器
     */
    public void startTimerSecond(){
        timerSecond = new Timer();
        Date date = new Date(System.currentTimeMillis()-5000);
        timerSecond.schedule(new TimerTask() {
            @Override
            public void run() {
                //运行在主线程
                hander.post(new Runnable() {
                    @Override
                    public void run() {
                        btn_up_sign.setText("上课签到\n"+TimeUtil.getCurrentTime());
                        btn_down_sign.setText("下课签到\n"+TimeUtil.getCurrentTime());
                    }
                });
            }
        },date,1000);
    }


    private boolean flag = false;
    /**
     * 定时任务
     */
    public void startMyTimer(){
        myTimer = new Timer();
        //开始上课日期
        Date startDate = eduTerm.getStartDate();
        //当前周
        int currentWeek = TimeUtil.getCurrentWeek(startDate);
        //上课的时间
        final long classUpTime = TimeUtil.getClassUpTime(startDate,currentWeek-1,course.getDay(),course.getStartSection());
        Date classUpDate = new Date(classUpTime);
        //下课时间
        final long classDownTime = TimeUtil.getClassDownTime(startDate,currentWeek-1,course.getDay(),course.getStartSection()+course.getTotalSection()-1);
        Date classDownDate = new Date(classDownTime);
        //定时器启动的时间
        Date timerStartDate = new Date(classUpTime-10*TimeUtil.M);
        //Date timerStartDate = new Date();

        //指定在上课前10分钟开始执行,每隔 1s 执行一次
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                hander.post(new Runnable() {
                    @Override
                    public void run() {
                        //当前时间
                        long currentTime = new Date().getTime();

                        //秒表的功能
                        btn_up_sign.setText("上课签到\n"+TimeUtil.getCurrentTime());
                        btn_down_sign.setText("下课签到\n"+TimeUtil.getCurrentTime());

                        //显示上课签到按钮（上课前10分钟  ---  下课前10分钟）
                        if (classUpTime - 10 * TimeUtil.M <= currentTime && currentTime < classDownTime - 10* TimeUtil.M){
                       //     Log.d("1", "run: 11111111111");
                            if (line_up_action.getVisibility() != View.VISIBLE) {
                                //刷新控件显示
                                initShowData();
                                line_up_action.setVisibility(View.VISIBLE);
                            }
                        }
                        //显示下课签到按钮（下课前10分钟  ---  下课后10分钟）
                        else if (classDownTime - 10* TimeUtil.M <= currentTime && currentTime <= classDownTime + 10* TimeUtil.M){
                     //       Log.d("1", "run: 2222222222");
                            //上课没有签到，记录未旷课
                            if (currentSignTimes == 0 && !flag){
                                //当前签到的次数
                                currentSignTimes = TimeUtil.FIRST_SIGN_UP;
                                firstSignStatusInfo = new SignStatus(TimeUtil.STATUS_ABSENT,-1,-1,-1L,-1D,-1D,"未知地理位置");
                                //保存第 1 次上传的状态
                                mSignUpSharePreference.saveFirstSignStatus(secondSignStatusInfo,currentSignTimes);
                                flag = true;

                            }

                            //刷新控件显示
                            initShowData();
                            line_up_action.setVisibility(View.GONE);
                            line_down_action.setVisibility(View.VISIBLE);

                        }
                        //超过上课的时间了
                        else if (currentTime > classDownTime + 10* TimeUtil.M){
                      //      Log.d("1", "run: 33333333333");
                            //上课没有签到，记录未旷课
                            if (currentSignTimes == 0){
                                //当前签到的次数
                                currentSignTimes = TimeUtil.FIRST_SIGN_UP;
                                firstSignStatusInfo = new SignStatus(TimeUtil.STATUS_ABSENT,-1,-1,-1L,-1D,-1D,"未知地理位置");
                                //保存第 1 次上传的状态
                                mSignUpSharePreference.saveFirstSignStatus(firstSignStatusInfo,currentSignTimes);
                            }

                            //下课没有签到，记录未早退
                            if (currentSignTimes != TimeUtil.SECOND_SIGN_DOWN) {
                                //当前签到的次数
                                currentSignTimes = TimeUtil.SECOND_SIGN_DOWN;
                                secondSignStatusInfo = new SignStatus(TimeUtil.STATUS_EARLY,-1,-1,-1L,-1D,-1D,"未知地理位置");
                                //保存第 2次上传的状态
                                mSignUpSharePreference.saveSecondSignStatus(secondSignStatusInfo,currentSignTimes);
                            }

                            //停止计时器
                            myTimer.cancel();

                            //刷新控件显示
                            initShowData();
                            line_down_action.setVisibility(View.GONE);

                        }

                    }
                });
            }
        },timerStartDate,1000);
    }

    /**
     * 上课签到部分
     */
   /* public void preSignUpShow(){

        //显示要上课的时间
        String startTime = TimeUtil.getClassStartTime(course.getStartSection());
        tv_up_time.setText("上课时间"+startTime);
        //下课的节数
        int endSection = course.getStartSection()+course.getTotalSection()-1;
        //显示要下课的时间
        String endTime = TimeUtil.getClassEndTime(endSection);
        tv_down_time.setText("下课时间"+endTime);
        //显示当前的位置
        //tv_up_current_loaction.setText("当前位置："+currentLoction.getAddr());

        //获取当前的位置
        MyLocationService.getInstance(getActivity()).getMyLocation(new MyLocationService.LocationCallbackListener() {
            @Override
            public void succssReceive(MyLocation myLocation) {
                Toast.makeText(getActivity(), myLocation.getAddr(), Toast.LENGTH_SHORT).show();
                firstLoctation = myLocation;
                tv_up_current_loaction.setText("当前位置："+firstLoctation.getAddr());
            }
        });

        //控件设置可见
        tv_up_time.setVisibility(View.VISIBLE);
        tv_down_time.setVisibility(View.VISIBLE);
        line_up_action.setVisibility(View.VISIBLE);

        //控件设置不可见
        tv_up_location.setVisibility(View.INVISIBLE);
        tv_up_status.setVisibility(View.INVISIBLE);
        tv_down_location.setVisibility(View.INVISIBLE);
        tv_down_status.setVisibility(View.INVISIBLE);
        line_down_action.setVisibility(View.INVISIBLE);

    }
*/


    /**
     * 上课签到部分
     */
    public void signUpShow(){
        SignStatus status = mSignUpSharePreference.loadFristSignStatus();
        Date upLoadDate = new Date(status.getUploadTime());

        //还没有进行签到
        if (currentSignTimes == 0){
            //上课的时间
            String startTime = TimeUtil.getClassStartTime(course.getStartSection());
            tv_up_time.setText("上课时间"+startTime);

            if (firstLoctation.getAddr() != null){
                tv_up_current_loaction.setText("当前位置："+firstLoctation.getAddr());
            }
            else {
                //获取当前位置并显示
                MyLocationService.getInstance(getActivity()).getMyLocation(new MyLocationService.LocationCallbackListener() {
                    @Override
                    public void succssReceive(MyLocation myLocation) {
                        firstLoctation = myLocation;
                        tv_up_current_loaction.setText("当前位置："+firstLoctation.getAddr());
                        // btn_up_refresh.setVisibility(View.VISIBLE);
                    }
                });
            }



            //控件设置可见
            tv_up_time.setVisibility(View.VISIBLE);
            line_up_action.setVisibility(View.INVISIBLE);
            //控件设置为不可见
            tv_up_upload.setVisibility(View.GONE);
            tv_up_recognize.setVisibility(View.GONE);
            tv_up_location.setVisibility(View.GONE);
            tv_up_status.setVisibility(View.GONE);
        }
        //已经签到过了
        else {
            //上课的时间
            String startTime = TimeUtil.getClassStartTime(course.getStartSection());
            //签到时间
            String signTime = String.format("签到时间%tH:%tM",upLoadDate,upLoadDate);
            if (status.getUploadTime() == -1L){
                tv_up_time.setText("上课时间"+startTime);
            }
            else{
                tv_up_time.setText(signTime+"(上课时间"+startTime+")");
            }

            //显示上课签到时的位置
            tv_up_location.setText(status.getAddr());

           // btn_up_refresh.setVisibility(View.GONE);
            //获取当前位置并显示
            /*MyLocationService.getInstance(getActivity()).getMyLocation(new MyLocationService.LocationCallbackListener() {
                @Override
                public void succssReceive(MyLocation myLocation) {
                    firstLoctation = myLocation;
                    tv_up_current_loaction.setText("当前位置："+firstLoctation.getAddr());
                  //  btn_up_refresh.setVisibility(View.VISIBLE);
                }
            });*/

            //*********************上课签到状态*************************************
            switch (status.getSignUpStatus()){
                //正常
                case TimeUtil.STATUS_NORMAL:
                    tv_up_status.setText("正常");
                    tv_up_status.setBackgroundResource(R.drawable.sign_status_normal);
                    break;
                //迟到
                case TimeUtil.STATUS_LATE:
                    tv_up_status.setText("迟到");
                    tv_up_status.setBackgroundResource(R.drawable.sign_status_late);
                    break;
                //早退
                case TimeUtil.STATUS_EARLY:
                    tv_up_status.setText("早退");
                    tv_up_status.setBackgroundResource(R.drawable.sign_status_early);
                    break;
                //旷课
                case TimeUtil.STATUS_ABSENT:
                    tv_up_status.setText("旷课");
                    tv_up_status.setBackgroundResource(R.drawable.sign_status_absent);
                    break;

                //为 -1 或者是其他状态
                default:
                    tv_up_status.setText("异常");
                    tv_up_status.setBackgroundResource(R.drawable.sign_status_absent);
                    break;
            }
            //*********************人脸识别状态*************************************
            //人脸识别成功
            if (status.getRecognizeStatus()== TimeUtil.RECOGNIZE_SUCCESS) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_normal_blue);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_up_recognize.setCompoundDrawables(null,null,drawable,null);
                tv_up_recognize.setVisibility(View.VISIBLE);
            }
            //人脸识别失败
            else if (status.getRecognizeStatus() == TimeUtil.RECOGNIZE_FAIL){
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_error);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_up_recognize.setCompoundDrawables(null,null, drawable,null);
                tv_up_recognize.setVisibility(View.VISIBLE);
            }
            //为其他状态（-1）表示还没有进行人脸识别
            else{
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_abnormal);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_up_recognize.setCompoundDrawables(null,null, drawable,null);
                tv_up_recognize.setVisibility(View.GONE);
            }
            //*********************数据上传状态*************************************
            //数据上传成功
            if (status.getUploadStatus() == TimeUtil.UPLOAD_SUCCESS) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_normal_blue);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_up_upload.setCompoundDrawables(null,null, drawable,null);
                tv_up_upload.setVisibility(View.VISIBLE);
            }
            //数据上传失败
            else if (status.getUploadStatus() == TimeUtil.UPLOAD_FAIL) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_error);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_up_upload.setCompoundDrawables(null,null, drawable,null);
                tv_up_upload.setVisibility(View.VISIBLE);

            }
            //为其他状态（-1）表示数据还没有上传
            else {
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_abnormal);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_up_upload.setCompoundDrawables(null,null, drawable,null);
                tv_up_upload.setVisibility(View.GONE);
            }

            //*********************控件显示状态*************************************
            //控件设置可见
            tv_up_time.setVisibility(View.VISIBLE);
            tv_up_location.setVisibility(View.VISIBLE);
            tv_up_status.setVisibility(View.VISIBLE);
            //控件设置为不可见
            line_up_action.setVisibility(View.GONE);
        }

    }



    /*
    * 下课签到显示的view
    */
    public void signDownShow(){
        SignStatus status = mSignUpSharePreference.laodSecondSignStatus();
        Date uploadTime = new Date(status.getUploadTime());
        int endSection = course.getStartSection()+course.getTotalSection()-1;

        //还没有进行签到
        if (currentSignTimes == 0){
            //上课的时间
            String startTime = TimeUtil.getClassEndTime(endSection);
            tv_down_time.setText("下课时间"+startTime);

            //控件设置可见
            tv_down_time.setVisibility(View.VISIBLE);
            //控件设置为不可见
            line_down_action.setVisibility(View.INVISIBLE);
            tv_down_upload.setVisibility(View.GONE);
            tv_down_recognize.setVisibility(View.GONE);
            tv_down_location.setVisibility(View.GONE);
            tv_down_status.setVisibility(View.GONE);
        }
        else if (currentSignTimes == TimeUtil.FIRST_SIGN_UP){
            //上课的时间
            String startTime = TimeUtil.getClassEndTime(endSection);
            tv_down_time.setText("下课时间"+startTime);

            if (secondLocation.getAddr() != null){
                tv_down_current_loaction.setText("当前位置："+secondLocation.getAddr());
            }
            else{
                //获取当前位置并显示
                MyLocationService.getInstance(getActivity()).getMyLocation(new MyLocationService.LocationCallbackListener() {
                    @Override
                    public void succssReceive(MyLocation myLocation) {
                        secondLocation = myLocation;
                        tv_down_current_loaction.setText("当前位置："+ secondLocation.getAddr());
                    }
                });
            }



            //控件设置可见
            tv_down_time.setVisibility(View.VISIBLE);
            line_down_action.setVisibility(View.INVISIBLE);
            //控件设置为不可见
            tv_down_upload.setVisibility(View.GONE);
            tv_down_recognize.setVisibility(View.GONE);
            tv_down_location.setVisibility(View.GONE);
            tv_down_status.setVisibility(View.GONE);
        }
        //已经签到过了
        else if (currentSignTimes == TimeUtil.SECOND_SIGN_DOWN){
            //上课的时间
            String startTime = TimeUtil.getClassEndTime(endSection);
            //签到时间
            String signTime = String.format("签到时间%tH:%tM",uploadTime,uploadTime);
            if (status.getUploadTime() == -1L){
                tv_down_time.setText("下课时间"+startTime);
            }
            else {
                tv_down_time.setText(signTime+"(下课时间"+startTime+")");
            }
            //显示上课签到时的位置
            tv_down_location.setText(status.getAddr());
            //获取当前位置并显示
            /*MyLocationService.getInstance(getActivity()).getMyLocation(new MyLocationService.LocationCallbackListener() {
                @Override
                public void succssReceive(MyLocation myLocation) {
                    secondLocation = myLocation;
                    tv_down_current_loaction.setText("当前位置："+secondLocation.getAddr());
                }
            });*/

            //*********************上课签到状态*************************************
            switch (status.getSignUpStatus()){
                //正常
                case TimeUtil.STATUS_NORMAL:
                    tv_down_status.setText("正常");
                    tv_down_status.setBackgroundResource(R.drawable.sign_status_normal);
                    break;
                //迟到
                case TimeUtil.STATUS_LATE:
                    tv_down_status.setText("迟到");
                    tv_down_status.setBackgroundResource(R.drawable.sign_status_late);
                    break;
                //早退
                case TimeUtil.STATUS_EARLY:
                    tv_down_status.setText("早退");
                    tv_down_status.setBackgroundResource(R.drawable.sign_status_early);
                    break;
                //旷课
                case TimeUtil.STATUS_ABSENT:
                    tv_down_status.setText("旷课");
                    tv_down_status.setBackgroundResource(R.drawable.sign_status_absent);
                    break;

                //为 -1 或者是其他状态
                default:
                    tv_down_status.setText("异常");
                    tv_down_status.setBackgroundResource(R.drawable.sign_status_absent);
                    break;
            }
            //*********************人脸识别状态*************************************
            //人脸识别成功
            if (status.getRecognizeStatus()== TimeUtil.RECOGNIZE_SUCCESS) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_normal_blue);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_down_recognize.setCompoundDrawables(null,null,drawable,null);
                tv_down_recognize.setVisibility(View.VISIBLE);
            }
            //人脸识别失败
            else if (status.getRecognizeStatus() == TimeUtil.RECOGNIZE_FAIL){
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_error);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_down_recognize.setCompoundDrawables(null,null,drawable,null);
                tv_down_recognize.setVisibility(View.VISIBLE);
            }
            //为其他状态（-1）表示还没有进行人脸识别
            else{
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_abnormal);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_down_recognize.setCompoundDrawables(null,null,drawable,null);
                tv_down_recognize.setVisibility(View.GONE);
            }
            //*********************数据上传状态*************************************
            //数据上传成功
            if (status.getUploadStatus() == TimeUtil.UPLOAD_SUCCESS) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_normal_blue);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_down_upload.setCompoundDrawables(null,null,drawable,null);
                tv_down_upload.setVisibility(View.VISIBLE);
            }
            //数据上传失败
            else if (status.getUploadStatus() == TimeUtil.UPLOAD_FAIL) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_error);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_down_upload.setCompoundDrawables(null,null,drawable,null);
                tv_down_upload.setVisibility(View.VISIBLE);
            }
            //为其他状态（-1）表示数据还没有上传
            else {
                Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.status_abnormal);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                tv_down_upload.setCompoundDrawables(null,null,drawable,null);
                tv_down_upload.setVisibility(View.GONE);
            }

            //*********************控件显示状态*************************************
            //控件设置可见
            tv_down_time.setVisibility(View.VISIBLE);
            tv_down_location.setVisibility(View.VISIBLE);
            tv_down_status.setVisibility(View.VISIBLE);
            //控件设置为不可见
            line_down_action.setVisibility(View.GONE);
        }

    }



    public void initAction(){
        btn_sure.setOnClickListener(this);
        btn_up_sign.setOnClickListener(this);
        btn_up_refresh.setOnClickListener(this);
        btn_down_sign.setOnClickListener(this);
        btn_down_refresh.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_up:
                signUpAction();
                break;
            case R.id.btn_up_refresh:
                signUpRefreshAction();
                break;
            case R.id.btn_down:
                signDownAction();
                break;
            case R.id.btn_down_refresh:
                signDwonRefreshAction();
                break;
            case R.id.tv_sure:
                sureAction();
                break;
            default:
                break;
        }
    }


    //确定按钮触发事件
    public void sureAction(){
        //preSignUpShow();
        Toast.makeText(getActivity(), firstLoctation.getAddr(), Toast.LENGTH_SHORT).show();
    }


    //上课签到按钮触发事件
    public void signUpAction(){
        Date startDate = eduTerm.getStartDate();
        int currentWeek = TimeUtil.getCurrentWeek(startDate);
        int endSec = course.getDay()+course.getTotalSection()-1;
        /*if (TimeUtil.getSignUpStatus(startDate,currentWeek,course.getDay(),course.getStartSection(),endSec) == -1){
            Toast.makeText(getActivity(), "现在不是上课签到时间", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (firstLoctation != null){
            //在启动Activity的意图中标记这是第 1 次签到
            Intent intent = ShowActivity.getActionStatIntent(getActivity(),course,student,eduTerm,firstLoctation,1);
            startActivityForResult(intent,FIRST_REQUEST_CODE);
        }
        else{
            Toast.makeText(getActivity(), "位置未更新", Toast.LENGTH_SHORT).show();
            return ;
        }

    }

    //位置刷新事件
    public void signUpRefreshAction(){
        btn_up_refresh.setVisibility(View.GONE);
        tv_up_current_loaction.setText("正在获取当前位置......");
        //获取位置
        MyLocationService.getInstance(getActivity()).getMyLocation(new MyLocationService.LocationCallbackListener() {
            @Override
            public void succssReceive(MyLocation myLocation) {
                firstLoctation = myLocation;
                tv_up_current_loaction.setText("当前位置："+firstLoctation.getAddr());
                btn_up_refresh.setVisibility(View.VISIBLE);
            }
        });
    }


    //下课签到按钮触发事件
    public void signDownAction(){
        Date startDate = eduTerm.getStartDate();
        int currentWeek = TimeUtil.getCurrentWeek(startDate);
        int endSec = course.getDay()+course.getTotalSection()-1;
       /* if (TimeUtil.getSignDownStatus(startDate,currentWeek,course.getDay(),course.getStartSection(),endSec) == -1){
            Toast.makeText(getActivity(), "现在不是下课签到时间", Toast.LENGTH_SHORT).show();
            return;
        }*/
        //在启动Activity的意图中标记这是第 2 次签到（下课签到)
        if (secondLocation != null){
            Intent intent = ShowActivity.getActionStatIntent(getActivity(),course,student,eduTerm, secondLocation,2);
            startActivityForResult(intent,SECOND_REQUEST_CODE);
        }
        else{
            Toast.makeText(getActivity(), "位置未更新", Toast.LENGTH_SHORT).show();
            return;
        }

    }


    //位置刷新事件
    public void signDwonRefreshAction(){
        btn_down_refresh.setVisibility(View.GONE);
        tv_down_current_loaction.setText("正在获取当前位置......");
        //获取位置
        MyLocationService.getInstance(getActivity()).getMyLocation(new MyLocationService.LocationCallbackListener() {
            @Override
            public void succssReceive(MyLocation myLocation) {
                secondLocation = myLocation;
                tv_down_current_loaction.setText("当前位置："+ secondLocation.getAddr());
                btn_down_refresh.setVisibility(View.VISIBLE);
            }
        });
        Log.d("1","firstLocation:"+firstLoctation.getAddr());
    }



    /**
     * 接收从签到的Activity返回的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == ShowActivity.RESULT_CODE) {
            switch(requestCode){
                //处理第一次请求的返回结果
                case FIRST_REQUEST_CODE:
                    handleFirstResult(data);
                    initShowData();
                    break;
                //处理第二次请求的返回结果
                case SECOND_REQUEST_CODE:
                    handleSecondResult(data);
                    initShowData();
                    break;
                default:
                    break;
            }
        }
    }



    /**
     * 处理第一次签到的返回结果
     * @param data
     */
    public void handleFirstResult(Intent data){
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            int signStatus = bundle.getInt("signStatus");
            int recognizeStatus = bundle.getInt("recognizeStatus");
            int uploadStatus = bundle.getInt("uploadStatus");
            Long uploadTime = bundle.getLong("uploadTime");
            //只有数据上传成功之后才修改当前的签到次数
            if (uploadStatus == TimeUtil.UPLOAD_SUCCESS){
                currentSignTimes = bundle.getInt("signTimes");//更改为第 1 次签到（已经上课签到了）
                //签到成功后显示
                signUpShow();
            }
            Log.d("1","recognizeStatus = "+recognizeStatus);
            Log.d("1","uploadTime = "+uploadTime);
            Log.d("1","uploadStatus = "+uploadStatus);
            Log.d("1","signStatus = "+signStatus);
           // Toast.makeText(getActivity(), "signStatus="+signStatus, Toast.LENGTH_SHORT).show();
            firstSignStatusInfo = new SignStatus(signStatus,recognizeStatus,uploadStatus,uploadTime,firstLoctation.getLongitude(),firstLoctation.getLatitude(),firstLoctation.getAddr());

            //保存第 1 次上传的状态
            mSignUpSharePreference.saveFirstSignStatus(firstSignStatusInfo,currentSignTimes);
        }
        else {
            Toast.makeText(getActivity(), "获取不到Bundle", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理第二次签到的返回结果
     * @param data
     */
    public void handleSecondResult(Intent data){
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            int signStatus = bundle.getInt("signStatus");
            int recognizeStatus = bundle.getInt("recognizeStatus");
            int uploadStatus = bundle.getInt("uploadStatus");
            Long uploadTime = bundle.getLong("uploadTime");
            //只有数据上传成功之后才修改当前的签到次数
            if (uploadStatus == TimeUtil.UPLOAD_SUCCESS){
                currentSignTimes = bundle.getInt("signTimes");//更改为第 2 次签到
            }
            secondSignStatusInfo = new SignStatus(signStatus,recognizeStatus,uploadStatus,uploadTime, secondLocation.getLongitude(), secondLocation.getLatitude(), secondLocation.getAddr());

            //保存第 2 次上传的状态
            mSignUpSharePreference.saveSecondSignStatus(secondSignStatusInfo,currentSignTimes);
        }
        else {
            Toast.makeText(getActivity(), "获取不到Bundle", Toast.LENGTH_SHORT).show();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        //停止秒表定时器
        if (timerSecond != null) {
            timerSecond.cancel();
        }
        if (myTimer != null) {
            myTimer.cancel();
        }
    }

    /**
     *
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
