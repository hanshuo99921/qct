package com.example.qct;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.qct.util.DatabaseOpenHelper;
import com.example.qct.util.Tpdxx;
import com.example.service.impl.PdxxServiceImpl;

public class GetPdxxmx {

	private RequestQueue GetPdxxmx_queue;
	private static final String TAG = "GetPdxxmx";
	public Context context;
	private List<Tpdxx> Lpdxx;
	DemoApplication app;

	public List<Tpdxx> getLpdxx() {
		return Lpdxx;
	}

	public void setLpdxx(List<Tpdxx> lpdxx) {
		Lpdxx = lpdxx;
	}

	public GetPdxxmx(Context context) {
		super();
		this.context = context;
	}

	public void execute() {
		// 获取远程数据
		GetPdxxmx_queue = Volley.newRequestQueue(context);
		app = (DemoApplication) context;
		int m = (Integer) app.get("m");
		int n = (Integer) app.get("n");
		String url = AppConst.Server_URL + "get_pdxxmx.php?lsy=" + app.get("lsy") + "&n=" + n + "&m=" + m;

		final PdxxServiceImpl pdxxService = new PdxxServiceImpl(context);

		Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method
				// stub
				// 更新本地数据
				Tpdxx pdxx = new Tpdxx();
				try {
					int total = response.getInt("total");
					if (total > 0) {
						JSONArray rows = response.getJSONArray("rows");

						for (int i = 0; i < total; i++) {
							JSONObject row = rows.getJSONObject(i);

							ContentValues values = new ContentValues();
							values.put("pdid", row.getInt("pdid"));
							values.put("jjrname", row.getString("jjrname"));
							values.put("jjraddr", row.getString("jjraddr"));
							values.put("jjrtel", row.getString("jjrtel"));
							values.put("sjrname", row.getString("sjrname"));
							values.put("sjraddr", row.getString("sjraddr"));
							values.put("sjrtel", row.getString("sjrtel"));
							values.put("xfrq", row.getString("xfrq"));
							values.put("xfsj", row.getString("xfsj"));
							values.put("memo", row.getString("memo"));

							// pdxx.setPdid(row.getInt("pdid"));
							// pdxx.setJjrname(row.getString("jjrname"));
							// pdxx.setJjrtel(row.getString("jjrtel"));
							// pdxx.setJjraddr(row.getString("jjraddr"));
							// pdxx.setSjraddr(row.getString("sjraddr"));
							// pdxx.setSjrname(row.getString("sjrname"));
							// pdxx.setSjrtel(row.getString("sjrtel"));
							// pdxx.setXfrq(row.getString("xfrq"));
							// pdxx.setXfsj(row.getString("xfsj"));
							// pdxx.setMemo(row.getString("memo"));
							DemoApplication app = (DemoApplication) context;
							values.put("lsy", Integer.valueOf((String) app.get("lsy")));

							DatabaseOpenHelper dbOpenHelper = new DatabaseOpenHelper(context);
							SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
							db.insert("pdxx", null, values);
							// pdxxService.save(pdxx);
							
							db.close();
						}
						showToast(total);
						showNotification(total);

						Lpdxx = pdxxService.find(1, 20);
						for (Tpdxx t : Lpdxx) {
							Log.d(TAG, t.toString());
						}
						// Intent intent = new Intent();
						// intent.setClass(context, MainActivity2.class);
						// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//
						// 默认的跳转类型,将Activity放到一个新的Task中
						// context.startActivity(intent);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		ErrorListener errorListener = new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.e(TAG, error.getMessage(), error);
			}
		};

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, listener, errorListener);

		GetPdxxmx_queue.add(jsonObjectRequest);
	}

	protected void showToast(int total) {
		// create the view
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.incoming_message_panel, null);

		TextView main_infoTextView = (TextView) inflater.inflate(R.layout.activity_imitate_taobao, null).findViewById(
				R.id.main_info);
		main_infoTextView.setText("您有" + total + "条信息待处理！");

		// set the text in the view
		TextView tv = (TextView) view.findViewById(R.id.message);
		tv.setText("您有" + total + "条信息待处理！");

		// show the toast
		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}

	/**
	 * The notification is the icon and associated expanded entry in the status
	 * bar.
	 */
	protected void showNotification(int total) {

		NotificationManager nm = (NotificationManager) context.getSystemService("notification");
		CharSequence from = "系统提示：";
		CharSequence message = "您有" + total + "条信息待处理！";

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context,
				IncomingMessageView.class), 0);
		String tickerText = context.getString(R.string.imcoming_message_ticker_text, message);

		// construct the Notification object.
		Notification notif = new Notification(R.drawable.stat_sample, tickerText, System.currentTimeMillis());

		// Set the info for the views that show in the notification panel.
		notif.setLatestEventInfo(context, from, message, contentIntent);

		// after a 100ms delay, vibrate for 250ms, pause for 100 ms and
		// then vibrate for 500ms.
		notif.vibrate = new long[] { 100, 250, 100, 500 };

		notif.defaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;

		// Note that we use R.layout.incoming_message_panel as the ID for
		// the notification. It could be any integer you want, but we use
		// the convention of using a resource id for a string related to
		// the notification. It will always be a unique number within your
		// application.
		nm.notify(R.string.imcoming_message_ticker_text, notif);

	}
}
