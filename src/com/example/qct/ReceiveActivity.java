package com.example.qct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.zijunlin.Zxing.Demo.CaptureActivity;

public class ReceiveActivity extends ListActivity implements OnClickListener, OnKeyListener {

	private static final String TAG = "ReceiveActivity";
	private Context context;
	private String tm;
	private ListView listView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> mStrings;
	private EditText editText;
	private OnItemClickListener listener;
	private final static int REQUEST_CODE = 3;
	private Bundle bundle;
	private DemoApplication app;
	private TextView mUserText;
	private Button btn_finish;
	private ImageButton btn_capture;
	private ImageButton btn_input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive);

		context = this;
		editText = (EditText) findViewById(R.id.receive_barcode);
		btn_capture = (ImageButton) findViewById(R.id.receive_capture);
		btn_finish = (Button) findViewById(R.id.receive_finish);
		btn_input = (ImageButton) findViewById(R.id.receive_input);
		listView = getListView();
		mStrings = new ArrayList<String>();
		mAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, R.id.textView, mStrings);
		setListAdapter(mAdapter);
		listView.setOnItemClickListener(listener);
		bundle = this.getIntent().getExtras();
		app = (DemoApplication) getApplication();
		if (bundle == null) {
			bundle = (Bundle) app.get("mBundle");
		}

		listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				// Log.d(TAG, mStrings.get(position).toString());
				tm = mStrings.get(position).toString();
				new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon).setTitle(R.string.alert_dialog_two_buttons_title)
						.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

								/* User clicked OK so do some stuff */
								// 删除
								mStrings.remove(position);
								Log.d(TAG, mStrings.toString());
								mAdapter.notifyDataSetChanged();
							}
						}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								/* User clicked Cancel so do some stuff */
							}
						}).create().show();
			}
		};
		btn_capture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, CaptureActivity.class);// 从哪里跳到哪里
				Bundle mbundle = bundle;
				if (mbundle == null) {
					mbundle = new Bundle();
				}
				Log.d(TAG, mStrings.toString());
				mbundle.putStringArrayList("listView", mStrings);
				mbundle.putCharSequence("from", "ReceiveActivity");
				intent.putExtras(mbundle);
				startActivityForResult(intent, REQUEST_CODE);
			}

		});

		btn_input.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String barcode = editText.getText().toString();
				barcode = barcode.replace("\n", "");
				if (barcode != null && barcode.trim().length() > 0) {
					mStrings.add(barcode);
					if (mAdapter != null) {
						mAdapter.notifyDataSetChanged();
					} else {
						mAdapter = new ArrayAdapter<String>(context, R.layout.listview_item, R.id.textView, mStrings);
						setListAdapter(mAdapter);
					}
					listView.setOnItemClickListener(listener);
				}
				editText.setText("");
			}
		});

		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
				Map<String, String> map = new HashMap<String, String>();
				// map.put("1", "value1");
				// map.put("2", "value2");
				map.put("total", mStrings.size() + "");
				DemoApplication app = (DemoApplication) getApplication();
				Object lsyObject = app.get("lsy");
				map.put("lsy", String.valueOf(lsyObject));
				for (int i = 0; i < mStrings.size(); i++) {
					map.put(i + "", mStrings.get(i));
				}
				Log.d(TAG, map.toString());
				JSONObject jsonObject = new JSONObject(map);
				String httpurl = AppConst.Server_URL + "yjjs_by_pda.php";
				JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Method.POST, httpurl, jsonObject, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TAG, "response -> " + response.toString());
						try {
							mStrings.clear();
							mAdapter.notifyDataSetChanged();
							JSONArray tmArray = (JSONArray) response.getJSONArray("wjstm");
							int jsjs = response.getInt("jsjs");
							String msg = "本次接收成功" + jsjs + "件!";
							if (tmArray.length() > 0) {
								msg += "\n接收失败" + tmArray.length() + "件！";
								for (int i = 0; i < tmArray.length(); i++) {
									mStrings.add(tmArray.getString(i));
								}
								mAdapter.notifyDataSetChanged();
								listView.setOnItemClickListener(listener);
							}
							// Toast.makeText(context, msg,
							// Toast.LENGTH_LONG).show();
							new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon).setTitle(msg)
									.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {

										}
									}).create().show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, error.getMessage(), error);
					}
				}) {
					// 注意此处override的getParams()方法,在此处设置post需要提交的参数根本不起作用
					// 必须象上面那样,构成JSONObject当做实参传入JsonObjectRequest对象里
					// 所以这个方法在此处是不需要的
					// @Override
					// protected Map<String, String> getParams() {
					// Map<String, String> map = new HashMap<String, String>();
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
		});
		if (bundle != null && bundle.containsKey("listView")) {
			mStrings = bundle.getStringArrayList("listView");
		}

		// listView = (ListView) findViewById(R.id.list);
		if (bundle != null && bundle.containsKey("barcode")) {
			mStrings.add(bundle.getString("barcode"));

			mAdapter.notifyDataSetChanged();

			// editText.setOnClickListener(this);
			// editText.setOnKeyListener(this);
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

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onClick(View v) {
		sendText();
	}

	private void sendText() {
		String text = mUserText.getText().toString();
		mAdapter.add(text);
		mUserText.setText(null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_CODE && resultCode == CaptureActivity.RESULT_CODE3) {
			bundle = data.getExtras();
			String strResult = bundle.getString("barcode");
			Log.i(TAG, "onActivityResult: " + strResult);
			Toast.makeText(context, strResult, Toast.LENGTH_SHORT).show();
			mStrings.add(strResult);
			mAdapter.notifyDataSetChanged();
			listView.setOnItemClickListener(listener);
		}
	}
}
