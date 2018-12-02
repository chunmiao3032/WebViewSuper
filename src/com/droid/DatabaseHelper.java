package com.droid;

import com.common.Global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	// 类没有实例化,是不能用作父类构造器的参数,必须声明为静态
	//private static final String name = "city"; // 数据库名称
	private static final int version = 2; // 数据库版本

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
