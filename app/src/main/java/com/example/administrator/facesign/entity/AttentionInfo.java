package com.example.administrator.facesign.entity;

/**
 * Created by Administrator on 2016/12/14.
 */

public class AttentionInfo {
    private String teacherName;
    private String courseName;
    private int weekend;
    private int day;
    private int status;
    private int startSection;
    private int endSection;

    public AttentionInfo() {
    }

    public AttentionInfo(String teacherName, String courseName, int weekend, int day, int status, int startSection, int endSection) {
        this.teacherName = teacherName;
        this.courseName = courseName;
        this.weekend = weekend;
        this.day = day;
        this.status = status;
        this.startSection = startSection;
        this.endSection = endSection;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getWeekend() {
        return weekend;
    }

    public void setWeekend(int weekend) {
        this.weekend = weekend;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }
}
