package com.example.administrator.facesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.EduTerm;
import com.example.administrator.facesign.entity.MyLocation;
import com.example.administrator.facesign.entity.Student;
import com.example.administrator.facesign.util.FaceDetectionUtil;
import com.example.administrator.facesign.util.FaceRecognitionCallbackListener;
import com.example.administrator.facesign.util.FindFaceCallbackListener;
import com.example.administrator.facesign.util.ImageUtil;
import com.example.administrator.facesign.util.TimeUtil;
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
import java.util.Date;

import static com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST;

public class ShowActivity extends BaseActivity {

    public static final int RESULT_CODE = 321;              //结果返回码
    //签到的状态
    private int signStatus=-1;
    //人脸识别的状态
    private int recognizeStatus=-1;
    //数据上传的状态
    private int uploadStatus=-1;
    //签到的次数（上课签到、下课签到）
    private int signTimes=-1;
    //签到时间
    private long uploadTime=-1L;

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
  //  private RelativeLayout relative_loading;

    /**
     * 课程信息
     */
    private Course course;

    /**
     * 学生信息
     */
    private Student student;
    /**
     * 开学日期和学期结束日期
     */
    private EduTerm eduTerm;

    /**
     * 当前的位置
     */
    private MyLocation myLocation;

    /**
     * 当前周
     */
    private int currentWeek;

    /**
     * 开学日期
     */
    private Date startDate;


    /**
     * 人脸识别的相似度
     */
    //private Double similar;

    /**
     * 返回按钮
     */
    private ImageView btn_back;

    /*
    *上传按钮
     */
    private TextView btn_upload;

    /**
     * 标题
     */
    private TextView tv_title;

    /**
     * 人脸识别加载对话框
     */
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        progressDialog = new ProgressDialog(ShowActivity.this);
        progressDialog.setMessage("人脸识别中...");
        progressDialog.setCancelable(false);

        //从启动该Activity的Activity中获取传递的数据
        getData();

