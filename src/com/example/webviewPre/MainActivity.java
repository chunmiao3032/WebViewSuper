package com.example.webviewPre;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.basic.BankResultOK;
import com.basic.CardLogResult;
import com.basic.OtherLinkResult;
import com.common.CommonMethord;
import com.common.Global;
import com.common.MyGridView;
import com.common.SquareImageView;
import com.droid.Activity01;
import com.droid.DatabaseHelper;
import com.example.webviewPre.R;
import com.example.webviewPre.CardApplyQueryActivity.GridViewAdapter;
import com.webservice.WebServiceManager;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends Activity {

	ImageButton btCard, ibZxkz;//ibXykgl
	ImageButton btLineLoan, btLoan, ibYJTE, ibQYCX, ibZXCX, ibXHBK, ibBHBK,
			ibGEBK;
	TextView tvLocation, tvLocation1, tvChangeLocation,tvCloseLocation;
	String _City;
	MyGridView gridView,grid2,grid3;
	List<BankResultOK> _BankOKList;
	List<OtherLinkResult> _OtherLinkList1;
	List<OtherLinkResult> _OtherLinkList2;
	List<OtherLinkResult> _OtherLinkList3;
	ScrollView svMain;
	RelativeLayout rlLayOut;
	// *******************************************************************************************************
	// ***********************************ͼƬ�ֲ���ʼ������***********************************************************
	// *******************************************************************************************************
	private int[] imgResIDs = new int[] { R.drawable.p2, R.drawable.p2,
			R.drawable.p2, R.drawable.p2, R.drawable.p2 };
	private int[] radioButtonID = new int[] { R.id.radio0, R.id.radio1,
			R.id.radio2, R.id.radio3, R.id.radio4 };
	private ViewPager pager;
	private RadioGroup mGroup;
	private ArrayList<View> items = new ArrayList<View>();
	private Runnable runnable;
	private int mCurrentItem = 0;
	private int mItem;
	private Runnable mPagerAction;
	private boolean isFrist = true;

	// *******************************************************************************************************
	// ***********************************ͼƬ�ֲ���ʼ��***********************************************************
	// *******************************************************************************************************

	// ************************************************
	// xxx�����뿨��
	// ************************************************
	private TextSwitcher textSwitcher_title;

	// Ҫ��ʾ���ı�
	String[] titles = new String[] { "��������", "��ҵ����", "�㷢����", "ũҵ����", "��������",
			"�ַ�����", "��������" };
	private int curStr;

	private DatabaseHelper helper;
	// ************************************************

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// ȥ�����ڱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);

		scrollImageHandler = new Handler();
		HandlerCity = new Handler();

		initLocation();

		initControl();

		initListener();

		scrollImageInit();

		Handler = new Handler();
		gridView = (MyGridView) findViewById(R.id.grid); 
		grid2 = (MyGridView) findViewById(R.id.grid2);
		grid3 = (MyGridView) findViewById(R.id.grid3);
		setData();
 
		setGrid2();
		setGrid3();

		helper = new DatabaseHelper(this);
		initUpdateShowApplyInfo();
		
		// �̶�scrollView��ʾλ��
		svMain.fullScroll(ScrollView.FOCUS_UP);
	}

	private void initUpdateShowApplyInfo() {
		textSwitcher_title = (TextSwitcher) findViewById(R.id.textSwitcher_title);

		textSwitcher_title.setFactory(new ViewSwitcher.ViewFactory() {
			@Override
			public View makeView() {
				final TextView tv = new TextView(MainActivity.this);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
				tv.setPadding(10, 10, 10, 10);
				tv.setTextColor(Color.BLACK);
				// tv.setTextColor(Color.RED);
				return tv;
			}
		});

		final Random r = new Random();

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				 
				List<CardLogResult> logList = getCardLog();
				if(logList != null && logList.size() > 0)
				{
					int n4 = r.nextInt(logList.size()); 
					CardLogResult randLog = logList.get(n4);
					String userName = randLog.USER_NAME;
					if(userName.contains("anyType"))
					{
						userName = String.valueOf(r.nextInt(999999));
					}
					userName = userName.substring(1, userName.length());
					textSwitcher_title.setText("�ո�*" + userName + "������ " + randLog.BANK_NAME + "���ÿ�");
					// + randLog.CARD_NAME );
				}
				else
				{
					int n4 = r.nextInt(999999);
					textSwitcher_title.setText("�ո�*" + n4 + "������" + titles[curStr++ % titles.length] + "���ÿ�");
			
				}
				handler.postDelayed(this, 3000);
			}
		}, 1000);

	}
 
	private List<CardLogResult> getCardLog()
	{
		List<CardLogResult> logList = new ArrayList<CardLogResult>(); 
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from MD_CARD_LOG",null);
		if (cursor.getCount() > 0) { //
		 
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) { 
				CardLogResult log = new CardLogResult();
				String USER_NAME = cursor.getString(cursor.getColumnIndex("USER_NAME"));
				String BANK_NAME = cursor.getString(cursor.getColumnIndex("BANK_NAME"));
				String CARD_NAME = cursor.getString(cursor.getColumnIndex("CARD_NAME")); 
				
				log.USER_NAME = USER_NAME;
				log.BANK_NAME = BANK_NAME;
				log.CARD_NAME = CARD_NAME;
				logList.add(log);
			} 
		}
		return logList;
	}

	// *******************************************************************************************************
	// ***********************************��ʼ�����÷���***********************************************************
	// *******************************************************************************************************

	// ��ʼ���ؼ�
	private void initControl() {
		svMain = (ScrollView) findViewById(R.id.svMain);
		tvChangeLocation = (TextView) findViewById(R.id.tvChangeLocation);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocation1 = (TextView) findViewById(R.id.tvLocation1);
		tvCloseLocation = (TextView) findViewById(R.id.tvCloseLocation);
		rlLayOut = (RelativeLayout)findViewById(R.id.rlLayOut);
		 
		btCard = (ImageButton) findViewById(R.id.ivCard);        //���ٰ쿨
		btLineLoan = (ImageButton) findViewById(R.id.btLineLoan);//p2p����
		btLoan = (ImageButton) findViewById(R.id.btLoan);        //���ٴ��� 
		ibXHBK = (ImageButton) findViewById(R.id.ibXHBK);        //�꿨����
		ibBHBK = (ImageButton) findViewById(R.id.ibBHBK);        //��ѧ����ҵ����
		ibGEBK = (ImageButton) findViewById(R.id.ibGEBK);        //��׽�
	}

	private void initListener() {
		
		tvLocation.setOnClickListener(tvLocation_OnClick);
		tvLocation1.setOnClickListener(tvLocation1_OnClick);
		tvCloseLocation.setOnClickListener(tvCloseLocation_OnClick);
		tvChangeLocation.setOnClickListener(tvChangeLocation_OnClick); 
  
		btCard.setOnClickListener(btCard_OnClick);//���ٰ쿨
		btLineLoan.setOnClickListener(btLineLoan_OnClick);//p2p����
		btLoan.setOnClickListener(btLoan_OnClick);//���ٴ��� 
		ibXHBK.setOnClickListener(ibXHBK_OnClick);//�꿨����
		ibBHBK.setOnClickListener(ibBHBK_OnClick);//��ѧ����ҵ����
		ibGEBK.setOnClickListener(ibGEBK_OnClick);//��׽�
 
	}

	/**
	 * ͼƬ�ֲ���ʼ��
	 */
	private void scrollImageInit() {
		pager = (ViewPager) findViewById(R.id.tuijian_pager);
		mGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		initAllItems();
	}

	private void initAllItemsEnd() {
		pager.setAdapter(new PagerAdapter() {
			// ����
			@Override
			public Object instantiateItem(View container, int position) {
				View layout = items.get(position % items.size());
				pager.addView(layout);
				return layout;
			}

			// ����
			@Override
			public void destroyItem(View container, int position, Object object) {
				View layout = items.get(position % items.size());
				pager.removeView(layout);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;

			}

			@Override
			public int getCount() {
				return imgResIDs.length;
			}

		});
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(final int arg0) {
				mCurrentItem = arg0 % items.size();
				pager.setCurrentItem(mCurrentItem);
				mGroup.check(radioButtonID[mCurrentItem]);

				final View vw = items.get(arg0).findViewById(
						R.id.tuijian_header_img);

				items.get(arg0).findViewById(R.id.tuijian_header_img)
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								OtherLinkResult rlt = (OtherLinkResult) vw
										.getTag();

								// Toast.makeText(MainActivity.this,
								// arg0 + rlt.LINK_URL, 2000).show();

								if (rlt != null || rlt.LINK_URL != null) {
									Intent intent = new Intent(
											MainActivity.this,
											WebViewActivity.class);
									intent.putExtra("Url", rlt.LINK_URL);// ��ǰ���Ӵ��ݹ�ȥ
									startActivity(intent);
								} else {
									Toast.makeText(MainActivity.this,
											rlt.LINK_DESC + "û������", 2000)
											.show();
								}
							}
						});
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mPagerAction = new Runnable() {
			@Override
			public void run() {

				if (mItem != 0) {
					if (isFrist == true) {
						mCurrentItem = 0;
						isFrist = false;
					} else {
						if (mCurrentItem == items.size() - 1) {
							mCurrentItem = 0;
						} else {
							mCurrentItem++;
						}
					}

					pager.setCurrentItem(mCurrentItem);
					mGroup.check(radioButtonID[mCurrentItem]);

				}
				pager.postDelayed(mPagerAction, 5000);
			}
		};
		pager.postDelayed(mPagerAction, 100);

	}

	// *******************************************************************************************************
	// ***********************************�����¼�***************************************************************
	// *******************************************************************************************************
	// ��������
	OnClickListener tvLocation_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			changeCity(v);
		}

	};

	// ��������
	OnClickListener tvLocation1_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			changeCity(v);
		}

	};

	// ��������
	OnClickListener tvChangeLocation_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			changeCity(v);
		}

	};

	OnClickListener tvCloseLocation_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			rlLayOut.setVisibility(View.GONE);
		}

	};
	
	//�꿨���Ȳ�ѯ
	OnClickListener ibXHBK_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//String IsNewUser,String IsFastGet,String IsHighQuotar
