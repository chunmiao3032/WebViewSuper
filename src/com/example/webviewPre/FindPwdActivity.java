package com.example.webviewPre;

import java.util.List;

import com.basic.OtherLinkResult;
import com.common.CommonMethord;
import com.example.webviewPre.R;
import com.webservice.WebServiceManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

public class FindPwdActivity extends Activity{

	ImageView ivEwm;
	List<OtherLinkResult> list;
	Handler cwjHandler;
	private ProgressDialog loginDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_find_pwd);
		
		cwjHandler = new Handler();
		ivEwm = (ImageView)findViewById(R.id.ivEwm);
		
		loadProgressBar();
		new Thread() {
			public void run() {
			  list = WebServiceManager.getLinks("FindPwd");
			  cwjHandler.post(mUpdateResults);
			}

		}.start();
		
	}
	
	final Runnable mUpdateResults = new Runnable() {
		public void run() { 
			loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}
			 if(list != null && list.size() > 0)
			 {
				 OtherLinkResult link = list.get(0);
				 if(link != null && link.LINK_IMG_DATA != null)
				 {
					  Bitmap bm = CommonMethord.base64ToBitmap(link.LINK_IMG_DATA);
					  ivEwm.setImageBitmap(bm);
				 }
			 }
		}
	};
	
	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		loginDialog.setMessage("正在下载图片...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}
	
}
