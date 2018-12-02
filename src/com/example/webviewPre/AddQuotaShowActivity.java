package com.example.webviewPre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.basic.AddQuotaResult;
import com.basic.CardResult;
import com.basic.LineLoanResult;
import com.basic.LoanResult;
import com.common.CommonMethord;
import com.common.Global;
import com.example.webviewPre.R;
import com.manager.DbManager_Data;
import com.webservice.WebServiceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class AddQuotaShowActivity extends ListActivity {
	private ListView listView;
	private int visibleLastIndex = 0; // ���Ŀ���������
	private int visibleItemCount; // ��ǰ���ڿɼ�������
	private AddQuotaListViewAdapter adapter;
	private View loadMoreView;
	private TextView loadMoreButton;
	private Handler handler = new Handler();

	TextView tvRowCount;

	TextView headTitle;
	ImageView ivBack;
	Button btMenu1, btMenu2;//,btMenu3;

	// -----------------------------------------------------
	// -------------------------ȫ�ֱ���,��ѯ����-----------------------
	// -----------------------------------------------------
	String _LoanType = ""; 
	
	Boolean isRun = false;

	// -----------------------------------------------------
	// -----------------------------------------------------
	//--------------���ؽ�����ʾ--------------------------------
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
		setContentView(R.layout.activity_add_quota_show);
		 
		cwjHandler = new Handler();
		initHeader("һ�����");

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
				AddQuotaResult card = (AddQuotaResult)adapter.getItem(arg2);
				Intent intent = new Intent(AddQuotaShowActivity.this,WebViewActivity.class);
				intent.putExtra("url",card.INCREASE_QUOTA_URL);
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

	final String[] menu1 = new String[] {"ȫ��","������Ѻ����","�������ô���","��ҵ���ô���","���ݵ�Ѻ����","����" };
	 
	public void initHeader(String title) {
		headTitle = (TextView) findViewById(R.id.headTitle);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		headTitle.setText(title);
		 
//		btMenu1 = (Button) findViewById(R.id.btMenu1);
//		btMenu2 = (Button) findViewById(R.id.btMenu2);
		 
//		btMenu1.setVisibility(View.GONE);
//		btMenu2.setVisibility(View.GONE);
		 
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
//		btMenu1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) { 
//				new AlertDialog.Builder(LoanShowActivity.this)
//				.setTitle("��ѡ��")
//				.setIcon(android.R.drawable.ic_dialog_info)
//				.setSingleChoiceItems(menu1, 0,
//						new DialogInterface.OnClickListener() {
//
//							public void onClick(DialogInterface dialog,int which)
//							{
//								if(which != 0)
//								{
//									_LoanType = menu1[which]; 
//									btMenu1.setText(_LoanType);
//								}
//								else
//								{
//									_LoanType = "";
//									btMenu1.setText("ȫ��");
//								}
//								initAdapter(); 
//								setListAdapter(adapter);
//								
//								dialog.dismiss();
//							}
//						}).setNegativeButton("ȡ��", null) 
//						.show();
//			}
//		});

	 
	
		 
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() { 

		List<AddQuotaResult> items = new ArrayList<AddQuotaResult>();

		items = WebServiceManager.getAddQuota("0");

		adapter = new AddQuotaListViewAdapter(this, items);
		
	}

	/**
	 * ģ���������
	 */
	private void loadData(int lastIndex) {
		List<AddQuotaResult> items = WebServiceManager.getAddQuota(String.valueOf(lastIndex));
		for (AddQuotaResult item : items) {
			adapter.addItem(item);
		}

		int count = adapter.getCount();
		tvRowCount.setText("Ϊ���ҵ���ؽ��Լ" + String.valueOf(count) + "����Ϣ");
		isRun = false;
	} 
	
	
	public class AddQuotaListViewAdapter extends BaseAdapter {
		 
		private List<AddQuotaResult> items;
		private LayoutInflater inflater;

		public AddQuotaListViewAdapter(Context context, List<AddQuotaResult> items) {
			this.items = items;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				view = inflater.inflate(R.layout.add_quota_list_item, null);
			}
			final AddQuotaResult card = items.get(position);
			//ivImg  tvApplyUserCount tvCardName tvTitleDesc  ivApply
			ImageView ivImg = (ImageView) view.findViewById(R.id.ivImg);
	 
			TextView tvCardName = (TextView) view.findViewById(R.id.tvCardName);
			TextView tvTitleDesc = (TextView) view.findViewById(R.id.tvTitleDesc);
			ImageView ivApply = (ImageView) view.findViewById(R.id.ivApply);
						
			Bitmap bitmap = CommonMethord.base64ToBitmap(card.INCREASE_QUOTA_IMG_DATA);
			ivImg.setImageBitmap(bitmap);
			 
			tvCardName.setText(card.INCREASE_QUOTA_NAME);
			tvTitleDesc.setText(card.INCREASE_QUOTA_DESC);
			 
			
			ivApply.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 
					Intent intent = new Intent(v.getContext(),WebViewActivity.class);
					intent.putExtra("url",card.INCREASE_QUOTA_URL);
					v.getContext().startActivity(intent);
					
				}
			});
			
			
			return view;
		}

		/**
		 * ����б���
		 * 
		 * @param item
		 */
		public void addItem(AddQuotaResult item) {
			items.add(item);
		}
	}

}