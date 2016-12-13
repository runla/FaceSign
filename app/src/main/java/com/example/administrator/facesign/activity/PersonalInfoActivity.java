package com.example.administrator.facesign.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class PersonalInfoActivity extends BaseActivity {

    private TextView tv_studentid;
    private TextView tv_major;
    private TextView tv_name;
    private TextView tv_schools;
    private TextView tv_grade;

    private ImageView btn_back;
    private TextView tv_bar_title;
    private TextView tv_sure;

    //private TextView tv_title;

    //个人头像
    // private ImageView img_head;
    private RoundedImageView img_head;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal_info);

        InitView();
        initAction();
        InitTextView();
    }


    public static void actionStart(Context mContext){
        Intent intent = new Intent(mContext,PersonalInfoActivity.class);
        mContext.startActivity(intent);
    }
    private void InitView(){
        tv_name = (TextView)findViewById(R.id.tv_personal_name);

        tv_studentid = (TextView)findViewById(R.id.tv_personal_number);

        tv_major = (TextView)findViewById(R.id.tv_personal_majar);

        tv_grade = (TextView)findViewById(R.id.tv_personal_grade);

        tv_schools = (TextView)findViewById(R.id.tv_personal_schools);
        // tv_title = (TextView) view.findViewById(tv_title);

        img_head = (RoundedImageView)findViewById(R.id.img_username_head);


        btn_back = (ImageView) findViewById(R.id.btn_back);

        tv_bar_title = (TextView) findViewById(R.id.tv_title);
        tv_bar_title.setText("个人信息");
        tv_sure = (TextView) findViewById(R.id.tv_sure);

        //  toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        // toolbar = (Toolbar) view.findViewById(R.id.toolbar);


    }
    private void initAction(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void InitTextView(){
        student = MySharedPreference.loadStudent(this);
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
                ImageUtil.saveBitmap(PersonalInfoActivity.this,response,student.getStudentid());
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(imageRequest);
    }
}
