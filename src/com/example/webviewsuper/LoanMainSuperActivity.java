package com.example.webviewsuper;

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
import com.example.webviewPre.LineLoanShowActivity;
import com.example.webviewPre.WebViewActivity;
import com.example.webviewPre.LoanShowActivity.GridViewAdapter;
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

public class LoanMainSuperActivity extends Activity {
	// private ListView listView;
	// private int visibleLastIndex = 0; // 最后的可视项索引
	// private int visibleItemCount; // 当前窗口可见项总数
	// private LineLoanListViewAdapter adapter;
	// private View loadMoreView;
	// private TextView loadMoreButton;
	private Handler handler = new Handler();

	// TextView tvRowCount;

	TextView headTitle;
	ImageView ivBack;
	Button btMenu1, btMenu2;// ,btMenu3;

	List<LineLoanResult> _LineLoanResultList;
	List<LoanResult> _LoanResultList;
	List<LineLoanResult> _LineLoanResultList_dxs;
	MyGridView gridView, gridView2, gridView3;

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
	Handler Handler = null;

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
		setContentView(R.layout.activity_loan_main_super);

		initHeader("贷款专区");

		Handler = new Handler();
		gridView = (MyGridView) findViewById(R.id.grid);// 信用贷款
		gridView2 = (MyGridView) findViewById(R.id.grid2);// p2p网贷
		gridView3 = (MyGridView) findViewById(R.id.grid3);// 大学生贷款

		setData();
		setData_p2p();
		setData_p2p_dxs();
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
	// 信用贷款
	// **********************************************************************************************************
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
			convertView = layoutInflater.inflate(R.layout.cardapplyquery_item,
					null);

			final LoanResult bank = list.get(position);

			TextView tvTitle1 = (TextView) convertView
					.findViewById(R.id.tvTitle1);
			TextView tvDesc1 = (TextView) convertView
					.findViewById(R.id.tvDesc1);

			ImageButton ItemImage1 = (ImageButton) convertView
					.findViewById(R.id.ItemImage1);

			tvTitle1.setText(bank.LOAN_NAME);
			tvDesc1.setText(bank.LOAN_DESC);

			if (bank.LOAN_IMG_DATA != null) {
				Bitmap bm = CommonMethord.base64ToBitmap(bank.LOAN_IMG_DATA);
				ItemImage1.setImageBitmap(bm);
			}

			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LoanMainSuperActivity.this,
							WebViewActivity.class);
					intent.putExtra("Url", bank.LOAN_URL);
					startActivity(intent);
				}
			});

			return convertView;
		}
	}

	// **********************************************************************************************************
	// P2P贷款
	// **********************************************************************************************************
	final Runnable mUpdateResults_p2p = new Runnable() {
		public void run() {
			/*loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}*/
			setGridView_p2p();
		}
	};

	private void setData_p2p() {
		// loadProgressBar();
		new Thread() {
			public void run() {
				// _LoanResultList = WebServiceManager.getLoan("1000");
				_LineLoanResultList = WebServiceManager.getLineLoan(
						Global.City, "个人信用贷款", "1000");
				Handler.post(mUpdateResults_p2p);
			}

		}.start();

	}

	private void setGridView_p2p() {
 
		GridViewAdapter_p2p adapter = new GridViewAdapter_p2p(
				getApplicationContext(), _LineLoanResultList);
		gridView2.setAdapter(adapter);
		 
	}

	public class GridViewAdapter_p2p extends BaseAdapter {
		Context context;
		List<LineLoanResult> list;

		public GridViewAdapter_p2p(Context _context, List<LineLoanResult> _list) {
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
				Bitmap bm = CommonMethord
						.base64ToBitmap(bank.LINE_LOAN_IMG_DATA);
				ItemImage1.setImageBitmap(bm);
			}

			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LoanMainSuperActivity.this,
							WebViewActivity.class);
					intent.putExtra("Url", bank.LINE_LOAN_URL);
					startActivity(intent);
				}
			});

			return convertView;
		}
	}

	// **********************************************************************************************************
	// 大学生贷款
	// **********************************************************************************************************
	final Runnable mUpdateResults_p2p_dxs = new Runnable() {
		public void run() {
//			loginDialog.cancel();
//			if (loginDialog != null && loginDialog.isShowing()) {
//				loginDialog.dismiss();
//			}
			setGridView_p2p_dxs();
		}
	};

	private void setData_p2p_dxs() {
		// loadProgressBar();
		new Thread() {
			public void run() {
				// _LoanResultList = WebServiceManager.getLoan("1000");
				_LineLoanResultList_dxs = WebServiceManager.getLineLoan(
						Global.City, "大学生贷款", "1000");
				Handler.post(mUpdateResults_p2p_dxs);
			}

		}.start();

	}

	private void setGridView_p2p_dxs() {
 
		GridViewAdapter_p2p adapter = new GridViewAdapter_p2p(
				getApplicationContext(), _LineLoanResultList_dxs);
		gridView3.setAdapter(adapter);
	}

}