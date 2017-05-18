package com.apress.gerber.use_maps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class AddOverlayActivity extends Activity {

	private MapView mMapView = null;// 百度地图控件
	private BaiduMap bdMap;// 百度地图对象
	private TextView travel_name;// 路线标题
	private Marker marker1;// marker

	private double latitude = 39.9401752;// 经纬度
	private double longitude = 116.400244;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	// 构建marker图标
	BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.maker);
	// GroundOptions
	BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.location);

    private BottomView bottomView;
    List<View> views = new ArrayList<>();
    Calendar c = Calendar.getInstance();
    String currentTime;
    int[] resourceId = new int[]{  R.drawable.pic1, R.drawable.pic2};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_overlay);
        mMapView = (MapView) findViewById(R.id.bmapview);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        bdMap = mMapView.getMap();
        bdMap.setMapStatus(msu);


        // 对marker覆盖物添加点击事件
        bdMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
              //  if (arg0 == marker1) {
                Bundle extraInfo = arg0.getExtraInfo();
                Info info = (Info) extraInfo.getSerializable("info");

                    final LatLng latLng = arg0.getPosition();
                    // 将经纬度转换成屏幕上的点
                     Point point =bdMap.getProjection().toScreenLocation(latLng);
					point.y -= 47;
					LatLng ll = bdMap.getProjection().fromScreenLocation(point);
                    Toast.makeText(AddOverlayActivity.this, latLng.toString(),
                            Toast.LENGTH_SHORT).show();

					bottomView.setVisibility(View.VISIBLE);
					TranslateAnimation animation = (TranslateAnimation) AnimationUtils.loadAnimation(AddOverlayActivity.this, R.anim.anim_show);
					bottomView.startAnimation(animation);

             //   }
                return false;
            }
        });


         //地图单击事件

        bdMap.setOnMapClickListener(new OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {

                //bottomView.setVisibility(View.VISIBLE);
               // TranslateAnimation animation = (TranslateAnimation) AnimationUtils.loadAnimation(AddOverlayActivity.this, R.anim.anim_show);
                //bottomView.startAnimation(animation);

                return false;
            }

            @Override
            public void onMapClick(LatLng latLng) {
              // displayInfoWindow(latLng);
            }
        });



        // 地图双击事件

        bdMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
            @Override
            public void onMapDoubleClick(LatLng arg0) {

            }
        });
          travel_name=(TextView)findViewById(R.id.Route_name);
          travel_name.setText("北京之旅");
          addPolylineOptions();
           bottom_lay();
    }



    private void bottom_lay()
    {
       // int[] resourceId = new int[]{R.drawable.mi,R.drawable.cardbag};
        for (int Id : resourceId) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(Id);
            views.add(imageView);
        }

        currentTime = c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月"+c.get(Calendar.DAY_OF_MONTH)+"日";
        bottomView = (BottomView) findViewById(R.id.bottomView);


        bottomView.addViewToViewFillper(views);//图片轮播

        // search(load_longtitude,load_latitude,rid);
         bottomView.setDateText("2016年10月11日");//日期
          bottomView.setMoodText("我很开心");//心情
            bottomView.setAddressText("后院酒吧");

        bottomView.setOnAddAndBackListener(new BottomView.OnBottomViewListener() {
            @Override
            public void back() {
                TranslateAnimation animation = (TranslateAnimation) AnimationUtils.loadAnimation(AddOverlayActivity.this, R.anim.anim_hide);
                bottomView.startAnimation(animation);
                bottomView.setVisibility(View.GONE);
            }

            @Override
            public void add() {

            }
            @Override
            public void viewFillper(ViewFlipper viewFlipper) {

            }
            @Override
            public void mood(LinearLayout bottomLinearLayout) {

            }
        });
    }

