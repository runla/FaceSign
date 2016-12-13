package com.example.administrator.facesign.fragment;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.facesign.R;
import com.example.administrator.facesign.activity.MainActivity;
import com.example.administrator.facesign.activity.SignUpStatusActivity;
import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.entity.MyLocation;
import com.example.administrator.facesign.entity.Student;
import com.example.administrator.facesign.util.BackgroundStyle;
import com.example.administrator.facesign.util.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseTableFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = "MainActivityTest";
    private TextView textView_day;
    private TextView textView_section;
    //课程所占单位宽度
    private int courseUnitHeight;
    //课程所占单位长度
    private int courseUnitWidth;
    //每天课程布局数组
    private LinearLayout[] linear_course;
    //课程按钮id
    private int[] course_btn_id;
    //控件id基数
    private static final int baseId=3000;
    //课程按钮数量
    private int course_btn_count = 0;

    private Button btn;

    private View view;

    private CourseInfo courseInfo;

    private Student student;

    private MyLocation myLocation;
    private List<Course> courseList = new ArrayList<>();

    private TextView tv_month;
    /**
     * 最后一个button 的id
     */
    private int lastButtonId=0;

    private boolean isCall = false;
    public CourseTableFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() !=null&&courseInfo==null) {
            courseInfo = (CourseInfo) getArguments().getSerializable("courseInfo");
            myLocation = getArguments().getParcelable("myLocation");
        }
        if (savedInstanceState != null&&myLocation==null) {
            myLocation = savedInstanceState.getParcelable("myLocation");
       //     lastButtonId = savedInstanceState.getInt("lastButtonId");
        }

        courseList = courseInfo.getCourseList();
        student = courseInfo.getStudent();
        view = inflater.inflate(R.layout.activity_course_table1, container, false);


        Log.d(TAG, "onCreateView: ");
        InitView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

    }

    //初始化View
    void InitView(){
        tv_month = (TextView) view.findViewById(R.id.tv_day1);
        String month = String.format("%tm月",new Date());
        tv_month.setText(month);
        linear_course = new LinearLayout[7];
        linear_course[0] = (LinearLayout)view.findViewById(R.id.course_monday_id);
        linear_course[1] = (LinearLayout)view.findViewById(R.id.course_tuesday_id);
        linear_course[2] = (LinearLayout)view.findViewById(R.id.course_wednesday_id);
        linear_course[3] = (LinearLayout)view.findViewById(R.id.course_thursday_id);
        linear_course[4] = (LinearLayout)view.findViewById(R.id.course_firday_id);
        linear_course[5] = (LinearLayout)view.findViewById(R.id.course_saturday_id);
        linear_course[6] = (LinearLayout)view.findViewById(R.id.course_sunday_id);

        textView_day = (TextView)view.findViewById(R.id.tv_day1);
        textView_section = (TextView)view.findViewById(R.id.tv_section1);

       /* show_Loaction = (TextView) view.findViewById(R.id.show_location);
        show_Loaction.setText(myLocation.getLocationdescribe());*/

        //获取控件的宽度和高度
        //Handler hander = new Handler();
        textView_section.post(new Runnable() {
            @Override
            public void run() {
                courseUnitHeight = textView_section.getHeight();
                courseUnitWidth = textView_day.getHeight();

                Log.d(TAG,"height="+courseUnitHeight+" width="+courseUnitWidth);
                AddCourse();
            }
        });

        MainActivity parentActivity = (MainActivity ) getActivity();
        parentActivity.setDataCallBack(new MainActivity.DataCallBack() {
            @Override
            public void onDataChange(MyLocation myLocation) {
                CourseTableFragment.this.myLocation = myLocation;
            }
        });
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    /*
    * 动态添加课程
     */
    void AddCourse(){

        //在同一个线性布局中上一个课程的最后一节课
        int[] lastClassPos = new int[7];
        lastClassPos[0] = 0;
        lastClassPos[1] = 0;
        lastClassPos[2] = 0;
        lastClassPos[3] = 0;
        lastClassPos[4] = 0;
        lastClassPos[5] = 0;
        lastClassPos[6] = 0;

        //当前上课课程数目
        int[] dayCoursenum = new int[7];
        dayCoursenum[0] = 0;
        dayCoursenum[1] = 0;
        dayCoursenum[2] = 0;
        dayCoursenum[3] = 0;
        dayCoursenum[4] = 0;
        dayCoursenum[5] = 0;
        dayCoursenum[6] = 0;

        List<String> coureNameList = new ArrayList<>();
        List<Drawable> drawableList = BackgroundStyle.initDrawableList(getActivity());

        int i = 0;
        for (Course course : courseList) {
            //这里先设置了宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,courseUnitHeight*course.getTotalSection());
            //params.setMargins(0,(course.getStartSection()-1)*courseUnitHeight,0,0);
            params.setMargins(0,courseUnitHeight*(course.getStartSection()-lastClassPos[course.getDay()-1]-1),0,0);
            Button button = new Button(getActivity());


            //设置按钮的显示模式
            if (!coureNameList.contains(course.getCourseName())) {
                coureNameList.add(course.getCourseName());
            }
            int index = coureNameList.indexOf(course.getCourseName());
            button.setBackground(drawableList.get(index));
            button.setTextColor(ContextCompat.getColor(getActivity(),R.color.md_white_100));
            button.setLayoutParams(params);
            //设置按钮的资源id
            button.setId(baseId+i);
            //设置按钮的课程内容
            button.setText(course.getCourseName());
            //设置字体大小
            button.setTextSize(10);
            //设置按钮宽度
            button.setWidth(courseUnitWidth);
            //按照上课的节数设置按钮的高度
            button.setHeight(courseUnitHeight*course.getTotalSection());

            lastClassPos[course.getDay()-1]=course.getStartSection()+course.getTotalSection()-1;

            //设置课程按钮监听事件
            button.setOnClickListener(CourseTableFragment.this);
            //添加至布局之中
            linear_course[course.getDay()-1].addView(button);
            i++;
            lastButtonId++;
            Log.d(TAG, "AddCourse: "+course.getCourseName()+" lastButtonId = "+lastButtonId);

        }
    }

    @Override
    public void onClick(View view) {
        int i = courseList.size()-1;
        while(i >=0){
            if (view.getId()==baseId+i) {
                showCourseInfoDiaglog(courseList.get(i));
                break;
            }
            i--;
        }

    }

    private void showCourseInfoDiaglog(final Course course){
        List<String> list = new ArrayList<String>();
        // list.add(course.getCourseName());
        list.add("教室  "+course.getRoom());
        list.add("教师  "+course.getTeacherName()+"/"+course.getTeacherId());
        list.add("节数  "+course.getStartSection()+"-"+(course.getTotalSection()+course.getStartSection()-1)+"节");
        String week="";
        if (course.getSingleOrDouble() ==1){
            week = "(单周)";
        }
        else if (course.getSingleOrDouble() == 2){
            week = "(双周)";
        }
        else {
            week="";
        }
        list.add("周数  "+course.getStartWeek()+"-"+(course.getTotalWeeks()+course.getStartWeek()-1)+"周"+week);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle(course.getCourseName());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final Date startDate = courseInfo.getEduTerm().getStartDate();
        final int currentWeek = TimeUtil.getCurrentWeek(startDate);
        final int startSec = course.getStartSection();
        final int endSec = startSec + course.getTotalSection()-1;

        builder.setPositiveButton("签到", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TimeUtil.getSignUpStatus(startDate,currentWeek,course.getDay(),startSec,endSec) != -1 || TimeUtil.getSignDownStatus(startDate,currentWeek,course.getDay(),startSec,endSec) != -1){
                    SignUpStatusActivity.actionStart(getActivity(),courseInfo,myLocation);
                    dialogInterface.dismiss();
                }
                else {
                    dialogInterface.dismiss();
                    showTip();
                }

            }
        });
        builder.show();
    }

    //提示现在不是上课时间
    private void showTip(){
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("现在不是签到时间");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("myLocation",myLocation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isCall = false;
    }
}
