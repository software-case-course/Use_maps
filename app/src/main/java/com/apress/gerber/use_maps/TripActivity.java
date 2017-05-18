package com.apress.gerber.use_maps;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.use_maps.editpic.LoadActivity;
import com.apress.gerber.use_maps.editpic.PicMainActivity;
import com.apress.gerber.use_maps.editpic.PicShow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class TripActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, NumberPicker.OnValueChangeListener {
    TextView tripLoaction_textView;
    NumberPicker numPincker_year, numPincker_month, numPincker_day;
    TextInputEditText textInput_mood;
    Calendar calendar;
    int currentYear, currentMonth, currentDay;
    GridView tripPicture_gridView;

    Intent intent;
 private  String[] pics;
   private String city,address,rid;
    private double lat,lng;
    public static ArrayList<View> imageViews = new ArrayList<>();

    TripPictureAdapter tripPictureAdapter;

    private static final int REQUEST_CODE = 100;

    boolean isSaved;//用来判断是否有保存成功
    List<String> nowCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        final Bundle bundle = this.getIntent().getExtras();
        /*获取Bundle中的数据，注意类型和key*/
       ///////////////////////////////////////////////////////////////
        rid=bundle.getString("RouteID");
       address=bundle.getString("location");//只用于显示的详细地址
        city=bundle.getString("city");
        lat=bundle.getDouble("latitude");
        lng=bundle.getDouble("longtitude");
        pics = PicShow.picurl;

        ////////////////////////////////////////////////////////
        tripLoaction_textView = (TextView) findViewById(R.id.tripLoaction_textView);
        textInput_mood = (TextInputEditText) findViewById(R.id.textInput_mood);
        numPincker_year = (NumberPicker) findViewById(R.id.numPincker_year);
        numPincker_month = (NumberPicker) findViewById(R.id.numPincker_month);
        numPincker_day = (NumberPicker) findViewById(R.id.numPincker_day);
        tripPicture_gridView = (GridView) findViewById(R.id.tripPicture_gridView);

        initialize();
    }

    private void initialize() {
        intent = getIntent();

        tripLoaction_textView.setText(address);
        calendar = Calendar.getInstance();

        numPincker_year.setOnValueChangedListener(this);
        numPincker_month.setOnValueChangedListener(this);
        numPincker_day.setOnValueChangedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //年     //必须先设置最大最小值
        currentYear = calendar.get(Calendar.YEAR);
        numPincker_year.setMinValue(currentYear - 10);
        numPincker_year.setMaxValue(currentYear + 10);
        numPincker_year.setValue(currentYear);

        //月
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        numPincker_month.setMinValue(1);
        numPincker_month.setMaxValue(12);
        numPincker_month.setValue(currentMonth);

        //日
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        numPincker_day.setMinValue(1);
        numPincker_day.setMaxValue(getMaxDateOfMonth(currentYear, currentMonth));
        numPincker_day.setValue(currentDay);


        tripPictureAdapter = new TripPictureAdapter(this);
        tripPicture_gridView.setOnItemClickListener(this);
        tripPicture_gridView.setOnItemLongClickListener(this);
        tripPicture_gridView.setAdapter(tripPictureAdapter);
    }


    private int getMaxDateOfMonth(int year, int month) {
        int max = 30;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                max = 31;
                break;
            case 2:
                max = ((year % 4 == 0 && year % 100 != 0)
                        || year % 400 == 0) ? 29 : 28;
                break;
        }
        return max;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                //当界面已编辑内容时弹出对话框提示“退出此次编辑？”
                // 创建退出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        TripActivity.this);
                builder.setMessage("确定要退出编辑吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isSaved = false;
                        // Intent intent=new Intent(TripActivity.this,MyActivity.class);
                        // intent=getIntent();
                        intent.putExtra("Saved", isSaved);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;

            case R.id.actionbar_title:
                //保存并上传

                if(PicShow.picurl!=null) {
                    isSaved = true;
                    if (isSaved) {
                        intent.putExtra("旅游时间", currentYear + "年" + currentMonth + "月" + currentDay + "日");
                        intent.putExtra("旅游心情", textInput_mood.getText().toString());
                    }
                    intent.putExtra("Saved", isSaved);
                    setResult(Activity.RESULT_OK, intent);
                    this.finish();

                    //////////////////////////////////////////////服务器上传数据
                    /////wx0216
                    Bmob.initialize(this, "c737f20a1f08276e5290e989a12e4481");

                     final String[] PhotofilePaths = pics;
                    Public_User user01 = BmobUser.getCurrentUser(Public_User.class);
                    if (null != user01) {
                        final Location_Photo LP = new Location_Photo();
                        LP.setLPUsername(user01);
                        Routes Rt = new Routes();
                        Rt.setObjectId(rid);
                        LP.setLPRouteID(Rt);
                        LP.setLocation_Longitude(lng);//经度
                        LP.setLocation_Latitude(lat);//纬度
                        LP.setCity(city);
                        LP.setEmotion(textInput_mood.getText().toString());
                        LP.setWritedate(BmobDate.createBmobDate("yyyy-mm-dd", currentYear + "-" + currentMonth + "-" + currentDay));
                        LP.setMydate(currentYear + "年" + currentMonth + "月" + currentDay + "日");
                        BmobFile.uploadBatch(PhotofilePaths, new UploadBatchListener() {
                            @Override
                            public void onSuccess(List<BmobFile> file, List<String> urls) {
                                if (urls.size() == PhotofilePaths.length) {
                                    LP.setPictures(urls);
                                    LP.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, final BmobException e) {
                                            if (e == null) {
                                                //Toast.makeText(TripActivity.this, "上传成功：", Toast.LENGTH_SHORT).show();
                                            } else {
                                               // Toast.makeText(TripActivity.this, "上传失败：" + s, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                                //Toast.makeText(TripActivity.this, "进度" + totalPercent, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(int i, String s) {
                                //Toast.makeText(TripActivity.this, "图片上传失败" + s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
                else
                    Toast.makeText(TripActivity.this, "请选择图片!", Toast.LENGTH_SHORT).show();
                //////////////////0216wx
                break;
        }
        return true;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        switch (picker.getId()) {
            case R.id.numPincker_year:
                currentYear = newVal;

                break;
            case R.id.numPincker_month:
                numPincker_day.setMinValue(1);
                currentMonth=newVal;
                numPincker_day.setMaxValue(getMaxDateOfMonth(currentYear, newVal));
                numPincker_day.setValue(Math.min(currentDay, getMaxDateOfMonth(currentYear, newVal)));
                break;
            case R.id.numPincker_day:
                currentDay = newVal;
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       // if (position == 15) {
       //     Toast.makeText(TripActivity.this, "照片数量已上限~", Toast.LENGTH_LONG).show();
       // } else
            if (position == imageViews.size()) {
            // Intent intent = new Intent(TripActivity.this, PictureSelectActivity.class);
            // 在 TripActivity 中的”添加照片“按钮跳转到选择图片界面  LoadActivity.class（这个是有两个按钮：选择相机||选择图库)

            PopupMenu menu = new PopupMenu(this, view);
            getMenuInflater().inflate(R.menu.takephoto_way, menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.choose_camera:
                            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivity(intent);
                           // startActivityForResult(intent, REQUEST_CODE);
                            break;
                        case R.id.choose_album:
                            Intent intent1=new Intent(TripActivity.this,PicMainActivity.class);
                           // startActivityForResult(intent1,REQUEST_CODE);
                            startActivity(intent1);
                            break;
                    }
                    return false;
                }
            });
            menu.show();

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (position != imageViews.size()) {
            PopupMenu menu = new PopupMenu(this, view);
            getMenuInflater().inflate(R.menu.popup_menu_delete, menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                       // case R.id.popup_menu_delete:
                        //    imageViews.remove(position);
                         //   tripPictureAdapter.notifyDataSetChanged();
                         //   break;
                        case R.id.popup_menu_deleteAll:
                            imageViews.removeAll(imageViews);
                            tripPictureAdapter.notifyDataSetChanged();
                            pics=null;
                            break;
                    }
                    return false;
                }
            });
            menu.show();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("PictureSelect", false)) {
                    tripPictureAdapter.notifyDataSetChanged();

            }
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK://按下返回键
                // 创建退出对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        TripActivity.this);
                builder.setMessage("确定要退出编辑吗？");

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       isSaved=false;
                       //Intent intent=new Intent(TripActivity.this,MyActivity.class);
                        //intent=getIntent();
                        intent.putExtra("Saved", isSaved);
                        TripActivity.this.setResult(Activity.RESULT_OK, intent);
                        TripActivity.this.finish();
                      // startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }

}