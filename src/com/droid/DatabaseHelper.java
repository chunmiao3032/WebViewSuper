package com.droid;

import com.common.Global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	// ��û��ʵ����,�ǲ����������๹�����Ĳ���,��������Ϊ��̬
	//private static final String name = "city"; // ���ݿ�����
	private static final int version = 2; // ���ݿ�汾

	public DatabaseHelper(Context context) {
		super(context, Global._ConnString4Data, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("info", "create table");
		db.execSQL("CREATE TABLE IF NOT EXISTS recentcity (id integer primary key autoincrement, name varchar(40), date INTEGER)");
	
		db.execSQL(
				"CREATE TABLE IF NOT EXISTS MD_CARD_LOG "
				+ "(ID integer primary key autoincrement, "
				+ "USER_NAME varchar(40),"
				+ "BANK_NAME varchar(40),"
				+ "CARD_NAME varchar(40), "
				+ "FLAG INTEGER,"
				+ "DATE varchar(60))");
	
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS MD_CARD_LOG"); 
		onCreate(db);
	}
}
