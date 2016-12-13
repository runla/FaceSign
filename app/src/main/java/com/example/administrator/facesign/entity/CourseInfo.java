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

	private EduTerm eduTerm;

	public CourseInfo(){
		
	}
	public CourseInfo(Student student, List<Course> courseList) {
		super();
		this.student = student;
		this.courseList = courseList;
	}

	public CourseInfo(Student student, List<Course> courseList, EduTerm eduTerm) {
		this.student = student;
		this.courseList = courseList;
		this.eduTerm = eduTerm;
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

	public EduTerm getEduTerm() {
		return eduTerm;
	}

	public void setEduTerm(EduTerm eduTerm) {
		this.eduTerm = eduTerm;
	}
}
