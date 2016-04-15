package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qct.util.DatabaseOpenHelper;
import com.example.qct.util.Tpdxx;
import com.example.qct.util.Tyjxx;
import com.example.service.YjxxServiceI;

public class YjxxServiceImpl implements YjxxServiceI {

	private DatabaseOpenHelper dbOpenHelper;

	public YjxxServiceImpl(Context context) {
		this.dbOpenHelper = new DatabaseOpenHelper(context);
	}

	@Override
	public void save(Tyjxx yjxx) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("pdid", yjxx.getPdid());
		values.put("lsy", yjxx.getLsy());
		values.put("tm", yjxx.getTm());

		db.insert("yjxx", null, values);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("yjxx", "id=?", new String[] { id.toString() });
	}

	@Override
	public void update(Tyjxx yjxx) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("pdid", yjxx.getPdid());
		values.put("lsy", yjxx.getLsy());
		values.put("tm", yjxx.getTm());

		db.update("yjxx", values, "id=?", new String[] { yjxx.getId()
				.toString() });
	}

	@Override
	public Tyjxx find(Integer id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("yjxx", null, "id=?",
				new String[] { id.toString() }, null, null, null);
		Tyjxx yjxx = new Tyjxx();
		if (cursor.moveToFirst()) {
			int pdid = cursor.getInt(cursor.getColumnIndex("pdid"));
			int lsy = cursor.getInt(cursor.getColumnIndex("lsy"));
			String tm = cursor.getString(cursor.getColumnIndex("tm"));

			yjxx.setId(id);
			yjxx.setPdid(pdid);
			yjxx.setLsy(lsy);
			yjxx.setTm(tm);
		}
		cursor.close();
		return yjxx;
	}

	@Override
	public List<Tyjxx> find(int page, int pagesize) {
		// TODO Auto-generated method stub
		List<Tyjxx> yjxxs = new ArrayList<Tyjxx>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("yjxx", null, null, null, null, null,
				"pdid asc", (page - 1) * pagesize + "," + pagesize);

		while (cursor.moveToNext()) {
			Tyjxx yjxx = new Tyjxx();
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			int pdid = cursor.getInt(cursor.getColumnIndex("pdid"));
			int lsy = cursor.getInt(cursor.getColumnIndex("lsy"));
			String tm = cursor.getString(cursor.getColumnIndex("tm"));

			yjxx.setId(id);
			yjxx.setPdid(pdid);
			yjxx.setLsy(lsy);
			yjxx.setTm(tm);

			yjxxs.add(yjxx);
		}
		cursor.close();
		return yjxxs;
	}

	@Override
	public long getCount() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("yjxx", new String[] { "count(*)" }, null,
				null, null, null, null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;
	}

	@Override
	public int findPdidByTm(String tm) {
		// TODO Auto-generated method stub
		int pdid = 0;

		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("yjxx", new String[] { "pdid" }, new String(
				"tm = " + tm), null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			pdid = cursor.getInt(0);
		}
		cursor.close();
		db.close();
		return pdid;
	}

}
