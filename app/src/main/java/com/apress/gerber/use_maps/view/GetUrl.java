package com.apress.gerber.use_maps.view;

import com.apress.gerber.use_maps.Public_User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by asus on 2017/3/7.
 */

public class GetUrl {
    private CallBack callBack;
    public  GetUrl(CallBack callBack)
    {
        this.callBack=callBack;
    }
    public interface CallBack
    {
        public void geturl(String url);
    }
    public void get_image() {
        Public_User user00 = BmobUser.getCurrentUser(Public_User.class);
        BmobQuery<Public_User> query = new BmobQuery<Public_User>();
        query.getObject(user00.getObjectId(), new QueryListener<Public_User>() {
            @Override
            public void done(Public_User pubuser, BmobException e) {
                if (e == null) {
                    String headpicurl = pubuser.getHeadPic();
                    callBack.geturl(headpicurl);
                } else {
                }
            }
        });
        //////////////////wx0213
    }
}

