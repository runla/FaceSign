package com.example.administrator.facesign.util;

/**
 * Created by Administrator on 2016/11/9.
 */
public class UrlUtil {
    public static final String v4IP = "119.29.229.16";
    public static String getLoginUrl(String username,String password){
        String urlPath = "http://"+v4IP+":8080/myServer/loginaction?username="+username +"&password=" + password;
        return urlPath;
    }

    public static String getDownloadImageUrl(String username){
        String urlPath = "http://"+v4IP+":8080/myServer/downloadImage?username="+username;
        return urlPath;
    }

    public static String getSignUpUrl(String username,int status)
    {
        String urlPath = "http://"+v4IP+":8080/myServer/signUpAction?status="+status+"&username="+username;
        return urlPath;
    }

    public static String getUploadInfoUrl(){
        String urlPath = "http://"+v4IP+":8080/myServer/uploadImage";
        return urlPath;
    }
}
