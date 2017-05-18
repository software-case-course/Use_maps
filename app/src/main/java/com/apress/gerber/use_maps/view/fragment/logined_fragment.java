package com.apress.gerber.use_maps.view.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.use_maps.Public_User;
import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.view.GetUrl;
import com.apress.gerber.use_maps.view.Menu_Activity;
import com.apress.gerber.use_maps.view.Name_Acivity;
import com.apress.gerber.use_maps.view.Sign_Activity;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Administrator on 2017/1/7.
 */

public class logined_fragment extends Fragment {

    private TextView settingtextview;
    private TextView loginouttextview;
    private TextView signtextview;
    private TextView nametextview;
    private Public_User myuser;

    private  Bitmap bitmap;
    private  String headpicurl;
    private Context context;
    private ImageView headimage;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESIZE_REQUEST_CODE = 2;
    private  static final  int SIGN=3;
    private  static final  int NAME=4;
    private static final String IMAGE_FILE_NAME = "header.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mine,container,false);
        myuser=new Public_User();
        settingtextview=(TextView)view.findViewById(R.id.setting_textview);
        loginouttextview=(TextView)view.findViewById(R.id.login_out_textview);
        signtextview=(TextView)view.findViewById(R.id.sign_textview);
        nametextview=(TextView)view.findViewById(R.id.name_textview);
        headimage=(ImageView)view.findViewById(R.id.Userhead);
        context= Menu_Activity.getInstance();
        GetUrl getUrl=new GetUrl(new GetUrl.CallBack(){
            @Override
            public void geturl(final String url) {
                Log.d("url",url);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        headpicurl=url;
                        new AnotherTask().execute("JSON");
                    }
                }).start();

            }
        });
        getUrl.get_image();
        getname_sign_image();

        loginouttextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//退出登录
                myuser.logOut();   //清除缓存用户对象
                FragmentManager fm =getActivity().getFragmentManager();
                fm.beginTransaction()
                        //替换Fragment
                        .replace(R.id.fragment_container,new login_fragment())
                        .commit();
            }
        });
        settingtextview.setOnClickListener(new View.OnClickListener() {//设置跳转
            @Override
            public void onClick(View view) {

            }
        });
        signtextview.setOnClickListener(new View.OnClickListener() {//标签
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Sign_Activity.class);
                startActivityForResult(intent,SIGN);
            }
        });
        nametextview.setOnClickListener(new View.OnClickListener() {//昵称
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Name_Acivity.class);
                startActivityForResult(intent,NAME);
            }
        });
        headimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
            }
        });



        return view;
    }
    /*异步加载图片*/
    private  class AnotherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            //对UI组件的更新操作
            headimage.setImageBitmap(bitmap);
            //显示

        }
        @Override
        protected String doInBackground(String... params) {
            //耗时的操作
            bitmap = getheadpic(headpicurl);
            return params[0];
        }

    }
    private void getname_sign_image() {
        ////////////////////////////////////////////////////通过myuser在用户界面写下数据当前数据sigmtextview.nametextview,headimage
        ///////0213
        Public_User user00= BmobUser.getCurrentUser(Public_User.class);
        BmobQuery<Public_User> query = new BmobQuery<Public_User>();
        query.getObject(user00.getObjectId(), new QueryListener<Public_User>() {
            @Override
            public void done( Public_User pubuser, BmobException e) {
                if(e == null){
                    signtextview.setText(pubuser.getIntroduction());
                    nametextview.setText(pubuser.getUsername());
                }
                else{
                }
            }
        });
    }

    /**
     * 根据URL获取Bitmap
     * */
    public Bitmap getheadpic(final String url){
        Bitmap bitmap = null;

        URL myFileURL;
        try {
            myFileURL = new URL(url);

            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            bitmap=toRoundBitmap(bitmap);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



        return bitmap;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 底下是调用图库做头像
     *
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData());
                    break;
                case RESIZE_REQUEST_CODE:
                    if (data != null) {
                        showResizeImage(data);
                    }
                    break;
                case SIGN:
                    showSign(data);
                    break;
                case NAME:
                    showName(data);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private  void showName(Intent data)
    {
        Bundle extras = data.getExtras();
        if (extras != null) {
            String text=extras.getString("name");
            nametextview.setText(text);
        }
    }
    private  void showSign(Intent data)
    {
        Bundle extras = data.getExtras();
        if (extras != null) {
            String text=extras.getString("sign");
            signtextview.setText(text);
        }
    }
    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Uri pl=data.getData();
            photo=toRoundBitmap(photo);
            ///////////////////////////////////////////////////////////////////上传头像
            //wx0215
            //Uri picUri=data.getData();//获取图片uri
            //resizeImage(picUri);
            Uri picUri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), photo, null,null));
            //下面方法将获取的uri转为String类型哦！
            String []imgs1={MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
            Cursor cursor=getActivity().managedQuery(picUri, imgs1, null, null, null);
            int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String img_url=cursor.getString(index);
            upload(img_url);
            ///////////////////////////////0215
            Drawable drawable = new BitmapDrawable(photo);
            headimage.setImageDrawable(drawable);
        }
    }

    /**
     * 将图片上传
     * @param imgpath
     */

    private void upload(String imgpath){
        final BmobFile icon = new BmobFile(new File(imgpath));
        icon.upload( new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Public_User user01= BmobUser.getCurrentUser(Public_User.class);
                    String headuri = icon.getUrl();
                    Public_User person = new Public_User();
                    person.setHeadPic(headuri);
                    person.update(user01.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                Toast.makeText(getActivity(),"设置头像成功",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(),"设置头像失败"+e,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(),"图片上传失败"+e,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 把bitmap转成圆形
     * */
    public Bitmap toRoundBitmap(Bitmap bitmap){
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        int r=0;
        //取最短边做边长
        if(width<height){
            r=width;
        }else{
            r=height;
        }
        //构建一个bitmap
        Bitmap backgroundBm=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas=new Canvas(backgroundBm);
        Paint p=new Paint();
        //设置边缘光滑，去掉锯齿
        p.setAntiAlias(true);
        RectF rect=new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r/2, r/2, p);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, p);
        return backgroundBm;
    }

}
