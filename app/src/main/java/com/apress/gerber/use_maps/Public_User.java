package com.apress.gerber.use_maps;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/1/8 0008.
 */

public class Public_User extends BmobUser {
    private String HeadPic;
    private String Introduction;

    public String getHeadPic() {
        return HeadPic;
    }
    public void setHeadPic(String HeadPic) {
        this.HeadPic = HeadPic;
    }

    public String getIntroduction() {
        return Introduction;
    }
    public void setIntroduction(String Introduction) {
        this.Introduction = Introduction;
    }
}
