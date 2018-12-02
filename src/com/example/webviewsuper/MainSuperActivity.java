package com.example.webviewsuper;

import com.example.webviewPre.R;
import com.example.webviewPre.CardApplyQueryActivity;
import com.example.webviewPre.CardShowActivity;
import com.example.webviewPre.MainActivity;
import com.webservice.WebServiceManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainSuperActivity extends Activity {

	ImageButton 
	ibCard,		//办卡专区
	ibLoan,		//贷款专区 
	ibAddQuota, //提额专区
	ibProgress,		//进度查询
	ibCardOthers, 	//办卡功能区
	ibAmerPos,		//美国POS
	ibMarketTools,	//营销工具
	ibCardStudy;	//卡神学院
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_super);
		
		writeLog();
		
		init();
		
		initLinistioner();
	}

	

	private void writeLog() {
		 try
		 {
			 TelephonyManager tm = (TelephonyManager) getApplicationContext()
						.getSystemService(Context.TELEPHONY_SERVICE);
			 
			 final String deviceID = tm.getDeviceId();
			 final String simNo = tm.getSimSerialNumber();
			 
			 final String version = tm.getDeviceSoftwareVersion();
			 final String netWork = tm.getNetworkOperator();
			 final String opName = tm.getNetworkOperatorName(); 
			 final int netType = tm.getNetworkType();
			 
			 new Thread() {
					public void run() {
					 WebServiceManager.WriteLog(opName + netType, netWork, version,deviceID, simNo);
					}

				}.start();
		 }
		 catch(final Exception ex)
		 {
			 new Thread() {
					public void run() {
					 WebServiceManager.WriteLog("异常" + ex.getMessage(),"","","","");
					}

				}.start();
		 }
		
	}

	public String getDeviceId(Context context) {

		// 读取序号
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		 
		return tm.getDeviceId();
	}



	private void init() {
		 
		ibCard = (ImageButton)findViewById(R.id.ibCard);
		ibLoan = (ImageButton)findViewById(R.id.ibLoan);
		ibAddQuota = (ImageButton)findViewById(R.id.ibAddQuota);
		ibProgress = (ImageButton)findViewById(R.id.ibProgress);
		ibCardOthers = (ImageButton)findViewById(R.id.ibCardOthers);
		ibAmerPos = (ImageButton)findViewById(R.id.ibAmerPos);
		ibMarketTools = (ImageButton)findViewById(R.id.ibMarketTools);
		ibCardStudy = (ImageButton)findViewById(R.id.ibCardStudy);
		
	}
	
	private void initLinistioner() {
		 
		//办卡专区
		ibCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						CardMainSuperActivity.class); 
				startActivity(intent);
			}
		});
		//贷款专区
		ibLoan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						LoanMainSuperActivity.class); 
				startActivity(intent); 
			}
		});
		//提额专区
		ibAddQuota.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						AddQuotaMainSuperActivity.class); 
				startActivity(intent); 
			}
		});
		//进度查询
		ibProgress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						CardApplyQueryActivity.class);
				startActivity(intent);
			}
		});
		//办卡功能区
		ibCardOthers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						OutLinkShowSuperActivity.class);
				intent.putExtra("LinkName", "BanKaGongNeng");
				intent.putExtra("HeaderName", "办卡工具"); 
				startActivity(intent);
			}
		});
		//美国POS
		ibAmerPos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						OtherAppMainSuperActivity.class);
				intent.putExtra("type", "美国POS");
				intent.putExtra("HeaderName", "美国POS");
				startActivity(intent);
			}
		});
		//营销工具
		ibMarketTools.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						OtherAppMainSuperActivity.class);
				intent.putExtra("type", "营销工具");
				intent.putExtra("HeaderName", "营销工具");
				startActivity(intent);
			}
		});
		//卡神学院
		ibCardStudy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						OutLinkShowSuperActivity.class);
				intent.putExtra("LinkName", "HuiYuanXueXi");
				intent.putExtra("HeaderName", "卡神学院");
				startActivity(intent);
			}
		});
		
	}





}
