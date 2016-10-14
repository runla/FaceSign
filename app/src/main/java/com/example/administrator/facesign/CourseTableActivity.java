package com.example.administrator.facesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CourseTableActivity extends AppCompatActivity implements View.OnClickListener {

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


    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_table);

        InitView();

        Log.d(TAG,"OnCreate");

    }

    //初始化View
    void InitView(){
        linear_course = new LinearLayout[7];
        linear_course[0] = (LinearLayout) findViewById(R.id.course_monday_id);
        linear_course[1] = (LinearLayout) findViewById(R.id.course_tuesday_id);
        linear_course[2] = (LinearLayout) findViewById(R.id.course_wednesday_id);
        linear_course[3] = (LinearLayout) findViewById(R.id.course_thursday_id);
        linear_course[4] = (LinearLayout) findViewById(R.id.course_firday_id);
        linear_course[5] = (LinearLayout) findViewById(R.id.course_saturday_id);
        linear_course[6] = (LinearLayout) findViewById(R.id.course_sunday_id);

        textView_day = (TextView)findViewById(R.id.tv_day1);
        textView_section = (TextView) findViewById(R.id.tv_section1);

        //获取控件的宽度和高度
        textView_day.post(new Runnable() {
            @Override
            public void run() {
                courseUnitHeight = textView_section.getHeight();
                courseUnitWidth = textView_day.getHeight();

                Log.d(TAG,"height="+courseUnitHeight+" width="+courseUnitWidth);
                AddCourse();
            }
        });
    }
    /*
    * 动态添加课程
     */
    void AddCourse(){
        for (int i = 0;i<3;i++){
            Button button = new Button(this);
            button.setText("课程"+i);
            button.setWidth(courseUnitWidth);
            button.setHeight(courseUnitHeight*(i+1));
            button.setY(courseUnitHeight*i);
            button.setId(baseId+i+1);
            button.setOnClickListener(this);
            linear_course[i].addView(button);

        }
        Log.d(TAG,"height="+courseUnitHeight+" width="+courseUnitWidth);
        Log.d(TAG,"AddCourse");
    }

    /**
     * 初始化监听事件
     */
    private void InitEvent(){

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case baseId+1:
                Toast.makeText(CourseTableActivity.this, "button1", Toast.LENGTH_SHORT).show();

                break;

            case baseId+2:
                Toast.makeText(CourseTableActivity.this, "button2", Toast.LENGTH_SHORT).show();
                break;

            case baseId+3:
                Toast.makeText(CourseTableActivity.this, "button3", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
