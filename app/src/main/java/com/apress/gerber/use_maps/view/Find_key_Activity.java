package com.apress.gerber.use_maps.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apress.gerber.use_maps.Public_User;
import com.apress.gerber.use_maps.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Wx on 2016/12/18.
 */

public class Find_key_Activity extends AppCompatActivity {
    private EditText email_input;
    private Button find_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_key);
        email_input=(EditText) findViewById(R.id.find_key_email_edittext);
        find_key=(Button)findViewById(R.id.find_key_find_key_button);

        find_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Public_User.resetPasswordByEmail(email_input.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(Find_key_Activity.this, "邮件发送成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Find_key_Activity.this, "邮件发送错误：" + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}
