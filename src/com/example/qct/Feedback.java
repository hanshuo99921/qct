package com.example.qct;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.duguang.baseanimation.ui.imitate.TaobaoPathbutton.TaobaoActivity;
import com.example.qct.util.Tpdxx;
import com.example.service.PdxxServiceI;
import com.example.service.YjxxServiceI;
import com.example.service.impl.PdxxServiceImpl;
import com.example.service.impl.YjxxServiceImpl;
import com.zijunlin.Zxing.Demo.CaptureActivity;

public class Feedback extends Activity {

	private static final String TAG = "Feedback";
	private Context context;
	private final static int REQUEST_CODE = 2;
	private EditText editText;
	Bundle bundle;
	private RequestQueue queue;
	DemoApplication app;
	ImageButton btn_feedback_scan;
	ImageButton btn_feedback_query;
	String[] tdfk;
	int clicked;
	String tm;
	int pdid;
	YjxxServiceI yjxxService;
	PdxxServiceI pdxxService;
	Tpdxx pdxx;
	String jjrname;
	String jjrtel;
	TextView sjrnameTextView;
	TextView sjrtelTextView;
	TextView sjraddrTextView;
	EditText feedbackEditText;
	Button choiceButton;
	Button feedbackButton;
	private String fkrq;
	private String fksj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		context = this;
		editText = (EditText) findViewById(R.id.feedback_barcode);
		sjrnameTextView = (TextView) findViewById(R.id.feedback_sjrname);
		sjraddrTextView = (TextView) findViewById(R.id.feedback_sjraddr);
		sjrtelTextView = (TextView) findViewById(R.id.feedback_sjrtel);
		feedbackEditText = (EditText) findViewById(R.id.feedback_edittext);
		choiceButton = (Button) findViewById(R.id.feedback_choice);
		feedbackButton = (Button) findViewById(R.id.feedback_button);
		bundle = this.getIntent().getExtras();
		queue = Volley.newRequestQueue(context);
		app = (DemoApplication) getApplication();
		tdfk = getResources().getStringArray(R.array.select_dialog_items);
		yjxxService = new YjxxServiceImpl(context);
		pdxxService = new PdxxServiceImpl(context);
		pdxx = new Tpdxx();
		jjrname = "";
		jjrtel = "";

