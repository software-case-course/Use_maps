package com.apress.gerber.use_maps.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.apress.gerber.use_maps.Location_Photo;
import com.apress.gerber.use_maps.R;
import com.apress.gerber.use_maps.Routes;
import com.apress.gerber.use_maps.view.MyListAdapter;
import com.apress.gerber.use_maps.view.MyListSet;
import com.zaaach.citypicker.CityPickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import rx.Subscription;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Administrator on 2017/1/7.
 */

public class search_fragment extends Fragment {

    private static final int REQUEST_CODE_PICK_CITY = 233;
    private Button searchbutton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int lastVisibleItem;
    private Handler handler = new Handler();
    private    boolean isLoading;;

    List<MyListSet> Mylistset=new ArrayList<>();
    MyListSet myListSet=new MyListSet();
    LinearLayoutManager lLayoutManager;

    RecyclerView lRecyclerView;
    MyListAdapter lAdapter;

    Integer Skipnumber1 = 20;
    Integer Skipnumber2 = 20;
    String getcity = null;

    public search_fragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mylist,container,false);
        searchbutton=(Button) view.findViewById(R.id.search_city_button);
        lRecyclerView = (RecyclerView) view.findViewById(R.id.rvUserProfile);//列表
        lLayoutManager = new LinearLayoutManager(getActivity());
        //2 为RecyclerView创建布局管理器，这里使用的是LinearLayoutManager，表示里面的Item排列是线性排列
        lRecyclerView.setLayoutManager(lLayoutManager);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
      //  initRecyclerView(view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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



        lRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = lLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem + 1 == lAdapter.getItemCount()) {

                    boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                    if (isRefreshing) {
                        lAdapter.notifyItemRemoved(lAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getData();
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }

        });
        lRecyclerView.setHasFixedSize(true);
        lLayoutManager = new LinearLayoutManager(getActivity());
        lRecyclerView.setLayoutManager(lLayoutManager);
        lRecyclerView.setItemAnimator(new DefaultItemAnimator());
         setData();
        lAdapter = new MyListAdapter(getActivity(),Mylistset);
        lRecyclerView.setAdapter(lAdapter);
        handler.sendEmptyMessageDelayed(0, 3000);

        //////////////////////////////////////////////////////////////////////////////////////////////////
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
            }
        });

        return view;
    }

    private void setData() {
        findlastroute(0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                getcity = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
              //////////////////////////////////////////////////////上传城市名
                Mylistset.clear();
                findCityroute(0, getcity);
            }
        }
    }
    /**
     * 获取测试数据
     */
    private void getData() {//加载更多的数据
        if (getcity == null) {
            findlastroute(Skipnumber1);
            Skipnumber1 = Skipnumber1 + 20;
        } else {
            findCityroute(Skipnumber2, getcity);
            Skipnumber2 = Skipnumber2 + 20;
        }
    }

    private void getData1() {//刷新的数据
        if (getcity == null) {
            Mylistset.clear();
            findlastroute(0);
        } else {
            Mylistset.clear();
            findCityroute(0, getcity);
        }
    }

    public void findlastroute(Integer skipnumber) {
        BmobQuery<Routes> query = new BmobQuery<Routes>();
        query.addWhereExists("Routename");
        query.setLimit(20 + skipnumber);
        query.setSkip(skipnumber);
        query.order("-createdAt");
        query.findObjects(new FindListener<Routes>() {
            @Override
            public void done(List<Routes> object, BmobException e) {
                if (e == null) {
                    for (Routes routes : object) {
                        MyListSet myListSet = new MyListSet();
                        myListSet.setIvImage(getResources().getDrawable(R.drawable.scene));
                        myListSet.setMylist_title(routes.getRoutename());
                        Mylistset.add(myListSet);
                        lAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        lAdapter.notifyItemRemoved(lAdapter.getItemCount());
                    }
                } else {
                }
            }
        });
    }

    public void findCityroute(Integer skipnumber, String city) {
        BmobQuery<Routes> query = new BmobQuery<Routes>();
        String[] findcity = {city + "市"};
        query.addWhereContainsAll("HaveCity", Arrays.asList(findcity));
        query.setLimit(20 + skipnumber);
        query.setSkip(skipnumber);
        query.order("-Likenumber,-createdAt");
        query.include("LPRouteID");
        query.findObjects(new FindListener<Routes>() {
            @Override
            public void done(List<Routes> object, BmobException e) {
                if (e == null) {
                    for (Routes Rt : object) {
                        MyListSet myListSet = new MyListSet();
                        myListSet.setIvImage(getResources().getDrawable(R.drawable.scene));
                        myListSet.setMylist_title(Rt.getRoutename());
                        Mylistset.add(myListSet);
                        lAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        lAdapter.notifyItemRemoved(lAdapter.getItemCount());
                    }
                } else {
                }
            }
        });
    }
}