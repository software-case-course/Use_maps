package com.apress.gerber.use_maps.editpic;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.TripActivity;
import com.apress.gerber.use_maps.editpic.chosepiture.bean.FolderBean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PicMainActivity extends AppCompatActivity {

    ////////////////////////////////
    public static PicMainActivity instance;
    ///////////////////////////////////////////

    private GridView mGridView;
   private List<String> mImgs;
    private ImageAdapter mImgAdapter;
    private RelativeLayout mBottomly;
  private Button finishbutton;
    private Button backbutton;
    private TextView mDirName;
    private TextView mDirCount;
    private File mCurrentDir;
    private int mMaxCount;
    private List<FolderBean> mFolderBeans=new ArrayList<FolderBean>();
    private ProgressDialog mProgressDialog;
    private static final int DATA_LOADED=0X110;
    private ListImageDirPopupWindow mDirPopupWindow;
    private Handler mHandler=new Handler() {

        public void handleMessage(android.os.Message msg){
         if(msg.what==DATA_LOADED)
         {
             mProgressDialog.dismiss();
             //绑定数据到view中
             data2View();
             initDirPopupWindow();
         }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pic);

        ////////////////////////////////////////
        instance=this;
        //////////////////////////////////////////

        initView();
        initDatas();
        initEvent();
        //把照片选择出来几张后，点击下面这个按钮，进入到对图片进行美化的activity:PicShow.class
        finishbutton=(Button) findViewById(R.id.finishbutton);
        finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arr = new String[mImgAdapter.getmSelectedImg().size()]; //Set-->数组
                mImgAdapter.getmSelectedImg().toArray(arr);
                if(arr.length==0) {
                    Toast.makeText(PicMainActivity.this,"未选择图片",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(PicMainActivity.this, PicShow.class);//A.this, B.class
                /* 通过Bundle对象存储需要传递的数据 */
                    Bundle bundle = new Bundle();
                /*字符、字符串、布尔、字节数组、浮点数等等，都可以传*/
                    bundle.putStringArray("pic", arr);
                /*把bundle对象assign给Intent*/
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            }
        });
        backbutton=(Button)findViewById(R.id.choosebackbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PicMainActivity.this);
                builder.setMessage("确定放弃选择照片？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Intent intent = new Intent(PicMainActivity.this, TripActivity.class);
                        PicMainActivity.this.finish();
                        //startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
    }
  protected  void initDirPopupWindow()
  {
      mDirPopupWindow=new ListImageDirPopupWindow(this,mFolderBeans);
      mDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
          @Override
          public void onDismiss() {
              lightOn();
          }
      });
      mDirPopupWindow.setOnDirSelectedListener(new ListImageDirPopupWindow.OnDirSelectedListener() {
          @Override
          public void onSelected(FolderBean folderBean) {
              mCurrentDir=new File(folderBean.getDir());
             mImgs= Arrays.asList( mCurrentDir.list(new FilenameFilter() {
                  @Override
                  public boolean accept(File dir, String filename) {
                      if(filename.endsWith(".jpg")||filename.endsWith(".jpeg")||filename.endsWith(".png"))
                      {
                          return true;
                      }
                      return false;
                  }
              }));
              mImgAdapter=new ImageAdapter(PicMainActivity.this,mImgs,mCurrentDir.getAbsolutePath());
              mGridView.setAdapter(mImgAdapter);
              mDirCount.setText(mImgs.size()+"");
              mDirName.setText(folderBean.getName());
              mDirPopupWindow.dismiss();
          }
      });
  }/**
     内容区域变亮
     */

    protected void lightOn()
    {
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=1.0f;
        getWindow().setAttributes(lp);
    }
    /**
     内容区域变暗
     */

    protected void lightOff()
    {
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=.3f;
        getWindow().setAttributes(lp);
    }
    protected void data2View()
    {
        if(mCurrentDir==null)
        {
            Toast.makeText(this,"未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;

        }
        mImgs= Arrays.asList(mCurrentDir.list());
        mImgAdapter=new ImageAdapter(this,mImgs,mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImgAdapter);
        mDirCount.setText(mMaxCount + "");
        mDirName.setText(mCurrentDir.getName());
    }
    private void initView() {
        mGridView= (GridView) findViewById(R.id.id_gridView);
        mBottomly=(RelativeLayout)findViewById(R.id.id_bottom_ly);
        mDirName=(TextView)findViewById(R.id.id_dir_name);
        mDirCount=(TextView)findViewById(R.id.id_dir_count);
    }

    /**
     * 利用ContentProvider扫描手机中的所有图片
     */
    private void initDatas() {
    if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    {
        Toast.makeText(this,"当前存储卡不可用！", Toast.LENGTH_SHORT).show();
        return;
    }
        mProgressDialog= ProgressDialog.show(this,null,"正在加载...");
        new Thread()
        {
            public void run()
            {
                Uri mIgUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr=PicMainActivity.this.getContentResolver();
                Cursor cursor=cr.query(mIgUri, null, MediaStore.Images.Media.MIME_TYPE + "=?or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]
                        {"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                Set<String> mDirPaths=new HashSet<String>();
                while (cursor.moveToNext())
                {
                    String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile=new File(path).getParentFile();
                    if(parentFile==null)
                    {
                        continue;
                    }
                    String dirPath=parentFile.getAbsolutePath();
                    FolderBean folderBean=null;
                    if(mDirPaths.contains(dirPath))
                    {
                        continue;
                    }
                    else
                    {
                        mDirPaths.add(dirPath);
                        folderBean=new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);

                    }
                    if(parentFile.list()==null)
                    {
                        continue;
                    }
                    int picSize=parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if(filename.endsWith(".jpg")||filename.endsWith(".jpeg")||filename.endsWith(".png"))
                                return true;
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    mFolderBeans.add(folderBean);
                    if(picSize>mMaxCount)
                    {
                        mMaxCount=picSize;
                        mCurrentDir=parentFile;
                    }
                }
                cursor.close();
                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(DATA_LOADED);
            };
        }.start();
    }
    private void initEvent() {
      mBottomly.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              mDirPopupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
              mDirPopupWindow.showAsDropDown(mBottomly,0,0);
              lightOff();
          }
      });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //按下返回键
                // 创建退出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PicMainActivity.this);
                builder.setMessage("确定放弃选择照片？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent intent = new Intent(PicMainActivity.this, TripActivity.class);
                       // startActivity(intent);
                        PicMainActivity.this.finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
