package com.apress.gerber.use_maps.editpic;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.editpic.chosepiture.util.chosepiture;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by asus on 2016/7/20.
 */
public class ImageAdapter extends BaseAdapter
{
    private static Set<String> mSelectedImg=new HashSet<String>();//图片路径的选择集合
    //private  static ArrayList<String>mSelectedImg=new ArrayList<String >();
    private String mDirPath;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;
    private int mScreenwidth;
    public ImageAdapter(Context context, List<String> mDatas, String dirPath)
    {

        this.mDirPath=dirPath;
        this.mImgPaths=mDatas;
        mInflater= LayoutInflater.from(context);
       WindowManager wm=   (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenwidth=outMetrics.widthPixels;
    }
    public Set<String> getmSelectedImg()
    {
        return mSelectedImg;
    }
    @Override
    public  int getCount()
    {
        return  mImgPaths.size();
    }
    @Override
    public Object getItem(int postion)
    {
        return mImgPaths.get(postion);
    }
    @Override
    public long getItemId(int postion)
    {
        return 0;
    }

    @Override
    public View getView(final int postion, View convertView, ViewGroup parent)
    {
       final ViewHolder viewHolder;
        if(convertView==null)
        {
            convertView=mInflater.inflate(R.layout.item_gridview,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.mImg=(ImageView)convertView.findViewById(R.id.id_item_image);
            viewHolder.mSelect=(ImageButton)convertView.findViewById(R.id.id_item_select);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        //重置状态
        viewHolder.mImg.setImageResource(R.drawable.p1);//no
        viewHolder.mSelect.setImageResource(R.drawable.ic_done_black_24dp);//没有被选上
        viewHolder.mImg.setColorFilter(null);
        viewHolder.mImg.setMaxWidth(mScreenwidth/3);
        chosepiture.getInstance(3, chosepiture.Type.LIFO).loadImage(mDirPath + "/" + mImgPaths.get(postion), viewHolder.mImg);//chosepicture=imageloader
       final String filePath=mDirPath+"/"+mImgPaths.get(postion);
        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             if(mSelectedImg.contains(filePath))//已被选择
                {
                    mSelectedImg.remove(filePath);
                    viewHolder.mImg.setColorFilter(null);
                    viewHolder.mSelect.setImageResource(R.drawable.p1);
                }
                else//未被选择
             {
                 mSelectedImg.add(filePath);
                 viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
                 viewHolder.mSelect.setImageResource(R.drawable.ic_done_black_24dp1);
             }
                notifyDataSetChanged();
            }
        });
        if(mSelectedImg.contains(filePath))
        {
            viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
            viewHolder.mSelect.setImageResource(R.drawable.ic_done_black_24dp1);//图片路径存储
        }
        return convertView;
    }
    private class ViewHolder
    {
        ImageView mImg;
        ImageButton mSelect;
    }

};