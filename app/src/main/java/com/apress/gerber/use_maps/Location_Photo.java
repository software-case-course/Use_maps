package com.apress.gerber.use_maps;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Wx on 2017/1/22.
 */

public class Location_Photo extends BmobObject {

    private static final long serialVersionUID = 1L;

    private Public_User LPUsername;
    private Routes LPRouteID;
    private double Location_Longitude;
    private double Location_Latitude;
    private String City;
    private String Emotion;
    private BmobDate Writedate;
    private String Mydate;
    private List<String> Pictures;

    public Public_User getLPUsername() {
        return LPUsername;
    }
    public void setLPUsername(Public_User LPUsername) {
        this.LPUsername = LPUsername;
    }

    public Routes getLPRouteID() {
        return LPRouteID;
    }
    public void setLPRouteID(Routes LPRouteID) {
        this.LPRouteID = LPRouteID;
    }

    public Number getLocation_Longitude() {
        return Location_Longitude;
    }
    public void setLocation_Longitude(double Location_Longitude) {
        this.Location_Longitude = Location_Longitude;
    }

    public Number getLocation_Latitude() {
        return Location_Latitude;
    }
    public void setLocation_Latitude(double Location_Latitude) {
        this.Location_Latitude = Location_Latitude;
    }

    public String getCity() {
        return City;
    }
    public void setCity(String City) {
        this.City = City;
    }

    public String getEmotion() {
        return Emotion;
    }
    public void setEmotion(String Emotion) {
        this.Emotion = Emotion;
    }

    public BmobDate getWritedate() {
        return Writedate;
    }
    public void setWritedate(BmobDate Writedate) {
        this.Writedate = Writedate;
    }

    public String getMydate() {
        return Mydate;
    }
    public void setMydate(String Mydate) {
        this.Mydate = Mydate;
    }

    public List<String> getPictures()
    {
        return Pictures;
    }
    public void setPictures(List<String> Pictures)
    {
        this.Pictures=Pictures;
    }
}

