package com.example.qct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qct.util.DatabaseOpenHelper;

public class Main_info extends ActionBarActivity {
	private Context context;
	private static final String TAG = "Main_info";

	private List<Map<String, Object>> mdata = new ArrayList<Map<String, Object>>();
	private ArrayAdapter<String> mAdapter;
	private SimpleAdapter sAdapter;
	private RequestQueue queue;
	private ListView listView;
	private int[] to = { R.id.info_item_no, R.id.info_item_jjraddr, R.id.info_item_jjrtel, R.id.info_item_jjrname,
			R.id.info_item_memo, R.id.info_item_pdid, R.id.info_item_xfsj };
	private String[] from = { "info_item_no", "info_item_jjraddr", "info_item_jjrtel", "info_item_jjrname",
			"info_item_memo", "info_item_pdid", "info_item_xfsj" };

	private String jjrname;
	private String jjrtel;
	private String name;
	private DemoApplication app;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_info);

		context = this;
		listView = (ListView) findViewById(R.id.main_info_list);
		jjrname = "";

		app = (DemoApplication) getApplication();
		String lsy = String.valueOf(app.get("lsy"));
		name = String.valueOf(app.get("name"));

		db = new DatabaseOpenHelper(context).getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from pdxx where (jsrq is null or fkrq is null) and lsy = '" + lsy
				+ "' order by pdid desc", null);
		mdata = new ArrayList<Map<String, Object>>();
		int i = 0;
		while (cursor.moveToNext()) {
			i = i + 1;

			int pdid = cursor.getInt(cursor.getColumnIndex("pdid"));
			jjrname = cursor.getString(cursor.getColumnIndex("jjrname"));
			jjrtel = cursor.getString(cursor.getColumnIndex("jjrtel"));

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("info_item_no", i);
			data.put("info_item_jjraddr", cursor.getString(cursor.getColumnIndex("jjraddr")));
			data.put("info_item_jjrtel", cursor.getString(cursor.getColumnIndex("jjrtel")));
			data.put("info_item_jjrname", cursor.getString(cursor.getColumnIndex("jjrname")));
			data.put("info_item_memo", cursor.getString(cursor.getColumnIndex("memo")));
			data.put("info_item_pdid", pdid);
			data.put(
					"info_item_xfsj",
					cursor.getString(cursor.getColumnIndex("xfrq")) + " "
							+ cursor.getString(cursor.getColumnIndex("xfsj")));
			data.put("info_item_sjrname", cursor.getString(cursor.getColumnIndex("sjrname")));
			data.put("info_item_sjraddr", cursor.getString(cursor.getColumnIndex("sjraddr")));
			data.put("info_item_sjrtel", cursor.getString(cursor.getColumnIndex("sjrtel")));
			Log.d(TAG, data.toString());
			mdata.add(data);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			String now = df.format(new Date());
			Log.d(TAG, now);
			String jsrq = now.substring(0, 10);
			String jssj = now.substring(11, 19);
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			if (cursor.getString(cursor.getColumnIndex("jsrq")) == null) {
				// 更新本地数据库
				ContentValues values = new ContentValues();
				values.put("jsrq", jsrq);
				values.put("jssj", jssj);
				db.update("pdxx", values, " id=? ", new String[] { id + "" });

				// 更新后台服务器数据库
				queue = Volley.newRequestQueue(getApplicationContext());
				String url = AppConst.Server_URL + "update_pdxx_jssj.php?pdid=" + pdid + "&jsrq=" + jsrq + "&jssj="
						+ jssj;
				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getBoolean("success")) {
										Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();

										// 更新获取到本地尚未接收派单数量
										Object object = app.get("num");
										int num = Integer.parseInt(String.valueOf(object));
										num = num - 1;
										if (num < 0) {
											num = 0;
										}
										app.put("num", num);

										// 给寄件用户发送短信通知
										// 获取短信管理器
										SmsManager smsManager = SmsManager.getDefault();
										// 设置短信内容
										String msg = null;
										msg = getResources().getString(R.string.short_message_begin) + jjrname
												+ getResources().getString(R.string.Main_info_SM_middle1) + name
												+ getResources().getString(R.string.Main_info_SM_middle2);
										Log.d(TAG, msg);
										// 拆分短信内容（手机短信长度限制）
										// ArrayList<String>
										// list = smsManager
										// .divideMessage(msg);
										// 发送短信
										// for (String text :
										// list) {
										// smsManager
										// .sendTextMessage(
										// jjrtel,
										// null,
										// text,
										// null,
										// null);
										// }

										String tel = "";
										Log.d(TAG, response.getString("pdid"));
										Cursor cursor2 = db.query("pdxx", null, "pdid=?",
												new String[] { response.getString("pdid") }, null, null, null);
										if (cursor2.moveToNext()) {
											tel = cursor2.getString(cursor2.getColumnIndex("jjrtel"));
										}
										Log.d(TAG, "jjrtel -> " + tel + " <-");
										if (tel != null && tel.length() > 0) {
											smsManager.sendTextMessage(tel, null, msg, null, null);
										}
										cursor2.close();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								Log.e("TAG", error.getMessage(), error);
							}
						});
				queue.add(jsonObjectRequest);
			} else {
				// 更新获取到本地尚未接收派单数量
				Object object = app.get("num");
				int num = Integer.parseInt(String.valueOf(object));
				num = num - 1;
				if (num < 0) {
					num = 0;
				}
				app.put("num", num);
			}

		}

		// DemoApplication app = (DemoApplication) getApplication();
		// Log.d(TAG, app.get("num") + "");
		// if ((Integer) app.get("n") * (Integer) app.get("m") > 0) {
		// app.put("num", (Integer) app.get("n") + (Integer) app.get("m"));
		// }
		// Log.d(TAG, app.get("num") + "");

		Log.d(TAG, mdata.toString());
		if (mdata.size() > 0) {
			// 显示待处理派单信息
			sAdapter = new SimpleAdapter(context, mdata, R.layout.info_item, from, to);
			listView.setAdapter(sAdapter);
			Animation anim = AnimationUtils.loadAnimation(context, R.anim.appear_top_left_in);
			listView.startAnimation(anim);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();
					bundle.putString("id", mdata.get(position).get("info_item_no").toString());
					bundle.putString("pdid", mdata.get(position).get("info_item_pdid").toString());
					bundle.putString("jjrname", mdata.get(position).get("info_item_jjrname").toString());
					bundle.putString("jjraddr", mdata.get(position).get("info_item_jjraddr").toString());
					bundle.putString("jjrtel", mdata.get(position).get("info_item_jjrtel").toString());
					bundle.putString("sjrname", mdata.get(position).get("info_item_sjrname").toString());
					bundle.putString("sjraddr", mdata.get(position).get("info_item_sjraddr").toString());
					bundle.putString("sjrtel", mdata.get(position).get("info_item_sjrtel").toString());
					bundle.putString("memo", mdata.get(position).get("info_item_memo").toString());
					bundle.putString("xfsj", mdata.get(position).get("info_item_xfsj").toString());
					DemoApplication app = (DemoApplication) getApplication();
					bundle.putString("lsy", (String) app.get("id"));
					Intent intent = new Intent();
					intent.putExtras(bundle);
					Log.d(TAG, bundle.toString());
					intent.setClass(context, List12.class);// 从哪里跳到哪里
					overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
					// intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}

			});
		} else {
			Toast.makeText(context, "没有未处理的信息！", Toast.LENGTH_SHORT).show();
			finish();
		}
		cursor.close();
		// db.close();
	}
}
