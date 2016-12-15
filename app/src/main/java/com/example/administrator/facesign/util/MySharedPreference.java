package com.example.administrator.facesign.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.administrator.facesign.entity.EduTerm;
import com.example.administrator.facesign.entity.Student;

import java.util.Date;

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

    public static void saveEduDate(Context context,EduTerm eduTerm){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Long start = eduTerm.getStartDate().getTime();
        editor.putString("startDate",start.toString());
        Long end = eduTerm.getEndDate().getTime();
        editor.putString("endDate",end.toString());
        editor.commit();
    }
    public static EduTerm loadEduDate(Context context){
        EduTerm eduTerm;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Date start = new Date(Long.parseLong(preferences.getString("startDate","0")));
        Date end = new Date(Long.parseLong(preferences.getString("endDate","0")));
        eduTerm = new EduTerm(start,end);
        return eduTerm;
    }
}
