package com.example.administrator.facesign.entity;

public class Student {
	//学生姓名
	private String name;
	//学号
	private String studentid;
	//专业
	private String major;
	//学院
	private String schools;
	//年级
	private String grade;
	
	public Student(String name, String studentid, String major, String schools, String grade) {
		super();
		this.name = name;
		this.studentid = studentid;
		this.major = major;
		this.schools = schools;
		this.grade = grade;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStudentid() {
		return studentid;
	}
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getSchools() {
		return schools;
	}
	public void setSchools(String schools) {
		this.schools = schools;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	

}
