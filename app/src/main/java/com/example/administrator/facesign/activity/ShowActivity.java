package com.example.administrator.facesign.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.facesign.R;
import com.example.administrator.facesign.util.FaceDetectionUtil;
import com.example.administrator.facesign.util.FaceRecognitionCallbackListener;
import com.example.administrator.facesign.util.FindFaceCallbackListener;
import com.example.administrator.facesign.util.HttpCallbackListener;
import com.example.administrator.facesign.util.HttpUtil;
import com.example.administrator.facesign.util.ImageUtil;
import com.example.administrator.facesign.util.UrlUtil;
import com.mingle.widget.LoadingView;

import java.io.File;

public class ShowActivity extends AppCompatActivity {

    private Bitmap bitmap;
    /**
     * 调用系统相机的请求码
     */
    private static final int Request_Camera=1;

    /**
     * 拍照后的图片
     */
    private ImageView img_show;

    //加载动画
    private LoadingView loadingView;
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
        loadingView = (LoadingView) findViewById(R.id.loadView);

        dismissLoadView();
    }

    private void showLoadView(){
        //设置可见
        loadingView.setVisibility(View.VISIBLE);

    }
    private void dismissLoadView(){
        //设置不可见
        loadingView.setVisibility(View.INVISIBLE);
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

            FaceDetectionUtil.findFace(filePath, new FindFaceCallbackListener() {
                @Override
                public void listenFaceNum(int num) {
                    //有检测到人脸
                    if (num != 0) {
                        faceIsExist();
                    }
                    else{
                        faceIsNotExist();
                    }
                }

                //显示拍照后的照片
                @Override
                public void getBitmap(Bitmap bitmap) {
                    img_show.setImageBitmap(bitmap);
                }
            });
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

    /**
     * 找到人脸之后做出的反应
     */
    private void faceIsExist(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("结果");
        builder.setMessage("检测到人脸");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("签到", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                //处理人脸识别结果
                handleRecognizeResult();
            }
        });
        builder.show();
    }

    /**
     * 找不到人脸之后做出的反应
     */
    private void faceIsNotExist(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("结果");
        builder.setMessage("无法检测到人脸");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("重拍", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //重新调用照相机
                startCamera();
            }
        });
        builder.show();
    }


    private void handleRecognizeResult(){
        showLoadView();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ShowActivity.this);
        String studentId = preferences.getString("studentId","");
        String secondPath = ImageUtil.getPersonalImagePath(studentId);
        FaceDetectionUtil.faceRecognitionStart(filePath, secondPath, new FaceRecognitionCallbackListener() {
            @Override//识别成功
            public void recognitionSuccess(int result) {
                if (result != 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recognizeSuccess();
                        }
                    });
                }
            }

            @Override//识别失败
            public void recognitionFail(int result) {
                if (result != 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recognizeFail();
                        }
                    });
                }
            }
        });
    }


    /**
     * 人脸识别成功后的反应（返回显示课程信息的界面）
     */
    private void recognizeSuccess(){
        dismissLoadView();

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("结果");
        builder.setMessage("签到成功");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                int status = 5;
                uploadSignUpResult(status);
                finish();
            }
        });
        builder.show();
    }

    /**
     * 人脸识别失败后的反应
     */
    private void recognizeFail(){
        dismissLoadView();

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("结果");
        builder.setMessage("人脸识别失败");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("重拍", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startCamera();
            }
        });
        builder.show();
    }




    /**
     * 上传签到结果
     */
    private void uploadSignUpResult(int status){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ShowActivity.this);
        String username = preferences.getString("studentId","");
        String urlPath = UrlUtil.getSignUpUrl(username,status);
        HttpUtil.getHttpToString(urlPath, new HttpCallbackListener() {
            @Override
            public void onFinishStr(String response) {
                Log.d(BaseActivity.TAG,response);
            }

            @Override
            public void onFinishTypeArray(byte[] response) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    //开始识别
    public void onClick_upload(View view){
        showLoadView();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ShowActivity.this);
        String studentId = preferences.getString("studentId","");
        String secondPath = ImageUtil.getPersonalImagePath(studentId);
        FaceDetectionUtil.faceRecognitionStart(filePath, secondPath, new FaceRecognitionCallbackListener() {
            @Override
            public void recognitionSuccess(int result) {
                if (result != 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recognizeSuccess();

                        }
                    });
                }
            }

            @Override
            public void recognitionFail(int result) {
                if (result != 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recognizeFail();
                        }
                    });
                }
            }
        });
    }

}