//			Intent intent = new Intent(MainActivity.this,
//					CardShowActivity.class);
//			intent.putExtra("IsNewUser", "1");
//			startActivity(intent);
			
			Intent intent = new Intent(MainActivity.this,
					CardApplyQueryActivity.class);
			startActivity(intent);
			
		}

	};

	//��ѧ����ҵ����
	OnClickListener ibBHBK_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			Intent intent = new Intent(MainActivity.this,
//					CardShowActivity.class);
//			intent.putExtra("IsFastGet", "1");
//			startActivity(intent);
			
			Intent intent = new Intent(MainActivity.this,
					LineLoanShowActivity.class);
			intent.putExtra("loantype", "��ѧ������");
			startActivity(intent);
			
		}

	};

	//�׽�
	OnClickListener ibGEBK_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					CardShowActivity.class);
			intent.putExtra("IsHighQuotar", "1");
			startActivity(intent);
		}

	};
 

	// ���ÿ�����-����
	OnClickListener ibXykgl_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			for (OtherLinkResult link : Global.OutlinksList) {
//				if (link.LINK_NAME.equals("SysLink5")) {
//					Intent intent = new Intent(MainActivity.this,
//							WebViewActivity.class);
//					intent.putExtra("Url", link.LINK_URL);
//					startActivity(intent);
//				}
//			}
		}

	};

	// ���ٰ쿨
	OnClickListener btCard_OnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					CardMainActivity.class);
			startActivity(intent);
		}
	};

	// ����
	OnClickListener btLineLoan_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					LineLoanShowActivity.class);
			startActivity(intent);
		}
	};

	// ����
	OnClickListener btLoan_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					LoanShowActivity.class);
			startActivity(intent);
		}
	};

	// ���
	OnClickListener ibYJTE_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this,
					AddQuotaShow1Activity.class);
			startActivity(intent);

		}
	};

	// *******************************************************************************************************
	// ***********************************��������***************************************************************
	// *******************************************************************************************************

	public void changeCity(View view) {
		Intent intent = new Intent(MainActivity.this, Activity01.class);
		intent.putExtra("city", _City);// ��ǰ���д��ݹ�ȥ
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && data != null) {
			Bundle bundle = data.getExtras();
			String returnCity = bundle.getString("city");
			if (returnCity != null && returnCity.trim().length() > 0) {
				_City = returnCity;
				HandlerCity.post(UpdateCity);
			}
		}
	}

	// *******************************************************************************************************
	// ***********************************��������***************************************************************
	// *******************************************************************************************************
	// ����״̬ʵ�ֵ�ͼ�������ڹ���
	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		locationClient.stop();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//svMain.fullScroll(ScrollView.FOCUS_UP);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	// **************************************************************************************************
	// *******************************ͼƬ�ֲ���ʾ***********************************************************
	// **************************************************************************************************
	List<OtherLinkResult> linksList = new ArrayList<OtherLinkResult>();
	List<OtherLinkResult> outlinksList = new ArrayList<OtherLinkResult>();

	private void initAllItems() {
		// ArrayList<View> items ��ͼƬ������д��ͼƬ

		new Thread() {
			public void run() {
				linksList = WebServiceManager.getLinks("MainTitle");
				//Global.OutlinksList = WebServiceManager.getLinks("SysLink");
				scrollImageHandler.post(mUpdateResults);
			}

		}.start();

	}

	Handler scrollImageHandler = null;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {

			// ��ʼ��Viewpager������item
			for (int i = 0; i < linksList.size(); i++) {
				if (i > 4) {
					break;
				}

				View layout1 = getLayoutInflater().inflate(
						R.layout.scroll_image_header, null);
				ImageView imageView1 = (ImageView) layout1
						.findViewById(R.id.tuijian_header_img);

				OtherLinkResult rlt = linksList.get(i);
				imageView1.setTag(rlt);
				if (rlt.LINK_IMG_DATA != null
						&& rlt.LINK_IMG_DATA.trim().length() > 0) {
					Bitmap bitmap = CommonMethord
							.base64ToBitmap(rlt.LINK_IMG_DATA.trim());
					if (bitmap != null) {
						imageView1.setImageBitmap(bitmap);
					} else {
						imageView1.setImageResource(R.drawable.ic_launcher);
					}
				} else {
					imageView1.setImageResource(R.drawable.ic_launcher);
				}

				items.add(layout1);
			}
			mItem = items.size();
			initAllItemsEnd();
		}
	};

	// *******************************************************************************************************
	// ***********************************��λ��ش��뿪ʼ**********************************************************
	// *******************************************************************************************************

	public LocationClient locationClient = null;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ

	public BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null)
				return;
			if (_City != null && _City.trim().length() > 0) {
				return;
			}
			_City = location.getCity();
			HandlerCity.post(UpdateCity);
		}
	};

	Handler HandlerCity = null;
	final Runnable UpdateCity = new Runnable() {
		public void run() {
			if (_City == null) {
				return;
			}
			String strCity = _City.replace("��", "");
			if (tvLocation.getText().toString().contains(strCity)) {
				return;
			}

			SharedPreferences remdname = getSharedPreferences("Global",
					Activity.MODE_PRIVATE);
			SharedPreferences.Editor edit = remdname.edit();
			edit.putString("City", strCity);
			edit.commit();

			com.common.Global.City = strCity;
			tvLocation.setText("����ǰλ��λ��" + strCity + "��");
			tvLocation1.setText(strCity);
		}
	};

	/**
	 * ��ʼ����λ���÷���
	 */
	private void initLocation() {
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

	// **************************************************************************************************
	// *******************************�����Ƽ�***********************************************************
	// **************************************************************************************************	
	private void setData() {

		new Thread() {
			public void run() {
				_BankOKList = WebServiceManager.getBanksOK();
				Handler.post(bankUpdateResults);
			}

		}.start();

	}

	Handler Handler = null;
	final Runnable bankUpdateResults = new Runnable() {
		public void run() {

			//Global.BankList = _BankOKList;
			setGridView();
			// �̶�scrollView��ʾλ��
			svMain.fullScroll(ScrollView.FOCUS_UP);
		}
	};

	private void setGridView() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(),
				_BankOKList);
		gridView.setAdapter(adapter);
	}

	public class GridViewAdapter extends BaseAdapter {
		Context context;
		List<BankResultOK> list;

		public GridViewAdapter(Context _context, List<BankResultOK> _list) {
			this.list = _list;
			this.context = _context;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.cardapplyquery_item,
					null);

			final BankResultOK bank = list.get(position);

			TextView tvTitle1 = (TextView) convertView
					.findViewById(R.id.tvTitle1);
			TextView tvDesc1 = (TextView) convertView
					.findViewById(R.id.tvDesc1);

			ImageButton ItemImage1 = (ImageButton) convertView
					.findViewById(R.id.ItemImage1);

			tvTitle1.setText(bank.BANK_NAME);
			tvDesc1.setText(bank.BANK_DESC);

			if (bank.BANK_IMG_DATA != null) {
				Bitmap bm = CommonMethord.base64ToBitmap(bank.BANK_IMG_DATA);
				ItemImage1.setImageBitmap(bm);
				// Drawable drawable = CommonMethord.bitmap2Drawable(bm);
				// ItemImage1.setBackground(drawable);
			}

			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,
							CardShowActivity.class);
					intent.putExtra("bank", bank.BANK_NAME);
					startActivity(intent);
				}
			});

			return convertView;
		}
	}

	// **************************************************************************************************
	// *******************************���ظ���ģ������***********************************************************
	// **************************************************************************************************	
	
	public class LinkGridViewAdapter extends BaseAdapter {
		Context context;
		List<OtherLinkResult> list;

		public LinkGridViewAdapter(Context _context, List<OtherLinkResult> _list) {
			this.list = _list;
			this.context = _context;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.main_link_item1,
					null);   

			final OtherLinkResult bank = list.get(position);

			TextView tvTitle1 = (TextView) convertView
					.findViewById(R.id.tvTitle1);
			TextView tvDesc1 = (TextView) convertView
					.findViewById(R.id.tvDesc1);

			SquareImageView ItemImage1 = (SquareImageView) convertView
					.findViewById(R.id.ItemImage1);

//			tvTitle1.setText(bank.LINK_DESC);
//			tvDesc1.setText(bank.LINK_NAME);
			tvTitle1.setVisibility(View.GONE);
			tvDesc1.setVisibility(View.GONE);
			
			if (bank.LINK_IMG_DATA != null) {
				Bitmap bm = CommonMethord.base64ToBitmap(bank.LINK_IMG_DATA);
				ItemImage1.setImageBitmap(bm);
			}

			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,
							WebViewActivity.class);
					intent.putExtra("Url", bank.LINK_URL);
					startActivity(intent);
				}
			});

			return convertView;
		}
	}
	
 
	private void setLinkGridView2() {
		  
		LinkGridViewAdapter adapter2 = new LinkGridViewAdapter(getApplicationContext(),_OtherLinkList2);
		grid2.setAdapter(adapter2);
		
	}
	private void setLinkGridView3() {
		  
		LinkGridViewAdapter adapter3 = new LinkGridViewAdapter(getApplicationContext(),_OtherLinkList3);
		grid3.setAdapter(adapter3);
		
	}
	 
	final Runnable UpdateGrid2 = new Runnable() {
		public void run() {
 
			setLinkGridView2(); 
		}
	};
	final Runnable UpdateGrid3 = new Runnable() {
		public void run() {
 
			setLinkGridView3(); 
		}
	};
	
 
	 
	private void setGrid2() {

		new Thread() {
			public void run() {
				_OtherLinkList2 = WebServiceManager.getLinks("BanKaGongNeng");
				Handler.post(UpdateGrid2);
			}

		}.start();

	}
	
	private void setGrid3() {

		new Thread() {
			public void run() {
				_OtherLinkList3 = WebServiceManager.getLinks("HuiYuanXueXi");
				Handler.post(UpdateGrid3);
			}

		}.start();

	}
	
	
	
	
	
	
}
