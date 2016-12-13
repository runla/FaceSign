package com.example.administrator.facesign.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.facesign.entity.SignStatus;

import java.util.function.DoubleToIntFunction;

/**
 * Created by Administrator on 2016/12/8.
 */

public class SignUpSharePreference {
    private String fileName;
    public static final String FIRST_SIGN_UP_STATUS = "firstSignUpStatus";
    public static final String FIRST_RECOGNIZE_STATUS = "firstRecognizeStatus";
    public static final String FIRST_UPLOAD_STATUS = "firstUploadStatus";
    public static final String FIRST_UPLOAD_TIME="firstUploadTime";
    public static final String FIRST_ADDR = "firstAddress";
    public static final String FIRST_LONGITUDE = "firstLongitude";
    public static final String FIRST_LATITUDE = "firstLatitude";

    public static final String SECOND_SIGN_UP_STATUS = "secondSignUpStatus";
    public static final String SECOND_RECOGNIZE_STATUS = "secondRecognizeStatus";
    public static final String SECOND_UPLOAD_STATUS = "secondUploadStatus";
    public static final String SECOND_UPLOAD_TIME="secondUploadTime";
    public static final String SECOND_ADDR = "secondAddress";
    public static final String SECOND_LONGITUDE = "secondLongitude";
    public static final String SECOND_LATITUDE = "secondLatitude";

    public static final String SIGN_TIMES = "signTimes";//签到的次数（上课签到、下课签到)

    private SharedPreferences mSharedPreferences;
    public SignUpSharePreference(Context mContext,int currentWeek, int day, int startSection){
        fileName = ""+currentWeek+"_"+day+"_"+startSection;
        mSharedPreferences = mContext.getSharedPreferences(fileName,Context.MODE_PRIVATE);
    }

    /**
     * 保存第一次签到的信息
     * @param firstStaus
     * @param signTimes
     */
    public void saveFirstSignStatus(SignStatus firstStaus,int signTimes){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(FIRST_SIGN_UP_STATUS,firstStaus.getSignUpStatus());
        editor.putInt(FIRST_RECOGNIZE_STATUS,firstStaus.getRecognizeStatus());
        editor.putInt(FIRST_UPLOAD_STATUS,firstStaus.getUploadStatus());
        editor.putLong(FIRST_UPLOAD_TIME,firstStaus.getUploadTime());
        editor.putString(FIRST_ADDR,firstStaus.getAddr());
        editor.putInt(SIGN_TIMES,signTimes);

        String latitude = firstStaus.getLatitude().toString();
        editor.putString(FIRST_LATITUDE,latitude);
        String longitude = firstStaus.getLongtitud().toString();
        editor.putString(FIRST_LATITUDE,longitude);
        editor.commit();
    }

    /**
     * 保存第二次的签到信息
     * @param secondStaus
     * @param signTimes
     */
    public void saveSecondSignStatus(SignStatus secondStaus,int signTimes){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(SECOND_SIGN_UP_STATUS,secondStaus.getSignUpStatus());
        editor.putInt(SECOND_RECOGNIZE_STATUS,secondStaus.getRecognizeStatus());
        editor.putInt(SECOND_UPLOAD_STATUS,secondStaus.getUploadStatus());
        editor.putLong(SECOND_UPLOAD_TIME,secondStaus.getUploadTime());
        editor.putInt(SIGN_TIMES,signTimes);

        editor.putString(SECOND_ADDR,secondStaus.getAddr());
        String latitude = secondStaus.getLatitude().toString();
        editor.putString(SECOND_LATITUDE,latitude);
        String longitude = secondStaus.getLongtitud().toString();
        editor.putString(SECOND_LATITUDE,longitude);
        editor.commit();
    }

    /**
     * 获取第一次签到信息
     * @return
     */
    public SignStatus loadFristSignStatus(){
        SignStatus signStatus = new SignStatus();
        signStatus.setSignUpStatus(mSharedPreferences.getInt(FIRST_SIGN_UP_STATUS,-1));
        signStatus.setRecognizeStatus(mSharedPreferences.getInt(FIRST_RECOGNIZE_STATUS,-1));
        signStatus.setUploadStatus(mSharedPreferences.getInt(FIRST_UPLOAD_STATUS,-1));
        signStatus.setUploadTime(mSharedPreferences.getLong(FIRST_UPLOAD_TIME,-1L));
        signStatus.setAddr(mSharedPreferences.getString(FIRST_ADDR,""));
        String latitude = mSharedPreferences.getString(FIRST_LATITUDE,"-1");
        String longitude = mSharedPreferences.getString(FIRST_LONGITUDE,"-1");
        signStatus.setLatitude(Double.parseDouble(latitude));
        signStatus.setLongtitud(Double.parseDouble(longitude));
        return signStatus;
    }

    /**
     * 获取第二次签到信息
     * @return
     */
    public SignStatus laodSecondSignStatus(){
        SignStatus signStatus = new SignStatus();
        signStatus.setSignUpStatus(mSharedPreferences.getInt(SECOND_SIGN_UP_STATUS,-1));
        signStatus.setRecognizeStatus(mSharedPreferences.getInt(SECOND_RECOGNIZE_STATUS,-1));
        signStatus.setUploadStatus(mSharedPreferences.getInt(SECOND_UPLOAD_STATUS,-1));
        signStatus.setUploadTime(mSharedPreferences.getLong(SECOND_UPLOAD_TIME,-1L));

        signStatus.setAddr(mSharedPreferences.getString(SECOND_ADDR,""));
        String latitude = mSharedPreferences.getString(SECOND_LATITUDE,"-1");
        String longitude = mSharedPreferences.getString(SECOND_LONGITUDE,"-1");
        signStatus.setLatitude(Double.parseDouble(latitude));
        signStatus.setLongtitud(Double.parseDouble(longitude));
        return signStatus;
    }

    /**
     * 获取当前是第几次签到了
     * @return
     */
    public int getSignTimes(){
        return mSharedPreferences.getInt(SIGN_TIMES,0);
    }
}
