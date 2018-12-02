package com.example.webviewPre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.basic.CardDetailsResult;
import com.basic.CardResult;
import com.common.CommonMethord; 
import com.example.webviewPre.R;
import com.webservice.WebServiceManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CardShow_DtlInfoActivity extends Activity {
	CardResult _Card;
	List<CardDetailsResult> cardDtllist;

	TextView headTitle;
	ImageView ivBack;

	ImageView ivCardImg;
	TextView tvCardName, tvApplyUserCount, tvTitleDesc1, tvTitleDesc2,
			tvTitleDesc3;
	TextView tvDesc1, tvDesc2, tvDesc3, tvDesc4, tvDesc5, tvDesc6;
	Button bt1, bt2, bt3, btApply;

	ScrollView sv1;
	ListView lv2;
	TextView tv3;
	SimpleAdapter mSimpleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去掉窗口标题
				requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cardshow_dtlinfo);

		Handler = new Handler();
		Intent intent = getIntent();
		_Card = (CardResult) intent.getSerializableExtra("card");

		initCtl();
		initHeader("信用卡详情");

		initText(_Card);

		initLinsener();
	}

	// *******************************************************************
	private void initLinsener() {
		bt1.setOnClickListener(bt1_OnClick);
		bt2.setOnClickListener(bt2_OnClick);
		bt3.setOnClickListener(bt3_OnClick);
		btApply.setOnClickListener(btApply_OnClick);

	}

	OnClickListener bt1_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sv1.setVisibility(View.VISIBLE);
			lv2.setVisibility(View.GONE);
			tv3.setVisibility(View.GONE);
		}
	};
	OnClickListener bt2_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sv1.setVisibility(View.GONE);
			lv2.setVisibility(View.VISIBLE);
			tv3.setVisibility(View.GONE);
			getCardDetail(_Card.ID);
		}
	};

	private void getCardDetail(final String cardID) {
		
		new Thread() {
			public void run() {
				cardDtllist = WebServiceManager.getCardDetails(cardID);
				Handler.post(mUpdateResults);
			}

		}.start();
	}

	Handler Handler = null;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			initLv();
		}
	};
	
	private void initLv()
	{
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		int i = 1;
		for (CardDetailsResult task : cardDtllist) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("tvDescType", task.DESC_TYPE);
			map.put("tvCardDesc1", task.CARD_DESC1);
			map.put("tvCardDesc2", task.CARD_DESC2); 
			listItem.add(map);
			i++;
		}
		mSimpleAdapter = new SimpleAdapter( 
				this,
				listItem,// 需要绑定的数据
				R.layout.cardshow_dtl_info_item, 
				new String[] { 
						"tvDescType",
						"tvCardDesc1",
						"tvCardDesc2" },
				new int[] { 
						R.id.tvDescType,
						R.id.tvCardDesc1, 
						R.id.tvCardDesc2 });
		lv2.setAdapter(mSimpleAdapter);
	}

	OnClickListener bt3_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			sv1.setVisibility(View.GONE);
			lv2.setVisibility(View.GONE);
			tv3.setVisibility(View.VISIBLE);
		}
	};
	OnClickListener btApply_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(),
					WebViewActivity.class);
			intent.putExtra("url", _Card.CARD_URL);
			startActivity(intent);
		}
	};

	// *******************************************************************

	private void initCtl() {

		ivCardImg = (ImageView) findViewById(R.id.ivCardImg);
		tvCardName = (TextView) findViewById(R.id.tvCardName);
		tvApplyUserCount = (TextView) findViewById(R.id.tvApplyUserCount);
		tvTitleDesc1 = (TextView) findViewById(R.id.tvTitleDesc1);
		tvTitleDesc2 = (TextView) findViewById(R.id.tvTitleDesc2);
		tvTitleDesc3 = (TextView) findViewById(R.id.tvTitleDesc3);
		tvDesc1 = (TextView) findViewById(R.id.tvDesc1);
		tvDesc2 = (TextView) findViewById(R.id.tvDesc2);
		tvDesc3 = (TextView) findViewById(R.id.tvDesc3);
		tvDesc4 = (TextView) findViewById(R.id.tvDesc4);
		tvDesc5 = (TextView) findViewById(R.id.tvDesc5);
		tvDesc6 = (TextView) findViewById(R.id.tvDesc6);

		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		btApply = (Button) findViewById(R.id.btApply);

		sv1 = (ScrollView) findViewById(R.id.sv1);
		lv2 = (ListView) findViewById(R.id.lv2);
		tv3 = (TextView) findViewById(R.id.tv3);
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

	private void initText(CardResult card) {
		if (card.CARD_IMG_DATA != null) {
			Bitmap bm = CommonMethord.base64ToBitmap(card.CARD_IMG_DATA);
			ivCardImg.setImageBitmap(bm);
		}
		tvCardName = (TextView) findViewById(R.id.tvCardName);
		tvApplyUserCount = (TextView) findViewById(R.id.tvApplyUserCount);
		tvTitleDesc1 = (TextView) findViewById(R.id.tvTitleDesc1);
		tvTitleDesc2 = (TextView) findViewById(R.id.tvTitleDesc2);
		tvTitleDesc3 = (TextView) findViewById(R.id.tvTitleDesc3);
		tvDesc1 = (TextView) findViewById(R.id.tvDesc1);
		tvDesc2 = (TextView) findViewById(R.id.tvDesc2);
		tvDesc3 = (TextView) findViewById(R.id.tvDesc3);
		tvDesc4 = (TextView) findViewById(R.id.tvDesc4);
		tvDesc5 = (TextView) findViewById(R.id.tvDesc5);
		tvDesc6 = (TextView) findViewById(R.id.tvDesc6);

		tvCardName.setText(card.CARD_NAME);
		tvApplyUserCount.setText(card.APPLY_USER_COUNT);
		tvTitleDesc1.setText(card.CARD_TITLE_DESC1);
		tvTitleDesc2.setText(card.CARD_TITLE_DESC2);
		tvTitleDesc3.setText(card.CARD_TITLE_DESC3);
		tvDesc1.setText(card.CARD_DESC1);
		tvDesc2.setText(card.CARD_DESC2);
		tvDesc3.setText(card.CARD_DESC3);
		tvDesc4.setText(card.CARD_DESC4);
		tvDesc5.setText(card.CARD_DESC5);
		tvDesc6.setText(card.CARD_DESC6);

		tv3.setText(card.CARD_DESC7);

	}

}
