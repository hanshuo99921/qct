package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qct.DemoApplication;
import com.example.qct.util.DatabaseOpenHelper;
import com.example.qct.util.Tpdxx;
import com.example.service.PdxxServiceI;

public class PdxxServiceImpl implements PdxxServiceI {

	private DatabaseOpenHelper dbOpenHelper;
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public PdxxServiceImpl(Context context) {
		this.dbOpenHelper = new DatabaseOpenHelper(context);
		this.context = context;
	}

	@Override
	public void save(Tpdxx pdxx) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("pdid", pdxx.getPdid());
		values.put("jjrname", pdxx.getJjrname());
		values.put("jjrtel", pdxx.getJjrtel());
		values.put("jjraddr", pdxx.getJjraddr());
		values.put("sjrname", pdxx.getSjrname());
		values.put("sjrtel", pdxx.getSjrtel());
		values.put("sjraddr", pdxx.getSjraddr());
		values.put("lrrq", pdxx.getLrrq());
		values.put("lrsj", pdxx.getLrsj());
		values.put("xfrq", pdxx.getXfrq());
		values.put("xfsj", pdxx.getXfsj());
		values.put("flag", pdxx.getFlag());
		values.put("memo", pdxx.getMemo());
		values.put("lsy", pdxx.getLsy());
		values.put("fkrq", pdxx.getFkrq());
		values.put("fksj", pdxx.getFksj());
		values.put("jsrq", pdxx.getJsrq());
		values.put("jssj", pdxx.getJssj());
		values.put("flag", pdxx.getFlag());

