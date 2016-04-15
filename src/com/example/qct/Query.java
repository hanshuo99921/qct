package com.example.qct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.duguang.baseanimation.ui.imitate.TaobaoPathbutton.TaobaoActivity;
import com.example.qct.util.DatabaseOpenHelper;

public class Query extends Activity {
	private static final String NAME = "NAME";
	private static final String IS_EVEN = "IS_EVEN";
	private static final String TAG = "QueryActivity";
	private DatabaseOpenHelper dbOpenHelper;

	Context context;
	ExpandableListView expandableListView;
	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
	List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

	private ExpandableListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandablelist);

		context = this;
		expandableListView = (ExpandableListView) findViewById(R.id.query_list);
		dbOpenHelper = new DatabaseOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		Cursor cursor = db.query("pdxx", null, null, null, null, null, " id asc");
		int i = 0;
		while (cursor.moveToNext()) {
			i = i + 1;
			int pdid = cursor.getInt(cursor.getColumnIndex("pdid"));
			String jjraddr = cursor.getString(cursor.getColumnIndex("jjraddr"));
			String jjrname = cursor.getString(cursor.getColumnIndex("jjrname"));
			String jjrtel = cursor.getString(cursor.getColumnIndex("jjrtel"));
			String memo = cursor.getString(cursor.getColumnIndex("memo"));
			String xfrq = cursor.getString(cursor.getColumnIndex("xfrq"));
			String xfsj = cursor.getString(cursor.getColumnIndex("xfsj"));

			Map<String, String> curGroupMap = new HashMap<String, String>();
			curGroupMap.put("pdid", pdid + "");
			curGroupMap.put("no", i + ".");
			curGroupMap.put("jjraddr", jjraddr);
			curGroupMap.put("jjrname", jjrname);
			curGroupMap.put("jjrtel", jjrtel);
			curGroupMap.put("memo", memo);
			curGroupMap.put("xfsj", xfrq + " " + xfsj);

			groupData.add(curGroupMap);

			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			Cursor cursor2 = db.query("yjxx", null, "pdid=?", new String[] { pdid + "" }, null, null, "id asc");
			int j = 0;
			while (cursor2.moveToNext()) {
				j = j + 1;
				int yjid = cursor2.getInt(cursor.getColumnIndex("id"));
				String tm = cursor2.getString(cursor2.getColumnIndex("tm"));

				Map<String, String> curChildMap = new HashMap<String, String>();
				curChildMap.put("yjno", j + ".");
				curChildMap.put("tm", tm);

				children.add(curChildMap);
			}
			childData.add(children);

		}

		cursor = db.query("yjxx", new String[] { "count(*)" }, "pdid=?", new String[] { "0" }, null, null, " id asc");
		cursor.moveToFirst();
		if (cursor.getLong(0) > 0) {
			Cursor cursor3 = db.query("yjxx", null, "pdid=?", new String[] { "0" }, null, null, null);
			int j = 0;
			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			Map<String, String> curGroupMap = new HashMap<String, String>();
			curGroupMap.put("pdid", "0");
			curGroupMap.put("no", i + 1 + ".");

			groupData.add(curGroupMap);
			while (cursor3.moveToNext()) {
				j = j + 1;

				Map<String, String> curChildMap = new HashMap<String, String>();
				curChildMap.put("yjno", j + ".");
				curChildMap.put("tm", cursor3.getString(cursor3.getColumnIndex("tm")));
				children.add(curChildMap);
			}
			childData.add(children);
		}
		// LayoutInflater inflate = LayoutInflater.from(context);
		// View view = inflate.inflate(R.layout.query_group, null);

		String[] groupFrom = { "no", "pdid", "jjraddr", "jjrname", "jjrtel", "memo", "xfsj" };
		int[] groupTo = { R.id.query_group_no, R.id.query_group_pdid, R.id.query_group_jjraddr, R.id.query_group_jjrname, R.id.query_group_jjrtel, R.id.query_group_memo,
				R.id.query_group_xfsj };

		String[] childFrom = { "yjno", "tm" };
		int[] childTo = { R.id.query_child_no, R.id.query_child_tm };

		// Set up our adapter
		mAdapter = new SimpleExpandableListAdapter(this, groupData, R.layout.query_group, groupFrom, groupTo, childData, R.layout.query_child, childFrom, childTo);
		expandableListView.setAdapter(mAdapter);

		cursor.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent();
			// intent.setClass(context, MainActivity2.class);
			intent.setClass(context, TaobaoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
