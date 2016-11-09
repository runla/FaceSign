package com.example.administrator.facesign.util;

/**
 * Created by Administrator on 2016/11/9.
 */
public class UrlUtil {
    public static final String v4IP = "192.168.253.1";
    public static String getLoginUrl(String username,String password){
        String urlPath = "http://"+v4IP+":8080/webtest/loginaction?username="+username +"&password=" + password;
        return urlPath;
    }

    public static String getDownloadImageUrl(String username){
        String urlPath = "http://"+v4IP+":8080/webtest/downloadImage?username="+username;
        return urlPath;
    }

    public static String getSignUpUrl(String username,int status)
    {
        String urlPath = "http://"+v4IP+":8080/webtest/signUpAction?status="+status+"&username="+username;
        return urlPath;
    }
}
