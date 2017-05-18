package com.apress.gerber.use_maps.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apress.gerber.use_maps.Public_User;
import com.apress.gerber.use_maps.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class Sign_Activity extends AppCompatActivity {
private EditText sign_edittext;
    private Button  sign_finish_button;
    private Button sign_back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_);
        sign_edittext=(EditText)findViewById(R.id.sign_edittext);
        sign_finish_button=(Button)findViewById(R.id.sign_finishbutton);
        sign_back_button=(Button)findViewById(R.id.sign_back_button);
        sign_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////////////////////////////上传Introduction
                /////////////wx0213
                Public_User user01= BmobUser.getCurrentUser(Public_User.class);
                Public_User user02= new Public_User();
                user02.setIntroduction(sign_edittext.getText().toString().trim());
                user02.update(user01.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            Toast.makeText(Sign_Activity.this,"设置签名成功",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Sign_Activity.this,"设置签名失败"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ////////wx0213
                Intent intent=new Intent();
                intent.putExtra("sign",sign_edittext.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        sign_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
