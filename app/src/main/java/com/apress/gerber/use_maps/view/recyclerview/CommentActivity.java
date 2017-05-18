package com.apress.gerber.use_maps.view.recyclerview;

import java.util.ArrayList;
import java.util.List;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apress.gerber.use_maps.Comments;
import com.apress.gerber.use_maps.Public_User;
import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.Routes;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ShineLu on 2017/1/21.
 */

public class CommentActivity extends AppCompatActivity{
    List<CommentSet>commentsets=new ArrayList<>();
    CommentSet commentSet=new CommentSet();
    InformationSet information=new InformationSet();
    LinearLayoutManager cLayoutManager;

    RecyclerView cRecyclerView;
    CommentAdapter cAdapter;

    private Button SendComment;
    private EditText MyComment;

    String rid , run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        final Bundle bundle = this.getIntent().getExtras();
        rid = bundle.getString("thisroutid");
        run = bundle.getString("thisrouteUsername");

        Toolbar toolbar=(Toolbar)findViewById(R.id.comment_toolbar);
        setSupportActionBar(toolbar);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_player);
      //  setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        initRecyclerView();
        SendComment=(Button)findViewById(R.id.send);
        MyComment = (EditText)findViewById(R.id.etComment);

        SendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routes rt = new Routes();
                rt.setObjectId(rid);
                Comments comm = new Comments();
                comm.setCRoute(rt);
                comm.setComment(MyComment.getText().toString());
                comm.setObserver(BmobUser.getCurrentUser(Public_User.class));
                comm.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            Toast.makeText(CommentActivity.this, "发送评论成功",Toast.LENGTH_LONG ).show();
                            MyComment.setText(null);
                            Routes route = new Routes();
                            route.increment("Commentsnumber");
                            route.update(rid, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                }
                            });
                        }else{
                            Toast.makeText(CommentActivity.this, "发送评论失败" + e.getMessage().toString(),Toast.LENGTH_LONG ).show();
                        }
                    }
                });
            }
        });
    }


    private void initRecyclerView() {
        //1 实例化RecyclerView
        cRecyclerView = (RecyclerView) findViewById(R.id.cRecyclerView);
        cLayoutManager = new LinearLayoutManager(this);
        //2 为RecyclerView创建布局管理器，这里使用的是LinearLayoutManager，表示里面的Item排列是线性排列
        cRecyclerView.setLayoutManager(cLayoutManager);

        //3 设置数据适配器*/

        findComments(rid);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                        CommentActivity.this.finish();

        }
        return true;
    }

    public void findComments(String rid){
        BmobQuery<Comments> query = new BmobQuery<Comments>();
        Routes Rt00 = new Routes();
        Rt00.setObjectId(rid);
        query.addWhereEqualTo("CRoute",new BmobPointer(Rt00));
        query.setLimit(100);
        query.order("-createdAt");
        query.include("Observer");
        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> object, BmobException e) {
                if (e == null) {
                    for (Comments comm : object) {
                        ////////////////////////////////////
                        CommentSet commentSet=new CommentSet();
                        commentSet.setIvUserImage(getResources().getDrawable(R.drawable.avater_default));
                        commentSet.setTvcomment(comm.getComment());
                        //String a = comm.getObserver().getUsername();
                        commentSet.setUsername(comm.getObserver().getUsername());
                        Log.i("haipa", comm.getObserver().getUsername());
                        commentsets.add(commentSet);
                        cAdapter=new CommentAdapter(CommentActivity.this,commentsets);
                        cRecyclerView.setAdapter(cAdapter);
                        /////////////////////////////////////
                    }
                }
                else {
                    Toast.makeText(CommentActivity.this,"查询失败"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
