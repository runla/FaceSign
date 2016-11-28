package com.example.administrator.facesign.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.MyLocation;
import com.example.administrator.facesign.entity.Student;
import com.example.administrator.facesign.util.FaceDetectionUtil;
import com.example.administrator.facesign.util.FaceRecognitionCallbackListener;
import com.example.administrator.facesign.util.FindFaceCallbackListener;
import com.example.administrator.facesign.util.ImageUtil;
import com.example.administrator.facesign.util.UrlUtil;
import com.example.administrator.facesign.vollery.AppController;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST;

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
    //private ProgressBar   loadingView;
    /**
     * 图片保存的路径
     */
    private String filePath;

    /**
     * 拍照后返回的图片
     */
    private File imageFile;

    /**
     * 加载布局
     */
    private RelativeLayout relative_loading;

    /**
     * 课程信息
     */
    private Course course;

    /**
     * 学生信息
     */
    private Student student;

    /**
     * 当前的位置
     */
    private MyLocation myLocation;

    /**
     * 人脸识别的相似度
     */
    private Double similar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null) {
            course = (Course) bundle.getSerializable("course");
            student = (Student) bundle.getSerializable("student");
            myLocation = bundle.getParcelable("myLocation");
        }
        if (savedInstanceState != null) {
            course = (Course) savedInstanceState.getSerializable("course");
            student = (Student) savedInstanceState.getSerializable("student");
            myLocation = savedInstanceState.getParcelable("myLocation");
        }
        initView();
        startCamera();
    }

    private void initView(){
        img_show = (ImageView) findViewById(R.id.img_show);
       // loadingView = (ProgressBar) findViewById(R.id.loadView);
        relative_loading = (RelativeLayout) findViewById(R.id.layout_load);
        dismissLoadView();
    }

    private void showLoadView(){
        //设置可见
        relative_loading.setVisibility(View.VISIBLE);
    }
    private void dismissLoadView(){
        //设置不可见
        relative_loading.setVisibility(View.INVISIBLE);

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

            //获取拍照后获得的图片路径，根据路径检测照片是否存在人脸
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
                //对人脸进行比对识别
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
        builder.setNegativeButton("尝试识别", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //即使找不到人脸也进行人脸比对识别
                handleRecognizeResult();
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
        //确保图片已经存在本地了
        if (!ImageUtil.isBitmapExist(secondPath)) {
            getImage();
        }

        //获取人脸比对结果，不管是否比对成功，我们都将数据进行上传
        FaceDetectionUtil.faceRecognitionStart(filePath, secondPath, new FaceRecognitionCallbackListener() {
            @Override
            public void recognitionSuccess(final Double similar) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //上传签到数据
                        myupload(filePath,similar);
                    }
                });

            }

        });
    }

    /**
     * 上传个人信息
     * @param filePath
     * @param similar
     */
    private void myupload(String filePath,final Double similar) {
        String urlPath = "http://192.168.1.104:8080/myServer/uploadImage";
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", student.getStudentid());
        params.addBodyParameter("day", course.getDay()+"");
        params.addBodyParameter("courseId", course.getCourseId());
        params.addBodyParameter("startSection", course.getStartSection()+"");
        params.addBodyParameter("totalSection", course.getTotalSection()+"");
        params.addBodyParameter("totalWeeks", course.getTotalWeeks()+"");
        params.addBodyParameter("startWeek", course.getStartWeek()+"");
        params.addBodyParameter("latitude",myLocation.getLatitude()+"");
        params.addBodyParameter("longitude",myLocation.getLongitude()+"");
        params.addBodyParameter("similar",similar+"");//相似度
        try {
            params.addBodyParameter("addr",URLEncoder.encode(myLocation.getAddr(), "utf-8"));
            params.addBodyParameter("describe",URLEncoder.encode(myLocation.getLocationdescribe(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//      传图片时，要写3个参数
//      imageFile：键名
//      new File(path)：要上传的图片，path图片路径
//      image/jpg：上传图片的扩展名
        params.addBodyParameter("imageFile", new File(filePath), "image/jpg");
        HttpUtils http = new HttpUtils();
        http.configResponseTextCharset("utf-8");
        http.send(POST,
                urlPath,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (similar > 65) {//认为认识比对结果的相似度大于65%是同一个人
                            recognizeSuccess();
                        }
                        else{
                            recognizeFail();
                        }

                        String resultStr = responseInfo.result;
                        Log.e("1", "上传成功：" + resultStr);
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.e("1", "上传失败：" + error.getExceptionCode() + ":" + msg);
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
        builder.setMessage("人脸比对成功，数据已上传");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
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
        builder.setMessage("人脸比对失败，数据已上传");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
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
     *     获取网络个人图片并保存至本地
     */
    private void getImage(){
        String urlPath = UrlUtil.getDownloadImageUrl(student.getStudentid());
        ImageRequest imageRequest = new ImageRequest(urlPath, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ImageUtil.saveBitmap(ShowActivity.this,response,student.getStudentid());
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(imageRequest);
    }

    /**
     * 保存当前状态
     * @param outState
     * @param outPersistentState
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("course",course);
        outState.putSerializable("student",student);
        outState.putParcelable("myLocation",myLocation);
    }
}
