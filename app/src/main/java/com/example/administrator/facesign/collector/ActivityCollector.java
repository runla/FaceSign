package com.example.administrator.facesign.collector;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    //添加一个Activity
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    //移除出一个Activity
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    //关闭所有Activity
    public static void finishAll(){
        for (Activity activity : activities) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
