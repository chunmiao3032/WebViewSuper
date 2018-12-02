package com.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.basic.CardLogResult;
import com.common.Global;
import com.droid.DatabaseHelper;
import com.webservice.WebServiceManager;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class CardLogService extends Service {

	private static final String TAG = "CardLogService";
	public static final String ACTION = "com.service.CardLogService";
	private DatabaseHelper helper;
	Timer timer;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			helper = new DatabaseHelper(this);

			timer = new Timer(true);
			timer.schedule(task1, 1000, 30 * 1000); // 延时1000ms后执行，1000ms执行一次
			timer.schedule(task2, 1000, 60 * 1000); // 延时1000ms后执行，1000ms执行一次
		} catch (Exception ex) {
		}
	}

	TimerTask task1 = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};

	TimerTask task2 = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 2;
			handler.sendMessage(message);
		}
	};

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				SubmitCardLog();//上传
				break;
			case 2:
				getCardLog();//下载
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void SubmitCardLog() {
		try {
			SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(
					"select * from MD_CARD_LOG where FLAG = 0", null);
			if (cursor.getCount() > 0) {

				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					String ID = cursor.getString(cursor.getColumnIndex("ID"));
					String BANK_NAME = cursor.getString(cursor
							.getColumnIndex("BANK_NAME"));
					String CARD_NAME = cursor.getString(cursor
							.getColumnIndex("CARD_NAME"));
					String rlt = WebServiceManager.submitCardLog(BANK_NAME,
							CARD_NAME);
					if (rlt.equals("success")) {
						db.execSQL("UPDATE MD_CARD_LOG SET FLAG = 1 where ID = "
								+ ID);
					}
				}
			}

			db.close();
		} catch (Exception ex) {
		}
	}

	public void getCardLog() {
		try {
			List<CardLogResult> cardLogList = WebServiceManager.getCardLog();
			if (cardLogList == null) {
				return;
			}
			if (cardLogList.size() > 0) {

				SQLiteDatabase db = helper.getReadableDatabase();
				if (cardLogList != null && cardLogList.size() > 0) {
					db.execSQL("delete from MD_CARD_LOG where flag = 1");
				}
				for (CardLogResult item : cardLogList) {

					db.execSQL("insert into MD_CARD_LOG"
							+ "(USER_NAME, BANK_NAME,CARD_NAME,DATE,FLAG) "
							+ "values('" + item.USER_NAME + "','"
							+ item.BANK_NAME + "','" + item.CARD_NAME + "','"
							+ item.DATE + "',1)");
				}
				db.close();
			}
		} catch (Exception ex) {
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
}
