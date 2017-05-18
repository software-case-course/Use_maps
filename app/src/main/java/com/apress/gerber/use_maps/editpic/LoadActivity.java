package com.apress.gerber.use_maps.editpic;

import android.app.Activity;
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


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoadActivity extends AppCompatActivity {

 private Button picbutton;
 private Button camerabutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        picbutton=(Button) findViewById(R.id.picbutton);
        camerabutton=(Button) findViewById(R.id.camerabutton);
        picbutton.setOnClickListener(new View.OnClickListener() {//监听按钮
            @Override
            public void onClick(View view) {
               // LoadActivity中选择图库按钮事件，跳转到类  PicMainActivity.class

                startActivity(new Intent(LoadActivity.this,PicMainActivity.class));
            }
        });
        camerabutton.setOnClickListener(new View.OnClickListener() {//监听按钮
            @Override
            public void onClick(View view) {
               //  LoadActivity中选择相机按钮事件，跳转到相机调用
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
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
                        LoadActivity.this);
                builder.setMessage("确定返回？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //按下返回键
                      //  Intent intent=new Intent(LoadActivity.this,TripActivity.class);
                        Intent intent = getIntent();
                       intent.putExtra("PictureSelect", false);
                        LoadActivity.this.setResult(Activity.RESULT_OK, intent);
                       LoadActivity.this.finish();
                       //startActivity(intent);
                        //setResult(Activity.RESULT_OK, intent)
                       // LoadActivity.this.finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
