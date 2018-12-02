package com.db;

import java.util.Date;

//import com.basic.AssetDO;
import com.common.Global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
  
public class DBOpenHelper_Data extends SQLiteOpenHelper {

	public DBOpenHelper_Data(Context context) {
		super(context, Global._ConnString4Data, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		  
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Task");
		db.execSQL("DROP TABLE IF EXISTS DoTask"); 
		onCreate(db);
	}


} 