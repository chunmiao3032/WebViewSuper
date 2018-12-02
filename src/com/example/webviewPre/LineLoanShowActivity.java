package com.example.webviewPre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.basic.CardResult;
import com.basic.LineLoanResult;
import com.basic.LoanResult;
import com.common.CommonMethord;
import com.common.Global;
import com.common.MyGridView;
import com.example.webviewPre.R;
import com.example.webviewPre.LoanShowActivity.GridViewAdapter;
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

public class LineLoanShowActivity extends Activity {
//	private ListView listView;
//	private int visibleLastIndex = 0; // 最后的可视项索引
//	private int visibleItemCount; // 当前窗口可见项总数
//	private LineLoanListViewAdapter adapter;
//	private View loadMoreView;
//	private TextView loadMoreButton;
	private Handler handler = new Handler();

//	TextView tvRowCount;

	TextView headTitle;
	ImageView ivBack;
	Button btMenu1, btMenu2;// ,btMenu3;
	
	List<LineLoanResult> _LoanResultList;
	MyGridView gridView;

	// -----------------------------------------------------
	// -------------------------全局变量,查询条件-----------------------
	// -----------------------------------------------------
	String _LoanType = "";

	Boolean isRun = false;
	
	String _Dxscydk = "";

	// -----------------------------------------------------
	// -----------------------------------------------------
	// --------------加载进度显示--------------------------------
	private ProgressDialog loginDialog;
	Handler cwjHandler = null;
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
		setContentView(R.layout.activity_line_loan_show);

		//Intent intent = getIntent();

		//cwjHandler = new Handler();
		initHeader("快速P2P贷款");
		
		Intent intent = getIntent();
		String dxscydk = intent.getStringExtra("loantype");
		if(dxscydk != null && dxscydk.trim().length() > 0)
		{
			_LoanType = dxscydk;
			//btMenu1.setVisibility(View.GONE);
			btMenu1.setText(_LoanType);
		}
		
		Handler = new Handler();
		gridView = (MyGridView) findViewById(R.id.grid); 		
		setData(); 
	}

	final String[] menu1 = new String[] { "全部", "车辆抵押贷款", "个人信用贷款", "企业信用贷款",
			"房屋抵押贷款","大学生贷款", "其他" };

	public void initHeader(String title) {
		headTitle = (TextView) findViewById(R.id.headTitle);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		btMenu1 = (Button) findViewById(R.id.btMenu1);
		btMenu2 = (Button) findViewById(R.id.btMenu2);
		// btMenu3 = (Button) findViewById(R.id.btMenu3);
		btMenu1.setText("贷款类型");
		btMenu2.setVisibility(View.GONE);
		;

		headTitle.setText(title);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
		btMenu1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(LineLoanShowActivity.this)
						.setTitle("请选择")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setSingleChoiceItems(menu1, 0,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										if (which != 0) {
											_LoanType = menu1[which];
											btMenu1.setText(_LoanType);
										} else {
											_LoanType = "";
											btMenu1.setText("全部");
										}
//										initAdapter();
//										setListAdapter(adapter);
									    setData();
										
										dialog.dismiss();
									}
								}).setNegativeButton("取消", null).show();
			}
		});

	}
  
	// **********************************************************************************************************

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
				//_LoanResultList = WebServiceManager.getLoan("1000");
				_LoanResultList = WebServiceManager.getLineLoan(Global.City,
						_LoanType, "1000");
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
		List<LineLoanResult> list;

		public GridViewAdapter(Context _context, List<LineLoanResult> _list) {
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

			final LineLoanResult bank = list.get(position);

			TextView tvTitle1 = (TextView) convertView
					.findViewById(R.id.tvTitle1);
			TextView tvDesc1 = (TextView) convertView
					.findViewById(R.id.tvDesc1);

			ImageButton ItemImage1 = (ImageButton) convertView
					.findViewById(R.id.ItemImage1);

			tvTitle1.setText(bank.LINE_LOAN_NAME);
			tvDesc1.setText(bank.LINE_LOAN_DESC);

			if (bank.LINE_LOAN_IMG_DATA != null) {
				Bitmap bm = CommonMethord.base64ToBitmap(bank.LINE_LOAN_IMG_DATA);
				ItemImage1.setImageBitmap(bm);
			}

			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LineLoanShowActivity.this,
							WebViewActivity.class);
					intent.putExtra("Url", bank.LINE_LOAN_URL);
					startActivity(intent);
				}
			});

			return convertView;
		}
	}

}