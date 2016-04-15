package com.example.qct.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public DatabaseOpenHelper(Context context) {
		super(context, "qct", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE pdxx (id integer primary key autoincrement,"
				+ "pdid integer not null unique,jjrtel varchar(30),jjrname varchar(30),"
				+ "jjraddr varchar(50),lrrq varchar(10),lrsj varchar(8),"
				+ "xfrq varchar(10),xfsj varchar(8),lsy int(6),memo varchar(50),"
				+ "jsrq varchar(10),jssj varchar(8),fkrq varchar(10),"
				+ "fksj varchar(8),flag int(4),sjrname varchar(30),sjraddr varchar(50),sjrtel varchar(30))");

		db.execSQL("CREATE TABLE yjxx (id integer primary key autoincrement,"
				+ "pdid integer ,lsy int(6),tm varchar(50) UNIQUE,sjrname varchar(30),sjraddr varchar(50),"
				+ "sjrtel varchar(30),jjrname varchar(30),jjraddr varchar(50),jjrtel varchar(30),lrrq varchar(10),"
				+ "lrsj varchar(10),zl int(6),zf int(9),memo varchar(50),bjje int(6),bjf int(6),qtf decimal(6,1)," +
				"zfy decimal(6,1),sjrff decimal(6,1),jz int(6),ywzl varchar(6),dshk decimal(6,1),part int(6),fjfw int(4),hzhm varchar(13))");

		db.execSQL("CREATE TABLE ywzl (id integer primary key,name varchar(4))");

		db.execSQL("CREATE TABLE jz (id integer primary key,name varchar(50),tel varchar(30),addr varchar(50),"
				+ "part int(6),flag varchar(5),memo varchar(100))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