        //状态恢复
        onRecoverInstanceState(savedInstanceState);
        initView();
        initAction();
        startCamera();

    }

    private void initView(){
        img_show = (ImageView) findViewById(R.id.img_show);
        // loadingView = (ProgressBar) findViewById(R.id.loadView);
        //relative_loading = (RelativeLayout) findViewById(R.id.layout_load);

        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_upload = (TextView) findViewById(R.id.tv_sure);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("考勤");
        btn_upload.setText("上传");

       // dismissLoadView();
    }


    private void initAction(){
        //返回
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CODE,getResultDate());
                finish();
            }
        });
        //上传
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //对人脸进行比对识别
                handleRecognizeResult();
            }
        });
    }
    /**
     * 从启动该activity的activity中获取数据
     */
    private void getData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            course = (Course) bundle.getSerializable("course");
            student = (Student) bundle.getSerializable("student");
            eduTerm = (EduTerm) bundle.getSerializable("eduTerm");
            myLocation = (MyLocation) bundle.getParcelable("myLocation");
            signTimes = bundle.getInt("signTimes");
            startDate = eduTerm.getStartDate();
            currentWeek = TimeUtil.getCurrentWeek(startDate);
        }
    }

    //显示加载控件
    private void showLoadView(){
        //设置可见
       // relative_loading.setVisibility(View.VISIBLE);
    }

    //隐藏加载控件
    private void dismissLoadView(){
        //设置不可见
       // relative_loading.setVisibility(View.INVISIBLE);

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
            //强处理结果返回
            setResult(RESULT_CODE,getResultDate());
            finish();
        }
    }


    public void onClick_Cannel(View view){
        setResult(RESULT_CODE,getResultDate());
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
        //showLoadView();
        //显示人脸识别加载框
        progressDialog.show();
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
                        int endSec = course.getStartSection()+course.getTotalSection()-1;
                        //上传时间
                        uploadTime = System.currentTimeMillis();
                        //上课签到
                        if (signTimes == TimeUtil.FIRST_SIGN_UP){
                            signStatus = TimeUtil.getSignUpStatus(startDate,currentWeek,course.getDay(),course.getStartSection(),endSec);
                        }
                        //下课签到
                        if (signTimes == TimeUtil.SECOND_SIGN_DOWN) {
                            signStatus = TimeUtil.getSignDownStatus(startDate,currentWeek,course.getDay(),course.getStartSection()+course.getTotalSection()-1,endSec);
                        }
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
        //String urlPath = "http://192.168.1.104:8080/myServer/uploadImage";
        String urlPath = UrlUtil.getUploadInfoUrl();
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
        params.addBodyParameter("uploadTime",uploadTime+"");//上传时间
        params.addBodyParameter("signStatus",signStatus+"");//签到状态
        params.addBodyParameter("signTimes",signTimes+"");//第几次签到
        params.addBodyParameter("courseClassId",course.getCourseClassId());
        params.addBodyParameter("currentWeek",currentWeek+"");//当前周
        try {
            //地址
            params.addBodyParameter("addr",URLEncoder.encode(myLocation.getAddr(), "utf-8"));
            //地址描述
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
                        uploadStatus = TimeUtil.UPLOAD_SUCCESS;
                        Log.d("1","相似度 = "+similar);
                        if (similar > 65) {//认为认识比对结果的相似度大于65%是同一个人
                            recognizeStatus = TimeUtil.RECOGNIZE_SUCCESS;
                            recognizeSuccess();
                        }
                        else{
                            recognizeStatus = TimeUtil.RECOGNIZE_FAIL;
                            recognizeFail();
                        }


                        String resultStr = responseInfo.result;
                        Log.e("1", "上传成功：" + resultStr);
                    }
                    @Override
                    public void onFailure(HttpException error, String msg) {
                        uploadStatus = TimeUtil.UPLOAD_FAIL;
                        Log.e("1", "上传失败：" + error.getExceptionCode() + ":" + msg);
                    }
                });
    }

    /**
     * 人脸识别成功后的反应（返回显示课程信息的界面）
     */
    private void recognizeSuccess(){
       // dismissLoadView();

        progressDialog.dismiss();

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("结果");
        builder.setMessage("人脸比对成功，数据已上传");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //将比对结果返回
                setResult(RESULT_CODE,getResultDate());
                finish();
            }
        });
        builder.show();
    }

    /**
     * 人脸识别失败后的反应
     */
    private void recognizeFail(){
      //  dismissLoadView();
        progressDialog.dismiss();

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("结果");
        builder.setMessage("人脸比对失败，数据已上传");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //将比对结果返回
                setResult(RESULT_CODE,getResultDate());
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
        outState.putInt("signStatus",signStatus);
        outState.putInt("recognizeStatus",recognizeStatus);
        outState.putInt("uploadStatus",uploadStatus);
        outState.putInt("signTimes",signTimes);
        outState.putLong("uploadTime",uploadTime);
    }

    /**
     * 当activity被销毁后，将数据重新恢复到原来的状态
     * @param savedInstanceState
     */
    public void onRecoverInstanceState(Bundle savedInstanceState){
        if (savedInstanceState != null){
            course = (Course) savedInstanceState.getSerializable("course");
            student = (Student) savedInstanceState.getSerializable("student");
            myLocation = (MyLocation) savedInstanceState.getSerializable("myLocation");
            signStatus = savedInstanceState.getInt("signStatus");
            recognizeStatus = savedInstanceState.getInt("recognizeStatus");
            uploadStatus = savedInstanceState.getInt("uploadStatus");
            signTimes = savedInstanceState.getInt("signTimes");
            uploadTime = savedInstanceState.getInt("uploadTime");
            startDate = eduTerm.getStartDate();
            currentWeek = TimeUtil.getCurrentWeek(startDate);
        }
    }


    /**
     * 设置返回的参数值，并将intent返回
     * @return
     */
    private Intent getResultDate(){
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("signStatus",signStatus);
        bundle.putInt("recognizeStatus",recognizeStatus);
        bundle.putInt("uploadStatus",uploadStatus);
        bundle.putInt("signTimes",signTimes);
        bundle.putLong("uploadTime",uploadTime);
        data.putExtras(bundle);
        return data;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //将比对结果返回
        setResult(RESULT_CODE,getResultDate());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("1","recognizeStatus1 = "+recognizeStatus);
        Log.d("1","uploadTime1 = "+uploadTime);
        Log.d("1","uploadStatus1 = "+uploadStatus);
        Log.d("1","signStatus1 = "+signStatus);
    }

    //****************************d对外公布的方法***********************************

    /**
     * 从外部启动该activity
     * @param mContext
     * @param course
     * @param student
     * @param myLocation
     */
    public static void actionStart(Context mContext,Course course,Student student,EduTerm eduTerm,MyLocation myLocation,int signTimes){

        Intent intent = getActionStatIntent(mContext,course,student,eduTerm,myLocation,signTimes);
        mContext.startActivity(intent);
    }


    /**
     * 启动该activity所需的 Intent
     * @param mContext
     * @param course
     * @param student
     * @param myLocation
     * @param signTimes     签到次数
     * @return
     */
    public static Intent getActionStatIntent(Context mContext,Course course,Student student,EduTerm eduTerm,MyLocation myLocation,int signTimes){
        Bundle bundle = new Bundle();
        bundle.putSerializable("course",course);
        bundle.putSerializable("student",student);
        bundle.putSerializable("eduTerm",eduTerm);
        bundle.putParcelable("myLocation",myLocation);
        bundle.putInt("signTimes",signTimes);
        Intent intent = new Intent(mContext,ShowActivity.class);
        intent.putExtras(bundle);
        return intent;
    }



}
