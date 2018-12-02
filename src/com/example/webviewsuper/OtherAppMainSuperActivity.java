package com.example.webviewsuper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.basic.AddQuotaResult;
import com.basic.OtherAppResult;
import com.common.CommonMethord;
import com.common.Global;
import com.common.MyGridView;
import com.common.UserInfo;
import com.example.webviewPre.LoginActivity;
import com.example.webviewPre.R;
import com.example.webviewPre.AddQuotaShow1Activity;
import com.example.webviewPre.WebViewActivity;
import com.example.webviewPre.AddQuotaShow1Activity.GridViewAdapter;
import com.webservice.WebServiceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OtherAppMainSuperActivity extends Activity {

	TextView headTitle;
	ImageView ivBack;

	MyGridView gridView, gridView2;
	List<OtherAppResult> _OtherAppList;
	Handler Handler = null;

	String _type;
	String _HeaderName;
	
	String _appName,_packageName,_appUrl;

	@Override
	protected void onRestart() {
		super.onRestart();
//		UserInfo userinfo = (UserInfo) getApplicationContext();
//		String UserCode = userinfo.getUserCode();
//		
//		 if(UserCode == null || UserCode == "" ||
//				 UserCode.length() == 0)
//		 {
//			  
//		 }
//		 else
//		 { 
//			 openAppDown(_appName,_packageName,_appUrl);
//		 }

	}

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
		setContentView(R.layout.activity_other_app_main_super);

		Handler = new Handler();
		Intent intent = getIntent();
		String type = intent.getStringExtra("type");
		_type = type;
		String HeaderName = intent.getStringExtra("HeaderName");
		_HeaderName = HeaderName;

		initHeader(_HeaderName);

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
	// 外部APP
	// **********************************************************************************************************

	private void setData() {
		loadProgressBar();
		new Thread() {
			public void run() {
				_OtherAppList = WebServiceManager.getOtherApp(_type);
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
 
		GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(),
				_OtherAppList);
		gridView.setAdapter(adapter);
	}

	public class GridViewAdapter extends BaseAdapter {
		Context context;
		List<OtherAppResult> list;

		public GridViewAdapter(Context _context, List<OtherAppResult> _list) {
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

			final OtherAppResult app = list.get(position);

			TextView tvTitle1 = (TextView) convertView
					.findViewById(R.id.tvTitle1);
			TextView tvDesc1 = (TextView) convertView
					.findViewById(R.id.tvDesc1);

			ImageButton ItemImage1 = (ImageButton) convertView
					.findViewById(R.id.ItemImage1);

			tvTitle1.setText(app.OTHER_APP_NAME);
			tvDesc1.setText(app.OTHER_APP_DESC);

			if (app.OTHER_APP_IMG_DATA != null) {

				Bitmap bm = CommonMethord
						.base64ToBitmap(app.OTHER_APP_IMG_DATA);
				Drawable drawable = CommonMethord.bitmap2Drawable(bm);

				ItemImage1.setBackground(drawable);
			}

			ItemImage1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Intent intent = new
					// Intent(OtherAppMainSuperActivity.this,
					// WebViewActivity.class);
					// intent.putExtra("Url", bank.INCREASE_QUOTA_URL);
					// startActivity(intent);
					
					_appName = app.OTHER_APP_NAME;
					_packageName = app.OTHER_APP_PACKAGE_NAME;
					_appUrl = app.OTHER_APP_URL;

					UserInfo userinfo = (UserInfo) getApplicationContext();
					String UserCode = userinfo.getUserCode();
					
					if (UserCode == null || UserCode == ""
							|| UserCode.length() == 0) {
						new AlertDialog.Builder(OtherAppMainSuperActivity.this,
								AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
								.setTitle("提示")
								.setMessage("您还没有登录, 请登录后查看！")
								.setCancelable(false)
								.setOnKeyListener(new OnKeyListener() {
									@Override
									public boolean onKey(
											DialogInterface dialog,
											int keyCode, KeyEvent event) {
										return (keyCode == KeyEvent.KEYCODE_SEARCH);
									}
								})
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {

												Intent intent = new Intent(
														OtherAppMainSuperActivity.this,
														LoginActivity.class);
												startActivityForResult(intent,
														1);
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {

												// finish();
											}
										}).show()
								.setCanceledOnTouchOutside(false);
					}

					if (UserCode == null || UserCode == ""
							|| UserCode.length() == 0) {
						// finish();
					} else {
						openApp(app.OTHER_APP_NAME,app.OTHER_APP_PACKAGE_NAME,app.OTHER_APP_URL);
					}

				}

				
			});

			return convertView;
		}
	}
	
	private void openApp(String appName,String packageName,String url) {
		try {
			PackageManager packageManager = getPackageManager();
			Intent intent = new Intent();
			intent = packageManager
					.getLaunchIntentForPackage(packageName);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();

			downFile(appName, url);
		}
	}
	
	final Runnable UpdateResults = new Runnable() {
		public void run() {
			new AlertDialog.Builder(OtherAppMainSuperActivity.this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
					.setTitle("提示")
					.setMessage("您还不是会员，请联系电话/微信：13184154587 开通会员!")
					.setCancelable(false)
					.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(
								DialogInterface dialog,
								int keyCode, KeyEvent event) {
							return (keyCode == KeyEvent.KEYCODE_SEARCH);
						}
					})
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int which) {

								}
							}).show().setCanceledOnTouchOutside(false);
		}
	};
	  
	void downFile(final String appName, final String fileUrl) {

		showDialogProgress(appName);

		new Thread() {
			public void run() {
				
				boolean closeCheck = WebServiceManager.checkUserCloseDate();
				if(!closeCheck)
				{
					loginDialog.cancel();
					loginDialog.dismiss();
					
					Handler.post(UpdateResults);
					return;
				}
				
				HttpClient client = new DefaultHttpClient();

				HttpGet get = new HttpGet(fileUrl);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {

						File file = new File(
								Environment.getExternalStorageDirectory(),
								appName + ".apk");
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						long count = 0;
						while ((ch = is.read(buf)) != -1) {

							fileOutputStream.write(buf, 0, ch);
							count += ch;

							int result = (int) (count * 100 / length);
							handler.sendEmptyMessage(result);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					update(appName);
				} catch (ClientProtocolException e) {
					e.printStackTrace();

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					loginDialog.cancel();
					loginDialog.dismiss();
				}
			}
		}.start();
	}

	private void showDialogProgress(final String appName) {
		loginDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		loginDialog.setMessage("正在下载 " + appName + " 请稍候...");
		loginDialog.setCanceledOnTouchOutside(false);
		loginDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		loginDialog.setProgress(0);
		loginDialog.setMax(100);
		loginDialog.setCancelable(false);
		loginDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				return (keyCode == KeyEvent.KEYCODE_SEARCH);
			}
		});
		loginDialog.show();
	}

	protected void update(String appName) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(
				Uri.fromFile(new File(Environment.getExternalStorageDirectory()
						.toString() + "/" + appName + ".apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
//		android.os.Process.killProcess(android.os.Process.myPid());
	}
  
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what >= 100) {
				// afterThread.run();
				loginDialog.cancel();
				loginDialog.dismiss();
			}
			loginDialog.setProgress(msg.what);
			super.handleMessage(msg);
		}
	};

}
