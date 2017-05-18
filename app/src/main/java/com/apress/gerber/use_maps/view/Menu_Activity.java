package com.apress.gerber.use_maps.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.apress.gerber.use_maps.Public_User;
import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.view.fragment.hot_fragment;
import com.apress.gerber.use_maps.view.fragment.login_fragment;
import com.apress.gerber.use_maps.view.fragment.logined_fragment;
import com.apress.gerber.use_maps.view.fragment.mine_fragment;
import com.apress.gerber.use_maps.view.fragment.search_fragment;

import cn.bmob.v3.Bmob;


public class Menu_Activity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rpTab;
    private RadioButton rbHot,rbSearch,rbAdd,rbLogin;
    private login_fragment flogin;
    private hot_fragment fhot;
    private mine_fragment fmine;
    private search_fragment fsearch;
    private logined_fragment flogined;
    private Public_User myuser;
    private static Menu_Activity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
       //改过！！！！！！！！！！！！
        setContentView(R.layout.new_activity_menu);
        Bmob.initialize(this, "c737f20a1f08276e5290e989a12e4481");
        //新的menu.xml！！！！！！！！！！！
        rpTab=(RadioGroup)findViewById(R.id.rd_group);
        rpTab.setOnCheckedChangeListener(this);

        rbHot=(RadioButton)findViewById(R.id.rd_menu_deal);
        rbSearch=(RadioButton)findViewById(R.id.rd_menu_poi);
        rbAdd=(RadioButton)findViewById(R.id.rd_menu_more);
        rbLogin=(RadioButton)findViewById(R.id.rd_menu_user);
        myuser=new Public_User();
        rbHot.setChecked(true);
        //怡-设置默认的第一个fragment
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fhot = new hot_fragment();
        fTransaction.add(R.id.fragment_container, fhot);
        fTransaction.commit();

      /*原menu.xml的id
      rpTab=(RadioGroup)findViewById(R.id.main_tab_bar);
        rpTab.setOnCheckedChangeListener(this);

        rbHot=(RadioButton)findViewById(R.id.main_tab_item_travel);
        rbSearch=(RadioButton)findViewById(R.id.main_tab_item_city);
        rbAdd=(RadioButton)findViewById(R.id.main_tab_item_add);
        rbLogin=(RadioButton)findViewById(R.id.main_tab_item_mine);

        rbHot.setChecked(true);*/
    }

    public void hideAllFragment(FragmentTransaction transaction){
        if(fsearch!=null){
            transaction.hide(fsearch);
        }
        if(fhot!=null){
            transaction.hide(fhot);
        }
        if(fmine!=null){
            transaction.hide(fmine);
        }
        if(flogin!=null){
            transaction.hide(flogin);
        }
        /**
         * 新添加登录后的界面
         */
        if(flogined!=null){
            transaction.hide(flogined);
        }
    }
    public static Menu_Activity getInstance(){
        // 因为我们程序运行后，Application是首先初始化的，如果在这里不用判断instance是否为空
        return instance;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (checkedId){
            case R.id.rd_menu_deal:
                if(fhot==null){
                    fhot = new hot_fragment();
                    transaction.add(R.id.fragment_container,fhot);
                }else{
                    transaction.show(fhot);
                }
                transaction.commit();
                break;
            case R.id.rd_menu_poi:
                if(fsearch==null){
                    fsearch = new search_fragment();
                    transaction.add(R.id.fragment_container,fsearch);
                }else{
                    transaction.show(fsearch);
                }
                transaction.commit();
                break;
            case R.id.rd_menu_more:
            //    myuser.logOut(Menu_Activity.this);   //清除缓存用户对象
                /**
                 * 判断用户是否登陆，以确定制作地图路线可否使用
                 */

              if( myuser.getCurrentUser(Public_User.class)==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            Menu_Activity.this);
                    builder.setMessage("请先登录/注册账号后再使用");
                    builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(flogin==null){
                                flogin = new login_fragment();
                                transaction.add(R.id.fragment_container,flogin);
                            }else{
                                transaction.show(flogin);
                            }
                            transaction.commit();
                        }

                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
                    else {
                    if (fmine == null) {
                        fmine = new mine_fragment();
                        transaction.add(R.id.fragment_container, fmine);
                    } else {
                        transaction.show(fmine);
                    }
                  transaction.commit();
             }
                break;
            case R.id.rd_menu_user:
                /**
                 * 判断其是否登录，切换对应的fragment
                 */
                if(myuser.getCurrentUser(Public_User.class)==null) {
                    if (flogin == null) {
                        flogin = new login_fragment();
                        transaction.add(R.id.fragment_container, flogin);
                    } else {
                        transaction.show(flogin);
                    }
                    transaction.commit();
                }else//这里是登录后的用户界面
                {
                    if (flogined == null) {
                        flogined = new logined_fragment();
                        //  flogined.getuser(myuser.getCurrentUser(Public_User.class));//获取当前的user的对象
                        transaction.add(R.id.fragment_container, flogined);
                    } else {
                        transaction.show(flogined);
                    }
                    transaction.commit();
                }
                break;
        }
        /**
         * 将此语句放在每个跳转fragment的后面
         */
    //    transaction.commit();
    }

}
