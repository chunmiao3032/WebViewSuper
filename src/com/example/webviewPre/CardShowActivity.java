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
	private int visibleLastIndex = 0; // ���Ŀ���������
	private int visibleItemCount; // ��ǰ���ڿɼ�������
	private ListViewAdapter adapter;
	private View loadMoreView;
	private TextView loadMoreButton;
	private Handler handler = new Handler();

	TextView tvRowCount;

	TextView headTitle;
	ImageView ivBack;
	Button btMenu1, btMenu2;// ,btMenu3;

	// -----------------------------------------------------
	// -------------------------ȫ�ֱ���,��ѯ����-----------------------
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
	// --------------���ؽ�����ʾ--------------------------------
	private ProgressDialog loginDialog;
	Handler cwjHandler = null;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {

			loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}
			setListAdapter(adapter); // �Զ�Ϊid��list��ListView����������
			int count = adapter.getCount();
			tvRowCount.setText("Ϊ���ҵ���ؽ��Լ" + String.valueOf(count) + "����Ϣ");
		}
	};

	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		loginDialog.setMessage("���ڼ��أ����Ե�...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ�����ڱ���
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
		initHeader("���ٰ쿨");
		helper = new DatabaseHelper(this);

		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
		loadMoreButton = (TextView) loadMoreView
				.findViewById(R.id.loadMoreButton);

		tvRowCount = (TextView) findViewById(R.id.tvRowCount);

		listView = getListView(); // ��ȡid��list��ListView

		listView.addFooterView(loadMoreView); // �����б�ײ���ͼ

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (isRun) {
					return;
				}

				int itemsLastIndex = adapter.getCount() - 1; // ���ݼ����һ�������
				final int lastIndex = itemsLastIndex + 1; // ���ϵײ���loadMoreView��
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& visibleLastIndex == lastIndex) {

					isRun = true;
					// ������Զ�����,��������������첽�������ݵĴ���
					loadMoreButton.setText("���Ժ���������������..."); // ���ð�ť����loading
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {

							loadData(lastIndex);

							adapter.notifyDataSetChanged(); // ���ݼ��仯��,֪ͨadapter
							listView.setSelection(visibleLastIndex
									- visibleItemCount + 1); // ����ѡ����

							loadMoreButton.setText("�����϶����ظ���"); // �ָ���ť����
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
				// ��¼�û�ѡ��Ŀ���Ϣ
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

	String[] menu1 = new String[] { "ȫ��", "��������", "��ͨ����", "�ַ�����", "ũҵ����",
			"�㷢����", "�������", "��������", "ƽ������", "��ҵ����", "��������" };
	final String[] menu2 = new String[] { "ȫ��", "20-25��", "26-35��", "36-45��",
			"46������" };

	public void initHeader(String title) {
		headTitle = (TextView) findViewById(R.id.headTitle);
		ivBack = (ImageView) findViewById(R.id.ivBack);
//		btMenu1 = (Button) findViewById(R.id.btMenu1);
//		btMenu2 = (Button) findViewById(R.id.btMenu2);
//		// btMenu3 = (Button) findViewById(R.id.btMenu3);
//		btMenu1.setText("����");
//		btMenu2.setText("�����");

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
//						.setTitle("��ѡ��")
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
////											btMenu1.setText("ȫ��");
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
//								}).setNegativeButton("ȡ��", null).show();
//			}
//		});
//
//		btMenu2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				new AlertDialog.Builder(CardShowActivity.this)
//						.setTitle("��ѡ��")
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
////											btMenu2.setText("ȫ��");
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
//								}).setNegativeButton("ȡ��", null).show();
//			}
//		});

		// btMenu3.setVisibility(View.GONE);
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		List<CardResult> items = new ArrayList<CardResult>();

		items = WebServiceManager.getCards(Global.City, _Bank, _Level,
				_CardOrg, _Ages, "0", _IsNewUser, _IsFastGet, _IsHighQuotar);

		adapter = new ListViewAdapter(this, items, helper);
	}

	/**
	 * ģ���������
	 */
	private void loadData(int lastIndex) {
		List<CardResult> items = WebServiceManager.getCards(Global.City, _Bank,
				_Level, _CardOrg, _Ages, String.valueOf(lastIndex), _IsNewUser,
				_IsFastGet, _IsHighQuotar);
		for (CardResult item : items) {
			adapter.addItem(item);
		}

		int count = adapter.getCount();
		tvRowCount.setText("Ϊ���ҵ���ؽ��Լ" + String.valueOf(count) + "�����ÿ�");
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