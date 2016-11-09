package com.example.administrator.facesign.util;

/**
 * Created by Administrator on 2016/11/9.
 */
public class UrlUtil {
    public static String getLoginUrl(String username,String password){
        String urlPath = "http://192.168.253.1:8080/webtest/loginaction?username="+username +"&password=" + password;
        return urlPath;
    }

    public static String getDownloadImageUrl(String username){
        String urlPath = "http://192.168.253.1:8080/webtest/downloadImage?username="+username;
        return urlPath;
    }
}