		db.insert("pdxx", null, values);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("pdxx", "id=?", new String[] { id.toString() });
	}

	@Override
	public void update(Tpdxx pdxx) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		// values.put("id", pdxx.getId());
		values.put("pdid", pdxx.getPdid());
		values.put("jjrname", pdxx.getJjrname());
		values.put("jjrtel", pdxx.getJjrtel());
		values.put("jjraddr", pdxx.getJjraddr());
		values.put("sjrname", pdxx.getSjrname());
		values.put("sjrtel", pdxx.getSjrtel());
		values.put("sjraddr", pdxx.getSjraddr());
		values.put("lrrq", pdxx.getLrrq());
		values.put("lrsj", pdxx.getLrsj());
		values.put("xfrq", pdxx.getXfrq());
		values.put("xfsj", pdxx.getXfsj());
		values.put("flag", pdxx.getFlag());
		values.put("memo", pdxx.getMemo());
		values.put("lsy", pdxx.getLsy());
		values.put("fkrq", pdxx.getFkrq());
		values.put("fksj", pdxx.getFksj());
		values.put("jsrq", pdxx.getJsrq());
		values.put("jssj", pdxx.getJssj());
		values.put("flag", pdxx.getFlag());

		db.update("pdxx", values, "id=?", new String[] { pdxx.getId()
				.toString() });
	}

	@Override
	public Tpdxx find(Integer pdid) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("pdxx", null, "pdid=?",
				new String[] { pdid.toString() }, null, null, null);
		Tpdxx pdxx = new Tpdxx();
		if (cursor.moveToFirst()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String jjrname = cursor.getString(cursor.getColumnIndex("jjrname"));
			String jjrtel = cursor.getString(cursor.getColumnIndex("jjrtel"));
			String jjraddr = cursor.getString(cursor.getColumnIndex("jjraddr"));
			String sjrname = cursor.getString(cursor.getColumnIndex("sjrname"));
			String sjrtel = cursor.getString(cursor.getColumnIndex("sjrtel"));
			String sjraddr = cursor.getString(cursor.getColumnIndex("sjraddr"));
			String memo = cursor.getString(cursor.getColumnIndex("memo"));
			String xfrq = cursor.getString(cursor.getColumnIndex("xfrq"));
			String xfsj = cursor.getString(cursor.getColumnIndex("xfsj"));
			String jsrq = cursor.getString(cursor.getColumnIndex("jsrq"));
			String jssj = cursor.getString(cursor.getColumnIndex("jssj"));
			int lsy = cursor.getInt(cursor.getColumnIndex("lsy"));
			String fkrq = cursor.getString(cursor.getColumnIndex("fkrq"));
			String fksj = cursor.getString(cursor.getColumnIndex("fksj"));

			pdxx.setId(id);
			pdxx.setPdid(pdid);
			pdxx.setJjrname(jjrname);
			pdxx.setJjraddr(jjraddr);
			pdxx.setJjrtel(jjrtel);
			pdxx.setSjrname(sjrname);
			pdxx.setSjraddr(sjraddr);
			pdxx.setSjrtel(sjrtel);
			pdxx.setMemo(memo);
			pdxx.setXfrq(xfrq);
			pdxx.setXfsj(xfsj);
			pdxx.setJsrq(jsrq);
			pdxx.setJssj(jssj);
			pdxx.setLsy(lsy);
			pdxx.setFkrq(fkrq);
			pdxx.setFksj(fksj);
		}
		cursor.close();
		return pdxx;
	}

	@Override
	public List<Tpdxx> find(int page, int pagesize) {
		// TODO Auto-generated method stub
		List<Tpdxx> pdxxs = new ArrayList<Tpdxx>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("pdxx", null, null, null, null, null,
				"pdid asc", (page - 1) * pagesize + "," + pagesize);

		while (cursor.moveToNext()) {
			Tpdxx pdxx = new Tpdxx();
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			int pdid = cursor.getInt(cursor.getColumnIndex("pdid"));
			String jjrname = cursor.getString(cursor.getColumnIndex("jjrname"));
			String jjrtel = cursor.getString(cursor.getColumnIndex("jjrtel"));
			String jjraddr = cursor.getString(cursor.getColumnIndex("jjraddr"));
			String sjrname = cursor.getString(cursor.getColumnIndex("sjrname"));
			String sjrtel = cursor.getString(cursor.getColumnIndex("sjrtel"));
			String sjraddr = cursor.getString(cursor.getColumnIndex("sjraddr"));
			String memo = cursor.getString(cursor.getColumnIndex("memo"));
			String xfrq = cursor.getString(cursor.getColumnIndex("xfrq"));
			String xfsj = cursor.getString(cursor.getColumnIndex("xfsj"));
			String jsrq = cursor.getString(cursor.getColumnIndex("jsrq"));
			String jssj = cursor.getString(cursor.getColumnIndex("jssj"));
			int lsy = cursor.getInt(cursor.getColumnIndex("lsy"));
			String fkrq = cursor.getString(cursor.getColumnIndex("fkrq"));
			String fksj = cursor.getString(cursor.getColumnIndex("fksj"));

			pdxx.setId(id);
			pdxx.setPdid(pdid);
			pdxx.setJjrname(jjrname);
			pdxx.setJjraddr(jjraddr);
			pdxx.setJjrtel(jjrtel);
			pdxx.setSjrname(sjrname);
			pdxx.setSjraddr(sjraddr);
			pdxx.setSjrtel(sjrtel);
			pdxx.setMemo(memo);
			pdxx.setXfrq(xfrq);
			pdxx.setXfsj(xfsj);
			pdxx.setJsrq(jsrq);
			pdxx.setJssj(jssj);
			pdxx.setLsy(lsy);
			pdxx.setFkrq(fkrq);
			pdxx.setFksj(fksj);

			pdxxs.add(pdxx);
		}
		cursor.close();
		return pdxxs;
	}

	@Override
	public long getCount() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("pdxx", new String[] { "count(*)" }, null,
				null, null, null, null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.delete("pdxx", null, null);
		Log.d("Pdxx", "表Tpdxx已被清除！");
		DemoApplication app = (DemoApplication) context;
		app.put("num", 0 + "");
	}

	@Override
	public long getToProceedCount() {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select count(*) from pdxx where jsrq is null or fkrq is null order by pdid desc",
						null);
		cursor.moveToFirst();
		long result = cursor.getLong(0);
		cursor.close();
		return result;
	}

	@Override
	public List<Tpdxx> findToProceed() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select * from pdxx where jsrq is null or fkrq is null order by pdid desc",
						null);
		List<Tpdxx> pdxxs = new ArrayList<Tpdxx>();
		if (cursor.moveToFirst()) {
			Tpdxx pdxx = new Tpdxx();
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			int pdid = cursor.getInt(cursor.getColumnIndex("pdid"));
			String jjrname = cursor.getString(cursor.getColumnIndex("jjrname"));
			String jjrtel = cursor.getString(cursor.getColumnIndex("jjrtel"));
			String jjraddr = cursor.getString(cursor.getColumnIndex("jjraddr"));
			String sjrname = cursor.getString(cursor.getColumnIndex("sjrname"));
			String sjrtel = cursor.getString(cursor.getColumnIndex("sjrtel"));
			String sjraddr = cursor.getString(cursor.getColumnIndex("sjraddr"));
			String memo = cursor.getString(cursor.getColumnIndex("memo"));
			String xfrq = cursor.getString(cursor.getColumnIndex("xfrq"));
			String xfsj = cursor.getString(cursor.getColumnIndex("xfsj"));
			String jsrq = cursor.getString(cursor.getColumnIndex("jsrq"));
			String jssj = cursor.getString(cursor.getColumnIndex("jssj"));
			int lsy = cursor.getInt(cursor.getColumnIndex("lsy"));
			String fkrq = cursor.getString(cursor.getColumnIndex("fkrq"));
			String fksj = cursor.getString(cursor.getColumnIndex("fksj"));

			pdxx.setId(id);
			pdxx.setPdid(pdid);
			pdxx.setJjrname(jjrname);
			pdxx.setJjraddr(jjraddr);
			pdxx.setJjrtel(jjrtel);
			pdxx.setSjrname(sjrname);
			pdxx.setSjraddr(sjraddr);
			pdxx.setSjrtel(sjrtel);
			pdxx.setMemo(memo);
			pdxx.setXfrq(xfrq);
			pdxx.setXfsj(xfsj);
			pdxx.setJsrq(jsrq);
			pdxx.setJssj(jssj);
			pdxx.setLsy(lsy);
			pdxx.setFkrq(fkrq);
			pdxx.setFksj(fksj);

			pdxxs.add(pdxx);
		}
		cursor.close();
		return pdxxs;
	}

	@Override
	public Tpdxx findByPdid(int pdid) {
		// TODO Auto-generated method stub
		Tpdxx pdxx = new Tpdxx();

		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("pdxx", new String[] { "jjrname", "jjrtel" },
				new String("pdid = " + pdid), null, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			// long result = cursor.getLong(0);
			pdxx.setJjrname(cursor.getString(0));
			pdxx.setJjrtel(cursor.getString(1));
		}
		cursor.close();
		db.close();
		return pdxx;
	}

}
