package com.example.administrator.facesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.facesign.connectUtil.HttpLogin;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.tool.SharePreferencesHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);
        InitUI();
        InitUserNameEdit();
        isLogined();

    }

    //点击登录按钮事件
    public void onClickLoginBtn(View view) {
        if (!userNameEdit.getText().toString().isEmpty() && !passwordEdit.getText().toString().isEmpty()){
            //记录新登录用户账号
            if (!recordUserName.isExist(userNameEdit.getText().toString())){//没有被记录则再添加一条新的记录
                recordUserName.putValueToStringList(userNameEdit.getText().toString());
            }


            //开启访问数据库线程，判断是否登录成功，如果成功则返回学生数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CourseInfo courseInfo  =  HttpLogin.LoginByPost(userNameEdit.getText().toString(),passwordEdit.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("courseInfoObject",courseInfo);
                    Message msg = new Message();
                    msg.setData(bundle);
                    myhandler.sendMessage(msg);
                }
            }).start();

        }
        else
        {
            Toast.makeText(LoginActivity.this, "学号或密码不为空", Toast.LENGTH_SHORT).show();
        }
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
            //先跳转到加载界面
            Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
            startActivity(intent);
            isLogin = true;
            recordUserName.putValue("isLogin",isLogin);
            Toast.makeText(LoginActivity.this, "欢迎你 "+courseInfo.getStudent().getName()+"!", Toast.LENGTH_SHORT).show();
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

    //初始化控件
    private void InitUI(){
        userNameEdit = (AutoCompleteTextView) findViewById(R.id.username_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        loginBtn = (Button) findViewById(R.id.login_button);
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
}
