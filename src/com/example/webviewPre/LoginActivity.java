package com.example.webviewPre;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.basic.OtherLinkResult;
import com.common.CommonMethord;
import com.common.Global;
import com.common.UserInfo;
import com.example.webviewPre.R;
import com.manager.DeviceManager;
import com.service.CardLogService;
import com.webservice.SoapControl;
import com.webservice.WebServiceManager;

public class LoginActivity extends Activity {
	Button btlogin;
	// Button btexit;
	EditText cmbUserCode;
	EditText txtPassword;
	CheckBox chkRemember;
	TextView TxtVersion;
	Button btCheckVersion;
	private String version = "0.00";
	private ProgressDialog loginDialog;
	ImageView ivStartPage;
	LinearLayout llMain;
	TextView tvFindPwd, tvRegister;
	ImageView ivLogo;
	Bitmap bmLogo;

	boolean set_lvleft = false;// 左下菜单显示隐藏

	Handler cwjHandler = null;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {

			loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}
			LoginSystem();

		}
	};
	final Runnable initLogo = new Runnable() {
		public void run() {

			ivLogo.setImageBitmap(bmLogo);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去掉窗口标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		cwjHandler = new Handler();
		try {
			loginDialog = new ProgressDialog(this);
			tvRegister = (TextView) findViewById(R.id.tvRegister);
			tvFindPwd = (TextView) findViewById(R.id.tvFindPwd);
			ivStartPage = (ImageView) findViewById(R.id.ivStartPage);
			llMain = (LinearLayout) findViewById(R.id.llMain);
			btCheckVersion = (Button) findViewById(R.id.btCheckVersion);
			cmbUserCode = (EditText) findViewById(R.id.cmbUserCode);
			txtPassword = (EditText) findViewById(R.id.txtPassword);
			btlogin = (Button) findViewById(R.id.btlogin);
			// btexit = (Button) findViewById(R.id.btexit);
			chkRemember = (CheckBox) findViewById(R.id.chkRemember);
			TxtVersion = (TextView) findViewById(R.id.TxtVersion);
			ivLogo = (ImageView) findViewById(R.id.ivLogo);

			initLogo();

			btlogin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					getDeviceId(getApplicationContext());
					DoLogin(true);
				}
			});

			// btexit.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// finish();
			// }
			// });

			btCheckVersion.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					//checkAppVersion();
					loadProgressBar();
					new Thread() {
						public void run() {
							checkAppVersion();
							cwjHandler.post(checkVersion);
						}
					}.start();
				}
			});

			tvFindPwd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(LoginActivity.this,
							FindPwdActivity.class);
					startActivity(intent);

				}
			});
			tvRegister.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(LoginActivity.this,
							RegisterActivity.class);
					startActivity(intent);

				}
			});

			version = this.getPackageManager().getPackageInfo(
					"com.example.webview", 0).versionName;
			TxtVersion.setText(version);
			Global.Version = version;
			saveGlobalMember(null, null, null, version, null);

			new Thread() {
				public void run() {
					checkAppVersion();
					cwjHandler.post(checkVersion);
				}
			}.start();

			new Thread() {
				public void run() {
					startService();// 启动同步申卡日志的后台service
				}
			}.start();
		} catch (Exception ex) {
			Toast.makeText(getApplication(), "登录异常:" + ex.getMessage(), 2000)
					.show();
		}

	}

	/*
	 * 加载登录界面logo
	 */
	private void initLogo() {
		try {
			new Thread() {
				public void run() {
					List<OtherLinkResult> listLink = WebServiceManager
							.getLinks("Logo");
					if (listLink != null) {
						if (listLink.size() > 0) {
							String img = listLink.get(0).LINK_IMG_DATA;
							if (img != null) {
								if (img.length() > 0) {
									bmLogo = CommonMethord.base64ToBitmap(img);
									cwjHandler.post(initLogo);
								}
							}
						}
					}
				}
			}.start();
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.getMessage(), 2000)
					.show();
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		llMain.setVisibility(View.VISIBLE);
		ivStartPage.setVisibility(View.GONE);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		llMain.setVisibility(View.VISIBLE);
		ivStartPage.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// return super.onKeyDown(keyCode, event);

		if (ivStartPage.getVisibility() == View.VISIBLE
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		} else if (ivStartPage.getVisibility() != View.VISIBLE
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;

	}

	String ver_Server;

	// 检查程序更新
	private void checkAppVersion() {
		// 获取版本信息
		try {
			String version = this.getPackageManager().getPackageInfo(
					"com.example.webview", 0).versionName;
			Global.Version = version;

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appName", "WebView");
			map.put("deviceId", Global.deviceId);
			SoapPrimitive sp = SoapControl.ExecuteWebMethod("GetLastVer", map,
					Global.SendDataTime);

			if (sp != null) {
				ver_Server = sp.toString().trim(); 
//				if (CompareVersion(ver_Server, version) <= 0) {
//					Toast.makeText(getApplication(), "当前版本已经是最新，无需升级。", 2000)
//							.show();
//					return;
//				} 
//				else {
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							LoginActivity.this)
//							.setTitle("系统提示")
//							.setMessage("检测到当前存在新版本，是否升级?")
//							.setCancelable(false)
//							.setPositiveButton("确定",
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog, int id) {
//											downFile();
//										}
//									})
//							.setNegativeButton("取消",
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog, int id) {
//											dialog.cancel();
//										}
//									});
//					builder.show();
//				}
			} 
			else {
				ver_Server = null;
//				Toast.makeText(getApplicationContext(), "获取程序版本号完毕！", 2000)
//						.show();
			}
		} catch (NameNotFoundException e1) {
			ver_Server = null;
//			e1.printStackTrace();
//			Toast.makeText(getApplicationContext(), "获取程序版本异常！", 2000).show();
		}
	}

	final Runnable checkVersion = new Runnable() {
		public void run() {
			
			loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}
			
			if (ver_Server != null) {
				if (CompareVersion(ver_Server, version) <= 0) {
					Toast.makeText(getApplication(), "当前版本已经是最新，无需升级。", 2000)
							.show();
					return;
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							LoginActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
							.setTitle("系统提示")
							.setMessage("检测到当前存在新版本，是否升级?")
							.setCancelable(false)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											downFile();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					builder.show();
				}
			}
			else {
				Toast.makeText(getApplicationContext(), "获取程序版本号完毕！", 2000)
						.show();
			}

		}
	};

	void downFile() {
		loginDialog = new ProgressDialog(this);
		loginDialog.setMessage("正在下载更新,请稍后...");
		loginDialog.setCanceledOnTouchOutside(false);
		loginDialog.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();

				HttpGet get = new HttpGet(Global.updateFileString);
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
								"WebView.apk");
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {

							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {

							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					update();
					loginDialog.cancel();
				} catch (ClientProtocolException e) {
					loginDialog.cancel();
					e.printStackTrace();

				} catch (IOException e) {
					loginDialog.cancel();
					e.printStackTrace();
				}
			}
		}.start();
	}

	protected void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(
				Uri.fromFile(new File(Environment.getExternalStorageDirectory()
						.toString() + "/WebView.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static int CompareVersion(String ver1, String ver2) {
		String[] strArray = ver1.split("\\.");
		String[] strArray2 = ver2.split("\\.");
		for (int i = 0; i < Math.min(strArray.length, strArray2.length); i++) {
			int num2 = Integer.valueOf(strArray[i]);
			int num3 = Integer.valueOf(strArray2[i]);
			if (num2 > num3) {
				return 1;
			}
			if (num2 < num3) {
				return -1;
			}
		}
		return 0;
	}

	private void DoLogin(boolean isOnline) {
		final String userCode = cmbUserCode.getText().toString();
		final String text = txtPassword.getText().toString();
		boolean isPwdRemember = chkRemember.isChecked();
		if (userCode == null || userCode.length() == 0) {
			this.cmbUserCode.setFocusable(true);
			Toast.makeText(getApplicationContext(), "用户名不能为空。", 2000).show();
		}
		if (text == null || text.length() == 0) {
			this.txtPassword.setFocusable(true);
			Toast.makeText(getApplicationContext(), "密码不能为空。", 2000).show();
		}
		saveLogin(userCode, text);

		loadProgressBar();
		new Thread() {
			public void run() {
				LoginThead(userCode, text);
				cwjHandler.post(mUpdateResults);
			}

		}.start();

	}

	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		loginDialog.setMessage("正在加载，请稍等...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}

	Bitmap bitmap;
	final Runnable UpdateStartPage = new Runnable() {
		public void run() {

			ivStartPage.setImageBitmap(bitmap);
		}
	};

	// 调用webservice后执行
	private void LoginSystem() {

		if (isLogin) {
			try {
				llMain.setVisibility(View.GONE);
				ivStartPage.setVisibility(View.VISIBLE);
				// try {
				// Bitmap bitmap = CommonMethord
				// .getHttpBitmap(Global.StartPage);
				// ivStartPage.setImageBitmap(bitmap);
				// } catch (Exception ex) {
				// Toast.makeText(getApplicationContext(),
				// "获取图片异常:" + ex.getMessage(), 2000).show();
				// }
				new Thread() {
					public void run() {
						bitmap = CommonMethord.getHttpBitmap(Global.StartPage);
						cwjHandler.post(UpdateStartPage);
					}

				}.start();

				new Handler().postDelayed(new Runnable() {
					public void run() {

//						Intent intent = new Intent(LoginActivity.this,
//								TabHostActivity.class);// MainActivity
//						startActivity(intent);
						
//						Intent resultIntent = new Intent();
//						Bundle bundle = new Bundle();
//						bundle.putString("rlt", "ok");
//						resultIntent.putExtras(bundle);
//						setResult(RESULT_OK, resultIntent);
						
						finish();
					}
				}, 3000);

			} catch (Exception e) {
				e.toString();
			}
		} else {
			new AlertDialog.Builder(LoginActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
					.setTitle("系统提示")
					// 设置对话框标题
					.setMessage("登录失败！" + ErrMsg)
					// 设置显示的内容
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {// 添加确定按钮
								public void onClick(DialogInterface dialog,
										int which) {// 确定按钮的响应事件

								}
							}).show();// 在按键响应事件中显示此对话框
		}

	}

	// 记录用户名密码
	private void saveLogin(String userCode, String text) {
		boolean isPwdRemember = chkRemember.isChecked();
		SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
		if (isPwdRemember) {
			SharedPreferences.Editor edit = remdname.edit();
			edit.putString("name", userCode);
			edit.putString("pass", text);
			edit.putBoolean("check", true);
			edit.commit();
		} else {
			SharedPreferences.Editor edit = remdname.edit();
			edit.putString("name", "");
			edit.putString("pass", "");
			edit.putBoolean("check", false);
			edit.commit();
		}
	}

	/**
	 * 保存登录信息
	 * 
	 * @param GlobalUser
	 * @param GlobalPass
	 * @param GlobalVersion
	 * @param GlobalDeviceID
	 */
	private void saveGlobalMember(String GlobalUser, String GlobalUserName,
			String GlobalPass, String GlobalVersion, String GlobalDeviceID) {
		try {
			SharedPreferences remdname = getSharedPreferences("Global",
					Activity.MODE_PRIVATE);
			SharedPreferences.Editor edit = remdname.edit();
			if (GlobalUser != null && GlobalUser.trim().length() > 0) {
				edit.putString("GlobalUser", GlobalUser);
			}
			if (GlobalUserName != null && GlobalUserName.trim().length() > 0) {
				edit.putString("GlobalUserName", GlobalUserName);
			}
			if (GlobalPass != null && GlobalPass.trim().length() > 0) {
				edit.putString("GlobalPass", GlobalPass);
			}
			if (GlobalVersion != null && GlobalVersion.trim().length() > 0) {
				edit.putString("GlobalVersion", GlobalVersion);
			}
			if (GlobalDeviceID != null && GlobalDeviceID.trim().length() > 0) {
				edit.putString("GlobalDeviceID", GlobalDeviceID);
			}
			edit.commit();
		} catch (Exception ex) {
		}
	}

	// 从配置文件中读取用户名密码
	@Override
	protected void onResume() {
		SharedPreferences remdname = getPreferences(Activity.MODE_PRIVATE);
		cmbUserCode.setText(remdname.getString("name", ""));
		txtPassword.setText(remdname.getString("pass", ""));
		boolean cheked = remdname.getBoolean("check", false);
		if (cheked) {
			chkRemember.setChecked(true);
		} else {
			chkRemember.setChecked(false);
		}
		super.onResume();
	}

	boolean isLogin = false;
	String ErrMsg = null;
	String ID = null;
	String IsAdmin = null;

	private void LoginThead(String userCode, String password) {
		try {
			// string userName, string password, string deviceId, string
			// clientVersion, int dbServer
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userName", userCode);
			map.put("password", password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass("Login",
					map);

			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					String ID = so.getPropertyAsString("ID");
					String UserCode = so.getPropertyAsString("UserCode");
					String UserName = so.getPropertyAsString("UserName");
					String Password = so.getPropertyAsString("Password");
					String Remark = so.getPropertyAsString("Remark");
					String StartPage = so.getPropertyAsString("StartPage");

					Global.ID = ID;
					Global.UserCode = UserCode;
					Global.UserName = UserName;
					Global.Password = Password;
					Global.Remark = Remark;
					Global.StartPage = StartPage;
					
					UserInfo userInfo = (UserInfo) getApplicationContext();
					userInfo.setUserCode(UserCode);
					userInfo.setUserName(UserName);
					userInfo.setPassword(Password);
					userInfo.setRemark(Remark);
					userInfo.setStartPage(StartPage);
					
					saveGlobalMember(UserCode, UserName, Password, null, null);

					isLogin = true;
				} else {
					ErrMsg = so.getPropertyAsString("ErrMsg");
					ID = so.getPropertyAsString("ID");
					isLogin = false;
				}
			}
		} catch (Exception ex) {
			String ErrMsg = "异常:" + ex.getMessage();
			isLogin = false;

		}

	}

	public void getDeviceId(Context context) {
		if (Global.deviceId == null || Global.deviceId.equals("")
				|| Global.deviceId.length() == 0) {
			// 读取序号
			final TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Global.deviceId = "" + tm.getDeviceId();
			saveGlobalMember(null, null, null, null, Global.deviceId);
		}
	}

	private void startService() {
		try {
			// 创建Intent
			Intent intent = new Intent();
			// 设置Class属性
			intent.setClass(LoginActivity.this, CardLogService.class);
			// 启动该Service
			startService(intent);
		} catch (Exception ex) {
			Log.e("启动service异常:", ex.getMessage());
		}
	}

}
