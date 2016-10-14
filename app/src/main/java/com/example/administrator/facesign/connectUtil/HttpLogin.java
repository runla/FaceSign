package com.example.administrator.facesign.connectUtil;

import android.util.Log;

import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.tool.XmlUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/10/5.
 */
public class HttpLogin {
    private static final String LOGIN_URL = "http://192.168.1.102:8080/webtest/loginaction";
    private static final String TAG = "httpLoginDebug";
    public static CourseInfo LoginByPost(String username, String passwd){
        Log.d(HttpLogin.TAG,"启动登录线程");
        CourseInfo msg = null;
        try {
            //初始化URL
            URL url = new URL(LOGIN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //设置请求方式
            conn.setRequestMethod("POST");

            //设置超时信息
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            //设置允许输入
            conn.setDoInput(true);
            //设置允许输出
            conn.setDoOutput(true);

            //post方式不能设置缓存，需手动设置为false
            conn.setUseCaches(false);

            //我们请求的数据
            String data = "password="+ URLEncoder.encode(passwd,"UTF-8")+
                    "&username="+URLEncoder.encode(username,"UTF-8");

            //獲取輸出流
            OutputStream out = conn.getOutputStream();

            out.write(data.getBytes());
            out.flush();
            out.close();
            conn.connect();

            if (conn.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = conn.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    message.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                message.close();
                // 返回字符串
                String resultStr = new String(message.toByteArray());
                msg = XmlUtil.xmlToObject_CourseInfo(resultStr);
                System.out.print(msg);
                Log.d("AnotherTest",""+msg);
                return msg;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(HttpLogin.TAG,"exit");
        return msg;
    }
}
