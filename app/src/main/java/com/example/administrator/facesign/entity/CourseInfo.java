package com.example.administrator.facesign.entity;

import android.app.Application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CourseInfo extends Application implements Serializable {

	//学生
	private Student student;
	//课程列表
	private List<Course> courseList = new ArrayList<Course>();
	
	public CourseInfo(){
		
	}
	public CourseInfo(Student student, List<Course> courseList) {
		super();
		this.student = student;
		this.courseList = courseList;
	}


	public Student getStudent() {
		return student;
	}


	public void setStudent(Student student) {
		this.student = student;
	}


	public List<Course> getCourseList() {
		return courseList;
	}


	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}
	
	
	
}
