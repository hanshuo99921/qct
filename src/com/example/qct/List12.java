package com.example.qct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.duguang.baseanimation.ui.imitate.TaobaoPathbutton.TaobaoActivity;
import com.example.qct.util.DatabaseOpenHelper;
import com.example.qct.util.Tpdxx;
import com.example.qct.util.Tyjxx;
import com.example.service.impl.PdxxServiceImpl;
import com.example.service.impl.YjxxServiceImpl;
import com.zijunlin.Zxing.Demo.CaptureActivity;

public class List12 extends ListActivity implements OnClickListener, OnKeyListener {

	private TextView mUserText;
	private static final String TAG = "List12";
	private RequestQueue queue;

	private ListView listView;
	private EditText editText;
	private ImageButton btn_input;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> mStrings = new ArrayList<String>();
	private Bundle bundle;
	private Context context;
	private OnItemClickListener listener;
	DemoApplication app;
	private final static int REQUEST_CODE = 1;
	private final static int REQUEST_CODE2 = 100;
	private String tm;

	private EditText sjrnameEditText;
	private EditText sjraddrEditText;
	private EditText sjrtelEditText;
	private EditText jjrnameEditText;
	private EditText jjraddrEditText;
	private EditText jjrtelEditText;
	private EditText zlEditText;
	private EditText zfEditText;
	private EditText memoEditText;
	private int modify_flag;
	private DatabaseOpenHelper dbOpenHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
	private String sjrname;
	private String sjraddr;
	private String sjrtel;
	private String jjrname;
	private String jjraddr;
	private String jjrtel;
	private String zl;
	private String zf;
	private String lrrq;
	private String lrsj;
	private String memo;
	private ArrayList<Tyjxx> MapTyjxx_List;
	private Tyjxx yjxx;
	private Tpdxx pdxx;
	private Boolean withPdxxFlag;
	private String fkrq;
	private String fksj;
	private int pdid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_12);

		context = this;
		editText = (EditText) findViewById(R.id.barcode);
		listView = getListView();
		tm = "";
		bundle = this.getIntent().getExtras();
		app = (DemoApplication) getApplication();
		if (bundle == null) {
			bundle = (Bundle) app.get("mBundle");
		}

		sjrname = "";
		sjraddr = "";
		sjrtel = "";
		jjrname = "";
		jjraddr = "";
		jjrtel = "";
		zl = "";
		zf = "";
		lrrq = "";
		lrsj = "";
		memo = "";
		withPdxxFlag = false;
		fkrq = "";
		fksj = "";
		pdid = 0;

		MapTyjxx_List = new ArrayList<Tyjxx>();
		yjxx = new Tyjxx();

		mAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, R.id.textView, mStrings);
		setListAdapter(mAdapter);
		listView.setOnItemClickListener(listener);

		TextView tv_id = (TextView) findViewById(R.id.list_12_no);
		TextView tv_pdid = (TextView) findViewById(R.id.list_12_pdid);
		TextView tv_jjrname = (TextView) findViewById(R.id.list_12_jjrname);
		TextView tv_jjraddr = (TextView) findViewById(R.id.list_12_jjraddr);
		TextView tv_jjrtel = (TextView) findViewById(R.id.list_12_jjrtel);
		TextView tv_memo = (TextView) findViewById(R.id.list_12_memo);
		TextView tv_xfsj = (TextView) findViewById(R.id.list_12_xfsj);

		pdxx = new Tpdxx();
		if (bundle != null) {
			// Log.d(TAG, bundle.getString("pdid"));
			tv_id.setText(bundle.getString("id"));
			tv_pdid.setText(bundle.getString("pdid"));
			tv_jjrname.setText(bundle.getString("jjrname"));
			tv_jjraddr.setText(bundle.getString("jjraddr"));
			tv_jjrtel.setText(bundle.getString("jjrtel"));
			tv_memo.setText(bundle.getString("memo"));
			tv_xfsj.setText(bundle.getString("xfsj"));

			pdxx.setId(Integer.valueOf(bundle.getString("id")));
			pdxx.setPdid(Integer.valueOf(bundle.getString("pdid")));
			pdxx.setJjrname(bundle.getString("jjrname"));
			pdxx.setJjraddr(bundle.getString("jjraddr"));
			pdxx.setJjrtel(bundle.getString("jjrtel"));
			pdxx.setMemo(bundle.getString("memo"));
			pdxx.setXfsj(bundle.getString("xfsj"));
			pdxx.setSjraddr(bundle.getString("sjraddr"));
			pdxx.setSjrname(bundle.getString("sjrname"));
			pdxx.setSjrtel(bundle.getString("sjrtel"));
			withPdxxFlag = true;
			// Log.d(TAG, pdxx.toString());
		}

		listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				// Log.d(TAG, mStrings.get(position).toString());
				tm = mStrings.get(position).toString();
				// Log.d(TAG, tm);
				new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon)
						.setTitle(R.string.alert_dialog_two_buttons_title)
						.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

								/* User clicked OK so do some stuff */
								// 删除
								Tyjxx t = new Tyjxx();
								// Log.d(TAG, mStrings.get(position));
								t.setTm(mStrings.get(position));
								if (MapTyjxx_List.contains(t)) {
									MapTyjxx_List.remove(t);
								}
								mStrings.remove(position);
								// Log.d(TAG, mStrings.toString());
								mAdapter.notifyDataSetChanged();

							}
						}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								Intent intent = new Intent();
								intent.setClass(context, ParcelFormActivity.class);// 从哪里跳到哪里
								Bundle mbundle = bundle;
								if (mbundle == null) {
									mbundle = new Bundle();
								}
								mbundle.putStringArrayList("listView", mStrings);
								mbundle.putCharSequence("tm", tm);
								mbundle.putCharSequence("from", "List12");
								intent.putExtras(mbundle);
								// startActivity(intent);
								startActivityForResult(intent, REQUEST_CODE2);
							}
						}).create().show();
			}

		};

		ImageButton button = (ImageButton) findViewById(R.id.capture);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(List12.this, CaptureActivity.class);// 从哪里跳到哪里
				Bundle mbundle = bundle;
				if (mbundle == null) {
					mbundle = new Bundle();
				}
				mbundle.putStringArrayList("listView", mStrings);
				mbundle.putCharSequence("from", "List12");
				intent.putExtras(mbundle);
				// startActivity(intent);
				startActivityForResult(intent, REQUEST_CODE);

			}

		});

		btn_input = (ImageButton) findViewById(R.id.input);
		btn_input.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String barcode = editText.getText().toString();
				barcode = barcode.replace("\n", "");
				if (barcode != null && barcode.trim().length() > 0) {
					mStrings.add(barcode);
					tm = barcode;
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					} else {
						mAdapter = new ArrayAdapter<String>(context, R.layout.listview_item, R.id.textView, mStrings);
						setListAdapter(mAdapter);
					}
					listView.setOnItemClickListener(listener);

					dbOpenHelper = new DatabaseOpenHelper(context);
					db = dbOpenHelper.getReadableDatabase();
					cursor = db.rawQuery("select * from yjxx where tm = '" + tm + "'", null);
					// Log.d(TAG, tm);

					LayoutInflater factory = LayoutInflater.from(context);
					final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
					sjrnameEditText = (EditText) textEntryView.findViewById(R.id.sjrname_edit);
					sjraddrEditText = (EditText) textEntryView.findViewById(R.id.sjraddr_edit);
					sjrtelEditText = (EditText) textEntryView.findViewById(R.id.sjrtel_edit);
					jjrnameEditText = (EditText) textEntryView.findViewById(R.id.jjrname_edit);
					jjraddrEditText = (EditText) textEntryView.findViewById(R.id.jjraddr_edit);
					jjrtelEditText = (EditText) textEntryView.findViewById(R.id.jjrtel_edit);
					zlEditText = (EditText) textEntryView.findViewById(R.id.zl_edit);
					zfEditText = (EditText) textEntryView.findViewById(R.id.zf_edit);
					memoEditText = (EditText) textEntryView.findViewById(R.id.memo_edit);
					modify_flag = 0;

					// Log.d(TAG, pdxx.toString());
					jjrnameEditText.setText(pdxx.getJjrname());
					jjraddrEditText.setText(pdxx.getJjraddr());
					jjrtelEditText.setText(pdxx.getJjrtel());
					sjrnameEditText.setText(pdxx.getSjrname());
					sjraddrEditText.setText(pdxx.getSjraddr());
					sjrtelEditText.setText(pdxx.getSjrtel());

					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						sjrnameEditText.setText(cursor.getString(cursor.getColumnIndex("sjrname")));
						sjraddrEditText.setText(cursor.getString(cursor.getColumnIndex("sjraddr")));
						sjrtelEditText.setText(cursor.getString(cursor.getColumnIndex("sjrtel")));
						jjrnameEditText.setText(cursor.getString(cursor.getColumnIndex("jjrname")));
						jjraddrEditText.setText(cursor.getString(cursor.getColumnIndex("jjraddr")));
						jjrtelEditText.setText(cursor.getString(cursor.getColumnIndex("jjrtel")));
						zlEditText.setText(cursor.getString(cursor.getColumnIndex("zl")));
						zfEditText.setText(cursor.getString(cursor.getColumnIndex("zf")));
						memoEditText.setText(cursor.getString(cursor.getColumnIndex("memo")));
						modify_flag = 1;

					}

					Intent intent = new Intent();
					intent.setClass(context, ParcelFormActivity.class);// 从哪里跳到哪里
					Bundle mbundle = bundle;
					if (mbundle == null) {
						mbundle = new Bundle();
					}
					mbundle.putStringArrayList("listView", mStrings);
					mbundle.putCharSequence("tm", tm);
					mbundle.putCharSequence("from", "List12");
					if (withPdxxFlag) {
						mbundle.putCharSequence("id", pdxx.getId() + "");
						mbundle.putCharSequence("pdid", pdxx.getPdid() + "");
						mbundle.putCharSequence("jjrname", pdxx.getJjrname());
						mbundle.putCharSequence("jjraddr", pdxx.getJjraddr());
						mbundle.putCharSequence("jjrtel", pdxx.getJjrtel());
						mbundle.putCharSequence("memo", pdxx.getMemo());
						mbundle.putCharSequence("sjrname", pdxx.getSjrname());
						mbundle.putCharSequence("sjraddr", pdxx.getSjraddr());
						mbundle.putCharSequence("sjrtel", pdxx.getSjrtel());
					}
					intent.putExtras(mbundle);
					// startActivity(intent);
					startActivityForResult(intent, REQUEST_CODE2);

				}
				editText.setText("");
			}

		});

		Button btn_finish = (Button) findViewById(R.id.finish);
		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Log.d(TAG, mStrings.size() + "");
				Toast toast;
				if (mStrings.size() > 0) {
					Bundle mBundle = bundle;
					if (mBundle != null && mBundle.containsKey("pdid")) {
						Log.d(TAG, "派单ID：" + mBundle.getString("pdid"));
					}

					queue = Volley.newRequestQueue(getApplicationContext());
					String url = "";
					// "MyLogin.php?account=";
					//
					PdxxServiceImpl pdxxService = new PdxxServiceImpl(getApplication());
					YjxxServiceImpl yjxxService = new YjxxServiceImpl(getApplication());

					if (mBundle != null && mBundle.containsKey("pdid")) {
						// 存在派单信息
						pdid = Integer.parseInt(mBundle.getString("pdid"));
						Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
						for (int i = 0; i < MapTyjxx_List.size(); i++) {
							Tyjxx ty = MapTyjxx_List.get(i);
							// Log.d(TAG, ty.toString());

							Map<String, String> yjxx = new HashMap<String, String>();
							yjxx.put("tm", ty.getTm());
							yjxx.put("sjrname", ty.getSjrname());
							yjxx.put("sjraddr", ty.getSjraddr());
							yjxx.put("sjrtel", ty.getSjrtel());
							yjxx.put("jjrname", ty.getJjrname());
							yjxx.put("jjraddr", ty.getJjraddr());
							yjxx.put("jjrtel", ty.getJjrtel());
							yjxx.put("jz", ty.getJz());
							yjxx.put("zl", ty.getZl());
							yjxx.put("ywzl", ty.getYwzl());
							yjxx.put("memo", ty.getMemo());
							yjxx.put("bjje", ty.getBjje());
							yjxx.put("dshk", ty.getDshk());
							yjxx.put("zf", ty.getZf());
							yjxx.put("bjf", ty.getBjf());
							yjxx.put("qtf", ty.getQtf());
							yjxx.put("zfy", ty.getZfy());
							yjxx.put("sjrff", ty.getSjrff());
							yjxx.put("lsy", ty.getLsy() + "");
							yjxx.put("part", ty.getPart() + "");
							yjxx.put("pdid", pdid + "");
							yjxx.put("fjfw", ty.getFjfw() + "");
							// Log.d(TAG, yjxx.toString());
							map.put(i + "", yjxx);

						}
						// Log.d(TAG, map.toString());
						RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
						JSONObject jsonObject = new JSONObject(map);
						String httpurl = AppConst.Server_URL + "save_yjxx_new.php";
						JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(httpurl, jsonObject,
								new Response.Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										// TODO Auto-generated method stub
										// Log.d(TAG, "response -> " +
										// response.toString());
										// Log.d(TAG, "pdid -> " + pdid +
										// " <-");
										try {
											String success_num = response.getString("success_num");
											if (success_num != null && Integer.parseInt(success_num) > 0) {
												JSONArray obj = response.getJSONArray("obj");
												String msg = "邮件 ";
												for (int i = 0; i < obj.length(); i++) {
													// Log.d(TAG, i + ":tm:" +
													// obj.getJSONObject(i).getString("tm"));
													msg += obj.getJSONObject(i).getString("tm") + " ";
													String fhtm = obj.getJSONObject(i).getString("tm");
													for (String s : mStrings) {
														if (s.equals(fhtm)) {
															mStrings.remove(s);
															Tyjxx t = new Tyjxx();
															t.setTm(fhtm);

															db = dbOpenHelper.getWritableDatabase();
															ContentValues values = new ContentValues();
															values.put("lrrq", obj.getJSONObject(i).getString("lrrq"));
															values.put("lrsj", obj.getJSONObject(i).getString("lrsj"));
															Log.d(TAG, "values ->" + values.toString() + "<-");
															db.update("yjxx", values, "tm=?", new String[] { s });
															values = new ContentValues();
															values.put("fkrq", obj.getJSONObject(i).getString("lrrq"));
															values.put("fksj", obj.getJSONObject(i).getString("lrsj"));
															db.update("pdxx", values, "pdid=?", new String[] { pdid
																	+ "" });
															MapTyjxx_List.remove(t);

															break;
														}
													}
												}
												// Log.d(TAG, "mStrings -> " +
												// mStrings.toString());
												mAdapter.notifyDataSetChanged();
												msg += "保存成功！";
												Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
												if (mStrings.size() > 0) {
													Toast.makeText(context, "请检查列表中邮件是否是其它机构录入！", Toast.LENGTH_LONG)
															.show();
												} else if (mStrings.size() == 0) {
													finish();
												}

												// 更新本机未处理派单条数
												DemoApplication app = (DemoApplication) getApplication();
												// Log.d(TAG, app.get("num") +
												// "");
												Object num = app.get("num");
												int j = Integer.parseInt(String.valueOf(num));
												j = j - 1;
												if (j < 0) {
													j = 0;
												}
												app.put("num", j);
												// Log.d(TAG,"已接收到本机尚未处理派单条数num："+j);

											} else {
												// Log.d(TAG, "obj为空");
												Toast.makeText(context, "保存失败，请检查列表中邮件是否是其它机构录入！", Toast.LENGTH_LONG)
														.show();
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
										// Log.e(TAG, error.getMessage(),
										// error);
									}
								}) {
							// 注意此处override的getParams()方法,在此处设置post需要提交的参数根本不起作用
							// 必须象上面那样,构成JSONObject当做实参传入JsonObjectRequest对象里
							// 所以这个方法在此处是不需要的
							// @Override
							// protected Map<String, String> getParams() {
							// Map<String, String> map = new HashMap<String,
							// String>();
							// map.put("name1", "value1");
							// map.put("name2", "value2");

							// return params;
							// }

							@Override
							public Map<String, String> getHeaders() {
								HashMap<String, String> headers = new HashMap<String, String>();
								headers.put("Accept", "application/json");
								headers.put("Content-Type", "application/json; charset=UTF-8");

								return headers;
							}
						};
						requestQueue.add(jsonRequest);

					} else {
						// 不存在派单信息
						// for (int i = 0; i < mStrings.size(); i++) {
						Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
						for (int i = 0; i < MapTyjxx_List.size(); i++) {
							Tyjxx ty = MapTyjxx_List.get(i);
							// Log.d(TAG, ty.toString());

							Map<String, String> yjxx = new HashMap<String, String>();
							yjxx.put("tm", ty.getTm());
							yjxx.put("sjrname", ty.getSjrname());
							yjxx.put("sjraddr", ty.getSjraddr());
							yjxx.put("sjrtel", ty.getSjrtel());
							yjxx.put("jjrname", ty.getJjrname());
							yjxx.put("jjraddr", ty.getJjraddr());
							yjxx.put("jjrtel", ty.getJjrtel());
							yjxx.put("jz", ty.getJz());
							yjxx.put("zl", ty.getZl());
							yjxx.put("ywzl", ty.getYwzl());
							yjxx.put("memo", ty.getMemo());
							yjxx.put("bjje", ty.getBjje());
							yjxx.put("dshk", ty.getDshk());
							yjxx.put("zf", ty.getZf());
							yjxx.put("bjf", ty.getBjf());
							yjxx.put("qtf", ty.getQtf());
							yjxx.put("zfy", ty.getZfy());
							yjxx.put("sjrff", ty.getSjrff());
							yjxx.put("lsy", ty.getLsy() + "");
							yjxx.put("part", ty.getPart() + "");
							yjxx.put("pdid", "0");
							yjxx.put("fjfw", ty.getFjfw() + "");
							// Log.d(TAG, yjxx.toString());
							map.put(i + "", yjxx);

						}
						// Log.d(TAG, map.toString());
						RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
						JSONObject jsonObject = new JSONObject(map);
						String httpurl = AppConst.Server_URL + "save_yjxx_new.php";
						JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(httpurl, jsonObject,
								new Response.Listener<JSONObject>() {

									@Override
									public void onResponse(JSONObject response) {
										// TODO Auto-generated method stub
										// Log.d(TAG, "response -> " +
										// response.toString());
										try {
											String success_num = response.getString("success_num");
											if (success_num != null && Integer.parseInt(success_num) > 0) {
												JSONArray obj = response.getJSONArray("obj");
												String msg = "邮件 ";
												for (int i = 0; i < obj.length(); i++) {
													// Log.d(TAG, i + ":tm:" +
													// obj.getJSONObject(i).getString("tm"));
													msg += obj.getJSONObject(i).getString("tm") + " ";
													String fhtm = obj.getJSONObject(i).getString("tm");
													for (String s : mStrings) {
														if (s.equals(fhtm)) {
															mStrings.remove(s);
															Tyjxx t = new Tyjxx();
															t.setTm(fhtm);

															db = dbOpenHelper.getWritableDatabase();
															ContentValues values = new ContentValues();
															values.put("lrrq", obj.getJSONObject(i).getString("lrrq"));
															values.put("lrsj", obj.getJSONObject(i).getString("lrsj"));
															Log.d(TAG, "values ->" + values.toString() + "<-");
															db.update("yjxx", values, "tm=?", new String[] { s });
															MapTyjxx_List.remove(t);
															break;
														}
													}
												}
												// Log.d(TAG, "mStrings -> " +
												// mStrings.toString());
												mAdapter.notifyDataSetChanged();
												msg += "保存成功！";
												Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
												if (mStrings.size() > 0) {
													Toast.makeText(context, "请检查列表中邮件是否是其它机构录入！", Toast.LENGTH_LONG)
															.show();
												} else if (mStrings.size() == 0) {
													cursor.close();
													db.close();
													finish();
												}
											} else {
												// Log.d(TAG, "obj为空");
												Toast.makeText(context, "保存失败，请检查列表中邮件是否是其它机构录入！", Toast.LENGTH_LONG)
														.show();
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
										// Log.e(TAG, error.getMessage(),
										// error);
									}
								}) {
							// 注意此处override的getParams()方法,在此处设置post需要提交的参数根本不起作用
							// 必须象上面那样,构成JSONObject当做实参传入JsonObjectRequest对象里
							// 所以这个方法在此处是不需要的
							// @Override
							// protected Map<String, String> getParams() {
							// Map<String, String> map = new HashMap<String,
							// String>();
							// map.put("name1", "value1");
							// map.put("name2", "value2");

							// return params;
							// }

							@Override
							public Map<String, String> getHeaders() {
								HashMap<String, String> headers = new HashMap<String, String>();
								headers.put("Accept", "application/json");
								headers.put("Content-Type", "application/json; charset=UTF-8");

								return headers;
							}
						};
						requestQueue.add(jsonRequest);

					}
					Toast.makeText(
							getApplicationContext(),
							getResources().getString(R.string.with_mail_begin) + mStrings.size()
									+ getResources().getString(R.string.with_mail_end), Toast.LENGTH_SHORT).show();
					// Intent intent = new Intent();
					// intent.setClass(context, TaobaoActivity.class);
					// intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					// startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), R.string.no_mail, Toast.LENGTH_SHORT).show();
				}
				// finish();

			}

		});

		if (bundle != null && bundle.containsKey("listView")) {
			mStrings = bundle.getStringArrayList("listView");
		}

		// listView = (ListView) findViewById(R.id.list);
		if (bundle != null && bundle.containsKey("barcode")) {
			mStrings.add(bundle.getString("barcode"));

			mAdapter.notifyDataSetChanged();

			editText.setOnClickListener(this);
			editText.setOnKeyListener(this);
		}

		editText.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// 自己设定的事件
					btn_input.performClick();
				}
				return false;
			}
		});
	}

	public void onClick(View v) {
		sendText();
	}

	private void sendText() {
		String text = mUserText.getText().toString();
		mAdapter.add(text);
		mUserText.setText(null);
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_ENTER:
				// sendText();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon)
					.setTitle(R.string.alert_dialog_two_buttons_title2)
					.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							/* User clicked OK so do some stuff */
							Intent intent = new Intent();
							// intent.setClass(context, MainActivity2.class);
							intent.setClass(context, TaobaoActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							startActivity(intent);

							finish();
						}
					}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							/* User clicked Cancel so do some stuff */
						}
					}).create().show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == CaptureActivity.RESULT_CODE) {
			bundle = data.getExtras();
			String strResult = bundle.getString("barcode");
			strResult = strResult.replace("\n", "");
			// Log.i(TAG, "onActivityResult: " + strResult);
			Toast.makeText(context, strResult, Toast.LENGTH_SHORT).show();
			mStrings.add(strResult);
			mAdapter.notifyDataSetChanged();
			listView.setOnItemClickListener(listener);
			tm = strResult;

			dbOpenHelper = new DatabaseOpenHelper(context);
			db = dbOpenHelper.getReadableDatabase();
			cursor = db.rawQuery("select * from yjxx where tm = '" + tm + "'", null);

			LayoutInflater factory = LayoutInflater.from(context);
			final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
			sjrnameEditText = (EditText) textEntryView.findViewById(R.id.sjrname_edit);
			sjraddrEditText = (EditText) textEntryView.findViewById(R.id.sjraddr_edit);
			sjrtelEditText = (EditText) textEntryView.findViewById(R.id.sjrtel_edit);
			jjrnameEditText = (EditText) textEntryView.findViewById(R.id.jjrname_edit);
			jjraddrEditText = (EditText) textEntryView.findViewById(R.id.jjraddr_edit);
			jjrtelEditText = (EditText) textEntryView.findViewById(R.id.jjrtel_edit);
			zlEditText = (EditText) textEntryView.findViewById(R.id.zl_edit);
			zfEditText = (EditText) textEntryView.findViewById(R.id.zf_edit);
			memoEditText = (EditText) textEntryView.findViewById(R.id.memo_edit);
			modify_flag = 0;

			// Log.d(TAG, pdxx.toString());
			jjrnameEditText.setText(pdxx.getJjrname());
			jjraddrEditText.setText(pdxx.getJjraddr());
			jjrtelEditText.setText(pdxx.getJjrtel());
			sjrnameEditText.setText(pdxx.getSjrname());
			sjraddrEditText.setText(pdxx.getSjraddr());
			sjrtelEditText.setText(pdxx.getSjrtel());

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				sjrnameEditText.setText(cursor.getString(cursor.getColumnIndex("sjrname")));
				sjraddrEditText.setText(cursor.getString(cursor.getColumnIndex("sjraddr")));
				sjrtelEditText.setText(cursor.getString(cursor.getColumnIndex("sjrtel")));
				jjrnameEditText.setText(cursor.getString(cursor.getColumnIndex("jjrname")));
				jjraddrEditText.setText(cursor.getString(cursor.getColumnIndex("jjraddr")));
				jjrtelEditText.setText(cursor.getString(cursor.getColumnIndex("jjrtel")));
				zlEditText.setText(cursor.getString(cursor.getColumnIndex("zl")));
				zfEditText.setText(cursor.getString(cursor.getColumnIndex("zf")));
				memoEditText.setText(cursor.getString(cursor.getColumnIndex("memo")));
				modify_flag = 1;

			}

			Intent intent = new Intent();
			intent.setClass(context, ParcelFormActivity.class);// 从哪里跳到哪里
			Bundle mbundle = bundle;
			if (mbundle == null) {
				mbundle = new Bundle();
			}
			mbundle.putStringArrayList("listView", mStrings);
			mbundle.putCharSequence("tm", tm);
			mbundle.putCharSequence("from", "List12");
			intent.putExtras(mbundle);
			// startActivity(intent);
			startActivityForResult(intent, REQUEST_CODE2);

		} else if (requestCode == REQUEST_CODE2 && resultCode == ParcelFormActivity.RESULT_CODE) {
			bundle = data.getExtras();
			// Log.d(TAG, bundle.getString("jjraddr"));
			Tyjxx yjxx = new Tyjxx();
			yjxx.setSjrname(bundle.getString("sjrname"));
			yjxx.setSjraddr(bundle.getString("sjraddr"));
			yjxx.setSjrtel(bundle.getString("sjrtel"));
			yjxx.setJjrname(bundle.getString("jjrname"));
			yjxx.setJjraddr(bundle.getString("jjraddr"));
			yjxx.setJjrtel(bundle.getString("jjrtel"));
			yjxx.setZl(bundle.getInt("zl") + "");
			yjxx.setZf(bundle.getString("yf"));
			yjxx.setLrrq(bundle.getString("lrrq"));
			yjxx.setLrsj(bundle.getString("lrsj"));
			yjxx.setTm(bundle.getString("tm"));
			yjxx.setMemo(bundle.getString("memo"));
			yjxx.setBjje(bundle.getString("bjje"));
			yjxx.setBjf(bundle.getString("bjf"));
			yjxx.setDshk(bundle.getString("dshk"));
			yjxx.setQtf(bundle.getString("qtf"));
			yjxx.setZfy(bundle.getString("zfy"));
			yjxx.setSjrff(bundle.getString("sjrff"));
			yjxx.setJz(bundle.getInt("jz") + "");
			yjxx.setYwzl(bundle.getInt("njpm") + "");
			yjxx.setLsy(bundle.getInt("lsy"));
			yjxx.setPart(bundle.getShort("part"));
			yjxx.setFjfw(bundle.getInt("fjfw"));
			if (MapTyjxx_List.contains(yjxx)) {
				MapTyjxx_List.remove(yjxx);
			}
			MapTyjxx_List.add(yjxx);
		}

	}

}
