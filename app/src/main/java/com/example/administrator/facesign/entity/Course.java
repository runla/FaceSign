package com.example.administrator.facesign.entity;

import java.io.Serializable;

public class Course implements Serializable{

	//课程名称
	private String courseName;
	//课程id
	private String courseId;
	//教室名称
	private String teacherName;
	//教室id
	private String teacherId;
	//教室
	private String room;
	//具体到星期几
	private int day;
	//开始周
	private int startWeek;
	//总周数
	private int totalWeeks;
	//开始上课节数
	private int startSection;
	//上课总周数
	private int totalSection;

	//单双周,单周为1，双周为2，部分为0
	private int singleOrDouble;

	private String courseClassId;

	public Course(String courseName, String courseId, String teacherName, String teacherId, String room,
				  int day, int startWeek, int totalWeeks, int startSection,
				  int totalSection, int singleOrDouble, String courseClassId) {
		this.courseName = courseName;
		this.courseId = courseId;
		this.teacherName = teacherName;
		this.teacherId = teacherId;
		this.room = room;
		this.day = day;
		this.startWeek = startWeek;
		this.totalWeeks = totalWeeks;
		this.startSection = startSection;
		this.totalSection = totalSection;
		this.singleOrDouble = singleOrDouble;
		this.courseClassId = courseClassId;
	}

	public String getCourseClassId() {
		return courseClassId;
	}

	public void setCourseClassId(String courseClassId) {
		this.courseClassId = courseClassId;
	}

	public int getSingleOrDouble() {
		return singleOrDouble;
	}


	public void setSingleOrDouble(int singleOrDouble) {
		this.singleOrDouble = singleOrDouble;
	}


	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getStartWeek() {
		return startWeek;
	}
	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}
	public int getTotalWeeks() {
		return totalWeeks;
	}
	public void setTotalWeeks(int totalWeeks) {
		this.totalWeeks = totalWeeks;
	}
	public int getStartSection() {
		return startSection;
	}
	public void setStartSection(int startSection) {
		this.startSection = startSection;
	}
	public int getTotalSection() {
		return totalSection;
	}
	public void setTotalSection(int totalSection) {
		this.totalSection = totalSection;
	}

	
	
	
	
	
}
