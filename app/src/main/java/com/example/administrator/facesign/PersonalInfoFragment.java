package com.example.administrator.facesign;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.facesign.entity.Student;
import com.example.administrator.facesign.util.ImageUtil;
import com.example.administrator.facesign.util.MySharedPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfoFragment extends Fragment {

    private TextView tv_studentid;
    private TextView tv_major;
    private TextView tv_name;
    private TextView tv_schools;
    private TextView tv_grade;

    //个人头像
    private ImageView img_head;
    private Student student;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        img_head = (ImageView) view.findViewById(R.id.img_username_head);
    }

    private void InitTextView(){
        student = MySharedPreference.loadStudent(getActivity());
        tv_name.setText(student.getName());
        tv_studentid.setText(student.getStudentid());
        tv_schools.setText(student.getSchools());
        tv_major.setText(student.getMajor());
        tv_grade.setText(student.getGrade());
        byte[] array = ImageUtil.imageProcessing(ImageUtil.getPersonalImagePath(student.getStudentid()));
        Bitmap bitmap_head = BitmapFactory.decodeByteArray(array,0,array.length);
        img_head.setImageBitmap(bitmap_head);
    }
}
