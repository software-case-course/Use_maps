package com.apress.gerber.use_maps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
//import android.graphics.Point;
import java.util.*;
import android.text.Editable;
import android.widget.ViewFlipper;

import com.apress.gerber.use_maps.view.Name_Acivity;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类

import com.baidu.location.LocationClientOption;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiSearch;//检索接口
import com.baidu.mapapi.search.poi.PoiResult;//搜索结果

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import mapapi.overlayutil.PoiOverlay;
import okhttp3.Route;

import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;//j检索结果回调

import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;


//import android.util.Log;

public class MyActivity extends Activity implements OnClickListener{
    List<String> haveCity=new ArrayList<String>();
    private Toast mToast;
   // private BMapManager mBMapManager;
    public MapView mMapView = null;
    public BaiduMap mBaiduMap;
    // 描述地图状态将要发生的状态
    private MapStatusUpdate msu;
    // 用于生成地图将要发生的变化
    private MapStatusUpdateFactory msuFactory;
   //private Button main_menu_button;
    private Context context;
    // private LocationData mLocData;
    //定位相关
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public MyLocationListener mLocationListenter = null;
    private boolean isFirstln = true;
    public double mLatitude,load_latitude;
    public double mLongtitude,load_longtitude;
    public String load_city;
    //自定义定位图标
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    //定义模式
    public MyLocationConfiguration.LocationMode mLocationMode;

    //覆盖物相关
    //private BitmapDescriptor mMarker;//定义覆盖物图标
     //private RelativeLayout mMarkerLy;

    //搜索定位相关
    private PoiSearch mPoiSearch;
    //private OnGetPoiSearchResultListener poiSearchListener;
    private OnClickListener oclistener;
    private EditText keyWordEditText;
    private EditText cityEditText;
    private Button queryButton;//s搜索按钮
    private Button nextDataBtn;//下一组搜索结果
    private static StringBuilder sb;

    //建议搜索
    private SuggestionSearch mSuggestionSearch=null;
    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private int load_Index = 0;

    // 记录检索类型
    private int type;
    // 记录页标
    private int page = 1;
    private int totalPage = 0;

    //覆盖物底层弹出
    private BottomView bottomView;
    private String poi_address,name,rid;//地址详细，地址名称,路线编号
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //int[] resourceId = new int[]{  R.drawable.one, R.drawable.mi, R.drawable.cardbag, R.drawable.bowrain};
    int[] resourceId = new int[]{};
    ///////////////////////////////////////////////////////////////////////////////
    List<View> views = new ArrayList<>();
    Calendar c = Calendar.getInstance();
     String currentTime,currentMood;//日期,心情
     Button launcher;//编辑按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该ntView方法之前实方法要再setConte现
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_my);
        this.context = this;
       Button main_menu_button=(Button)findViewById(R.id.main_menu) ;
        main_menu_button.setOnClickListener(this);


        final Bundle bundle = this.getIntent().getExtras();
        /*获取Bundle中的数据，注意类型和key*/
        ///////////////////////////////////////////////////////////////
        rid=bundle.getString("RouteID");//路线编号
        //Toast.makeText(MyActivity.this,"路线编号："+rid,Toast.LENGTH_LONG).show();

        initView();
        initLocation();//初始化定位

        // initMarker();//覆盖物

        initSearch();//搜索定位

      //底层弹出布局
        bottom_lay();
    }
