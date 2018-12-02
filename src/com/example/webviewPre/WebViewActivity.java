package com.example.webviewPre;

import com.common.CustomDialog;
import com.common.Global;
import com.common.UserInfo;
import com.example.webviewPre.R;
import com.example.webviewsuper.OtherAppMainSuperActivity;
import com.webservice.WebServiceManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WebViewActivity extends Activity implements initWebView {

	private WebView webview;
	TextView headTitle;
	ImageView ivBack;
	String myTitle = "";
	CustomDialog dialog;
	String _Url;

	Handler Handler = null;

	@Override
	protected void onRestart() {
		super.onRestart();

		UserInfo userinfo = (UserInfo) getApplicationContext();
		String UserCode = userinfo.getUserCode();
		if (UserCode == null || UserCode == "" || UserCode.length() == 0) {
			finish();
		} else {
			setData();
		}

	}

	@Override
	protected void onPause() {
		webview.reload();

		super.onPause();
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ�����ڱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webview);

		Intent intent = getIntent();
		_Url = intent.getStringExtra("Url");

		Handler = new Handler();
		initHeader("");
		webview = (WebView) findViewById(R.id.webview);

		UserInfo userinfo = (UserInfo) getApplicationContext();
		String UserCode = userinfo.getUserCode();

		if (UserCode == null || UserCode == "" || UserCode.length() == 0) {
			new AlertDialog.Builder(this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
					.setTitle("��ʾ")
					.setMessage("�㻹û�е�¼, ���¼��鿴��")
					.setCancelable(false)
					.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							return (keyCode == KeyEvent.KEYCODE_SEARCH);
						}
					})
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									Intent intent = new Intent(
											WebViewActivity.this,
											LoginActivity.class);
									startActivity(intent);
								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									finish();
								}
							}).show().setCanceledOnTouchOutside(false);
		} else {
			setData();
		}
	}

	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			new AlertDialog.Builder(WebViewActivity.this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
					.setTitle("��ʾ")
					.setMessage("�������ǻ�Ա������ϵ�绰/΢�ţ�13184154587 ��ͨ��Ա!")
					.setCancelable(false)
					.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							return (keyCode == KeyEvent.KEYCODE_SEARCH);
						}
					})
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).show().setCanceledOnTouchOutside(false);
		}
	};

	private void setData() {
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

	private void LoadWebView() {
		WebChromeClient myWebChromeClient = new WebChromeClient() {
			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				// TODO Auto-generated method stub
			}
		};

		try {

			if (_Url != null) {

				webview.loadUrl(_Url);// "http://cn.bing.com"

				WebSettings webSettings = webview.getSettings();
				webSettings.setJavaScriptEnabled(true);// ����WebView���ԣ��ܹ�ִ��Javascript�ű�
				webSettings.setAllowFileAccess(true);// ���ÿ��Է����ļ�
				webSettings.setBuiltInZoomControls(true);// ����֧������
				webSettings.setDomStorageEnabled(true);// DOM����API
				webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
				webSettings.setPluginState(PluginState.ON);
				webSettings.setLoadWithOverviewMode(true);
				webSettings.setMediaPlaybackRequiresUserGesture(false);

				webview.setWebViewClient(new webViewClient());
				webview.setWebChromeClient(myWebChromeClient);
				webview.setDownloadListener(new MyWebViewDownLoadListener());

			}

		} catch (Exception ex) {

		}
	}

	final Runnable UpdateWebView = new Runnable() {
		public void run() {

			LoadWebView();

		}
	};

	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			try {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			} catch (Exception ex) {

			}
		}

	}

	public void initHeader(String title) {
		try {
			headTitle = (TextView) findViewById(R.id.headTitle);
			ivBack = (ImageView) findViewById(R.id.ivBack);
			headTitle.setText(title);
			ivBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					finish();
				}
			});

		} catch (Exception ex) {

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	// ���û���
	// ����Activity���onKeyDown(int keyCoder,KeyEvent event)����
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
				webview.goBack(); // goBack()��ʾ����WebView����һҳ��
				return true;
			} else if ((keyCode == KeyEvent.KEYCODE_BACK)
					&& !webview.canGoBack()) {
				finish();// �����˳�����
			}
		} catch (Exception ex) {

		}
		return false;
	}

	// Web��ͼ
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			try {
				view.loadUrl(url);
			} catch (Exception ex) {
			}
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			// �������������������������Ը���errorCode��ֵ�����жϣ�������ϸ�Ĵ���
			String errorHtml = "<html><body><h1>NOT FOUND NET!</h1></body></html>";

			view.loadData(errorHtml, "text/html", "UTF-8");
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {

			super.onPageFinished(view, url);
		}
	}

}
