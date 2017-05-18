package com.apress.gerber.use_maps;

import cn.bmob.v3.BmobObject;

/**
 * Created by Wx on 2017/1/22.
 */

public class Comments extends BmobObject {
    private Public_User Observer;
    private Routes CRoute;
    private String Comment;

    public Public_User getObserver() {
        return Observer;
    }
    public void setObserver(Public_User Observer) {
        this.Observer = Observer;
    }


    public Routes getCRoute() {
        return CRoute;
    }
    public void setCRoute(Routes CRoute) {
        this.CRoute = CRoute;
    }

    public String getComment() {
        return Comment;
    }
    public void setComment(String Comment) {
        this.Comment = Comment;
    }

}

