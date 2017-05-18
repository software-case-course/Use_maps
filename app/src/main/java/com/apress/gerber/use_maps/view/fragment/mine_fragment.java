package com.apress.gerber.use_maps.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.apress.gerber.use_maps.MyActivity;
import com.apress.gerber.use_maps.Public_User;
import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.Routes;
import com.apress.gerber.use_maps.view.Menu_Activity;
import com.apress.gerber.use_maps.view.recyclerview.MyAdapter;
import com.apress.gerber.use_maps.view.recyclerview.MySet;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by Administrator on 2017/1/7.
 */

public class mine_fragment extends Fragment {
    //添加我的RecyclerView
    private SwipeRefreshLayout myswipeLayout;
    private int lastVisibleItem;
    private Handler handler = new Handler();
    private boolean isLoading;
    private Drawable drawable;
    List<MySet> mySets = new ArrayList<>();
    MySet mysets=new MySet();
    LinearLayoutManager myLayoutManager;

    RecyclerView myRecyclerView;
    MyAdapter myAdapter;

    public mine_fragment() {
        super();
    }

    private ImageView addimage;
    public String Routenumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.myRecyclerView);
        myLayoutManager = new LinearLayoutManager(getActivity());
        //2 为RecyclerView创建布局管理器，这里使用的是LinearLayoutManager，表示里面的Item排列是线性排列
        myRecyclerView.setLayoutManager(myLayoutManager);
        //进度条
        myswipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.myswipeLayout);
        myswipeLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        myswipeLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        //  initRecyclerView(view);
        myswipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 1000);
            }
        });

        myRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = myLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem + 1 == myAdapter.getItemCount()) {
                    Log.d("test", "loading executed");

                    boolean isRefreshing = myswipeLayout.isRefreshing();
                    if (isRefreshing) {
                        myAdapter.notifyItemRemoved(myAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData();

                                Log.d("test", "load more completed");
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }

        });
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        getData();
        myAdapter = new MyAdapter(getActivity(),mySets);
        myRecyclerView.setAdapter(myAdapter);
        handler.sendEmptyMessageDelayed(0, 3000);


        addimage = (ImageView) view.findViewById(R.id.add_own_story);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //跳到肖丹的界面！！
                ////////////////////////////////////////////////////////
                final Routes rt = new Routes();
                rt.setRUsername(BmobUser.getCurrentUser(Public_User.class));
                rt.setLikenumber(0);
                rt.setCommentsnumber(0);
                rt.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(getActivity(), "路线创建成功", Toast.LENGTH_SHORT).show();
                            Routenumber = rt.getObjectId();
                            /////////////////////////////////////////////////////////////
                            Intent intent = new Intent(getActivity(), MyActivity.class);
                            intent.putExtra("RouteID", Routenumber);
                            startActivity(intent);
                            /////////////////////////////////////////////////
                        } else {
                            Toast.makeText(getActivity(), "路线创建失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return view;
    }
    public void getData()
    {
        findmyroute();
    }

    public void findmyroute() {
        BmobQuery<Routes> query1 = new BmobQuery<Routes>();
        query1.addWhereEqualTo("RUsername",BmobUser.getCurrentUser(Public_User.class));
        BmobQuery<Routes> query2 = new BmobQuery<Routes>();
        query2.addWhereExists("Routename");
        List<BmobQuery<Routes>> andQuerys = new ArrayList<BmobQuery<Routes>>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        BmobQuery<Routes> query = new BmobQuery<Routes>();
        query.and(andQuerys);
        query.setLimit(100);
        query.order("-createdAt");
        query.findObjects(new FindListener<Routes>() {
            @Override
            public void done(List<Routes> object, BmobException e) {
                if (e == null) {
                    for (Routes routes : object) {
                        MySet mysets=new MySet();
                        mysets.setMyImage(getResources().getDrawable(R.drawable.scene));
                        mysets.setMytitle(routes.getRoutename());
                        mySets.add(mysets);
                    }
                } else {
                }
            }
        });
    }
}
