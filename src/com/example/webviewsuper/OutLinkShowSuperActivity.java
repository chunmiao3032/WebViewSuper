package com.example.webviewsuper;

import java.util.List;

import com.basic.BankResultOK;
import com.basic.OtherLinkResult;
import com.common.CommonMethord; 
import com.common.MyGridView;
import com.common.SquareImageView;
import com.example.webviewPre.LoanShowActivity;
import com.example.webviewPre.R;
import com.example.webviewPre.MainActivity;
import com.example.webviewPre.WebViewActivity;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OutLinkShowSuperActivity extends Activity{
	
	MyGridView gridView;
	List<OtherLinkResult> _OtherLinkList;
	
	TextView headTitle;
	ImageView ivBack;
	
	String _LinkName;
	String _HeaderName;
	
	private ProgressDialog loginDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去掉窗口标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_out_link_show);
		
		Intent intent = getIntent();
		String LinkName = intent.getStringExtra("LinkName");
		_LinkName = LinkName; 
		String HeaderName = intent.getStringExtra("HeaderName");
		_HeaderName = HeaderName; 
		
		initHeader(HeaderName);
		Handler = new Handler();
		gridView = (MyGridView) findViewById(R.id.grid);
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
	
	private void setData() { 
		
		loadProgressBar();
		new Thread() {
			public void run() { 
				_OtherLinkList = WebServiceManager.getLinks(_LinkName);
				Handler.post(mUpdateResults);
			}

		}.start();

	}
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
	
	private void setGridView() {
 
		LinkGridViewAdapter adapter2 = new LinkGridViewAdapter(getApplicationContext(),_OtherLinkList);
		gridView.setAdapter(adapter2); 
	}

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
			convertView = layoutInflater.inflate(R.layout.cardapplyquery_item,null);   //main_link_item1

			final OtherLinkResult bank = list.get(position);
			
			TextView tvTitle1 = (TextView) convertView.findViewById(R.id.tvTitle1);
			TextView tvDesc1 = (TextView) convertView.findViewById(R.id.tvDesc1); 
			  
			ImageButton ItemImage1 = (ImageButton) convertView
					.findViewById(R.id.ItemImage1);
 
			tvTitle1.setText(bank.LINK_DESC);
			tvDesc1.setText(bank.LINK_NAME);
			
			if(bank.LINK_IMG_DATA != null)
			{
				Bitmap bm = CommonMethord.base64ToBitmap(bank.LINK_IMG_DATA);
				ItemImage1.setImageBitmap(bm);
			}
			
			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(OutLinkShowSuperActivity.this, WebViewActivity.class); 
					intent.putExtra("Url", bank.LINK_URL);
					startActivity(intent);
				}
			}); 

			return convertView;
		}
	} 
	
	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		loginDialog.setMessage("正在加载，请稍等...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}

}
