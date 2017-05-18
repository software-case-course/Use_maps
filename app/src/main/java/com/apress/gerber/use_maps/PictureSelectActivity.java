package com.apress.gerber.use_maps;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PictureSelectActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout layout = new RelativeLayout(this);
        Button button = new Button(this);
        button.setText("返回");
        layout.addView(button);
        setContentView(layout);

        int[] resId = new int[]{R.drawable.bowrain, R.drawable.cardbag, R.drawable.mi, R.drawable.welcome_welcome};

        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(resId[i]);
            if (!(TripActivity.imageViews.size() >= 7)) {
                TripActivity.imageViews.add(imageView);
            }else{
                //提示用户照片数量已上限
            }
        }
        Toast.makeText(PictureSelectActivity.this,TripActivity.imageViews.size()+"张图片",Toast.LENGTH_SHORT).show();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                intent.putExtra("PictureSelect", true);
                PictureSelectActivity.this.setResult(Activity.RESULT_OK, intent);
                PictureSelectActivity.this.finish();
            }
        });


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //按下返回键

                Intent intent = getIntent();
                intent.putExtra("PictureSelect", false);
                PictureSelectActivity.this.setResult(Activity.RESULT_OK, intent);
                PictureSelectActivity.this.finish();
                //setResult(Activity.RESULT_OK, intent);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
