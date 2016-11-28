package com.example.administrator.facesign.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.entity.Student;
import com.example.administrator.facesign.util.ImageUtil;
import com.example.administrator.facesign.util.MySharedPreference;
import com.example.administrator.facesign.util.UrlUtil;
import com.example.administrator.facesign.vollery.AppController;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfoFragment extends Fragment {

    private TextView tv_studentid;
    private TextView tv_major;
    private TextView tv_name;
    private TextView tv_schools;
    private TextView tv_grade;
    private Toolbar toolbar;
    //private TextView tv_title;

    //个人头像
  // private ImageView img_head;
    private RoundedImageView img_head;
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
     //   toolbar.setTitle("个人信息");
    }

    private void InitView(View view){
        tv_name = (TextView) view.findViewById(R.id.tv_personal_name);

        tv_studentid = (TextView) view.findViewById(R.id.tv_personal_number);

        tv_major = (TextView) view.findViewById(R.id.tv_personal_majar);

        tv_grade = (TextView) view.findViewById(R.id.tv_personal_grade);

        tv_schools = (TextView) view.findViewById(R.id.tv_personal_schools);
       // tv_title = (TextView) view.findViewById(tv_title);

        img_head = (RoundedImageView) view.findViewById(R.id.img_username_head);

      //  toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        // toolbar = (Toolbar) view.findViewById(R.id.toolbar);


    }

    private void InitTextView(){
        student = MySharedPreference.loadStudent(getActivity());
        tv_name.setText(student.getName());
        tv_studentid.setText(student.getStudentid());
        tv_schools.setText(student.getSchools());
        tv_major.setText(student.getMajor());
        tv_grade.setText(student.getGrade());

        if (ImageUtil.isBitmapExist(ImageUtil.getPersonalImagePath(student.getStudentid()))){
            byte[] array = ImageUtil.imageProcessing(ImageUtil.getPersonalImagePath(student.getStudentid()));
            Bitmap bitmap_head = BitmapFactory.decodeByteArray(array,0,array.length);
            img_head.setImageBitmap(bitmap_head);
        }
        else{
            getImage();
        }
    }

    private void getImage(){
        String urlPath = UrlUtil.getDownloadImageUrl(student.getStudentid());
        ImageRequest imageRequest = new ImageRequest(urlPath, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                img_head.setImageBitmap(response);
                ImageUtil.saveBitmap(getActivity(),response,student.getStudentid());
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(imageRequest);
    }
}