/*
	 //添加标注覆盖物
	private void addMarkerOverlay(LatLng latLng) {
		//bdMap.clear();
		// 定义marker坐标点
		LatLng point = latLng;

		// 构建markerOption，用于在地图上添加marker
		OverlayOptions options = new MarkerOptions()//
				.position(point)// 设置marker的位置
				.icon(bitmap)// 设置marker的图标
				.zIndex(9);// 設置marker的所在層級
				//.draggable(true);// 设置手势拖拽
		// 在地图上添加marker，并显示
		marker1 = (Marker) bdMap.addOverlay(options);
	}
*/

    private void addOverlays(List<Info>infos)
    {
        bdMap.clear();
        LatLng latLng=null;
        OverlayOptions options;

        List<LatLng>all_latlng=new ArrayList<LatLng>();//
        for(Info info:infos)
        {
             latLng=new LatLng(info.getLatitude(),info.getLongitude());
             all_latlng.add(latLng);
            options=new MarkerOptions().position(latLng).icon(bitmap).zIndex(9);
            marker1 = (Marker) bdMap.addOverlay(options);

            Bundle arg0=new Bundle();
            arg0.putSerializable("info",info);
            marker1.setExtraInfo(arg0);
        }
        //LatLngBounds bounds=new LatLngBounds.Builder().include(all_latlng.get(0)).include(all_latlng.get(1)).include(all_latlng.get(2)).include(all_latlng.get(3)).build();
        //MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        bdMap.setMapStatus(u);
    }


	/**
	 * 添加折线覆盖物
	 */
	private void addPolylineOptions() {
		bdMap.clear();
		// 点
		LatLng pt1 = new LatLng(latitude + 1, longitude);
		LatLng pt2 = new LatLng(latitude, longitude - 1);
		LatLng pt3 = new LatLng(latitude - 1, longitude - 1);
		LatLng pt5 = new LatLng(latitude, longitude + 1);

        addOverlays(Info.infos);
       // addMarkerOverlay(pt1);
       // addMarkerOverlay(pt2);
       // addMarkerOverlay(pt3);
       // addMarkerOverlay(pt5);

       // LatLngBounds bounds=new LatLngBounds.Builder().include(pt1).include(pt2).include(pt3).include(pt5).build();
       // MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(bounds.getCenter());
        //bdMap.setMapStatus(u);

        List<LatLng> points = new ArrayList<LatLng>();
		points.add(pt1);
		points.add(pt2);
		points.add(pt3);
		points.add(pt5);
		//
		PolylineOptions polylineOptions = new PolylineOptions();
		polylineOptions.points(points);
		polylineOptions.color(0xFF000000);
		polylineOptions.width(6);// 折线线宽
		bdMap.addOverlay(polylineOptions);
	}

	/**
	 * 显示弹出窗口覆盖物
	 */
	private void displayInfoWindow(final LatLng latLng) {
		// 创建infowindow展示的view
		Button btn = new Button(getApplicationContext());
		btn.setBackgroundResource(R.drawable.popup);
		btn.setText("点我点我~");
		btn.setTextColor(0xAA000000);
		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
				.fromView(btn);
		// infowindow点击事件
		OnInfoWindowClickListener infoWindowClickListener = new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick() {
				reverseGeoCode(latLng);
				// 隐藏InfoWindow
				bdMap.hideInfoWindow();
			}
		};
		// 创建infowindow
		InfoWindow infoWindow = new InfoWindow(bitmapDescriptor, latLng, -47,
				infoWindowClickListener);

		// 显示InfoWindow
		bdMap.showInfoWindow(infoWindow);
	}

	/**
	 * 反地理编码得到地址信息
	 */
	private void reverseGeoCode(LatLng latLng) {
		// 创建地理编码检索实例
		GeoCoder geoCoder = GeoCoder.newInstance();
		//
		OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
			// 反地理编码查询结果回调函数
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					// 没有检测到结果
					Toast.makeText(AddOverlayActivity.this, "抱歉，未能找到结果",
							Toast.LENGTH_LONG).show();
				}
				Toast.makeText(AddOverlayActivity.this,
						"位置：" + result.getAddress(), Toast.LENGTH_LONG).show();
			}

			// 地理编码查询结果回调函数
			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					// 没有检测到结果
				}
			}
		};
		// 设置地理编码检索监听者
		geoCoder.setOnGetGeoCodeResultListener(listener);
		//
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
		// 释放地理编码检索实例
		// geoCoder.destroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		// 回收bitmip资源
		bitmap.recycle();
		bitmap2.recycle();
	}


}
