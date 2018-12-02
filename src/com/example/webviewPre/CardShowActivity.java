package com.example.webviewPre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.basic.CardResult;
import com.common.Global;
import com.droid.DatabaseHelper;
import com.example.webviewPre.R;
import com.manager.DbManager_Data;
import com.webservice.WebServiceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class CardShowActivity extends ListActivity {
	private ListView listView;
	private int visibleLastIndex = 0; // 最后的可视项索引
	private int visibleItemCount; // 当前窗口可见项总数
	private ListViewAdapter adapter;
	private View loadMoreView;
	private TextView loadMoreButton;
	private Handler handler = new Handler();

	TextView tvRowCount;

	TextView headTitle;
	ImageView ivBack;
	Button btMenu1, btMenu2;// ,btMenu3;

	// -----------------------------------------------------
	// -------------------------全局变量,查询条件-----------------------
	// -----------------------------------------------------
	String _Bank = "";
	String _Level = "";
	String _CardOrg = "";
	String _Ages = "";

	String _IsNewUser = "";
	String _IsFastGet = "";
	String _IsHighQuotar = "";

	Boolean isRun = false;

	private DatabaseHelper helper;

	// -----------------------------------------------------
	// -----------------------------------------------------
	// --------------加载进度显示--------------------------------
	private ProgressDialog loginDialog;
	Handler cwjHandler = null;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {

			loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}
			setListAdapter(adapter); // 自动为id是list的ListView设置适配器
			int count = adapter.getCount();
			tvRowCount.setText("为您找到相关结果约" + String.valueOf(count) + "条信息");
		}
	};

	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		loginDialog.setMessage("正在加载，请稍等...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉窗口标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cardshow);

		Intent intent = getIntent();
		String bank = intent.getStringExtra("bank");
		if (bank != null) {
			_Bank = bank;
		}
		String IsNewUser = intent.getStringExtra("IsNewUser");
		if (IsNewUser != null) {
			_IsNewUser = IsNewUser;
		}
		String IsFastGet = intent.getStringExtra("IsFastGet");
		if (IsFastGet != null) {
			_IsFastGet = IsFastGet;
		}
		String IsHighQuotar = intent.getStringExtra("IsHighQuotar");
		if (IsHighQuotar != null) {
			_IsHighQuotar = IsHighQuotar;
		}

		cwjHandler = new Handler();
		initHeader("快速办卡");
		helper = new DatabaseHelper(this);

		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		loadMoreButton = (TextView) loadMoreView
				.findViewById(R.id.loadMoreButton);

		tvRowCount = (TextView) findViewById(R.id.tvRowCount);

		listView = getListView(); // 获取id是list的ListView

		listView.addFooterView(loadMoreView); // 设置列表底部视图

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (isRun) {
					return;
				}

				int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
				final int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& visibleLastIndex == lastIndex) {

					isRun = true;
					// 如果是自动加载,可以在这里放置异步加载数据的代码
					loadMoreButton.setText("请稍候，正在玩命加载中..."); // 设置按钮文字loading
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {

							loadData(lastIndex);

							adapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
							listView.setSelection(visibleLastIndex
									- visibleItemCount + 1); // 设置选中项

							loadMoreButton.setText("向上拖动加载更多"); // 恢复按钮文字
						}
					}, 2000);

				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				visibleItemCount = visibleItemCount;
				visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CardResult card = (CardResult) adapter.getItem(arg2);
				// 记录用户选择的卡信息
				InsertCard(card);
				Intent intent = new Intent(CardShowActivity.this,
						WebViewActivity.class);
				intent.putExtra("url", card.CARD_URL);
				startActivity(intent);
			}

		});

		loadProgressBar();
		new Thread() {
			public void run() {
				initAdapter();
				cwjHandler.post(mUpdateResults);
			}

		}.start();
	}

	String[] menu1 = new String[] { "全部", "中信银行", "交通银行", "浦发银行", "农业银行",
			"广发银行", "光大银行", "招商银行", "平安银行", "兴业银行", "花旗银行" };
	final String[] menu2 = new String[] { "全部", "20-25岁", "26-35岁", "36-45岁",
			"46岁以上" };

	public void initHeader(String title) {
		headTitle = (TextView) findViewById(R.id.headTitle);
		ivBack = (ImageView) findViewById(R.id.ivBack);
//		btMenu1 = (Button) findViewById(R.id.btMenu1);
//		btMenu2 = (Button) findViewById(R.id.btMenu2);
//		// btMenu3 = (Button) findViewById(R.id.btMenu3);
//		btMenu1.setText("银行");
//		btMenu2.setText("年龄段");

//		if (Global.BankList != null && Global.BankList.size() > 0) {
//			menu1 = new String[Global.BankList.size()];
//			for (int i = 0; i < Global.BankList.size(); i++) {
//				menu1[i] = Global.BankList.get(i).BANK_NAME;
//			}
//		}

		headTitle.setText(title);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
//		btMenu1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				new AlertDialog.Builder(CardShowActivity.this)
//						.setTitle("请选择")
//						.setIcon(android.R.drawable.ic_dialog_info)
//						.setSingleChoiceItems(menu1, 0,
//								new DialogInterface.OnClickListener() {
//
//									public void onClick(DialogInterface dialog,
//											int which) {
////										if (which != 0) {
//											_Bank = menu1[which];
//											btMenu1.setText(_Bank);
////										} else {
////											_Bank = "";
////											btMenu1.setText("全部");
////										}
//										new Thread() {
//											public void run() {
//												initAdapter();
//												cwjHandler.post(mUpdateResults);
//											}
//
//										}.start();
////										initAdapter();
////										setListAdapter(adapter);
//
//										dialog.dismiss();
//									}
//								}).setNegativeButton("取消", null).show();
//			}
//		});
//
//		btMenu2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				new AlertDialog.Builder(CardShowActivity.this)
//						.setTitle("请选择")
//						.setIcon(android.R.drawable.ic_dialog_info)
//						.setSingleChoiceItems(menu2, 0,
//								new DialogInterface.OnClickListener() {
//
//									public void onClick(DialogInterface dialog,
//											int which) {
////										if (which != 0) {
//											_Ages = menu2[which];
//											btMenu2.setText(_Ages);
////										} else {
////											_Ages = "";
////											btMenu2.setText("全部");
////										}
//										new Thread() {
//											public void run() {
//												initAdapter();
//												cwjHandler.post(mUpdateResults);
//											}
//
//										}.start();
////										initAdapter();
////										setListAdapter(adapter);
//
//										dialog.dismiss();
//									}
//								}).setNegativeButton("取消", null).show();
//			}
//		});

		// btMenu3.setVisibility(View.GONE);
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		List<CardResult> items = new ArrayList<CardResult>();

		items = WebServiceManager.getCards(Global.City, _Bank, _Level,
				_CardOrg, _Ages, "0", _IsNewUser, _IsFastGet, _IsHighQuotar);

		adapter = new ListViewAdapter(this, items, helper);
	}

	/**
	 * 模拟加载数据
	 */
	private void loadData(int lastIndex) {
		List<CardResult> items = WebServiceManager.getCards(Global.City, _Bank,
				_Level, _CardOrg, _Ages, String.valueOf(lastIndex), _IsNewUser,
				_IsFastGet, _IsHighQuotar);
		for (CardResult item : items) {
			adapter.addItem(item);
		}

		int count = adapter.getCount();
		tvRowCount.setText("为您找到相关结果约" + String.valueOf(count) + "张信用卡");
		isRun = false;
	}

	public void InsertCard(CardResult card) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from MD_CARD_LOG "
				+ "where USER_NAME = '" + Global.UserCode
				+ "' and BANK_NAME ='" + card.BANK_ID + "' and CARD_NAME = '"
				+ card.CARD_NAME + "'", null);
		if (cursor.getCount() > 0) { //
			return;
		}
		db.execSQL("insert into MD_CARD_LOG(USER_NAME,BANK_NAME,CARD_NAME,FLAG,DATE) "
				+ "values('"
				+ Global.UserCode
				+ "','"
				+ card.BANK_ID
				+ "','"
				+ card.CARD_NAME
				+ "','"
				+ "0','"
				+ System.currentTimeMillis()
				+ "')");
		db.close();
	}

}