/*
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle extraInfo = marker.getExtraInfo();
                Info info = (Info) extraInfo.getSerializable("info");
                ImageView iv = (ImageView) mMarkerLy.findViewById(R.id.id_info_img);
                TextView name = (TextView) mMarkerLy.findViewById(R.id.id_info_name);
                TextView distance = (TextView) mMarkerLy.findViewById(R.id.id_info_distance);
                TextView zan = (TextView) mMarkerLy.findViewById(R.id.id_info_zan);
                iv.setImageResource(info.getImgId());
                distance.setText(info.getDistance());
                name.setText(info.getName());
                zan.setText(info.getZan() + "");
                InfoWindow infoWindow;
                TextView tv = new TextView(context);
                tv.setBackgroundResource(R.drawable.location_tips);
                tv.setPadding(30, 20, 30, 50);
                tv.setText(info.getName());
                final LatLng latLng = marker.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
                p.y -= 47;
                LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);
                BitmapDescriptor tips = BitmapDescriptorFactory.fromView(tv);
                infoWindow = new InfoWindow(tips, ll, 10,
                        new InfoWindow.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick() {
                                mBaiduMap.hideInfoWindow();
                            }
                        });
                mBaiduMap.showInfoWindow(infoWindow);
                mMarkerLy.setVisibility(View.VISIBLE);
                return true;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                mMarkerLy.setVisibility(View.GONE);//点击地图后，布局消失
                mBaiduMap.hideInfoWindow();//点击地图后提示框消失
            }
            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }
        });
    }
*/
    private void initSearch() {
        cityEditText = (EditText) findViewById(R.id.city_edittext);
        keyWordEditText = (EditText) findViewById(R.id.searchkey);
        queryButton = (Button) findViewById(R.id.query_button);
        nextDataBtn=(Button)findViewById(R.id.next_data_btn);
        nextDataBtn.setEnabled(false);
        //queryButton.setEnabled(false);
        queryButton.setOnClickListener(this);
        nextDataBtn.setOnClickListener(this);
        //实例化PoiSearch对象
        mPoiSearch = PoiSearch.newInstance();
        // 设置检索监听器
        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchListener);

        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(suggestionListener);

        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
       sugAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
      //  sugAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1);
        keyWorldsView.setAdapter(sugAdapter);

        keyWorldsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length()<=0) {
                    return;
                }
                String city=cityEditText.getText().toString();
                //Toast.makeText(MyActivity.this, city, Toast.LENGTH_SHORT).show();

            mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(city));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queryButton.setEnabled(true);
                page = 1;
                totalPage = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
