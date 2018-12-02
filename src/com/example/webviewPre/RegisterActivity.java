package com.example.webviewPre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.basic.OtherLinkResult;
import com.common.CommonMethord;
import com.common.Global;
import com.example.webviewPre.R;
import com.webservice.SoapControl;
import com.webservice.WebServiceManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	EditText etUserCode, etUserName, etUserPwd1, etUserPwd2, etPhone;
	Button btRegister;
	EditText tvDeviceNo;
	String _DeviceNo;
	String ErrMsg;
	TextView tvLogin;
	ImageView ivLogo;
	Bitmap bmLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 去掉窗口标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);

		cwjHandler = new Handler();
		initControl();

		btRegister.setOnClickListener(btRegister_OnClick);
		tvLogin.setOnClickListener(tvLogin_OnClick);
		initLogo();
	}

	final Runnable initLogo = new Runnable() {
		public void run() {

			ivLogo.setImageBitmap(bmLogo);

		}
	};

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

	private void initControl() {
		ivLogo = (ImageView) findViewById(R.id.ivLogo);
		etUserCode = (EditText) findViewById(R.id.etUserCode);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etUserPwd1 = (EditText) findViewById(R.id.etUserPwd1);
		etUserPwd2 = (EditText) findViewById(R.id.etUserPwd2);
		etPhone = (EditText) findViewById(R.id.etPhone);
		btRegister = (Button) findViewById(R.id.btRegister);
		tvDeviceNo = (EditText) findViewById(R.id.tvDeviceNo);
		tvLogin = (TextView) findViewById(R.id.tvLogin);

		_DeviceNo = getDeviceId(getApplicationContext());
		tvDeviceNo.setText(_DeviceNo);
	}

	OnClickListener tvLogin_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			finish();

		}
	};

	OnClickListener btRegister_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (etUserCode.getText().toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), "用户帐号不可以为空!", 2000)
						.show();
				return;
			}

			etUserName.setText(etUserCode.getText());
			// if(etUserName.getText().toString().trim().length() == 0)
			// {
			// Toast.makeText(getApplicationContext(), "用户姓名不可以为空!",
			// 2000).show();
			// return;
			// }
			if (etUserPwd1.getText().toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), "用户密码不可以为空!", 2000)
						.show();
				return;
			} else {
				if (etUserPwd1.getText().toString().trim().length() < 6) {
					Toast.makeText(getApplicationContext(), "用户密码长度必须大于6位!",
							2000).show();
					return;
				}

			}
			if (etUserPwd2.getText().toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), "用户密码不可以为空!", 2000)
						.show();
				return;
			} else {
				if (!etUserPwd2.getText().toString()
						.equals(etUserPwd1.getText().toString())) {
					Toast.makeText(getApplicationContext(), "两次输入的密码不一致!", 2000)
							.show();
					return;
				}
			}
			if (etPhone.getText().toString().trim().length() == 0) {
				Toast.makeText(getApplicationContext(), "用户电话不可以为空!", 2000)
						.show();
				return;
			} else {
				if (!etPhone.getText().toString().startsWith("1")) {
					Toast.makeText(getApplicationContext(), "用户电话号码格式不正确1!",
							2000).show();
					return;
				}
				if (!etPhone.getText().toString().startsWith("13")
						&& !etPhone.getText().toString().startsWith("14")
						&& !etPhone.getText().toString().startsWith("15")
						&& !etPhone.getText().toString().startsWith("17")
						&& !etPhone.getText().toString().startsWith("18")) {
					
					Toast.makeText(getApplicationContext(), "用户电话号码格式不正确2!",
							2000).show();
					return;
				}
				if (etPhone.getText().toString().trim().length() != 11) {
					Toast.makeText(getApplicationContext(), "用户电话号码位数不正确!",
							2000).show();
					return;
				}
			}

			loadProgressBar();
			new Thread() {
				public void run() {
					RegisterThead(etUserCode.getText().toString().trim(),
							etUserName.getText().toString().trim(), etUserPwd2
									.getText().toString().trim(), etPhone
									.getText().toString().trim());
					cwjHandler.post(mUpdateResults);
				}

			}.start();

		}
	};

	public String getDeviceId(Context context) {

		// 读取序号
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	private void RegisterThead(String userCode, String userName,
			String password, String phone) {
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phoneNo", phone);
			map.put("name", userName);
			map.put("userName", userCode);
			map.put("password", password);
			map.put("deviceId", _DeviceNo);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass("Register",
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

					ErrMsg = "Success";

				} else {
					ErrMsg = so.getPropertyAsString("ErrMsg");
				}
			}
		} catch (Exception ex) {
			ErrMsg = "异常:" + ex.getMessage();

		}

	}

	Handler cwjHandler = null;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {

			loginDialog.cancel();
			if (loginDialog != null && loginDialog.isShowing()) {
				loginDialog.dismiss();
			}
			if (ErrMsg.equals("Success")) {
				Toast.makeText(getApplicationContext(), "注册成功,请牢记帐号及密码！", 2)
						.show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "注册失败:" + ErrMsg, 10)
						.show();
			}

		}
	};

	private ProgressDialog loginDialog;

	private void loadProgressBar() {
		loginDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
		loginDialog.setMessage("请稍等...");
		loginDialog.setCancelable(false);
		loginDialog.show();
	}
}
