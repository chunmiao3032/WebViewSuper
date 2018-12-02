package com.example.webviewPre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.basic.BankResultOK;
import com.basic.CardResult;
import com.basic.LineLoanResult;
import com.basic.LoanResult;
import com.common.CommonMethord;
import com.common.Global;
import com.common.MyGridView;
import com.example.webviewPre.R;
import com.example.webviewPre.CardQuotaShowActivity.GridViewAdapter;
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
import android.util.DisplayMetrics;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class LoanShowActivity extends Activity { 
	
	MyGridView gridView;
	List<LoanResult> _LoanResultList;
	
	//private ListView listView;
//	private int visibleLastIndex = 0; // 最后的可视项索引
//	private int visibleItemCount; // 当前窗口可见项总数
	//private LoanListViewAdapter adapter;
//	private View loadMoreView;
//	private TextView loadMoreButton;
	private Handler handler = new Handler();

//	TextView tvRowCount;

	TextView headTitle;
	ImageView ivBack;
//	Button btMenu1, btMenu2;//,btMenu3;

	// -----------------------------------------------------
	// -------------------------全局变量,查询条件-----------------------
	// -----------------------------------------------------
//	String _LoanType = ""; 
//	
//	Boolean isRun = false;

	// -----------------------------------------------------
	// -----------------------------------------------------
	private ProgressDialog loginDialog;
//	Handler cwjHandler = null;
//	final Runnable mUpdateResults = new Runnable() {
//		public void run() {
//
//			loginDialog.cancel();
//			if (loginDialog != null && loginDialog.isShowing()) {
//				loginDialog.dismiss();
//			}
//			setListAdapter(adapter); // 自动为id是list的ListView设置适配器
//			int count = adapter.getCount();
//			tvRowCount.setText("为您找到相关结果约" + String.valueOf(count) + "条信息");
//		}
//	};
	
	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this);
		loginDialog.setMessage("正在加载，请稍等...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉窗口标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_loan_show);
		 
//		cwjHandler = new Handler();
		initHeader("快速信用贷款");

//		loadMoreView = getLayoutInflater().inflate(R.layout.load_more, null);
//		loadMoreButton = (TextView) loadMoreView
//				.findViewById(R.id.loadMoreButton);

//		tvRowCount = (TextView) findViewById(R.id.tvRowCount);
 
		gridView = (MyGridView) findViewById(R.id.grid); 
		Handler = new Handler();
		
		setData();
		
//		listView = getListView(); // 获取id是list的ListView
//
//		listView.addFooterView(loadMoreView); // 设置列表底部视图
//
//		listView.setOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//				if (isRun) {
//					return;
//				}
//
//				int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
//				final int lastIndex = itemsLastIndex + 1; // 加上底部的loadMoreView项
//				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
//						&& visibleLastIndex == lastIndex) {
//
//					isRun = true;
//					// 如果是自动加载,可以在这里放置异步加载数据的代码
//					loadMoreButton.setText("请稍候，正在玩命加载中..."); // 设置按钮文字loading
//					handler.postDelayed(new Runnable() {
//						@Override
//						public void run() {
//
//							loadData(lastIndex);
//
//							adapter.notifyDataSetChanged(); // 数据集变化后,通知adapter
//							listView.setSelection(visibleLastIndex
//									- visibleItemCount + 1); // 设置选中项
//
//							loadMoreButton.setText("向上拖动加载更多"); // 恢复按钮文字
//						}
//					}, 2000);
//
//				}
//
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//
//				visibleItemCount = visibleItemCount;
//				visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
//			}
//		});
//	 
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) { 
//				LoanResult card = (LoanResult)adapter.getItem(arg2);
//				Intent intent = new Intent(LoanShowActivity.this,WebViewActivity.class);
//				intent.putExtra("url",card.LOAN_URL);
//				startActivity(intent);
//			}
//			
//		});
//	
//		loadProgressBar();
//		new Thread() {
//			public void run() {
//				initAdapter();
//				cwjHandler.post(mUpdateResults);
//			}
//
//		}.start();
	}

//	final String[] menu1 = new String[] {"全部","车辆抵押贷款","个人信用贷款","企业信用贷款","房屋抵押贷款","其他" };
//	 
	public void initHeader(String title) {
		headTitle = (TextView) findViewById(R.id.headTitle);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		headTitle.setText(title);
		 
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		}); 
		 
	}
	
