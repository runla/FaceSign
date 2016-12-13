package com.example.administrator.facesign.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.util.UrlUtil;
import com.example.administrator.facesign.util.Utility;
import com.example.administrator.facesign.vollery.AppController;

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
    //保存账号文件名
    private static final String File_Name="RecordAccount";

    private Boolean isLogin = false;

    private CourseInfo courseInfo = null;

    private TextView tv_loading;

    private ProgressBar bar_loading;

    /**
     * 账号（学号）
     */
    private String username;
    /**
     * 账号密码
     */
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);
        //获取保存在本地的账号密码

        InitUI();
        InitUserNameEdit();
    }
    private void savePasswor(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("studentId",username);
        editor.putString("password",password);
        editor.commit();
    }
    private void getPassword(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        username = preferences.getString("studentId","");
        password = preferences.getString("password","");
        userNameEdit.setText(username);
        passwordEdit.setText(password);
    }
    //初始化控件
    private void InitUI(){
        userNameEdit = (AutoCompleteTextView) findViewById(R.id.username_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        loginBtn = (Button) findViewById(R.id.login_button);
        bar_loading = (ProgressBar) findViewById(R.id.bar_loading);
        bar_loading.setVisibility(View.INVISIBLE);

        getPassword();
    }
    //点击登录按钮事件
    public void onClickLoginBtn(View view) {
        if (!userNameEdit.getText().toString().isEmpty() && !passwordEdit.getText().toString().isEmpty()){
            loginBtn.setEnabled(false);
            userNameEdit.setEnabled(false);
            passwordEdit.setEnabled(false);
            username = userNameEdit.getText().toString().trim();
            password = passwordEdit.getText().toString().trim();
            savePasswor();

            //原型进度条
            bar_loading.setVisibility(View.VISIBLE);
            //检查登录账号
            checkLogin();
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


    //初始化账号自动补全中的字符串
    private void InitUserNameEdit(){
        strList = new ArrayList<String>();
        strList.add(username);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,strList);
        userNameEdit.setAdapter(adapter);
    }

    private void updateUIThread(CourseInfo courseInfo){
        //保存到本地数据库的操作
        if (courseInfo != null) {
            //跳转到主界面
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("courseInfo",courseInfo);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "欢迎你 "+courseInfo.getStudent().getName()+"!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLogin(){
        String urlPath = UrlUtil.getLoginUrl(username,password);
        StringRequest stringRequest = new StringRequest(urlPath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loginBtn.setEnabled(true);
                        bar_loading.setVisibility(View.INVISIBLE);
                        userNameEdit.setEnabled(true);
                        passwordEdit.setEnabled(true);
                        if (!TextUtils.isEmpty(response) && !response.equals("登录失败")) {
                            Log.d(TAG,response);
                            //处理个人数据
                            courseInfo = Utility.handleCourseInfo(response);
                            updateUIThread(courseInfo);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginBtn.setEnabled(true);
                bar_loading.setVisibility(View.INVISIBLE);
                userNameEdit.setEnabled(true);
                passwordEdit.setEnabled(true);
                Toast.makeText(LoginActivity.this, "网络连接有误", Toast.LENGTH_SHORT).show();

                Log.e(TAG, error.getMessage(), error);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
