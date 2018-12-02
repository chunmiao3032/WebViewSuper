package com.example.webviewsuper;

import java.util.ArrayList;
import java.util.List;

import com.basic.AddQuotaResult;
import com.common.CommonMethord;
import com.common.MyGridView;
import com.example.webviewPre.R;
import com.example.webviewPre.AddQuotaShow1Activity;
import com.example.webviewPre.WebViewActivity;
import com.example.webviewPre.AddQuotaShow1Activity.GridViewAdapter;
import com.webservice.WebServiceManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AddQuotaMainSuperActivity extends Activity {

	TextView headTitle;
	ImageView ivBack;

	MyGridView gridView, gridView2;
	List<AddQuotaResult> _AddQuotaList;
	Handler Handler = null;

	private ProgressDialog loginDialog;

	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		loginDialog.setMessage("正在加载，请稍等...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_quota_main_super);
		
		Handler = new Handler();
		initHeader("提额专区");
		
		gridView = (MyGridView) findViewById(R.id.grid);// 在线提额
		gridView2 = (MyGridView) findViewById(R.id.grid2);// 提额方法
	
		setData(); 
	}

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

	// **********************************************************************************************************
	// 在线提额
	// **********************************************************************************************************

	private void setData() {
		loadProgressBar();
		new Thread() {
			public void run() {
				_AddQuotaList = WebServiceManager.getAddQuota("1000");
				Handler.post(mUpdateResults);
			}
		}.start();

	}

	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}
			setGridView();
		}
	};

	private void setGridView() {
  
		List<AddQuotaResult> addQuotaList_online =new ArrayList<AddQuotaResult>();
		List<AddQuotaResult> addQuotaList_Methord =new ArrayList<AddQuotaResult>();
		
		for(AddQuotaResult item : _AddQuotaList)
		{
			if(item.INCREASE_QUOTA_URL != null && 
					item.INCREASE_QUOTA_URL != "" && 
				    !item.INCREASE_QUOTA_URL.contains("anyType") &&
					item.INCREASE_QUOTA_URL.trim().length() > 0)
			{
				addQuotaList_online.add(item);
			}
			if(item.INCREASE_QUOTA_METHORD_URL != null && 
					item.INCREASE_QUOTA_METHORD_URL != "" && 
					!item.INCREASE_QUOTA_METHORD_URL.contains("anyType") &&
					item.INCREASE_QUOTA_METHORD_URL.trim().length() > 0)
			{
				addQuotaList_Methord.add(item);
			}
		}
		
		GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(),
				addQuotaList_online);
		gridView.setAdapter(adapter);
		
		GridViewAdapter_Methord adapter1 = new GridViewAdapter_Methord(getApplicationContext(),
				addQuotaList_Methord);
		gridView2.setAdapter(adapter1);
	}

	public class GridViewAdapter extends BaseAdapter {
		Context context;
		List<AddQuotaResult> list;

		public GridViewAdapter(Context _context, List<AddQuotaResult> _list) {
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

			final AddQuotaResult bank = list.get(position);

			TextView tvTitle1 = (TextView) convertView
					.findViewById(R.id.tvTitle1);
//			TextView tvDesc1 = (TextView) convertView
//					.findViewById(R.id.tvDesc1);

			ImageButton ItemImage1 = (ImageButton) convertView
					.findViewById(R.id.ItemImage1);

			tvTitle1.setText(bank.INCREASE_QUOTA_NAME);
//			tvDesc1.setText(bank.INCREASE_QUOTA_DESC);

			if (bank.INCREASE_QUOTA_IMG_DATA != null) { 

				Bitmap bm = CommonMethord
						.base64ToBitmap(bank.INCREASE_QUOTA_IMG_DATA);
				Drawable drawable = CommonMethord.bitmap2Drawable(bm);

				ItemImage1.setBackground(drawable);
			}

			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AddQuotaMainSuperActivity.this,
							WebViewActivity.class);
					intent.putExtra("Url", bank.INCREASE_QUOTA_URL);
					startActivity(intent);
				}
			});

			return convertView;
		}
	}

	// **********************************************************************************************************
	// 提额方法
	// **********************************************************************************************************
  
	public class GridViewAdapter_Methord extends BaseAdapter {
		Context context;
		List<AddQuotaResult> list;

		public GridViewAdapter_Methord(Context _context, List<AddQuotaResult> _list) {
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

			final AddQuotaResult bank = list.get(position);

			TextView tvTitle1 = (TextView) convertView
					.findViewById(R.id.tvTitle1);
//			TextView tvDesc1 = (TextView) convertView
//					.findViewById(R.id.tvDesc1);

			ImageButton ItemImage1 = (ImageButton) convertView
					.findViewById(R.id.ItemImage1);

			tvTitle1.setText(bank.INCREASE_QUOTA_NAME);
//			tvDesc1.setText(bank.INCREASE_QUOTA_DESC);

			if (bank.INCREASE_QUOTA_IMG_DATA != null) {
				Bitmap bm = CommonMethord
						.base64ToBitmap(bank.INCREASE_QUOTA_IMG_DATA);
				Drawable drawable = CommonMethord.bitmap2Drawable(bm);

				ItemImage1.setBackground(drawable);
			}

			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AddQuotaMainSuperActivity.this,
							WebViewActivity.class);
					intent.putExtra("Url", bank.INCREASE_QUOTA_METHORD_URL);
					startActivity(intent);
				}
			});

			return convertView;
		}
	}

	//
}
