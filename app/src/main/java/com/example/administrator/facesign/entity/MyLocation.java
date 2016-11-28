package com.example.administrator.facesign.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/23.
 */

public class MyLocation implements Parcelable {
    //时间
    private String time;
    //纬度
    private double latitude;
    //经度
    private double longitude;
    //地址
    private String addr;
    //类型码
    private int errorCode;
    //定位器描述
    private String describe;
    //位置语义化信息
    private String locationdescribe;
    //省
    private String province;
    //城市
    private String city;
    //区
    private String district;
    //街道
    private String street;
    //建筑物id
    private String buildingID;
    //建筑物
    private String buildingName;
    //楼层信息
    private String Floor;

    public MyLocation(){}

    public MyLocation(String time, double latitude, double longitude, String addr, int errorCode, String describe,
                      String locationdescribe, String province, String city, String district, String street,
                      String buildingID, String buildingName, String floor) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addr = addr;
        this.errorCode = errorCode;
        this.describe = describe;
        this.locationdescribe = locationdescribe;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
        this.buildingID = buildingID;
        this.buildingName = buildingName;
        Floor = floor;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String buildingID) {
        this.buildingID = buildingID;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getLocationdescribe() {
        return locationdescribe;
    }

    public void setLocationdescribe(String locationdescribe) {
        this.locationdescribe = locationdescribe;
    }


    /**
     * 将JsonObject对象转为实体类
     * @param jsonObject
     */
    public void jsonObjectToEntity(JSONObject jsonObject){
        try {
            this.time = jsonObject.getString("time");
            this.latitude = jsonObject.getDouble("latitude");
            this.longitude = jsonObject.getDouble("longitude");
            this.addr = jsonObject.getString("addr");
            this.errorCode = jsonObject.getInt("errorCode");
            this.describe = jsonObject.getString("describe");
            this.locationdescribe = jsonObject.getString("locationdescribe");

            this.city = jsonObject.getString("city");
            this.district = jsonObject.getString("district");
            this.street = jsonObject.getString("street");
            this.province = jsonObject.getString("province");
            this.buildingID = jsonObject.getString("buildingID");
            this.buildingName = jsonObject.getString("buildingName");
            this.Floor = jsonObject.getString("Floor");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将实体类转为JsonObject对象
     * @return
     */
    public JSONObject entityToJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time",this.time);
            jsonObject.put("latitude",this.latitude);
            jsonObject.put("longitude",this.longitude);
            jsonObject.put("addr",this.addr);
            jsonObject.put("errorCode",this.errorCode);
            jsonObject.put("describe",this.describe);
            jsonObject.put("locationdescribe",this.locationdescribe);
            jsonObject.put("city",this.city);
            jsonObject.put("district",this.district);
            jsonObject.put("street",this.street);
            jsonObject.put("province",this.province);
            jsonObject.put("buildingID",this.buildingID);
            jsonObject.put("buildingName",this.buildingName);
            jsonObject.put("Floor",this.Floor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.addr);
        dest.writeInt(this.errorCode);
        dest.writeString(this.describe);
        dest.writeString(this.locationdescribe);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.street);
        dest.writeString(this.buildingID);
        dest.writeString(this.buildingName);
        dest.writeString(this.Floor);
    }

    protected MyLocation(Parcel in) {
        this.time = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.addr = in.readString();
        this.errorCode = in.readInt();
        this.describe = in.readString();
        this.locationdescribe = in.readString();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.street = in.readString();
        this.buildingID = in.readString();
        this.buildingName = in.readString();
        this.Floor = in.readString();
    }

    public static final Parcelable.Creator<MyLocation> CREATOR = new Parcelable.Creator<MyLocation>() {
        @Override
        public MyLocation createFromParcel(Parcel source) {
            return new MyLocation(source);
        }

        @Override
        public MyLocation[] newArray(int size) {
            return new MyLocation[size];
        }
    };
}