		btn_feedback_scan = (ImageButton) findViewById(R.id.feedback_capture);
		btn_feedback_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, CaptureActivity.class);
				Bundle mbundle = bundle;
				if (mbundle == null) {
					mbundle = new Bundle();
				}
				mbundle.putCharSequence("from", "Feedback");
				intent.putExtras(mbundle);

				startActivityForResult(intent, REQUEST_CODE);
			}
		});

		btn_feedback_query = (ImageButton) findViewById(R.id.feedback_query);
		btn_feedback_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				tm = editText.getText().toString();
				tm = tm.replace("\n", "");
				if (tm != null && tm.trim().length() > 0) {
					String lsy = app.get("lsy").toString();
					String url = AppConst.Server_URL + "get_tdyjxx.php?tm=" + tm + "&lsy=" + lsy;
					JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									// TODO Auto-generated method stub

									try {
										if (response.getInt("total") == 1) {
											JSONObject json = response.getJSONObject("rows");
											TextView sjraddr = (TextView) findViewById(R.id.feedback_sjraddr);
											TextView sjrname = (TextView) findViewById(R.id.feedback_sjrname);
											TextView sjrtel = (TextView) findViewById(R.id.feedback_sjrtel);
											sjraddr.setText(json.getString("sjraddr"));
											sjrname.setText(json.getString("sjrname"));
											sjrtel.setText(json.getString("sjrtel"));
											jjrname = json.getString("jjrname");
											jjrtel = json.getString("jjrtel");

											EditText et = (EditText) findViewById(R.id.feedback_edittext);
											et.setEnabled(true);
										} else if (response.getInt("total") > 1) {
											Toast.makeText(context, R.string.feedback_tm_not_uniqe, Toast.LENGTH_SHORT)
													.show();
										} else {
											Toast.makeText(context, R.string.feedback_no_result, Toast.LENGTH_SHORT)
													.show();
										}

										choiceButton.setEnabled(true);
										feedbackButton.setEnabled(true);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										Toast.makeText(context, R.string.feedback_json_error, Toast.LENGTH_SHORT)
												.show();
									}
								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									// TODO Auto-generated method stub
									Log.d(TAG, error.toString());
									Toast.makeText(context, R.string.feedback_fail, Toast.LENGTH_SHORT).show();
								}
							});
					queue.add(jsonObjectRequest);
				} else {
					Toast.makeText(context, R.string.feedback_not_input_tm, Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button btn_feedback_choice = (Button) findViewById(R.id.feedback_choice);
		btn_feedback_choice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon)
						.setTitle(R.string.alert_dialog_single_choice)
						.setSingleChoiceItems(R.array.select_dialog_items, 0, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

								/*
								 * User clicked on a radio button do some stuff
								 */
								clicked = whichButton;
							}
						}).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

								/* User clicked Yes so do some stuff */

								EditText et = (EditText) findViewById(R.id.feedback_edittext);
								et.setText(tdfk[clicked]);
								// Toast.makeText(getApplicationContext(),
								// "您选择了：" + tdfk[clicked],
								// Toast.LENGTH_SHORT).show();
							}
						}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

								/* User clicked No so do some stuff */
							}
						}).create().show();
			}
		});

		Button btn_feedback = (Button) findViewById(R.id.feedback_button);
		btn_feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// SimpleDateFormat df = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
				// String now = df.format(new Date());
				// Log.d(TAG, now);
				// fkrq = now.substring(0, 10);
				// fksj = now.substring(11, 19);
				DemoApplication app = (DemoApplication) getApplication();
				Object lsyObject = app.get("lsy");
				String tdy = String.valueOf(lsyObject);
				String url;
				try {
					url = AppConst.Server_URL + "save_tdfk.php?tm=" + tm + "&yjzt="
							+ java.net.URLEncoder.encode(tdfk[clicked], "UTF-8") + "&tdy=" + tdy;

					JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									// TODO Auto-generated method stub
									try {
										if (response.getBoolean("success")) {
											Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT)
													.show();
											editText.setText("");
											feedbackEditText.setText("");
											feedbackEditText.setEnabled(false);
											sjraddrTextView.setText("");
											sjrtelTextView.setText("");
											sjrnameTextView.setText("");
											choiceButton.setEnabled(false);
											feedbackButton.setEnabled(false);

											// fkrq =
											// response.getString("fkrq");
											// fksj =
											// response.getString("fksj");

											if (jjrtel != null && jjrtel.trim().length() > 0) {

												// 弹出对话框，询问是否要发送短信
												new AlertDialog.Builder(context)
														.setIcon(R.drawable.alert_dialog_icon)
														.setTitle(R.string.need_short_message)
														.setPositiveButton(R.string.need_short_message_yes,
																new DialogInterface.OnClickListener() {
																	public void onClick(DialogInterface dialog,
																			int whichButton) {
																		// 选择是，发送短信
																		// 获取短信管理器
																		SmsManager smsManager = SmsManager.getDefault();
																		// 设置短信内容
																		String msg = null;
																		msg = getResources().getString(
																				R.string.short_message_begin)
																				+ jjrname
																				+ getResources().getString(
																						R.string.short_message_middle1)
																				+ tm
																				+ getResources().getString(
																						R.string.short_message_middle2)
																				+ tdfk[clicked]
																				+ getResources().getString(
																						R.string.short_message_end);
																		Log.d(TAG, msg);
																		// 拆分短信内容（手机短信长度限制）
																		// ArrayList<String>
																		// list
																		// =
																		// smsManager
																		// .divideMessage(msg);
																		// 发送短信
																		// for
																		// (String
																		// text
																		// :
																		// list)
																		// {
																		// smsManager
																		// .sendTextMessage(
																		// jjrtel,
																		// null,
																		// text,
																		// null,
																		// null);
																		// }
																		smsManager.sendTextMessage(jjrtel, null, msg,
																				null, null);
																		Toast.makeText(context,
																				R.string.short_message_sended,
																				Toast.LENGTH_SHORT).show();

																	}
																})
														.setNegativeButton(R.string.need_short_message_no,
																new DialogInterface.OnClickListener() {
																	public void onClick(DialogInterface dialog,
																			int whichButton) {
																		// 选择否，不发送短信
																	}
																}).create().show();

											} else {
												Toast.makeText(context, R.string.jjrtel_is_null, Toast.LENGTH_LONG)
														.show();
											}
										} else {
											Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT)
													.show();

										}
										// finish();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
									}

								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									// TODO Auto-generated method stub
									Log.e("TAG", error.getMessage(), error);
									Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
								}
							});
					queue.add(jsonObjectRequest);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Toast.makeText(context, e1.getMessage(), Toast.LENGTH_SHORT).show();
				}
				editText.requestFocus();
			}
		});

		editText.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// 自己设定的事件
					btn_feedback_query.performClick();
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feedback, menu);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == CaptureActivity.RESULT_CODE2) {
			bundle = data.getExtras();
			String strResult = bundle.getString("barcode");
			// Log.i(TAG, "onActivityResult: " + strResult);
			// Toast.makeText(context, strResult, Toast.LENGTH_SHORT).show();
			editText.setText(strResult);
			btn_feedback_query.performClick();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent intent = new Intent();
			// intent.setClass(context, MainActivity2.class);
			intent.setClass(context, TaobaoActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			startActivity(intent);
			finish();

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
