package com.example.qct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qct.util.DatabaseOpenHelper;

public class MainSetting extends Activity {

	private static final String TAG = "MainSetting";
	private Context context;
	private Button btn_clearButton;
	private Button btn_updateButton;
	private ProgressBar progressBar;
	private Dialog dialog;

	private DatabaseOpenHelper dbOpenHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
	private RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_setting);

		context = this;
		btn_clearButton = (Button) findViewById(R.id.main_setting_cleardata);
		btn_updateButton = (Button) findViewById(R.id.main_setting_updatedata);

		// btn_clear.setVisibility(View.GONE);
		btn_clearButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon)
						.setTitle(R.string.clear_database)
						.setPositiveButton(R.string.need_short_message_yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// 选择是，清除数据库
								context.deleteDatabase("qct");
								DemoApplication app = (DemoApplication) getApplication();
								app.put("num", 0 + "");
								Toast.makeText(context, R.string.clear_database_success, Toast.LENGTH_LONG).show();
							}
						}).setNegativeButton(R.string.need_short_message_no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// 选择否，不做任何操作
							}
						}).create().show();
			}

		});

		btn_updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				queue = Volley.newRequestQueue(context);
				String url = AppConst.Server_URL + "get_base_data.php";
				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
						new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								// Log.d(TAG, response.toString());

								ProgressDialog mypDialog=new ProgressDialog(context);
					            //实例化
					            mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					            //设置进度条风格，风格为圆形，旋转的
					            mypDialog.setTitle("正在同步数据......");
					            //设置ProgressDialog 标题
//					            mypDialog.setMessage(getResources().getString(R.string.second));
					            //设置ProgressDialog 提示信息
//					            mypDialog.setIcon(R.drawable.android);
					            //设置ProgressDialog 标题图标
//					            mypDialog.setButton("Google",this);
					            //设置ProgressDialog 的一个Button
					            mypDialog.setIndeterminate(false);
					            //设置ProgressDialog 的进度条是否不明确
					            mypDialog.setCancelable(false);
					            //设置ProgressDialog 是否可以按退回按键取消
					            mypDialog.show();
					            //让ProgressDialog显示

								try {
									dbOpenHelper = new DatabaseOpenHelper(context);
									db = dbOpenHelper.getWritableDatabase();
									db.execSQL("delete  from ywzl");
									JSONArray njpmJsonArray = response.getJSONArray("njpm");

									// 更新内件品名表
									for (int i = 0; i < njpmJsonArray.length(); i++) {
										JSONObject njpmJsonObject = njpmJsonArray.getJSONObject(i);
										ContentValues values = new ContentValues();
										values.put("id", njpmJsonObject.getString("id"));
										values.put("name", njpmJsonObject.getString("name"));
										db.insert("ywzl", null, values);
										Log.d(TAG, njpmJsonObject.toString());
									}
									mypDialog.setProgress(30);
									db.execSQL("delete  from jz");
									JSONArray jzkhJsonArray = response.getJSONArray("jzkh");
									// 更新记账客户表
									for (int i = 0; i < jzkhJsonArray.length(); i++) {
										JSONObject jzkhJsonObject = jzkhJsonArray.getJSONObject(i);
										Log.d(TAG, jzkhJsonObject.toString());
										ContentValues values = new ContentValues();
										values.put("id", jzkhJsonObject.getString("id"));
										values.put("name", jzkhJsonObject.getString("name"));
										values.put("tel", jzkhJsonObject.getString("tel"));
										values.put("addr", jzkhJsonObject.getString("addr"));
										values.put("part", jzkhJsonObject.getString("part"));
										values.put("flag", jzkhJsonObject.getString("flag"));
										values.put("memo", jzkhJsonObject.getString("memo"));
										mypDialog.setProgress(100-jzkhJsonArray.length()+i);
										Log.d(TAG, values.toString());
										db.insert("jz", null, values);
										// Log.d(TAG,
										// jzkhJsonObject.toString());
									}
									mypDialog.setProgress(100);
									db.close();
									dbOpenHelper.close();
									
									Toast.makeText(context, "本机数据已更新完毕！", Toast.LENGTH_SHORT).show();
									mypDialog.dismiss();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								// Log.e("TAG", error.getMessage(), error);
							}
						});
				queue.add(jsonObjectRequest);

			}
		});

		Button btn_test = (Button) findViewById(R.id.main_setting_parcelbill);
		btn_test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, ParcelFormActivity.class);
				startActivity(intent);
			}
		});
	}

}
