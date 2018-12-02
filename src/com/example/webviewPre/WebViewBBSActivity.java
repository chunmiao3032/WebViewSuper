package com.example.webviewPre;

import java.lang.reflect.InvocationTargetException;

import com.common.Global;
import com.common.UserInfo;
import com.example.webviewPre.R;
import com.webservice.WebServiceManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings.PluginState;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WebViewBBSActivity extends Activity {

	private WebView webview;
	TextView headTitle;
	ImageView ivBack;
	Handler Handler = null;
	String Url = "http://fuwu.lcsxjw.com";//"http://fuwu.kashang8.com";//"http://bbs.kashang8.com/";// 社区网址
	
	@Override
	protected void onRestart() {
		super.onRestart();

		UserInfo userinfo = (UserInfo) getApplicationContext();
		String UserCode = userinfo.getUserCode();
		if (UserCode == null || UserCode == "" || UserCode.length() == 0) {
			finish();
		} else {
			setData(Url);
		}

	}
	
	@Override  
	protected void onPause ()  
	{   
		webview.reload();
	    super.onPause (); 
	}  

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉窗口标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview_bbs);

		Handler = new Handler();
		
		initHeader("社区");

		

		webview = (WebView) findViewById(R.id.webview);
		
		UserInfo userinfo = (UserInfo) getApplicationContext();
		String UserCode = userinfo.getUserCode();

		if (UserCode == null || UserCode == "" || UserCode.length() == 0) {
			new AlertDialog.Builder(this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
					.setTitle("提示")
					.setMessage("你还没有登录, 请登录后查看！")
					.setCancelable(false)
					.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							return (keyCode == KeyEvent.KEYCODE_SEARCH);
						}
					})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									Intent intent = new Intent(
											WebViewBBSActivity.this,
											LoginActivity.class);
									startActivity(intent);
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									finish();
								}
							}).show().setCanceledOnTouchOutside(false);
		} else {
			 setData(Url);
		}
		
	
		
	}

	private void LoadUrl(String Url) { 
		webview.loadUrl(Url);// "http://cn.bing.com"  
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);// 设置WebView属性，能够执行Javascript脚本 
		webSettings.setAllowFileAccess(true);// 设置可以访问文件 
		webSettings.setBuiltInZoomControls(true);// 设置支持缩放
		webSettings.setDomStorageEnabled(true);//DOM储存API 
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setPluginState(PluginState.ON);  
		webSettings.setLoadWithOverviewMode(true); 
		// 设置Web视图
		webview.setWebViewClient(new webViewClient()); 
		webview.setWebChromeClient(new WebChromeClient()); 
		webview.setDownloadListener(new MyWebViewDownLoadListener());
	}
	
	private void setData(final String Url) {
		new Thread() {
			public void run() {
				boolean closeCheck = WebServiceManager.checkUserCloseDate();
				if (!closeCheck) {
					Handler.post(mUpdateResults);
					return;
				} 
				Handler.post(UpdateWebView);
			}
		}.start();

	}
	
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			new AlertDialog.Builder(WebViewBBSActivity.this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
					.setTitle("提示")
					.setMessage("您还不是会员，请联系电话/微信：13184154587 开通会员!")
					.setCancelable(false)
					.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							return (keyCode == KeyEvent.KEYCODE_SEARCH);
						}
					})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).show().setCanceledOnTouchOutside(false);
		}
	};
	
	final Runnable UpdateWebView = new Runnable() {
		public void run() {

			LoadUrl(Url);

		}
	};

	
	
	private class MyWebViewDownLoadListener implements DownloadListener {  
		  
        @Override  
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,  
                                    long contentLength) {  
            Uri uri = Uri.parse(url);  
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
            startActivity(intent);  
        }  
  
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		else if ((keyCode == KeyEvent.KEYCODE_BACK) && !webview.canGoBack()) {
			finish();// 结束退出程序
		} 
		return false;
	}

	// Web视图
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}
