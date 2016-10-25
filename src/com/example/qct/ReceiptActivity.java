package com.example.qct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.qct.util.DatabaseOpenHelper;

public class ReceiptActivity extends Activity {

	private Context context;
	private static final String TAG = "TaobaoActivity";

	private String jjrname;
	private String jjraddr;
	private String jjrtel;
	private String sjrname;
	private String sjraddr;
	private String sjrtel;
	private String hztm;
	private String yyjtm;
	private int lsy;
	private int part;
	private String jz;
	private String yf;
	private String qtf;
	private String zfy;
	private ArrayList<String> khdmStrings;
	private ArrayAdapter<String> khdmAdapter;

	private EditText jjrnameEditText;
	private EditText jjraddrEditText;
	private EditText jjrtelEditText;
	private EditText sjrnameEditText;
	private EditText sjraddrEditText;
	private EditText sjrtelEditText;
	private EditText hztmEditText;
	private EditText yyjtmEditText;
	private Button saveButton;
	private Spinner jzSpinner;
	private EditText yfEditText;
	private EditText qtfEditText;
	private EditText zfyEditText;

	private RequestQueue queue;
	private JsonObjectRequest jsonObjectRequest;
	private DemoApplication app;
	private DatabaseOpenHelper dbOpenHelper;
	private SQLiteDatabase db;
	private Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);

		context = this;
		jjrnameEditText = (EditText) findViewById(R.id.receipt_jjrname_EditText);
		jjraddrEditText = (EditText) findViewById(R.id.receipt_jjraddr_EditText);
		jjrtelEditText = (EditText) findViewById(R.id.receipt_jjrtel_EditText);
		sjrnameEditText = (EditText) findViewById(R.id.receipt_sjrname_EditText);
		sjraddrEditText = (EditText) findViewById(R.id.receipt_sjraddr_EditText);
		sjrtelEditText = (EditText) findViewById(R.id.receipt_sjrtel_EditText);
		hztmEditText = (EditText) findViewById(R.id.receipt_hztm_editText);
		yyjtmEditText = (EditText) findViewById(R.id.receipt_yyjtm_editText);
		saveButton = (Button) findViewById(R.id.receipt_save_button);
		yfEditText = (EditText) findViewById(R.id.receipt_yf_EditText);
		qtfEditText = (EditText) findViewById(R.id.receipt_qtf_EditText);
		zfyEditText = (EditText) findViewById(R.id.receipt_zfy_EditText);
		jzSpinner = (Spinner) findViewById(R.id.receipt_khdm_spinner);

		khdmStrings = new ArrayList<String>();
		dbOpenHelper = new DatabaseOpenHelper(context);
		db = dbOpenHelper.getWritableDatabase();
		app = (DemoApplication) getApplication();
		Object object = app.get("lsy");
		lsy = Integer.parseInt(String.valueOf(object));
		object = app.get("part");
		part = Integer.parseInt(String.valueOf(object));

		queue = Volley.newRequestQueue(getApplicationContext());

		hztmEditText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// 自己设定的事件
					yyjtmEditText.setFocusable(true);
					yyjtmEditText.setFocusableInTouchMode(true);
					yyjtmEditText.requestFocus();
					yyjtmEditText.findFocus();
					return true;
				}
				return false;
			}
		});

		yyjtmEditText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					// if (keyCode == KeyEvent.KEYCODE_ENTER) {
					yyjtm = yyjtmEditText.getText().toString();
					if (yyjtm != null) {
						yyjtm = yyjtm.replaceAll("\\s*", "");
						yyjtmEditText.setText(yyjtm);
						if (yyjtm.length() >= 12 && yyjtm.length() <= 13) {
							// 到后台服务器查询原邮件条码对应的收、寄件人信息
							String url = AppConst.Server_URL + "get_yyjxx_by_tm.php?tm=" + yyjtm;
							Log.d(TAG, "url -> " + url + " <-");
							jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									// TODO Auto-generated method stub
									Log.d(TAG, "response ---> " + response.toString() + " <---");
									try {
										if (Boolean.valueOf(response.getString("success"))) {
											String hztm_server=response.getString("hztm");
											hztm = hztmEditText.getText().toString();
											if ("".equals(hztm_server) || (hztm != null && hztm.equals(hztm_server))) {
												jjrname = response.getString("sjrname");
												jjraddr = response.getString("sjraddr");
												jjrtel = response.getString("sjrtel");
												sjrname = response.getString("jjrname");
												sjraddr = response.getString("jjraddr");
												sjrtel = response.getString("jjrtel");

												jjraddrEditText.setText(jjraddr);
												jjrnameEditText.setText(jjrname);
												jjrtelEditText.setText(jjrtel);
												sjraddrEditText.setText(sjraddr);
												sjrnameEditText.setText(sjrname);
												sjrtelEditText.setText(sjrtel);

												saveButton.setEnabled(true);
											} else {
												Toast.makeText(context,
														"邮件" + yyjtm + "已做过回执！回执条码为：" + response.getString("hztm"),
														Toast.LENGTH_LONG).show();
												hztmEditText.selectAll();
											}
										} else {
											Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG)
													.show();
											yyjtmEditText.selectAll();
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									// TODO Auto-generated method stub
									// Log.e("TAG", error.getMessage(), error);
									if (error.networkResponse != null && error.networkResponse.data != null) {
										byte[] htmlBodyBytes = error.networkResponse.data;
										Log.e("TAG", new String(htmlBodyBytes), error);
										Toast.makeText(context, new String(htmlBodyBytes), Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(context, "网络错误！请检查后重试。", Toast.LENGTH_LONG).show();
									}
								}
							});
							queue.add(jsonObjectRequest);
						} else {
							Toast.makeText(context, "邮件条码不规范！", Toast.LENGTH_LONG);
							yyjtmEditText.setFocusable(true);
							yyjtmEditText.setFocusableInTouchMode(true);
							yyjtmEditText.requestFocus();
							yyjtmEditText.findFocus();
						}
					}
					return true;
				}
				return false;
			}
		});

		khdmStrings.add("");
		cursor = db.query("jz", null, "part=?", new String[] { part + "" }, null, null, null);
		while (cursor.moveToNext()) {
			khdmStrings.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		khdmAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, khdmStrings);
		jzSpinner.setAdapter(khdmAdapter);
		jzSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
				// TODO Auto-generated method stub

				cursor = db.query("jz", null, "name=?", new String[] { khdmStrings.get(pos) }, null, null, null);
				if (cursor.moveToNext()) {
					jz = cursor.getString(cursor.getColumnIndex("id"));
					jjrname = cursor.getString(cursor.getColumnIndex("name"));
					jjraddr = cursor.getString(cursor.getColumnIndex("addr"));
					jjrtel = cursor.getString(cursor.getColumnIndex("tel"));
				} else {
					jz = "0";
				}
				jjrnameEditText.setText(jjrname);
				jjraddrEditText.setText(jjraddr);
				jjrtelEditText.setText(jjrtel);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		yfEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				// zfy = zfy_EditText.getText().toString();
				// qtf = qtf_EditText.getText().toString();
				// yf = yf_EditText.getText().toString();
				// bjf = bjf_EditText.getText().toString();
				int b = 0;
				int y = 0;
				double q = 0;
				double z = 0;

				if (qtf != null && qtf.length() > 0) {
					q = Double.valueOf(qtf);
				}

				if (zfy != null && zfy.length() > 0) {
					z = Double.valueOf(zfy);
				}

				if (s.length() > 0) {
					y = Integer.valueOf(s.toString());
					z = q + b + y;
				} else {
					z = q + b;
				}

				if (z == 0) {
					zfy = "";
				} else {
					zfy = String.valueOf(z);
				}
				Log.d(TAG, "zfy->" + zfy + "<-");
				zfyEditText.setText(zfy);
				yf = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		qtfEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Log.d(TAG, s.toString());
				// zfy = zfy_EditText.getText().toString();
				// qtf = qtf_EditText.getText().toString();
				// yf = yf_EditText.getText().toString();
				// bjf = bjf_EditText.getText().toString();
				int b = 0;
				int y = 0;
				double q = 0;
				double z = 0;

				if (yf != null && yf.length() > 0) {
					y = Integer.valueOf(yf);
				}

				if (s.length() > 0) {
					q = Double.valueOf(s.toString());
					z = q + b + y;
				} else {
					z = y + b;
				}

				if (z == 0) {
					zfy = "";
				} else {
					zfy = String.valueOf(z);
				}

				Log.d(TAG, "zfy->" + zfy + "<-");
				zfyEditText.setText(zfy);
				qtf = s.toString();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				jjrname = jjrnameEditText.getText().toString();
				jjraddr = jjraddrEditText.getText().toString();
				jjrtel = jjrtelEditText.getText().toString();
				sjrname = sjrnameEditText.getText().toString();
				sjraddr = sjraddrEditText.getText().toString();
				sjrtel = sjrtelEditText.getText().toString();
				yyjtm = yyjtmEditText.getText().toString().replaceAll("\\s*", "");
				hztm = hztmEditText.getText().toString().replaceAll("\\s*", "");
				yf = yfEditText.getText().toString();
				qtf = qtfEditText.getText().toString();
				zfy = zfyEditText.getText().toString();

				String url = AppConst.Server_URL + "save_hz.php";
				Map<String, String> yjxx = new HashMap<String, String>();
				yjxx.put("jjrname", jjrname);
				yjxx.put("jjraddr", jjraddr);
				yjxx.put("jjrtel", jjrtel);
				yjxx.put("sjrname", sjrname);
				yjxx.put("sjraddr", sjraddr);
				yjxx.put("sjrtel", sjrtel);
				yjxx.put("hztm", hztm);
				yjxx.put("yyjtm", yyjtm);
				yjxx.put("lsy", lsy + "");
				yjxx.put("part", part + "");
				yjxx.put("jz", jz);
				yjxx.put("yf", yf);
				yjxx.put("qtf", qtf);
				yjxx.put("zfy", zfy);

				Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
				map.put("0", yjxx);
				JSONObject jsonObject = new JSONObject(map);
				JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(url, jsonObject,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								// TODO Auto-generated method stub
								Log.d(TAG, "response ---> " + response.toString() + " <---");
								try {
									Toast.makeText(context, response.getString("msg"), Toast.LENGTH_LONG).show();
									if (Boolean.valueOf(response.getString("success"))) {
										saveButton.setEnabled(false);
										finish();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								// TODO Auto-generated method stub

							}
						});
				queue.add(jsonRequest);
			}
		});

	}
}
