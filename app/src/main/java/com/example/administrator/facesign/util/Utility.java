package com.example.administrator.facesign.util;

import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.entity.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */
public class Utility {
    public static CourseInfo handleCourseInfo(String response){
        CourseInfo info = new CourseInfo();
        List<Course> courseList = new ArrayList<Course>();
        Student student = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            String studentStr = jsonObject.getString("student");
            student = handleStudent(studentStr);
            //文章对象数组
            JSONArray list2 = (JSONArray) jsonObject.getJSONArray("courseList");
            for (int i = 0; i < list2.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) list2.get(i);

                String courseName = jsonObject1.getString("courseName");
                String courseId = jsonObject1.getString("courseId");
                String teacherName = jsonObject1.getString("teacherName");
                String teacherId = jsonObject1.getString("teacherId");
                String room = jsonObject1.getString("room");
                int day = jsonObject1.getInt("day");
                int startWeek = jsonObject1.getInt("startWeek");
                int totalWeeks = jsonObject1.getInt("totalWeeks");
                int startSection = jsonObject1.getInt("startSection");
                int totalSection = jsonObject1.getInt("totalSection");
                int singleOrDouble = jsonObject1.getInt("singleOrDouble");

                Course course = new Course(courseName,courseId,teacherName,teacherId,room,day,startWeek,totalWeeks,startSection,totalSection,singleOrDouble);
                courseList.add(course);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        info = new CourseInfo(student,courseList);
        return info;
    }
    public static Student handleStudent(String response){
        JSONObject jsonObject = null;
        Student student = null;
        try {
            jsonObject = new JSONObject(response);
            String name = jsonObject.getString("name");
            String studentid = jsonObject.getString("studentid");
            String major = jsonObject.getString("major");
            String schools = jsonObject.getString("schools");
            String grade = jsonObject.getString("grade");
            student = new Student(name,studentid,major,schools,grade);
            return student;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return student;
    }
}
