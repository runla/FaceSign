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

    public static final int VERSION = 3;

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
     * @param studentId
     * @param startTime     学期开始的时间
     */
    public void saveCourse(Course course,String studentId,Long startTime){

        if (course != null) {
            ContentValues values = new ContentValues();
            values.put("studentId",studentId);
            values.put("courseClassId",course.getCourseClassId());
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
            values.put("startTime",startTime.toString());
            db.insert("Course",null,values);
        }
    }
    /**
     * 判断数据库中是否已经存在该课程
     */
    public boolean isExistCourseList(String studentId,String courseClassId,Long startTime) {
        Cursor cursor = db.query("Course", null, "studentId = ? and courseClassId = ? and startTime = ? ", new String[]{studentId, courseClassId,startTime.toString()}, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    /**
     *保存课程
     * @param courseList
     * @param studentId
     * @param startTime
     * @return
     */
    public boolean saveCourseList(List<Course>courseList,String studentId,Long startTime){
        for (Course course : courseList) {
            if (!isExistCourseList(studentId,course.getCourseClassId(),startTime)) {
                saveCourse(course,studentId,startTime);
            }
        }
        return true;
    }
    public List<Course> loadCourseList(String studentId,Long startTime){
        List<Course> courseList = new ArrayList<Course>();
        Log.d(BaseActivity.TAG,studentId);

        Cursor cursor = db.query("Course",null,"studentId = ? and startTime = ? ",new String[]{studentId,startTime.toString()},null,null,null);
        if (cursor.moveToFirst()){
            do {
                String courseName = cursor.getString(cursor.getColumnIndex("courseName"));
                String courseId = cursor.getString(cursor.getColumnIndex("courseId"));
                String courseClassId = cursor.getString(cursor.getColumnIndex("courseClassId"));
                String teacherName = cursor.getString(cursor.getColumnIndex("teacherName"));
                String teacherId = cursor.getString(cursor.getColumnIndex("teacherId"));
                String room = cursor.getString(cursor.getColumnIndex("room"));
                int day = cursor.getInt(cursor.getColumnIndex("day"));
                int startWeek = cursor.getInt(cursor.getColumnIndex("startWeek"));
                int totalWeeks = cursor.getInt(cursor.getColumnIndex("totalWeeks"));
                int singleOrDouble = cursor.getInt(cursor.getColumnIndex("singleOrDouble"));
                int startSection = cursor.getInt(cursor.getColumnIndex("startSection"));
                int totalSection = cursor.getInt(cursor.getColumnIndex("totalSection"));
               // Long startTime = Long.parseLong(cursor.getString(cursor.getColumnIndex("startTime")));

                Course course = new Course(courseName,courseId,teacherName,teacherId,room,
                        day,startWeek,totalWeeks,startSection,totalSection,singleOrDouble,courseClassId);
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
