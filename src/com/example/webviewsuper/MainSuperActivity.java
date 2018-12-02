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
	ibCard,		//�쿨ר��
	ibLoan,		//����ר�� 
	ibAddQuota, //���ר��
	ibProgress,		//���Ȳ�ѯ
	ibCardOthers, 	//�쿨������
	ibAmerPos,		//����POS
	ibMarketTools,	//Ӫ������
	ibCardStudy;	//����ѧԺ
	
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
					 WebServiceManager.WriteLog("�쳣" + ex.getMessage(),"","","","");
					}

				}.start();
		 }
		
	}

	public String getDeviceId(Context context) {

		// ��ȡ���
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
		 
		//�쿨ר��
		ibCard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						CardMainSuperActivity.class); 
				startActivity(intent);
			}
		});
		//����ר��
		ibLoan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						LoanMainSuperActivity.class); 
				startActivity(intent); 
			}
		});
		//���ר��
		ibAddQuota.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						AddQuotaMainSuperActivity.class); 
				startActivity(intent); 
			}
		});
		//���Ȳ�ѯ
		ibProgress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						CardApplyQueryActivity.class);
				startActivity(intent);
			}
		});
		//�쿨������
		ibCardOthers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						OutLinkShowSuperActivity.class);
				intent.putExtra("LinkName", "BanKaGongNeng");
				intent.putExtra("HeaderName", "�쿨����"); 
				startActivity(intent);
			}
		});
		//����POS
		ibAmerPos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						OtherAppMainSuperActivity.class);
				intent.putExtra("type", "����POS");
				intent.putExtra("HeaderName", "����POS");
				startActivity(intent);
			}
		});
		//Ӫ������
		ibMarketTools.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						OtherAppMainSuperActivity.class);
				intent.putExtra("type", "Ӫ������");
				intent.putExtra("HeaderName", "Ӫ������");
				startActivity(intent);
			}
		});
		//����ѧԺ
		ibCardStudy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(MainSuperActivity.this,
						OutLinkShowSuperActivity.class);
				intent.putExtra("LinkName", "HuiYuanXueXi");
				intent.putExtra("HeaderName", "����ѧԺ");
				startActivity(intent);
			}
		});
		
	}





}
