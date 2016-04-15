package com.example.qct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qct.util.BadgeView;
import com.example.qct.util.DatabaseOpenHelper;
import com.example.service.PdxxServiceI;
import com.example.service.impl.PdxxServiceImpl;

public class MainActivity2 extends ActionBarActivity {

	private static final String TAG = "MainActivity";
	private static int id = 100;
	private RequestQueue MainActivity2_queue;
	private Context context;
	private ListView listView;
	private ArrayAdapter<String> mAdapter;
	private SimpleAdapter sAdapter;
	private ArrayList<String> mStrings = new ArrayList<String>();
	private List<Map<String, Object>> mdata = new ArrayList<Map<String, Object>>();
	private OnItemClickListener listener;
	private int[] to = { R.id.info_item_no, R.id.info_item_jjraddr, R.id.info_item_jjrtel, R.id.info_item_jjrname, R.id.info_item_memo, R.id.info_item_pdid, R.id.info_item_xfsj };
	private String[] from = { "info_item_no", "info_item_jjraddr", "info_item_jjrtel", "info_item_jjrname", "info_item_memo", "info_item_pdid", "info_item_xfsj" };
	private ImageButton btn_info;
	private BadgeView badge;
	private RequestQueue queue;
	private PdxxServiceI pdxxService;
	private String lsy;
	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity2);

		context = this;
		DemoApplication app = (DemoApplication) getApplication();
		lsy = (String) app.get("lsy");

		pdxxService = new PdxxServiceImpl(context);
		listView = (ListView) findViewById(R.id.main_list);
		Button btn_clear = (Button) findViewById(R.id.main_cleardata);
		// btn_clear.setVisibility(View.GONE);
		btn_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// PdxxServiceImpl pdxxService = new PdxxServiceImpl(
				// getApplication());
				// pdxxService.clear();
				new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon).setTitle(R.string.clear_database)
						.setPositiveButton(R.string.need_short_message_yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// 选择是，清除数据库
								context.deleteDatabase("qct");
								DemoApplication app = (DemoApplication) getApplication();
								app.put("num", 0 + "");
								finish();

							}
						}).setNegativeButton(R.string.need_short_message_no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// 选择否，不做任何操作
							}
						}).create().show();

				// btn_info.performClick();
			}

		});

		btn_info = (ImageButton) findViewById(R.id.main_info);
		count = (int) pdxxService.getToProceedCount();

		if (count > 0) {
			badge = new BadgeView(this, btn_info);
			badge.setText(String.valueOf(count));
			badge.toggle(true);
		}
		btn_info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SQLiteDatabase db = new DatabaseOpenHelper(context).getReadableDatabase();
				Cursor cursor = db.rawQuery("select * from pdxx where (jsrq is null or fkrq is null) and lsy = '" + lsy + "' order by pdid desc", null);
				mdata = new ArrayList<Map<String, Object>>();
				int i = 0;
				while (cursor.moveToNext()) {
					i = i + 1;

					int pdid = cursor.getInt(cursor.getColumnIndex("pdid"));

					Map<String, Object> data = new HashMap<String, Object>();
					data.put("info_item_no", i);
					data.put("info_item_jjraddr", cursor.getString(cursor.getColumnIndex("jjraddr")));
					data.put("info_item_jjrtel", cursor.getString(cursor.getColumnIndex("jjrtel")));
					data.put("info_item_jjrname", cursor.getString(cursor.getColumnIndex("jjrname")));
					data.put("info_item_memo", cursor.getString(cursor.getColumnIndex("memo")));
					data.put("info_item_pdid", pdid);
					data.put("info_item_xfsj", cursor.getString(cursor.getColumnIndex("xfrq")) + " " + cursor.getString(cursor.getColumnIndex("xfsj")));
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
						String url = AppConst.Server_URL + "update_pdxx_jssj.php?pdid=" + pdid + "&jsrq=" + jsrq + "&jssj=" + jssj;
						JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								try {
									if (response.getBoolean("success")) {
										Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
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
					}

				}

				DemoApplication app = (DemoApplication) getApplication();
				Log.d(TAG, app.get("num") + "");
				if ((Integer) app.get("n") * (Integer) app.get("m") > 0) {
					app.put("num", (Integer) app.get("n") + (Integer) app.get("m"));
				}
				Log.d(TAG, app.get("num") + "");

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
				cursor.close();
				if (count > 0) {
					badge.toggle(false);
				}
			}

		});

		// btn_info.performClick();

		ImageButton btn_receive = (ImageButton) findViewById(R.id.main_receive);
		btn_receive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ReceiveActivity.class);
				intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				// finish();
			}

		});

		ImageButton btn_get = (ImageButton) findViewById(R.id.main_get);
		btn_get.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, List12.class);
				intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			}
		});

		ImageButton btn_feedback = (ImageButton) findViewById(R.id.main_feedback);
		btn_feedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, Feedback.class);
				intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(TAG, "onStart方法开始执行------------------");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(TAG, "onRestart方法开始执行------------------");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity2, menu);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// btn_info.performClick();
		Log.d(TAG, "onResume方法开始执行--------------------");
//		DemoApplication app = (DemoApplication) getApplication();
//		Log.d(TAG, app.get("num") + "");
//		Object a = app.get("num");
//		count = Integer.parseInt(String.valueOf(a));

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon).setTitle(R.string.alert_dialog_two_buttons_title3)
					.setPositiveButton(R.string.alert_dialog_ok2, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							/* User clicked OK so do some stuff */
							finish();
						}
					}).setNegativeButton(R.string.alert_dialog_cancel2, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							/* User clicked Cancel so do some stuff */
						}
					}).create().show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
