package com.example.administrator.facesign.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.facesign.R;
import com.example.administrator.facesign.activity.ActivityCourseSign;
import com.example.administrator.facesign.db.CourseDB;
import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.entity.Student;
import com.example.administrator.facesign.util.MySharedPreference;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<Course> courseList = new ArrayList<>();

    private Spinner spinner;

    public CourseTableFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_course_table, container, false);

        //提取共享数据
        //courseInfo = (CourseInfo)getActivity().getApplication();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String studentId = preferences.getString("studentId","");
        courseList = CourseDB.getInstance(getActivity()).loadCourseList(studentId);
        student = MySharedPreference.loadStudent(getActivity());
        //student = courseInfo.getStudent();
        //courseList = courseInfo.getCourseList();

        InitView();
        // Inflate the layout for this fragment
        return view;
    }

    //初始化View
    void InitView(){
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
        spinner = (Spinner) view.findViewById(R.id.spinner1);
        initSpinner();
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
    }
    private void initSpinner(){
        List<String> list = new ArrayList<String>();
        for (int i = 1; i <= 25; i++) {
            list.add("第"+i+"周");
        }

        //SpinnerAdapter adapter = new SpinnerAdapter(list,getActivity());
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //setDropDownHeight(200);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {

               // String[] languages = getResources().getStringArray(R.array.languages);
               // Toast.makeText(MainActivity.this, "你点击的是:"+languages[pos], 2000).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
//设置spinner下拉框的高度
    public void setDropDownHeight(int pHeight) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(pHeight);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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

       // Button course_btn[][] = new Button[7][7];

        Course[] c = new Course[courseList.size()];
       // Course[] c = (Course[]) courseList.toArray();
        int count=0;
        for (Course course : courseList) {
            c[count++] = course;
        }

        for (int k = 0; k < count-1; k++) {
            for (int j = k+1; j < count; j++) {
                if (c[k].getStartSection()>c[j].getStartSection()){
                    Course temp = c[k];
                    c[k] = c[j];
                    c[j] = temp;
                }
            }
        }
        courseList = Arrays.asList(c);


        //Log.d(TAG,"AddCourse========="+courseInfo.getStudent().getName());
        int i = 0;
        for (Course course : courseList) {
             //这里先设置了宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,courseUnitHeight*course.getTotalSection());
            //params.setMargins(0,(course.getStartSection()-1)*courseUnitHeight,0,0);
            params.setMargins(0,courseUnitHeight*(course.getStartSection()-lastClassPos[course.getDay()-1]-1),0,0);
            Button button = new Button(getActivity());

            button.setBackgroundResource(R.drawable.backgroud_course_btn);
            button.setTextColor(getResources().getColor(R.color.text_color));

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
        }

     }


    @Override
    public void onClick(View view) {
        int i = courseList.size()-1;
        while(i >=0){
            if (view.getId()==baseId+i) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("course",courseList.get(i));
                Intent intent = new Intent(getActivity(),ActivityCourseSign.class);
                intent.putExtras(bundle);
                startActivity(intent);
               // Toast.makeText(getActivity(), "button1", Toast.LENGTH_SHORT).show();
                break;
            }
            i--;
        }

    }

}
