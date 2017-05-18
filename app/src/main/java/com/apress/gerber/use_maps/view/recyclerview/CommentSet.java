package com.apress.gerber.use_maps.view.recyclerview;

import android.graphics.drawable.Drawable;

/**
 * Created by ShineLu on 2017/1/21.
 */

public class CommentSet {
    Drawable ivUserImage;
    String tvcomment;
    String username;

    public Drawable getIvUserImage(){return ivUserImage;}
    public void setIvUserImage(Drawable ivUserImage){this.ivUserImage=ivUserImage;}

    public String getTvcomment(){return tvcomment;}
    public void setTvcomment(String tvcomment){this.tvcomment=tvcomment;}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username=username;}
}
