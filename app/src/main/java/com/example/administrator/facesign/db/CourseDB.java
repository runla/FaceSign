package com.example.administrator.facesign.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.facesign.activity.BaseActivity;
import com.example.administrator.facesign.entity.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CourseDB {
    //数据库名称
    public static final String DB_NAME = "CourseDB";

    public static final int VERSION = 2;

    private static CourseDB courseDB;

    private SQLiteDatabase db;

    private CourseDB(Context context)
    {
        CourseOpenHelper dbHelper = new CourseOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CSDNDBm实例
     */
    public synchronized static CourseDB getInstance(Context context){
        if (courseDB == null) {
            courseDB = new CourseDB(context);
        }
        return courseDB;
    }

    /**
     *
     * @param course
     *
     */
    public void saveCourse(Course course,String studentId){

        if (course != null) {
            ContentValues values = new ContentValues();
            values.put("studentId",studentId);
            values.put("courseId",course.getCourseId());
            values.put("courseName",course.getCourseName());
            values.put("teacherName",course.getTeacherName());
            values.put("teacherId",course.getTeacherId());
            values.put("room",course.getRoom());
            values.put("day",course.getDay());
            values.put("startWeek",course.getStartWeek());
            values.put("totalWeeks",course.getTotalWeeks());
            values.put("singleOrDouble",course.getSingleOrDouble());
            values.put("startSection",course.getStartSection());
            values.put("totalSection",course.getTotalSection());

            db.insert("Course",null,values);
        }
    }
    /**
     * 判断数据库中是否已经存在该课程
     */
    public boolean isExistCourseList(String studentId,String courseId) {
        Cursor cursor = db.query("Course", null, "studentId = ? and courseId = ?", new String[]{studentId, courseId}, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }
    public List<Course> loadCourseList(String studentId){
        List<Course> courseList = new ArrayList<Course>();
        Log.d(BaseActivity.TAG,studentId);
        Cursor cursor = db.query("Course",null,"studentId = ? ",new String[]{studentId},null,null,null);
        if (cursor.moveToFirst()){
            do {
                String courseName = cursor.getString(cursor.getColumnIndex("courseName"));
                String courseId = cursor.getString(cursor.getColumnIndex("courseId"));
                String teacherName = cursor.getString(cursor.getColumnIndex("teacherName"));
                String teacherId = cursor.getString(cursor.getColumnIndex("teacherId"));
                String room = cursor.getString(cursor.getColumnIndex("room"));
                int day = cursor.getInt(cursor.getColumnIndex("day"));
                int startWeek = cursor.getInt(cursor.getColumnIndex("startWeek"));
                int totalWeeks = cursor.getInt(cursor.getColumnIndex("totalWeeks"));
                int singleOrDouble = cursor.getInt(cursor.getColumnIndex("singleOrDouble"));
                int startSection = cursor.getInt(cursor.getColumnIndex("startSection"));
                int totalSection = cursor.getInt(cursor.getColumnIndex("totalSection"));


                Course course = new Course(courseName,courseId,teacherName,teacherId,room,
                        day,startWeek,totalWeeks,startSection,totalSection,singleOrDouble);
                courseList.add(course);
            }
            while(cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return courseList;
    }
}
