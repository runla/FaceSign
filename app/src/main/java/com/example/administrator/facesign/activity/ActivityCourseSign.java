package com.example.administrator.facesign.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.facesign.R;
import com.example.administrator.facesign.entity.Course;


public class ActivityCourseSign extends BaseActivity implements View.OnClickListener {

    private Button btn_sign;
    private Button btn_back;

    private Course course;
    private TextView tv_courseName;
    private TextView tv_room;
    private TextView tv_teacher;
    private TextView tv_section;
    private TextView tv_week;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_sign);
        ActivityCollector.addActivity(this);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        course = (Course) bundle.getSerializable("course");
        InitView();
        InitTextView();
    }

    private void InitView(){
        btn_sign = (Button) findViewById(R.id.btn_sign);
        btn_back = (Button) findViewById(R.id.btn_back);

        tv_courseName = (TextView) findViewById(R.id.tv_course_sign_name);
        tv_room = (TextView) findViewById(R.id.tv_course_sign_room);
        tv_section = (TextView) findViewById(R.id.tv_course_sign_section);
        tv_teacher= (TextView) findViewById(R.id.tv_course_sign_teacher);
        tv_week = (TextView) findViewById(R.id.tv_course_sign_weeks);

        btn_back.setOnClickListener(this);
        btn_sign.setOnClickListener(this);
    }
    private void InitTextView(){
        tv_courseName.setText(course.getCourseName());
        tv_room.setText(course.getRoom());
        int end = course.getTotalSection()+course.getStartSection()-1;
        tv_section.setText(course.getStartSection()+"-"+end+"节");
        tv_teacher.setText(course.getTeacherName()+"/"+course.getTeacherId());
        int endweek = course.getStartWeek()+course.getTotalWeeks()-1;
        tv_week.setText(course.getStartWeek()+"-"+endweek+"周");
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_sign:
                startActivity(new Intent(ActivityCourseSign.this, ShowActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
