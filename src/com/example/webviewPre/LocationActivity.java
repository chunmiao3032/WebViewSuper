package com.example.webviewPre;
 
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
 
//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.map.BitmapDescriptor;

import com.example.webviewPre.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity {

	TextView tv;
	String city;
	// 定位相关声明
	public LocationClient locationClient = null;
	// 自定义图标
	//BitmapDescriptor mCurrentMarker = null;
	boolean isFirstLoc = true;// 是否首次定位
	
	public BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null)
				return;
 
			city = location.getCity();
			Toast.makeText(getApplication(), city, 2000).show();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_location);

		tv = (TextView) findViewById(R.id.tv);
		 
  
		locationClient = new LocationClient(getApplicationContext()); // 实例化LocationClient类
		locationClient.registerLocationListener(myListener); // 注册监听函数
		this.setLocationOption(); // 设置定位参数
		locationClient.start(); // 开始定位

	}

	/**
	 * 设置定位参数
	 */
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPS
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll"); // 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000); // 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向

		locationClient.setLocOption(option);
	} 

}
