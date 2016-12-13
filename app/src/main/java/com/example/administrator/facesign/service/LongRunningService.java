package com.example.administrator.facesign.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.administrator.facesign.R;
import com.example.administrator.facesign.activity.MainActivity;
import com.example.administrator.facesign.broadcast.AlarmReceiver;
import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.util.TimeUtil;
import com.example.administrator.facesign.util.UrlUtil;
import com.example.administrator.facesign.util.Utility;
import com.example.administrator.facesign.vollery.AppController;

import java.util.Date;

import static android.app.AlarmManager.RTC_WAKEUP;

/**
 * Created by Administrator on 2016/11/29.
 */

public class LongRunningService extends Service {
    private static final String TAG = "LongRunningService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //从本地文件中获取账号密码
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LongRunningService.this);
        String username = preferences.getString("studentId","");
        String password = preferences.getString("password","");
        checkLogin(username,password);

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void actionStart(Context mContext, String username, String password){

    }

    private void checkLogin(String username, String password){
        String urlPath = UrlUtil.getLoginUrl(username,password);
        StringRequest stringRequest = new StringRequest(urlPath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response) && !response.equals("登录失败")) {
                            Log.d(TAG,response);
                            //处理个人数据
                            CourseInfo courseInfo = Utility.handleCourseInfo(response);
                            //下节课上课的课程信息
                            Course course = TimeUtil.getNearestCourse(courseInfo);
                            if (course==null){
                                stopSelf();
                            }
                            //下节课上课的时间
                            Long courseAlarmTime = TimeUtil.getNearestTime(courseInfo);
                            //设置定时器
                            setAlarmTime(courseAlarmTime,course);
                        }
                        else{
                            //Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("1", error.getMessage(), error);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    /**
     * 定时器设置
     * @param alarmTime
     */
    private void setAlarmTime(Long alarmTime, Course course){
        Long currentTime = (new Date()).getTime();
        if (alarmTime==-1) {
            alarmTime = currentTime+TimeUtil.M*10;
        }
        //上课时间只剩下 <= 10分钟
        else if (alarmTime - currentTime <= TimeUtil.M*10){
            Log.d(TAG,"setAlarmTime");
            String describe = "还有"+(alarmTime-currentTime)/TimeUtil.M+"分钟就要上课了";

            alarmTime = currentTime+TimeUtil.M*20;
            //通知栏显示即将上课的课程信息
            showNotification(course,describe);
        }
        else if (alarmTime - currentTime > TimeUtil.M*10){
            alarmTime -= TimeUtil.M*10;
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent1 = new Intent(LongRunningService.this,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(LongRunningService.this,0,intent1,0);
        alarmManager.set(RTC_WAKEUP,alarmTime,pendingIntent);
        Log.d(TAG, "距离下次上课的时间还有 " + TimeUtil.timeDescribe(alarmTime));
    }

    /**
     * 通知栏显示课程消息
     * @param course
     */
    private void showNotification(Course course,String tickerDescribe){
        Log.d(TAG,"showNotification");
        Bitmap LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Intent it = new Intent(LongRunningService.this, MainActivity.class);
        PendingIntent pit = PendingIntent.getActivity(getApplicationContext(), 0, it, 0);
        //设置图片,通知标题,发送时间,提示方式等属性
        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());
        mBuilder.setContentTitle(course.getCourseName())                                         //标题
                .setContentText(course.getRoom()+"  "+course.getStartSection()+"-"+(course.getTotalSection()+course.getStartSection()-1)+"节")                   //内容
                  .setSubText(tickerDescribe)                                //内容下面的一小段文字
                .setTicker(tickerDescribe)                         //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())                                //设置通知时间
                .setSmallIcon(R.mipmap.ic_launcher)                             //设置小图标
                .setLargeIcon(LargeBitmap)                                          //设置大图标
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                .setAutoCancel(true)                                                //设置点击后取消Notification
                .setContentIntent(pit);                                              //设置PendingIntent
        Notification notify1 = mBuilder.build();
        NotificationManager mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNManager.notify(1, notify1);
    }
}
