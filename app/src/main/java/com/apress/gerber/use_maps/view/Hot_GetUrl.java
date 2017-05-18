package com.apress.gerber.use_maps.view;

import android.util.Log;
import android.widget.Toast;

import com.apress.gerber.use_maps.Public_User;
import com.apress.gerber.use_maps.Routes;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by asus on 2017/3/10.
 */

public class Hot_GetUrl {
    private CallBack callBack;
    private Routes route;

    public Hot_GetUrl(CallBack callBack, Routes route) {
        this.callBack = callBack;
        this.route = route;
    }

    public interface CallBack {
        public void geturl(String url);
    }

    public void findroutepic() {
        String a = route.getRUsername().getHeadPic();//头像url
        Log.i("url", a);
        callBack.geturl(a);
    }
}

