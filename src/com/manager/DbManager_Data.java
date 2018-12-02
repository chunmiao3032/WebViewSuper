package com.manager;

import java.util.ArrayList;
import java.util.List;
 
import com.common.CommonMethord;
import com.db.DBOpenHelper_Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbManager_Data {
	// Fields
	private String _ConnString4Data;
	private static DbManager_Data _Instance;

	private DBOpenHelper_Data helper;
	private SQLiteDatabase database;
	private Cursor cursor;
	private Cursor cursorBatchNo;

	public DbManager_Data(Context context) {

		helper = new DBOpenHelper_Data(context);
		database = helper.getWritableDatabase();
	}

	public Cursor getCursor() {
		return cursor;
	}

	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	public Cursor getcursorBatchNo() {
		return cursor;
	}

	public void setcursorBatchNo(Cursor cursor) {
		this.cursorBatchNo = cursor;
	}

	public DBOpenHelper_Data getHelper() {
		return helper;
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public Cursor GetAllTask() {

		if (database == null) {
			// database = helper.getWritableDatabase();
		}
		if (!database.isOpen()) {
			// database = helper.getWritableDatabase();
		}
		if (cursor != null || (cursor != null && cursor.moveToFirst() != false)) {
			cursor.close();
		}
		cursor = database.rawQuery("select id from Task", null);
		if (cursor.moveToFirst()) {
			return cursor;
		} else {
			cursor.close();
			return null;
		}
	}

 
	 
	 

}
