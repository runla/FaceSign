package com.example.administrator.facesign;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.entity.Student;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfoFragment extends Fragment {

    private TextView tv_studentid;
    private TextView tv_major;
    private TextView tv_name;
    private TextView tv_schools;
    private TextView tv_grade;

    private Student student;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d("fragmenttest","*********");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        InitView(view);
        InitTextView();

        //InitTextView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void InitView(View view){
        tv_name = (TextView) view.findViewById(R.id.tv_personal_name);

        tv_studentid = (TextView) view.findViewById(R.id.tv_personal_number);

        tv_major = (TextView) view.findViewById(R.id.tv_personal_majar);

        tv_grade = (TextView) view.findViewById(R.id.tv_personal_grade);

        tv_schools = (TextView) view.findViewById(R.id.tv_personal_schools);

       /* ViewTreeObserver observer = tv_schools.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tv_studentid.setText("kljsdlfkjsdlkfjsdlk");
            }
        });*/

    }

    private void InitTextView(){
        CourseInfo courseInfo = (CourseInfo)getActivity().getApplication();
        student = courseInfo.getStudent();

        //tv_studentid.setText("kljsdlfkjsdlkfjsdlk");
        tv_name.setText(student.getName());
        tv_studentid.setText(student.getStudentid());
        tv_schools.setText(student.getSchools());
        tv_major.setText(student.getMajor());
        tv_grade.setText(student.getGrade());
    }
}