OnGetPoiSearchResultListener poiSearchListener=new OnGetPoiSearchResultListener() {
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null
                || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            Toast.makeText(MyActivity.this, "未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
            mBaiduMap.clear();
            MyPoiOverlay poiOverlay = new MyPoiOverlay(mBaiduMap);
            poiOverlay.setData(poiResult);// 设置POI数据
            mBaiduMap.setOnMarkerClickListener(poiOverlay);
            poiOverlay.addToMap();// 将所有的overlay添加到地图上
            poiOverlay.zoomToSpan();
            //
            totalPage = poiResult.getTotalPageNum();// 获取总分页数
            Toast.makeText(
                    MyActivity.this,
                    "总共查到" + poiResult.getTotalPoiNum() + "个兴趣点, 分为"
                            + totalPage + "页", Toast.LENGTH_SHORT).show();

        }
        if(poiResult.error==SearchResult.ERRORNO.AMBIGUOUS_KEYWORD)
        {//当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字的城市列表
            String strInfo="在";
            for(CityInfo cityInfo:poiResult.getSuggestCityList())
            {
                strInfo=strInfo + cityInfo.city;
                strInfo+=",";
            }
            strInfo+="找到结果";
            Toast.makeText(MyActivity.this,strInfo,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR)
        {
            bottomView.setAddressText("无详细结果");
        }
        else {// 正常返回结果的时候，此处可以获得很多相关信息
             name = poiDetailResult.getName();
            poi_address= poiDetailResult.getAddress();
            bottomView.setAddressText("【"+name+"】"+poi_address);

            load_latitude=poiDetailResult.getLocation().latitude;
            load_longtitude=poiDetailResult.getLocation().longitude;

           search(load_longtitude,load_latitude,rid);

        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

};


    OnGetSuggestionResultListener suggestionListener=new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            if(suggestionResult==null||suggestionResult.getAllSuggestions()==null)
            {
                return;
            }
            sugAdapter.clear();
            for(SuggestionResult.SuggestionInfo info:suggestionResult.getAllSuggestions())
            {
                if(info.key!=null)
                {
                    sugAdapter.add(info.key);
                }
            }
            sugAdapter.notifyDataSetChanged();
        }
    };


    public class MyPoiOverlay extends PoiOverlay{
        public MyPoiOverlay(BaiduMap arg0) {
            super(arg0);
        }
        @Override
        public boolean onPoiClick(int arg0) {
            super.onPoiClick(arg0);
            final PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
            String name=poiInfo.name;
            //////////////////////////////////////////////
             load_city=poiInfo.city;//待上传的该点的城市名
            /////////////////////////////////////////////
            Button btn=new Button((MyActivity.this));
            btn.setText(name);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(btn);
            InfoWindow infoWindow=new InfoWindow(bitmapDescriptor, poiInfo.location, -50,
                    new InfoWindow.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick() {

                        }
                    });
            // 显示InfoWindow
            mBaiduMap.showInfoWindow(infoWindow);

            // 检索poi详细信息
              mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiInfo.uid));
                bottomView.setVisibility(View.VISIBLE);
                TranslateAnimation animation = (TranslateAnimation) AnimationUtils.loadAnimation(MyActivity.this, R.anim.anim_show);
                bottomView.startAnimation(animation);
                return super.onPoiClick(arg0);
        }
    }

    private void bottom_lay()
    {
        //int[] resourceId = new int[]{R.drawable.welcome_welcome};
            for (int Id : resourceId) {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(Id);
                views.add(imageView);
            }

        //currentTime = c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月"+c.get(Calendar.DAY_OF_MONTH)+"日";
        bottomView = (BottomView) findViewById(R.id.bottomView);
       // bottomView.setAddressText(poi_address);
        bottomView.addViewToViewFillper(views);//图片轮播

       // search(load_longtitude,load_latitude,rid);
       // bottomView.setDateText(currentTime);//日期
      //  bottomView.setMoodText(currentMood);//心情

        //心情
        bottomView.setOnAddAndBackListener(new BottomView.OnBottomViewListener() {
            @Override
            public void back() {
                TranslateAnimation animation = (TranslateAnimation) AnimationUtils.loadAnimation(MyActivity.this, R.anim.anim_hide);
                bottomView.startAnimation(animation);
                bottomView.setVisibility(View.GONE);
            }

            @Override
            public void add() {
                //点击地层布局的“编辑”按钮调整到 Tripactivity.class界面
                haveCity.add(load_city);
                //Log.i("testcity", haveCity.get(0));
                Intent intent = new Intent(MyActivity.this, TripActivity.class);
                intent.putExtra("latitude",load_latitude);//待上传的纬度
                intent.putExtra("longtitude",load_longtitude);//待上传的经度
                intent.putExtra("location",poi_address);//用作编辑页面的显示地址
                intent.putExtra("city",load_city);//待上传的城市名
                intent.putExtra("RouteID",rid);//路线编号
                startActivityForResult(intent, 111);

            }
            @Override
            public void viewFillper(ViewFlipper viewFlipper) {
                Toast.makeText(MyActivity.this, "点击右上角上传图片喔~", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void mood(LinearLayout bottomLinearLayout) {
                Toast.makeText(MyActivity.this, "点击右上角编辑心情喔~", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("Saved", false)) {
                   bottomView.setDateText(data.getStringExtra("旅游时间"));
                    String mood = data.getStringExtra("旅游心情");
                    bottomView.setMoodText(mood.length()==0?"心情空空如也~":mood);
                    bottomView.removeViewOfViewFillper();
                    bottomView.addViewToViewFillper(TripActivity.imageViews);
                }
            }
        }

    }

            //城市内搜索
            private void citySearch(int page) {
                // 设置检索参数
                PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
                String city=cityEditText.getText().toString().trim();
                String keyWord=keyWordEditText.getText().toString().trim();
                if(city.isEmpty())
                {
                    city="广州";
                }
                citySearchOption.city(city);// 城市
                citySearchOption.keyword(keyWord);// 关键字
                citySearchOption.pageCapacity(15);// 默认每页10条
                citySearchOption.pageNum(page);// 分页编号
                // 发起检索请求
                mPoiSearch.searchInCity(citySearchOption);
            }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.query_button:
                type = 0;
                page = 1;
                queryButton.setEnabled(false);
                nextDataBtn.setEnabled(true);
                mBaiduMap.clear();
                citySearch(page);
                break;
            case R.id.next_data_btn:
                switch (type) {
                    case 0:
                        if (++page <= totalPage) {
                            citySearch(page);
                        } else {

                            Toast.makeText(MyActivity.this, "已经查到了最后一页~",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                break;

            case R.id.main_menu:
                   Intent intent=new Intent(MyActivity.this,AddOverlayActivity.class);
                    startActivity(intent);
               break;

            default:
                break;
        }
    }

/*
    private void initMarker() {
        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
        mMarkerLy = (RelativeLayout) findViewById(R.id.id_maker_ly);//图标maker对应的布局文件

    }
    */

    private void initLocation() {
        //默认模式为普通模式
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mLocationClient = new LocationClient(this);
        mLocationListenter = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListenter);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
////////////////////////////////////////////////////////////////////////////////////////
       // onStart();
        ////////////////////////////////////////////////////////////////

        mLocationClient.setLocOption(option);
        //初始化图标
        mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.gps_locked);

        myOrientationListener = new MyOrientationListener(context);

        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });

    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
       msu = MapStatusUpdateFactory.zoomTo(15.0f);
       // mMapView.showZoomControls(true);
        mBaiduMap.setMapStatus(msu);
        final GeoCoder geoCoder = GeoCoder.newInstance();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                // 设置地图新中心点
                msu = msuFactory.newLatLng(arg0);
                mBaiduMap.animateMapStatus(msu);
                 geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(arg0));
                 geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                    }
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                        if (result == null ||  result.error != SearchResult.ERRORNO.NO_ERROR) {
                            Toast.makeText(MyActivity.this, "未知地址!",Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(MyActivity.this,
                                "地图中心点移动到：" + result.getAddress(), Toast.LENGTH_SHORT)
                                .show();

                    }
                });

            }
            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        //开启方向传感器
        myOrientationListener.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        //停止方向传感器
        myOrientationListener.stop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mPoiSearch.destroy();//释放poi检索对象
        mMapView.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        //  you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.id_map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.id_map_site:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.id_map_traffic:
                if (mBaiduMap.isTrafficEnabled()) {
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通（off）");

                } else {
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通（on）");
                }
                break;
            case R.id.id_map_location:
                centerToMyLocation();
                break;
            //模式按钮选择
            case R.id.id_map_mode_common:
                mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                break;
            case R.id.id_map_mode_following:
                mLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                break;
            case R.id.id_map_mode_compass:
                mLocationMode = MyLocationConfiguration.LocationMode.COMPASS;
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void centerToMyLocation() {
        LatLng latLng = new LatLng(mLatitude,mLongtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);

    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData data = new MyLocationData.Builder()//
                    .direction(mCurrentX)//
                    .accuracy(location.getRadius())//
                    .latitude(location.getLatitude()*1E6)//
                    .longitude(location.getLongitude()*1E6).build();
            mBaiduMap.setMyLocationData(data);
            //设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(config);
            //更新经纬度
            mLatitude = location.getLatitude()*1E6;
            mLongtitude = location.getLongitude()*1E6;

            if (isFirstln) {
                LatLng latLng = new LatLng(mLatitude,mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstln = false;
                Toast.makeText(context, location.getAddrStr(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //按下返回键
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(
                        MyActivity.this);
                builder.setMessage("是否保存该旅游轨迹？");

                builder.setPositiveButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Routes RS = new Routes();
                        RS.setObjectId(rid);
                        RS.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e == null){
                                    Toast.makeText(MyActivity.this,"删除该路线成功",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(MyActivity.this,"删除该路线失败"+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        MyActivity.this.finish();
                    }
                });

                builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                            final EditText inputServer = new EditText(MyActivity.this);
                            inputServer.setFocusable(true);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                            builder.setTitle("*给你的这趟旅程起个名字吧*").setView(inputServer).setNegativeButton("取消",null);
                        
                        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String inputName = inputServer.getText().toString();
                                            MyActivity.this.finish();
                                            //上传服务器（路线ID、旅迹名字）
                                            Routes r1 = new Routes();
                                            //Log.i("testcity1", haveCity.get(0));
                                            r1.setHaveCity(haveCity);
                                            r1.setRoutename(inputName);
                                            r1.update(rid, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if(e == null){
                                                    }
                                                }
                                            });
                                        }
                                    });
                            builder.show();
                        }
                });
                builder.create().show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void search(double longitude , double latitude , String route){
        BmobQuery<Location_Photo> eq1 = new BmobQuery<Location_Photo>();
        eq1.addWhereEqualTo("Location_Longitude",longitude);//

        BmobQuery<Location_Photo> eq2 = new BmobQuery<Location_Photo>();
        eq2.addWhereEqualTo("Location_Latitude", latitude);//

        BmobQuery<Location_Photo> eq3 = new BmobQuery<Location_Photo>();
        eq2.addWhereEqualTo("RouteID", route);//

        List<BmobQuery<Location_Photo>> andQuerys = new ArrayList<BmobQuery<Location_Photo>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        andQuerys.add(eq3);

        BmobQuery<Location_Photo> query = new BmobQuery<Location_Photo>();
        query.and(andQuerys);
        query.findObjects(new FindListener<Location_Photo>() {
            @Override
            public void done(List<Location_Photo> list, BmobException e) {
                if(e==null){
                    for (Location_Photo LPF : list) {
                        Toast.makeText(MyActivity.this,LPF.getMydate(), Toast.LENGTH_SHORT).show();

                        if(LPF.getEmotion()==null)
                        {
                            bottomView.setMoodText("心情空空如也");//心情

                        }
                        else
                        {
                            String emotion = LPF.getEmotion();
                            bottomView.setMoodText(emotion);//心情
                        }

                        if(LPF.getMydate()==null)
                        {
                            bottomView.setDateText("旅游日期");//日期
                        }
                        else {
                            String time = LPF.getMydate();
                            bottomView.setDateText(time);//日期
                        }
                    }
                }
                else{
                    Toast.makeText(MyActivity.this,"查询数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
