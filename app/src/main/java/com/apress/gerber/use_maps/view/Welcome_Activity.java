package com.apress.gerber.use_maps.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.apress.gerber.use_maps.R;

import cn.bmob.v3.Bmob;


public class Welcome_Activity extends Welcome_Base_Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

       // AseoZdpAseo.initType(this, AseoZdpAseo.SCREEN_TYPE);
        LinearLayout linearLayout =(LinearLayout) findViewById(R.id.welcome_image);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.welcome);
        linearLayout.startAnimation(animation);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(Welcome_Activity.this, Menu_Activity.class);
                startActivity(intent);
                Welcome_Activity.this.finish();
            }
        }.start();
    }

}
