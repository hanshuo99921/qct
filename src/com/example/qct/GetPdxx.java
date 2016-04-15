package com.example.qct;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.service.impl.PdxxServiceImpl;

public class GetPdxx implements Runnable {

	private static final String TAG = "GetPdxx";
	private RequestQueue GetPdxx_queue;
	public Context applicationContext;

	public GetPdxx(Context applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		GetPdxx_queue = Volley.newRequestQueue(applicationContext);
		DemoApplication app = (DemoApplication) applicationContext;
		String msg = (String) app.get("id");
		Log.d(TAG, msg);

		String url = AppConst.Server_URL + "get_pdxx.php?lsy=" + app.get("id");

		// String url =
		// "http://192.168.8.103/qct/MyLogin.php?account=1&pwd=000000";
		// Log.d(TAG, url + ":" + System.currentTimeMillis() + ".");
		Log.d(TAG, url);

		final ErrorListener errorListener = new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.e(TAG, error.getMessage(), error);
			}
		};

		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Log.d("TAG", "尚未接收的派单数量："+response.getJSONObject("rows")
									.getString("num"));
							// final PdxxServiceImpl pdxxService = new
							// PdxxServiceImpl(
							// applicationContext);
							// long num = pdxxService.getCount();
							DemoApplication app = (DemoApplication) applicationContext;
							int num = Integer.parseInt( String.valueOf(app.get("num")));
							// String num=(String) app.get("num");
							Log.d(TAG, "已经获取到本机但尚未接收的派单条数num："+num);

							int i = Integer.valueOf(response.getJSONObject(
									"rows").getString("num"));	//后台派给当前揽收员未接收的派单数
							Log.d(TAG, "后台派给当前揽收员未接收的派单数i：" + i);
							if (i > num) {
								app.put("m", num);
								app.put("n", i);
								Log.d(TAG, "已经获取到本机但尚未接收的派单条数m : " + app.get("m"));
								Log.d(TAG, "后台派给当前揽收员未接收的派单数n : " + app.get("n"));
								GetPdxxmx gp = new GetPdxxmx(applicationContext);
								gp.execute();

								app.put("num", i + "");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}, errorListener);
		GetPdxx_queue.add(jsonObjectRequest);

	}

}
