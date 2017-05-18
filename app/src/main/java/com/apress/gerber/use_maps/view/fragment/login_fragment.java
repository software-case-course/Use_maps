package com.apress.gerber.use_maps.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apress.gerber.use_maps.Public_User;
import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.view.Find_key_Activity;
import com.apress.gerber.use_maps.view.Menu_Activity;
import com.apress.gerber.use_maps.view.Register_Activity;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by Administrator on 2017/1/7.
 */

public class login_fragment extends Fragment {

    private Button login_in_button;
    private Button turnback;
    private EditText userID;
    private EditText userpassword;
    private TextView register;
    private TextView forgetPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_in_,container,false);

        login_in_button=(Button) view.findViewById(R.id.login_in_button);
        turnback=(Button)view.findViewById(R.id.turnback);
        userID=(EditText) view.findViewById(R.id.login_in_emai_edittext);
        userpassword=(EditText)view. findViewById(R.id.editText2);
        register=(TextView)view.findViewById(R.id.textView) ;
        forgetPassword=(TextView)view.findViewById(R.id.textView3);

        //返回
        turnback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0)
            {
                
            }
        });

        login_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(userID.getText()!=null&&userpassword.getText()!=null)
                {
                    final Public_User user = new Public_User();
                    user.setUsername(userID.getText().toString());
                    user.setPassword(userpassword.getText().toString());

                    user.login(new SaveListener<Public_User>() {
                        @Override
                        public void done(Public_User pubuser, BmobException e) {
                            if(e == null){
                                if (user.getEmailVerified()) {
                                    Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(getActivity(),Menu_Activity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(getActivity(), "请前往邮箱验证", Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(getActivity(), "登录失败" +e, Toast.LENGTH_SHORT).show();
                                userpassword.setText(null);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(), "邮箱或密码为空", Toast.LENGTH_LONG).show();
                    userpassword.setText(null);
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(userID.getText()!=null&&userpassword.getText()!=null)
                {
                    Intent intent=new Intent(getActivity(),Register_Activity.class);
                    startActivity(intent);
                }

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(userID.getText()!=null&&userpassword.getText()!=null)
                {
                    Intent intent=new Intent(getActivity(),Find_key_Activity.class);
                    startActivity(intent);
                }

            }
        });

        return view;
    }


}
