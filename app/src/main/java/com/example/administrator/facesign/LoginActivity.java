package com.example.administrator.facesign;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.facesign.activity.BaseActivity;
import com.example.administrator.facesign.collector.ActivityCollector;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.tool.SharePreferencesHelper;
import com.example.administrator.facesign.tool.XmlUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LoginActivity extends BaseActivity {

    //账号
    private AutoCompleteTextView userNameEdit;
    //密码
    private EditText passwordEdit;
    //登录按钮
    private Button loginBtn;
    //保存在手机的账号列表
    private ArrayList<String> strList;
    //自定义工具类，用来保存账号信息
    private SharePreferencesHelper recordUserName;//自定义类
    //保存账号文件名
    private static final String File_Name="RecordAccount";

    private Boolean isLogin = false;

    private MyHandler myhandler = new MyHandler(this);

    private CourseInfo courseInfo = null;

    private TextView tv_loading;

    private ProgressBar bar_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);
        InitUI();
        InitUserNameEdit();
        isLogined();

    }
    //初始化控件
    private void InitUI(){
        userNameEdit = (AutoCompleteTextView) findViewById(R.id.username_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        loginBtn = (Button) findViewById(R.id.login_button);
        bar_loading = (ProgressBar) findViewById(R.id.bar_loading);
        bar_loading.setVisibility(View.INVISIBLE);
    }
    //点击登录按钮事件
    public void onClickLoginBtn(View view) {
        if (!userNameEdit.getText().toString().isEmpty() && !passwordEdit.getText().toString().isEmpty()){
                loginBtn.setEnabled(false);
                userNameEdit.setEnabled(false);
                passwordEdit.setEnabled(false);
                //记录新登录用户账号
                if (!recordUserName.isExist(userNameEdit.getText().toString())){//没有被记录则再添加一条新的记录
                    recordUserName.putValueToStringList(userNameEdit.getText().toString());
                }
            //原型进度条
            bar_loading.setVisibility(View.VISIBLE);

            MyTask task = new MyTask();
            task.execute("http://192.168.1.104:8080/webtest/loginaction",userNameEdit.getText().toString(),passwordEdit.getText().toString());


            //开启访问数据库线程，判断是否登录成功，如果成功则返回学生数据
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    CourseInfo courseInfo  =  HttpLogin.LoginByPost(userNameEdit.getText().toString(),passwordEdit.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("courseInfoObject",courseInfo);
                    Message msg = new Message();
                    msg.setData(bundle);
                    myhandler.sendMessage(msg);

                }
            }).start();*/

        }
        else
        {
            Toast.makeText(LoginActivity.this, "学号或密码不为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginBtn.setEnabled(true);
        bar_loading.setVisibility(View.INVISIBLE);
        userNameEdit.setEnabled(true);
        passwordEdit.setEnabled(true);
    }

    //配合子线程更新UI线程
    private void updateUIThread(Message msg){
        Bundle bundle = new Bundle();
        bundle = msg.getData();
//        String result = bundle.getString("result");
        courseInfo = (CourseInfo) bundle.getSerializable("courseInfoObject");
        if (courseInfo != null) {
            CourseInfo data = (CourseInfo)getApplication();
            data.setCourseList(courseInfo.getCourseList());
            data.setStudent(courseInfo.getStudent());
            /*//先跳转到加载界面
            Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
            startActivity(intent);*/
            isLogin = true;
            recordUserName.putValue("isLogin",isLogin);
            Toast.makeText(LoginActivity.this, "欢迎你 "+courseInfo.getStudent().getName()+"!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }

    }
    private void updateUIThread(CourseInfo courseInfo){

        if (courseInfo != null) {
            CourseInfo data = (CourseInfo)getApplication();
            data.setCourseList(courseInfo.getCourseList());
            data.setStudent(courseInfo.getStudent());

            Toast.makeText(LoginActivity.this, "欢迎你 "+courseInfo.getStudent().getName()+"!", Toast.LENGTH_SHORT).show();
            //先跳转到加载界面
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            isLogin = true;
            recordUserName.putValue("isLogin",isLogin);

        }
        else {
            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }

    }
    //弱引用LoginActivity，防止内存泄露
    private static class MyHandler extends Handler {
        private final WeakReference<LoginActivity> mActivity;

        public MyHandler(LoginActivity activity) {
            mActivity = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (mActivity.get() == null) {
                return;
            }
            mActivity.get().updateUIThread(msg);
        }
    }



    //初始化账号自动补全中的字符串
    private void InitUserNameEdit(){
        strList = new ArrayList<String>();
        recordUserName= new SharePreferencesHelper(LoginActivity.this,File_Name);
        isLogin = recordUserName.getBoolean("isLogin");

        strList = recordUserName.getStringList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,strList);
        userNameEdit.setAdapter(adapter);
    }

    //判断之前是否已经登录过，在方法InitUserNameEdit之后调用
    private void isLogined(){
        //如果已经登录过了，就可以直接跳到主界面，不用再次登录
        if (isLogin) {
            //Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            //startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }



    public class MyTask extends AsyncTask<String ,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "doInBackground(Params... params) called");
            try {
                URL url = new URL(params[0]);
                String name = params[1];
                String password = params[2];
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                //post方式不能设置缓存，需手动设置为false
                connection.setUseCaches(false);

                //我们请求的数据
                String data = "password="+ URLEncoder.encode(password,"UTF-8")+
                        "&username="+URLEncoder.encode(name,"UTF-8");

                //connection.setRequestProperty("contentType", "GBK");
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                // connection.getContentLengthLong();
                //獲取輸出流
                OutputStream out = connection.getOutputStream();
                out.write(data.getBytes());
                out.flush();
                out.close();
                Log.i(TAG, "responseCode = "+connection.getResponseCode());

                if (connection.getResponseCode() == 200) {

                    // 获取响应的输入流对象
                    InputStream inputStream = connection.getInputStream();
                    // 创建字节输出流对象
                    ByteArrayOutputStream message = new ByteArrayOutputStream();
                    // 定义读取的长度
                    int len = -1;
                    int count=0;
                    // 定义缓冲区
                    byte buffer[] = new byte[1024];

                    long total = 102400;
                    total = connection.getContentLength();
                    Log.e(TAG, "total = "+total);
                    // 按照缓冲区的大小，循环读取
                    while ((len = inputStream.read(buffer)) != -1) {
                        // 根据读取的长度写入到os对象中
                        message.write(buffer, 0, len);
                        count += len;
                        //调用publishProgress公布进度,最后onProgressUpdate方法将被执行
                        //publishProgress((int) ((count / (float) total) * 100));
                        //为了演示进度,休眠500毫秒
                        Thread.sleep(500);
                    }
                   // return new String(message.toByteArray(), "utf-8");
                    return new String(message.toByteArray());
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progresses) {
            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");
            //tv_loading.setText("loading..." + progresses[0] + "%");
        }

        @Override
        protected void onPostExecute(String resultStr) {
            if (!resultStr.equals("loginfail")){
                Log.i(TAG, "onPostExecute(Result result) called");
                Log.i(TAG, resultStr);
                updateUIThread(XmlUtil.xmlToObject_CourseInfo(resultStr));
            }
            else{
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                loginBtn.setEnabled(true);
                bar_loading.setVisibility(View.INVISIBLE);
                userNameEdit.setEnabled(true);
                passwordEdit.setEnabled(true);
            }
        }
    }
}