//
//	/**
//	 * 初始化适配器
//	 */
//	private void initAdapter() { 
//
//		List<LoanResult> items = new ArrayList<LoanResult>();
//
//		items = WebServiceManager.getLoan("0");
//
//		adapter = new LoanListViewAdapter(this, items);
////		int count = adapter.getCount();
////		tvRowCount.setText("为您找到相关结果约" + String.valueOf(count) + "个贷款");
//	}
//
//	/**
//	 * 模拟加载数据
//	 */
//	private void loadData(int lastIndex) {
//		List<LoanResult> items = WebServiceManager.getLoan(String.valueOf(lastIndex));
//		for (LoanResult item : items) {
//			adapter.addItem(item);
//		}
//
//		int count = adapter.getCount();
//		tvRowCount.setText("为您找到相关结果约" + String.valueOf(count) + "个贷款");
//		isRun = false;
//	} 
	
	
	//public class LoanListViewAdapter extends BaseAdapter {
		 
//		private List<LoanResult> items;
//		private LayoutInflater inflater;
//
//		public LoanListViewAdapter(Context context, List<LoanResult> items) {
//			this.items = items;
//			inflater = (LayoutInflater) context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//
//		@Override
//		public int getCount() {
//			return items.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return items.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View view, ViewGroup parent) {
//			if (view == null) {
//				view = inflater.inflate(R.layout.loan_list_item, null);
//			}
//			final LoanResult card = items.get(position);
//			//ivImg  tvApplyUserCount tvCardName tvTitleDesc  ivApply
//			ImageView ivImg = (ImageView) view.findViewById(R.id.ivImg);
//	 
//			TextView tvCardName = (TextView) view.findViewById(R.id.tvCardName);
//			TextView tvTitleDesc = (TextView) view.findViewById(R.id.tvTitleDesc);
//			Button ivApply = (Button) view.findViewById(R.id.ivApply);
//						
//			Bitmap bitmap = CommonMethord.base64ToBitmap(card.LOAN_IMG_DATA);
//			ivImg.setImageBitmap(bitmap);
//			 
//			tvCardName.setText(card.LOAN_NAME);
//			tvTitleDesc.setText(card.LOAN_DESC);
//			 
//			
//			ivApply.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					 
//					Intent intent = new Intent(v.getContext(),WebViewActivity.class);
//					intent.putExtra("url",card.LOAN_URL);
//					v.getContext().startActivity(intent);
//					
//				}
//			});
//			
//			
//			return view;
//		}
//
//		/**
//		 * 添加列表项
//		 * 
//		 * @param item
//		 */
//		public void addItem(LoanResult item) {
//			items.add(item);
//		}
	//}

//**********************************************************************************************************	
	
	Handler Handler = null;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}
			setGridView();
		}
	};
	
	private void setData() { 
		loadProgressBar();
		new Thread() {
			public void run() {
				_LoanResultList = WebServiceManager.getLoan("1000");
				Handler.post(mUpdateResults);
			}

		}.start();

	}
	
	private void setGridView() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(),
				_LoanResultList);
		gridView.setAdapter(adapter);
	}

	public class GridViewAdapter extends BaseAdapter {
		Context context;
		List<LoanResult> list;

		public GridViewAdapter(Context _context, List<LoanResult> _list) {
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
			convertView = layoutInflater.inflate(R.layout.cardapplyquery_item, null);

			final LoanResult bank = list.get(position);
			
			TextView tvTitle1 = (TextView) convertView.findViewById(R.id.tvTitle1);
			TextView tvDesc1 = (TextView) convertView.findViewById(R.id.tvDesc1); 
			  
			ImageButton ItemImage1 = (ImageButton) convertView
					.findViewById(R.id.ItemImage1);
 
			tvTitle1.setText(bank.LOAN_NAME);
			tvDesc1.setText(bank.LOAN_DESC);
			
			if(bank.LOAN_IMG_DATA != null)
			{
				Bitmap bm = CommonMethord.base64ToBitmap(bank.LOAN_IMG_DATA);
				ItemImage1.setImageBitmap(bm);
			}
			
			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LoanShowActivity.this, WebViewActivity.class); 
					intent.putExtra("Url", bank.LOAN_URL);
					startActivity(intent);
				}
			}); 

			return convertView;
		}
	}
	
	
}