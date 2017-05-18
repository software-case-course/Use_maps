package com.apress.gerber.use_maps.view.fragment;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apress.gerber.use_maps.L.BaseActivity;
import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.Routes;
import com.apress.gerber.use_maps.view.recyclerview.InformationAdapter;
import com.apress.gerber.use_maps.view.recyclerview.InformationSet;
import com.apress.gerber.use_maps.view.recyclerview.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Administrator on 2017/1/7.
 */

public class hot_fragment extends Fragment {
    /**
     * 展示类型的数据
     */
    List<String> mTypes = new ArrayList<>();
    /**
     * 筛选类型整体
     */
    LinearLayout mTypeAll;
    /**
     * 筛选点赞优先整体
     */
    LinearLayout mLikeAll;
    /**
     * 布局layout整体
     */
    LinearLayout mLayoutAll;
    /**
     * 筛选类型cb
     */
    CheckBox mTypeCb;
    /**
     * 筛选点赞优先cb
     */
    CheckBox mLikeCb;
    /**
     * 布局layout cb
     */
    CheckBox mLayoutCb;
    BaseActivity baseActivity;
    String whatFirst;
    private SwipeRefreshLayout Infor_swipeRefreshLayout;
    private int lastVisibleItem;
    private Handler handler = new Handler();
    private boolean isLoading;
    private Drawable drawable;
    List<InformationSet> informationSets = new ArrayList<>();
    //InformationSet information=new InformationSet();
    LinearLayoutManager mLayoutManager;

    RecyclerView mRecyclerView;
    InformationAdapter mAdapter;

    Integer skipnumber = 20;
    Boolean flag = true;

    public hot_fragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information, container, false);
        initDate();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.Infor_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        //2 为RecyclerView创建布局管理器，这里使用的是LinearLayoutManager，表示里面的Item排列是线性排列
        mRecyclerView.setLayoutManager(mLayoutManager);

        //cardview之间的间隔
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        //进度条
        Infor_swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Infor_swipeLayout);
        Infor_swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        Infor_swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        //  initRecyclerView(view);
        Infor_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData1();
                    }
                }, 1000);
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    Log.d("test", "loading executed");

                    boolean isRefreshing = Infor_swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
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
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setData();
        mAdapter = new InformationAdapter(getActivity(), informationSets);
        mRecyclerView.setAdapter(mAdapter);
        handler.sendEmptyMessageDelayed(0, 3000);
        initView(view);
        return view;
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        // 初始化类型
        mTypes.add("时间优先");
        mTypes.add("点赞数优先");
        mTypes.add("评论数优先");
        whatFirst = "createdAt";
    }

    /**
     * 初始化控件
     */
    private void initView(View view) {
        mTypeAll = (LinearLayout) view.findViewById(R.id.ll_place_tab);
        mTypeCb = (CheckBox) view.findViewById(R.id.cb_place);
        mLikeAll = (LinearLayout) view.findViewById(R.id.ll_type);
        mLikeCb = (CheckBox) view.findViewById(R.id.cb_type);
        mLayoutAll = (LinearLayout) view.findViewById(R.id.ll_time);
        mLayoutCb = (CheckBox) view.findViewById(R.id.cb_time);
        // 点击选择城市整体
        mTypeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTypeCb.isChecked())
                    mTypeCb.setChecked(false);
                else
                    mTypeCb.setChecked(true);
            }
        });
        // 点击选择类型整体
        mLikeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLikeCb.isChecked())
                    mLikeCb.setChecked(false);
                else
                    mLikeCb.setChecked(true);
            }
        });
        // 点击选择时间整体
        mLayoutAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLayoutCb.isChecked())
                    mLayoutCb.setChecked(false);
                else
                    mLayoutCb.setChecked(true);
            }
        });
        // 选择类型cb
        mTypeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                baseActivity=new BaseActivity();
//                baseActivity.filterTabToggle(isChecked, mTypeAll, mTypes, new AdapterView.OnItemClickListener() {
//
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                        baseActivity.hidePopListView();
//                        mTypeCb.setText(mTypes.get(position));
//                    }
//                }, mTypeCb, mLikeCb);
                if (isChecked == true) {
                    whatFirst = "createdAt";
                    mLikeCb.setChecked(false);
                    mLayoutCb.setChecked(false);
                } else {
                    ;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData1();
                    }
                }, 1000);
            }
        });
        mLikeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    whatFirst = "-Likenumber";
                    mTypeCb.setChecked(false);
                    mLayoutCb.setChecked(false);
                } else {
                    ;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData1();
                    }
                }, 1000);
            }
        });
        mLayoutCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    whatFirst = "-Commentsnumber";
                    mLikeCb.setChecked(false);
                    mTypeCb.setChecked(false);
                } else {
                    ;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData1();
                    }
                }, 1000);
            }
        });
    }

    private void setData() {
        findroute(0);
    }

    private void getData() {
        findroute(skipnumber);
        skipnumber = skipnumber + 20;
    }

    private void getData1() {//刷新的数据
        informationSets.clear();
        findroute(0);
    }

    private void findroute(Integer skipnumber) {
        BmobQuery<Routes> query = new BmobQuery<Routes>();
        query.addWhereExists("RUsername");
        query.setLimit(20 + skipnumber);
        query.setSkip(skipnumber);
        query.order(whatFirst);
        query.include("RUsername");
        query.findObjects(new FindListener<Routes>() {
            @Override
            public void done(List<Routes> object, BmobException e) {
                if (e == null) {
                    for (Routes route : object) {
                        ////////////////////////////////////
                        final InformationSet information = new InformationSet();
                        information.setTvLoaction(route.getRoutename());
                        information.setAuthor_head_url(route.getRUsername().getHeadPic());
                        information.setAuthor_id(route.getRUsername().getUsername());
                        information.setBtnLike(false);
                        information.setTvLikeCount(route.getLikenumber());        //添加这个
                        information.setTvCommentCount(route.getCommentsnumber());
                        information.setTvDate(route.getCreatedAt().toString());
                        information.setrouteId(route.getObjectId());
                        information.setrouteUsername(route.getRUsername().getUsername());

                        informationSets.add(information);
                        mAdapter.notifyDataSetChanged();
                        Infor_swipeRefreshLayout.setRefreshing(false);
                        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                    }
                } else {
                    Toast.makeText(getActivity(), "查询失败" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}