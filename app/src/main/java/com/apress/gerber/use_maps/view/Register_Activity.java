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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Register_Activity extends AppCompatActivity {

    private EditText email;
    private EditText name;
    private EditText password;
    private EditText surepassword;
    private Button regsiter;

    Pattern p = null;
    Matcher m = null;
    boolean flg = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        Bmob.initialize(this, "c737f20a1f08276e5290e989a12e4481");
    }
    public void register_click(View view) {
        email = (EditText) findViewById(R.id.editText1);
        name = (EditText) findViewById(R.id.editText2);
        password = (EditText) findViewById(R.id.editText3);
        surepassword = (EditText) findViewById(R.id.editText4);
        regsiter = (Button) findViewById(R.id.register_register_button);
        p = Pattern.compile("[a-zA-Z0-9_]{8,16}");
        m = p.matcher(password.getText().toString());
        String conpassword, inpassword;
        conpassword = surepassword.getText().toString();
        inpassword = password.getText().toString();
        flg = m.matches();
        if (!flg) {
            Toast.makeText(Register_Activity.this, "请输入正确格式的密码！8~16位", Toast.LENGTH_SHORT).show();
        }
        else if (!conpassword.equals(inpassword)) {
            Toast.makeText(Register_Activity.this, "密码确认错误！", Toast.LENGTH_SHORT).show();
        }
        else {
            final Public_User user = new Public_User();
            user.setEmail(email.getText().toString().trim());
            user.setUsername(name.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());
            user.signUp(new SaveListener<Public_User>() {
                @Override
                public void done(Public_User pubuser, BmobException e) {
                    if(e == null){
                        Toast.makeText(Register_Activity.this, "注册成功,请前往邮箱验证" , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register_Activity.this, Menu_Activity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Register_Activity.this, "注册失败：" + e , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

