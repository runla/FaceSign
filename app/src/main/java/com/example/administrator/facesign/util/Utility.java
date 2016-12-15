package com.example.administrator.facesign.util;

import com.example.administrator.facesign.entity.AttentionInfo;
import com.example.administrator.facesign.entity.Course;
import com.example.administrator.facesign.entity.CourseInfo;
import com.example.administrator.facesign.entity.EduTerm;
import com.example.administrator.facesign.entity.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */
public class Utility {
    public static List<AttentionInfo> handleAttentionInfoList(String response){
        List<AttentionInfo> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String teacherName = jsonObject.getString("teacherName");
                String courseName = jsonObject.getString("courseName");
                int weekend = jsonObject.getInt("weekend");
                int day = jsonObject.getInt("day");
                int statu = jsonObject.getInt("status");
                int startSection = jsonObject.getInt("startSection");
                int endSection = jsonObject.getInt("endSection");
                list.add(new AttentionInfo(teacherName,courseName,weekend,day,statu,startSection,endSection));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static CourseInfo handleCourseInfo(String response){
        CourseInfo info = new CourseInfo();
        List<Course> courseList = new ArrayList<Course>();
        Student student = null;
        EduTerm eduTerm = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            String studentStr = jsonObject.getString("student");//处理学生基本信息
            student = handleStudent(studentStr);
            String eduTermStr = jsonObject.getString("eduTerm");//处理开学时间
            eduTerm = handleDate(eduTermStr);
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
                String courseClassId = jsonObject1.getString("courseClassId");

                Course course = new Course(courseName,courseId,teacherName,teacherId,room,day,startWeek,totalWeeks,startSection,totalSection,singleOrDouble,courseClassId);
                courseList.add(course);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //对课程按照开始上课的节数进行排序
        Collections.sort(courseList, new Comparator<Course>() {
            /**
             * 返回一个基本的数据类型的整数
             * 返回 -1表示 course1 小于 course2
             * 返回  1表示 course1 大于 course2
             * 返回 0表示 course1 等于于 course2
             * @return
             */
            public int compare(Course course1, Course course2) {
                if (course1.getStartSection()>course2.getStartSection()) {
                    return 1;
                }
                if (course1.getStartSection()<course2.getStartSection()) {
                    return -1;
                }
                return 0;
            }
        });
        info = new CourseInfo(student,courseList,eduTerm);
        return info;
    }
    public static EduTerm handleDate(String response){
        EduTerm eduTerm = null;
        JSONObject jsonObject = null;
        SimpleDateFormat  dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            jsonObject = new JSONObject(response);
            String start = jsonObject.getString("startDate");
            String end = jsonObject.getString("endDate");
            eduTerm = new EduTerm(dFormat.parse(start),dFormat.parse(end));
            return eduTerm;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return eduTerm;
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
