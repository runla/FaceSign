package com.example.administrator.facesign.util;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/7.
 */

public  class TimerTaskUtil {
    private Timer timer;
    private Handler handler;
    public TimerTaskUtil(){
        timer = new Timer();
        handler = new Handler(Looper.getMainLooper());
    }
    //---------------内部方法-----------------------------------


    //------------------内部类---------------------------
    class MyTask extends TimerTask{
        @Override
        public void run() {
            //发送到主线程
            handler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    //---------------对外公布方法-----------------------------------
    public void startTimer(){

    }
    public void stopTimer(){

    }

    public interface TimerDataCallback{
    }
}
