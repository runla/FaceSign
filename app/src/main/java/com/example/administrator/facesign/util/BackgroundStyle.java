package com.example.administrator.facesign.util;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.administrator.facesign.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */

public class BackgroundStyle {


    public static List<Drawable> initDrawableList(Context mContext){
        List<Drawable> drawableList = new ArrayList<>();
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_1));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_2));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_3));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_4));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_5));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_6));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_7));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_8));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_9));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_10));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_11));
        drawableList.add(ContextCompat.getDrawable(mContext, R.drawable.course_button_12));
        return drawableList;
    }
}
