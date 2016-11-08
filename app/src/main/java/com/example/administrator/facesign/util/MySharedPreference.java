package com.example.administrator.facesign.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.administrator.facesign.entity.Student;

/**
 * Created by Administrator on 2016/11/8.
 */
public class MySharedPreference {

    public static void saveStudent(Context context, Student student){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name",student.getName());
        editor.putString("studentId",student.getStudentid());
        editor.putString("grade",student.getGrade());
        editor.putString("major",student.getMajor());
        editor.putString("schools",student.getSchools());
        editor.commit();
    }

    public static Student loadStudent(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String name = preferences.getString("name","");
        String studentid = preferences.getString("studentId","");
        String grade = preferences.getString("grade","");
        String major = preferences.getString("major","");
        String schools = preferences.getString("schools","");
        Student student = new Student(name,studentid,major,schools,grade);
        return student;
    }
}
