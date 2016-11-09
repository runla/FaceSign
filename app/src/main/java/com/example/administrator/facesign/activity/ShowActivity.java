package com.example.administrator.facesign.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.facesign.R;
import com.example.administrator.facesign.util.FaceDetectionUtil;
import com.example.administrator.facesign.util.FaceRecognitionCallbackListener;
import com.example.administrator.facesign.util.ImageUtil;

import java.io.File;
public class ShowActivity extends AppCompatActivity {

    private static final int Request_Camera=1;
    private Bitmap bitmap;
    private ImageView img_show;
    private RelativeLayout layout_load;

    /**
     * 图片保存的路径
     */
    private String filePath;

    /**
     * 拍照后返回的图片
     */
    private File imageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        initView();
        startCamera();
    }

    private void initView(){
        img_show = (ImageView) findViewById(R.id.img_show);
        layout_load = (RelativeLayout) findViewById(R.id.layout_load);
        //设置不可见
        layout_load.setVisibility(View.INVISIBLE);
    }

    /**
     * 照相机的调用
     */
    void startCamera(){
        filePath = ImageUtil.getSaveImagePath();
        imageFile = new File(filePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(intent,Request_Camera);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Request_Camera && resultCode == Activity.RESULT_OK ) {

            img_show.setImageBitmap(FaceDetectionUtil.findFace(filePath));
            //刷新手机图库
            ImageUtil.refreshGallery(ShowActivity.this, imageFile);
        }
        if (requestCode == Request_Camera && resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }


    public void onClick_Cannel(View view){
        finish();
    }

    private int myresult;
    //开始识别
    public void onClick_upload(View view){
        layout_load.setVisibility(View.VISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ShowActivity.this);
        String studentId = preferences.getString("studentId","");
        String secondPath = ImageUtil.getPersonalImagePath(studentId);
        FaceDetectionUtil.faceRecognitionStart(filePath, secondPath, new FaceRecognitionCallbackListener() {
            @Override
            public void recognitionSuccess(int result) {
                myresult = result;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (myresult == FaceDetectionUtil.RECOGNITION_SUCCESS){
                            showResultDialogSuccess();
                        }
                        else if(myresult == FaceDetectionUtil.RECOGNITION_FAIL){
                            showResultDialogFail();
                        }
                    }
                });
            }
        });
    }


    private void showResultDialogSuccess(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("识别结果"); //设置标题
        builder.setMessage("签到成功"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }
    private void showResultDialogFail(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("识别结果"); //设置标题
        builder.setMessage("人脸识别失败，签到失败"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }
}
