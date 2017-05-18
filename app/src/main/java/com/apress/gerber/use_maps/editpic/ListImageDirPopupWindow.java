package com.apress.gerber.use_maps.editpic;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.editpic.chosepiture.bean.FolderBean;
import com.apress.gerber.use_maps.editpic.chosepiture.util.chosepiture;

import java.util.List;

/**
 * Created by asus on 2016/7/20.
 */
public class ListImageDirPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private List<FolderBean> mDatas;
    public interface OnDirSelectedListener
    {
        void onSelected(FolderBean folderBean);
    }
    public  OnDirSelectedListener mListener;
    public void setOnDirSelectedListener( OnDirSelectedListener mListener)
    {
        this.mListener=mListener;
    }
    public ListImageDirPopupWindow(Context context, List<FolderBean> datas)
    {
        calWidthAndHeight(context);
        mConvertView= LayoutInflater.from(context).inflate(R.layout.popup_main,null);
        mDatas=datas;
        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        initViews(context);
        initEvent();
    }
    private void initViews(Context context) {
        mListView=(ListView)mConvertView.findViewById(R.id.id_list_dir);
        mListView.setAdapter(new ListDirAdapter(context,mDatas));

    }
    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
                if (mListener != null) {
                    mListener.onSelected(mDatas.get(postion));
                }
            }
        });
    }

    /**
     * 计算popupwindow的宽度和高度
     * @param context
     */
    private  void  calWidthAndHeight(Context context)
    {
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mWidth=outMetrics.widthPixels;
        mHeight=(int)(outMetrics.heightPixels*0.7);
    }
  private  class ListDirAdapter extends ArrayAdapter<FolderBean>
  {
      private LayoutInflater mInflater;
      private List<FolderBean> mDatas;

      public  ListDirAdapter(Context context, List<FolderBean> objects)
      {
          super(context,0,objects);
          mInflater= LayoutInflater.from(context);
      }
      public View getView(int position, View convertView, ViewGroup parent)
      {
          ViewHolder holder=null;
          if(convertView==null)
          {
              holder=new ViewHolder();
              convertView=mInflater.inflate(R.layout.item_popup_main,parent,false);
              holder.mImg=(ImageView)convertView.findViewById(R.id.id_id_dir_item_image);
              holder.mDirName=(TextView)convertView.findViewById(R.id.id_dir_item_name);
              holder.mDirCount=(TextView)convertView.findViewById(R.id.id_dir_item_count);
              convertView.setTag(holder);
          }
          else
          {
              holder=(ViewHolder)convertView.getTag();

          }
          FolderBean bean=getItem(position);
          holder.mImg.setImageResource(R.drawable.p1);
          chosepiture.getInstance(3, chosepiture.Type.LIFO).loadImage(bean.getFirstImgPath(),holder.mImg);//getInstance()没有内容的
          holder.mDirCount.setText(bean.getCout()+"");
          holder.mDirName.setText(bean.getName());
          return convertView;
      }
      private class ViewHolder
      {
          ImageView mImg;
          TextView mDirName;
          TextView mDirCount;
      }
  }
}
