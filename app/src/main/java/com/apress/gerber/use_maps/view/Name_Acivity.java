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

public class Name_Acivity extends AppCompatActivity {
    private EditText name_edittext;
    private Button  name_finish_button;
    private Button name_back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name__acivity);
        name_edittext=(EditText)findViewById(R.id.name_edittext);
        name_finish_button=(Button)findViewById(R.id.name_finishbutton);
        name_back_button=(Button)findViewById(R.id.name_back_button);
        name_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////////////////////////////上传nickname
                ///////0213
                Public_User user01= BmobUser.getCurrentUser(Public_User.class);
                Public_User user02= new Public_User();
                user02.setUsername(name_edittext.getText().toString().trim());
                user02.update(user01.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            Toast.makeText(Name_Acivity.this,"设置昵称成功",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Name_Acivity.this,"设置昵称失败"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ////////wx0213
                Intent intent=new Intent();
                intent.putExtra("name",name_edittext.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        name_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
