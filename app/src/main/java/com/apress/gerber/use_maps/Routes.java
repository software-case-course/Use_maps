package com.apress.gerber.use_maps;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Wx on 2017/1/22.
 */

public class Routes extends BmobObject {
    private Public_User RUsername;
    private Integer Likenumber;
    private Integer Commentsnumber;
    private String Routename;
    private BmobRelation Liker;
    private List<String> HaveCity;


    public Public_User getRUsername() {
        return RUsername;
    }

    public void setRUsername(Public_User RUsername) {
        this.RUsername = RUsername;
    }

    public Integer getLikenumber() {
        return Likenumber;
    }

    public void setLikenumber(Integer Likenumber) {
        this.Likenumber = Likenumber;
    }

    public Integer getCommentsnumber() {
        return Commentsnumber;
    }

    public void setCommentsnumber(Integer Commentsnumber) {
        this.Commentsnumber = Commentsnumber;
    }

    public String getRoutename() {
        return Routename;
    }

    public void setRoutename(String Routename) {
        this.Routename = Routename;
    }

    public BmobRelation getLiker() {
        return Liker;
    }

    public void setLiker(BmobRelation Liker) {
        this.Liker = Liker;
    }

    public List<String> getHaveCity() {
        return HaveCity;
    }

    public void setHaveCity(List<String> HaveCity) {
        this.HaveCity = HaveCity;
    }
}
