package com.allen.iguangwai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "database.db";// ���ݿ�����
	public static final int VERSION = 1;
	public static final String TABLE_ARTICLE = "article";// ���ݱ�
	public static final String ID = "id";//
	public static final String TYPE = "type";//
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String DESCRIPTION = "description";
	public static final String PUBLISHTIME = "publishTime";
	public static final String FIRSTPICURL = "firstPicUrl";
	public static final String[] ALL_STRINGS = { ID, TYPE, TITLE, AUTHOR,
			DESCRIPTION, PUBLISHTIME, FIRSTPICURL };
	private Context context;

	public SQLHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//  �������ݿ�󣬶����ݿ�Ĳ���
		String sql = "create table if not exists " + TABLE_ARTICLE
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID
				+ " TEXT UNIQUE, " + TITLE + " TEXT , " + TYPE + " TEXT , "
				+ AUTHOR + " TEXT , " + DESCRIPTION + " TEXT , " + PUBLISHTIME
				+ " TEXT , " + FIRSTPICURL + " TEXT)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �������ݿ�汾�Ĳ���
		onCreate(db);
	}

}
