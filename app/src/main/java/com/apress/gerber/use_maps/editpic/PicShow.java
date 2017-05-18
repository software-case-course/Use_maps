package com.apress.gerber.use_maps.editpic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.TripActivity;
import com.apress.gerber.use_maps.editpic.chosepiture.util.chosepiture;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;


import java.io.File;

public class PicShow extends AppCompatActivity {
    private GridView myshowgirdview;
    private Button finishbutton;
    private Button backbutton;
    public  static String[] picurl;
    private int num;
    private int image_count;
    private ShowImageAdapter showImageAdapter;
    private Bitmap mainBitmap;
    public static final int ACTION_REQUEST_EDITIMAGE = 9;
    private int imageWidth, imageHeight;//
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"点击所要的图片进入编辑状态",Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_pic_show);
        myshowgirdview=(GridView)findViewById(R.id.show_gridView);

        final Bundle bundle = this.getIntent().getExtras();
        //获取Bundle中的数据，注意类型和key
       // picurl=null;
        picurl = bundle.getStringArray("pic");
/*
        image_count= picurl.length;
        for(int i=0;i<image_count;i++)
        {
            ImageView imageView = new ImageView(this);
            chosepiture.getInstance(3, chosepiture.Type.LIFO).loadImage(picurl[i], imageView);
           // imageView.setImageURI(Uri.parse(picurl[i]));
            TripActivity.imageViews.add(imageView);
        }
        */
        finishbutton=(Button)findViewById(R.id.show_finishbutton);
        backbutton=(Button)findViewById(R.id.show_backbutton);

        //图片经过美化等处理之后，点击保存按钮，则返回到第二个布局（编辑心情、图片缩略显示的那个页面）
        finishbutton.setOnClickListener(new View.OnClickListener() {//////////////////////////////////////////////////////////////////
            @Override
            public void onClick(View view) {
              //  Intent intent=new Intent(PicShow.this,);
            //    intent.putExtra("pics",picurl);
            //    startActivity(intent);

                image_count= picurl.length;
                for(int i=0;i<image_count;i++)
                {
                    ImageView imageView = new ImageView(PicShow.this);
                    chosepiture.getInstance(3, chosepiture.Type.LIFO).loadImage(picurl[i], imageView);
                    // imageView.setImageURI(Uri.parse(picurl[i]));
                    TripActivity.imageViews.add(imageView);
                }

               Intent intent = getIntent();
                intent.putExtra("PictureSelect", true);
              //  intent.putExtra("pics ",picurl);
                PicShow.this.setResult(Activity.RESULT_OK, intent);
                PicShow.this.finish();

                ////////////////////////////////////同时销毁PicMainActivity，保证返回到TripActivity页面
                PicMainActivity.instance.finish();
                //////////////////////////////////////////

            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PicShow.this);
                builder.setMessage("放弃已选择的这些照片？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        picurl=null;
                        PicShow.this.finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
        showImageAdapter=new ShowImageAdapter(this,picurl);
        myshowgirdview.setAdapter(showImageAdapter);
        myshowgirdview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent it = new Intent(PicShow.this, EditImageActivity.class);

               DisplayMetrics metrics = getResources().getDisplayMetrics();
               imageWidth = metrics.widthPixels;
               imageHeight = metrics.heightPixels;
               num=i;

               it.putExtra(EditImageActivity.FILE_PATH, picurl[i]);
               File outputFile = FileUtils.genEditFile();
               it.putExtra(EditImageActivity.EXTRA_OUTPUT,
                       outputFile.getAbsolutePath());

               PicShow.this.startActivityForResult(it,
                       ACTION_REQUEST_EDITIMAGE);
           }
       });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            handleEditorImage(data);
        }
    }
    private void handleEditorImage(Intent data) {
        String newFilePath = data.getStringExtra(EditImageActivity.SAVE_FILE_PATH);
        boolean isImageEdit = data.getBooleanExtra(EditImageActivity.IMAGE_IS_EDIT, false);

        if (isImageEdit)
            Toast.makeText(this, getString(R.string.save_path, newFilePath), Toast.LENGTH_LONG).show();
        //System.out.println("newFilePath---->" + newFilePath);
        Log.d("image is edit", isImageEdit + "");
        picurl[num]=newFilePath;
        ShowImageAdapter myadapter=new ShowImageAdapter(PicShow.this,picurl);
        showImageAdapter=myadapter;
        myshowgirdview.setAdapter(showImageAdapter);
        //chosepiture.getInstance(3, chosepiture.Type.LIFO).loadImage(picurl[num], imageView);
     //   LoadImageTask loadTask = new LoadImageTask();
      //  loadTask.execute(newFilePath);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //按下返回键
                // 创建退出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PicShow.this);
                builder.setMessage("放弃已选择的这些照片？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent intent=new Intent(PicShow.this,PicMainActivity.class);
                       // startActivity(intent);
                        picurl=null;
                        PicShow.this.finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
