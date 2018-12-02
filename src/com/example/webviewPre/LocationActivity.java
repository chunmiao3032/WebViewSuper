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
	// ��λ�������
	public LocationClient locationClient = null;
	// �Զ���ͼ��
	//BitmapDescriptor mCurrentMarker = null;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	
	public BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
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
		 
  
		locationClient = new LocationClient(getApplicationContext()); // ʵ����LocationClient��
		locationClient.registerLocationListener(myListener); // ע���������
		this.setLocationOption(); // ���ö�λ����
		locationClient.start(); // ��ʼ��λ

	}

	/**
	 * ���ö�λ����
	 */
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // ��GPS
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// ���ö�λģʽ
		option.setCoorType("bd09ll"); // ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.setScanSpan(5000); // ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.setIsNeedAddress(true); // ���صĶ�λ���������ַ��Ϣ
		option.setNeedDeviceDirect(true); // ���صĶ�λ��������ֻ���ͷ�ķ���

		locationClient.setLocOption(option);
	} 

}
