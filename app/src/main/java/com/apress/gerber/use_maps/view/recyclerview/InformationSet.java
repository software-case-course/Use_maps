package com.apress.gerber.use_maps.view.recyclerview;


import android.graphics.drawable.Drawable;

public class InformationSet {

    String tvLoaction;        //地址
    Drawable ivImageView;      //主图片
    Drawable author_head;       //用户头像
    String author_head_url;
    String author_id;       //用户名
    boolean btnLike;        //是否点赞,true则用点亮图标显示
    Integer tvLikeCount;         //点赞数量
    Integer tvCommentCount;     //评论数量
    String tvDate;          //日期
    Drawable commentbutton;  //评论
    String routeId;
    String routeUsername;


    public String getTvDate() {
        return tvDate;
    }

    public void setTvDate(String tvDate) {
        this.tvDate = tvDate;
    }

    public Integer getTvCommentCount() {
        return tvCommentCount;
    }

    public void setTvCommentCount(Integer tvCommentCount) {
        this.tvCommentCount = tvCommentCount;
    }

    public Integer getTvLikeCount() {
        return tvLikeCount;
    }

    public void setTvLikeCount(Integer tvLikeCount) {
        this.tvLikeCount = tvLikeCount;
    }


    public boolean isBtnLike() {
        return btnLike;
    }

    public void setBtnLike(boolean btnLike) {
        this.btnLike = btnLike;
    }

    public Drawable getAuthor_head() {
        return author_head;
    }

    public void setAuthor_head(Drawable author_head) {this.author_head = author_head;}

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    /*public Drawable getIvImageView() {return ivImageView;}

    public void setIvImageView(Drawable ivImageView) {this.ivImageView = ivImageView;}*/

    public String getTvLoaction() {
        return tvLoaction;
    }

    public void setTvLoaction(String tvLoaction) {
        this.tvLoaction = tvLoaction;
    }

    public String getrouteId() {
        return routeId;
    }

    public void setrouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getrouteUsername() {
        return routeUsername;
    }

    public void setrouteUsername(String routeUsername) {
        this.routeUsername = routeUsername;
    }

    public String getAuthor_head_url() {
        return author_head_url;
    }

    public void setAuthor_head_url(String author_head_url) {this.author_head_url = author_head_url;}
}
