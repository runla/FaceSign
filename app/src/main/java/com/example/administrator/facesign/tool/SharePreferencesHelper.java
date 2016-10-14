package com.example.administrator.facesign.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/19.
 * @author chenjianrun
 * @city zhuhai
 * *
 * 功能：
 * 对sharedPreferences类的功能封装
 * 实例化时需要两个参数，一个是context,另一个是保存文件的name
 * 使用putValues保存数据
 * 使用方法getInt、getString获取相应的数据
 */
public class SharePreferencesHelper {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int stringListCount = 0;
    public static final String COUNT_KEY = "count";
    public static final String PREFIX_KEY = "prefixKey";

    Context context;
    Activity activity;
    public SharePreferencesHelper(Context context,String filename){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(filename,0);
        editor = sharedPreferences.edit();
        stringListCount = sharedPreferences.getInt(COUNT_KEY,0);
       // Log.d("SharePreferencesHelper","context");

    }

    public SharePreferencesHelper(Activity activity, String filename){
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(filename,0);
        editor = sharedPreferences.edit();
        stringListCount = sharedPreferences.getInt(COUNT_KEY,0);
       // Log.d("SharePreferencesHelper","activity");

    }

    public void putValue(String key,Boolean value){
        editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    public void putValue(String key,String value){
        editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public void putValue(String key,int value){
        editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public void putValueToStringList(String value){
        editor = sharedPreferences.edit();
        stringListCount++;
        editor.putString(SharePreferencesHelper.PREFIX_KEY +stringListCount,value);
        editor.putInt(COUNT_KEY,stringListCount);
        editor.commit();
    }
    public ArrayList<String> getStringList(){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = stringListCount;i>0;i--){
            list.add(getString(SharePreferencesHelper.PREFIX_KEY +i));
            Log.d("SharePreferencesHelper",getString(SharePreferencesHelper.PREFIX_KEY +i));
        }
        return list;
    }


    public String getString(String key){
        return sharedPreferences.getString(key,null);
    }
    public int getInt(String key){
        return sharedPreferences.getInt(key,0);
    }
    public Boolean getBoolean(String key){  return sharedPreferences.getBoolean(key,false); }
    public int getStringListCount(){  return stringListCount; }

    public void clearData(){
        editor.clear();
        editor.commit();
    }

    public boolean isExist(String value){
        String tempkey = "";
        String tempvalue = "";

        for(int i = stringListCount;i > 0;i--){
           tempkey = PREFIX_KEY +i;
           tempvalue = sharedPreferences.getString(tempkey,null);
            if (value.equals(tempvalue)){
                return true;
            }
       }
        return false;
    }
    public boolean isExist(int value){

        String tempkey = "";
        int tempvalue;

        for(int i = stringListCount;i > 0;i--){
            tempkey = PREFIX_KEY +i;
            tempvalue = sharedPreferences.getInt(tempkey,0);
            if (value == tempvalue){
                return true;
            }
        }
        return false;
    }
}
