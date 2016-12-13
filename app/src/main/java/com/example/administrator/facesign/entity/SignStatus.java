package com.example.administrator.facesign.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/8.
 */

public class SignStatus implements Serializable{
    private int signUpStatus;
    private int recognizeStatus;
    private int uploadStatus;
    private Long uploadTime;
    private Double longitude;//经度
    private Double latitude;//纬度
    private String addr;//位置

    public SignStatus(int signUpStatus, int recognizeStatus, int uploadStatus, Long uploadTime, Double longtitud, Double latitude, String addr) {
        this.signUpStatus = signUpStatus;
        this.recognizeStatus = recognizeStatus;
        this.uploadStatus = uploadStatus;
        this.uploadTime = uploadTime;
        this.longitude = longtitud;
        this.latitude = latitude;
        this.addr = addr;
    }

    public Double getLongtitud() {
        return longitude;
    }

    public void setLongtitud(Double longtitud) {
        this.longitude = longtitud;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public SignStatus() {
    }

    public int getSignUpStatus() {
        return signUpStatus;
    }

    public void setSignUpStatus(int signUpStatus) {
        this.signUpStatus = signUpStatus;
    }

    public int getRecognizeStatus() {
        return recognizeStatus;
    }

    public void setRecognizeStatus(int recognizeStatus) {
        this.recognizeStatus = recognizeStatus;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public Long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }

}
