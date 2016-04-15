package com.example.qct;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.duguang.baseanimation.ui.imitate.TaobaoPathbutton.TaobaoActivity;
import com.example.qct.util.DatabaseOpenHelper;
import com.example.qct.util.SystemUiHider;
import com.example.service.PreferencesService;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogcatHelper.getInstance(this).stop();
	}

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private RequestQueue queue;
	private static final String TAG = "MainActivity";
	private Context context;

	private EditText idEditText;
	private EditText pwdEditText;
	private PreferencesService service;

	private String account;
	private String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);

		context = this;

		LogcatHelper.getInstance(this).start();

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		idEditText = (EditText) findViewById(R.id.account);
		pwdEditText = (EditText) findViewById(R.id.pwd);
		service = new PreferencesService(this);
		Map<String, String> params = service.getPreferences();
		idEditText.setText(params.get("id"));
		pwdEditText.setText(params.get("password"));

		account = params.get("id");
		pwd = params.get("password");

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
					}
					controlsView.animate().translationY(visible ? 0 : mControlsHeight).setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
				}

				if (visible && AUTO_HIDE) {
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
		findViewById(R.id.dummy_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon).setTitle(R.string.alert_dialog_two_buttons_title3)
						.setPositiveButton(R.string.alert_dialog_ok2, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

								/*
								 * User clicked OK so do some stuff
								 */
								Intent intent = new Intent();
								intent.setClass(FullscreenActivity.this, TaobaoActivity.class);
								// startActivity(intent);
								finish();
							}
						}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

								/*
								 * User clicked Cancel so do some stuff
								 */
							}
						}).create().show();

			}
		});

		Button login = (Button) this.findViewById(R.id.login);
		login.setOnClickListener(new ButtonClickListener());

		DatabaseOpenHelper dbOpenHelper = new DatabaseOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("pdxx", new String[] { "count(*) num" }, "jsrq is null and lsy = '" + account + "'", null, null, null, null);
		int num = 0;
		cursor.moveToFirst();
		num = cursor.getInt(cursor.getColumnIndex("num")); // 已经获取到本机但尚未接收的派单条数
		DemoApplication app = (DemoApplication) getApplication();
		app.put("num", num);
		app.put("m", 0);
		app.put("n", 0);
		cursor.close();
	}

	private final class ButtonClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			// EditText accountText = (EditText) findViewById(R.id.account);
			// String account = accountText.getText().toString();
			// EditText pwdText = (EditText) findViewById(R.id.pwd);
			// String pwd = pwdText.getText().toString();

			account = idEditText.getText().toString();
			pwd = pwdEditText.getText().toString();

			 account = "79";
			 pwd = "13553019177";

			if (account != null && pwd != null && account.trim().length() > 0 && pwd.trim().length() > 0) {
				queue = Volley.newRequestQueue(getApplicationContext());
				String url = AppConst.Server_URL + "MyLogin.php?account=" + account + "&pwd=" + pwd + "";

				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							Log.d("TAG", response.getString("success"));
							if (Boolean.parseBoolean(response.getString("success"))) {
								// 登录成功
								Log.d(TAG, response.getString("msg"));

								service.save(account, pwd);

								DemoApplication app = (DemoApplication) getApplication();
								app.put("id", response.getJSONObject("obj").getString("id"));
								app.put("lsy", response.getJSONObject("obj").getString("id"));
								app.put("part", response.getJSONObject("obj").get("part"));
								app.put("name", response.getJSONObject("obj").getString("name"));
								ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
								executor.scheduleAtFixedRate(new GetPdxx(getApplication()), 1, 200, TimeUnit.SECONDS);

								Intent intent = new Intent();
								// intent.setClass(FullscreenActivity.this,
								// MainActivity2.class);
								intent.setClass(FullscreenActivity.this, TaobaoActivity.class);
								// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								// | Intent.FLAG_ACTIVITY_NEW_TASK);
								// intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
								startActivity(intent);
								// finish();
							} else {
								// 登录失败
								Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();
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
						if (error.networkResponse != null && error.networkResponse.data != null) {
							byte[] htmlBodyBytes = error.networkResponse.data;
							Log.e("TAG", new String(htmlBodyBytes), error);

							Toast.makeText(context, new String(htmlBodyBytes), Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(context, getResources().getString(R.string.login_fail), Toast.LENGTH_LONG).show();
						}
					}
				});
				queue.add(jsonObjectRequest);
			} else {
				Toast.makeText(context, R.string.account_is_null, Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
