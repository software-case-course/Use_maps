package com.apress.gerber.use_maps.editpic.chosepiture.bean;

/**
 * Created by asus on 2016/5/15.
 */
public class FolderBean {
    /**
     * 当前文件夹的路径
     */
    private String dir;
    private String firstImgPath;
    private  String name;
    private int count;
    public void setDir(String dir)
    {
        this.dir=dir;
        int lastIndexOf=this.dir.lastIndexOf("/");
        this.name=this.dir.substring(lastIndexOf);
    }
    public String getDir()
    {
        return dir;
    }
    public void setFirstImgPath(String firstImgPath)
    {
        this.firstImgPath=firstImgPath;
    }
    public  String getFirstImgPath()
    {
        return firstImgPath;
    }
    public String getName()
    {
        return name;
    }
    public void setCount(int count)
    {
        this.count=count;
    }
    public int getCout()
    {
        return count;
    }
}
