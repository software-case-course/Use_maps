package com.apress.gerber.use_maps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Info implements Serializable
{
    private static final long serialVersionUID=-1010711775;
    private double latitude;
    private double longitude;
    private int imgId;//覆盖物的图片
    private String name;//覆盖物的名称
    private String distance;//覆盖物离自己的距离
    private int zan;//覆盖物对应的点赞数

    public static List<Info>infos=new ArrayList<Info>();
    static
    {
        infos.add(new Info(40.9401752,116.400244,R.drawable.welcome_welcome,"英伦贵族小旅馆","距离000米",1300));
        infos.add(new Info(39.9401752,115.400244,R.drawable.welcome_welcome,"正佳","距离000米",1310));
        infos.add(new Info(38.9401752,115.400244,R.drawable.welcome_welcome,"麦当劳","距离000米",1230));
        infos.add(new Info(39.9401752,117.400244,R.drawable.welcome_welcome,"五山水果","距离000米",1700));
    }

    public Info(double latitude,double longitude,int imgId,String name,String distance,int zan)
    {
        this.latitude=latitude;
        this.longitude=longitude;
        this.imgId=imgId;
        this.name=name;
        this.distance=distance;
        this.zan=zan;
    }

    public double getLatitude()
    {
        return latitude;
    }
    public void setLatitude(double latitude)
    {
        this.latitude=latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }
    public void setLongitude(double longitude)
    {
        this.longitude=longitude;
    }

    public int getImgId()
    {
        return imgId;
    }
    public void setImgId(int imgId)
    {
        this.imgId=imgId;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }

    public String getDistance()
    {
        return distance;
    }
    public void setDistance(String distance)
    {
        this.distance=distance;
    }

    public int getZan()
    {
        return zan;
    }
    public void setZan(int zan)
    {
        this.zan=zan;
    }
}
