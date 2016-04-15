package com.example.qct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.R.integer;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.qct.util.DatabaseOpenHelper;

public class ParcelFormActivity extends Activity {

	private static final String TAG = "ParcelFormActivity";
	private Context context;
	public final static int RESULT_CODE = 100;

	private String sjrname;
	private String sjraddr;
	private String sjrtel;
	private String jjrname;
	private String jjraddr;
	private String jjrtel;
	private String tm;
	private String zl;// 重量
	private String bjje;// 保价金额
	private String dshk;// 代收货款
	private String qtf;// 其他费
	private String yf;// 邮费
	private String bjf;// 保价费
	private String zfy;// 总费用
	private String sjrff;// 收件人付费
	private String jz;// 记账户代码
	private String njpm;// 内件品名代码
	private String bz;// 备注
	private String lrrq;
	private String lrsj;
	private String lsy;
	private String part;
	private int pdid;
	private int fjfw;

	private EditText sjrname_EditText;
	private EditText sjraddr_EditText;
	private EditText sjrtel_EditText;
	private EditText jjrname_EditText;
	private EditText jjraddr_EditText;
	private EditText jjrtel_EditText;
	private EditText zl_EditText;
	private EditText bjje_EditText;
	private EditText dshk_EditText;
	private EditText yf_EditText;
	private EditText bjf_EditText;
	private EditText sjrff_EditText;
	private EditText qtf_EditText;
	private EditText zfy_EditText;
	private EditText bz_EditText;
	private Spinner jz_Spinner;
	private Spinner njpm_Spinner;
	private CheckBox fjfw_swfd_CheckBox;
	private CheckBox fjfw_qt_CheckBox;

	private ArrayList<String> khdmStrings;
	private ArrayList<String> njpmStrings;

	private ArrayAdapter<String> khdmAdapter;
	private ArrayAdapter<String> njpmAdapter;

	private DatabaseOpenHelper dbOpenHelper;
	private SQLiteDatabase db;
	private Cursor cursor;

	private Bundle bundle;
	private ArrayList<String> mStrings;
	private Boolean updateBoolean; // True:yjxx表中已存在，需要执行update操作;False:yjxx表中不存在，需要执行insert操作

	private Boolean bjjeFlag;
	private Boolean withPdxxFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parcel_form);

		context = this;
		sjrname = "";
		sjraddr = "";
		sjrtel = "";
		jjrname = "";
		jjraddr = "";
		jjrtel = "";
		tm = "";
		zl = "";
		bjje = "";
		dshk = "";
		yf = "";
		bjf = "";
		qtf = "";
		zfy = "";
		sjrff = "";
		jz = "";
		njpm = "";
		bz = "";
		lrrq = "";
		lrsj = "";
		updateBoolean = false;
		withPdxxFlag = false;
		fjfw = 0;

		DemoApplication app = (DemoApplication) getApplication();
		Object lsyObject = app.get("lsy");
		lsy = String.valueOf(lsyObject);
		Object partObject = app.get("part");
		part = String.valueOf(partObject);
		bjjeFlag = false;

		sjrname_EditText = (EditText) findViewById(R.id.sjrname_EditText);
		sjraddr_EditText = (EditText) findViewById(R.id.sjraddr_EditText);
		sjrtel_EditText = (EditText) findViewById(R.id.sjrtel_EditText);
		jjrname_EditText = (EditText) findViewById(R.id.jjrname_EditText);
		jjraddr_EditText = (EditText) findViewById(R.id.jjraddr_EditText);
		jjrtel_EditText = (EditText) findViewById(R.id.jjrtel_EditText);
		zl_EditText = (EditText) findViewById(R.id.zl_EditText);
		bjje_EditText = (EditText) findViewById(R.id.bjje_EditText);
		dshk_EditText = (EditText) findViewById(R.id.dshk_EditText);
		yf_EditText = (EditText) findViewById(R.id.yf_EditText);
		bjf_EditText = (EditText) findViewById(R.id.bjf_EditText);
		sjrff_EditText = (EditText) findViewById(R.id.sjrff_EditText);
		qtf_EditText = (EditText) findViewById(R.id.qtf_EditText);
		zfy_EditText = (EditText) findViewById(R.id.zfy_EditText);
		bz_EditText = (EditText) findViewById(R.id.bz_EditText);
		jz_Spinner = (Spinner) findViewById(R.id.khdm_spinner);
		njpm_Spinner = (Spinner) findViewById(R.id.njpm_spinner);
		fjfw_swfd_CheckBox = (CheckBox) findViewById(R.id.fjfw_swfd_checkBox);
		fjfw_qt_CheckBox = (CheckBox) findViewById(R.id.fjfw_qt_checkBox);

		bjf_EditText.setKeyListener(null);
		zfy_EditText.setKeyListener(null);

		njpmStrings = new ArrayList<String>();
		khdmStrings = new ArrayList<String>();
		mStrings = new ArrayList<String>();
		bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		// Log.d(TAG, bundle.getString("from"));
		if (bundle != null && bundle.containsKey("tm")) {
			tm = bundle.getString("tm");
			// Log.d(TAG, tm);
		}

		bjje_EditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onTextChanged ->" + s + "<-" + s.length());

				if (s.length() > 0) {
					// 计算保价费
					int i = 0;
					double f = 0;
					// bjje = bjje_EditText.getText().toString();
					bjje = s.toString();
					if (bjje != null && bjje.trim().length() > 0) {
						i = Integer.parseInt(bjje);
						if (i > 0 && i <= 200) {
							f = 2;
						} else if (i > 200) {
							f = i;
							f = Math.ceil(f / 100);
							Log.d(TAG, "f:" + Math.ceil(f));
						} else {
							// i<=0
						}
						bjf = String.valueOf((int) f);
						bjf_EditText.setText(bjf);
					}

					// 更新总费用
					// zfy = zfy_EditText.getText().toString();
					// qtf = qtf_EditText.getText().toString();
					// yf = yf_EditText.getText().toString();
					int y = 0;
					if (yf != null && yf.length() > 0) {
						y = Integer.valueOf(yf);
					}
					int b = Integer.valueOf(bjf);
					double q = 0;
					if (qtf != null && qtf.length() > 0) {
						q = Double.valueOf(qtf);
					}
					double z = q + b + y;
					zfy = String.valueOf(z);
					Log.d(TAG, "zfy->" + zfy + "<-");
					zfy_EditText.setText(zfy);

				}
				// else {
				// bjf_EditText.setText("");
				//
				// zfy = zfy_EditText.getText().toString();
				// qtf = qtf_EditText.getText().toString();
				// yf = yf_EditText.getText().toString();
				// int y = 0;
				// if (yf.length() > 0) {
				// y = Integer.valueOf(yf);
				// }
				// double q = 0;
				// if (qtf.length() > 0) {
				// q = Double.valueOf(qtf);
				// }
				// double z = q + y;
				// if (z == 0) {
				// zfy = "";
				// } else {
				// zfy = String.valueOf(z);
				// }
				// Log.d(TAG, "zfy->" + zfy + "<-");
				// zfy_EditText.setText(zfy);
				// }
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

		yf_EditText.addTextChangedListener(new TextWatcher() {

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

				// Log.d(TAG, bjf);
				if (bjf != null && bjf.length() > 0) {
					b = Integer.valueOf(bjf);
				}

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
				zfy_EditText.setText(zfy);
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

		qtf_EditText.addTextChangedListener(new TextWatcher() {

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

				if (bjf != null && bjf.length() > 0) {
					b = Integer.valueOf(bjf);
				}

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
				zfy_EditText.setText(zfy);
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

		dbOpenHelper = new DatabaseOpenHelper(context);
		db = dbOpenHelper.getWritableDatabase();
		njpmStrings.add("");
		cursor = db.query("ywzl", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			// Log.d(TAG,
			// cursor.getString(cursor.getColumnIndex("id"))+":"+cursor.getString(cursor.getColumnIndex("name")));
			njpmStrings.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		njpmAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, njpmStrings);
		njpm_Spinner.setAdapter(njpmAdapter);
		njpm_Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
				// TODO Auto-generated method stub
				// Toast.makeText(context, "你点击的是:" + pos + ":" +
				// njpmStrings.get(pos), 2000).show();
				cursor = db.query("ywzl", null, "name=?", new String[] { njpmStrings.get(pos) }, null, null, null);
				if (cursor.moveToNext()) {
					njpm = cursor.getString(cursor.getColumnIndex("id"));
				} else {
					njpm = "0";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		khdmStrings.add("");
		cursor = db.query("jz", null, "part=?", new String[]{part}, null, null, null);
		while (cursor.moveToNext()) {
			khdmStrings.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		khdmAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, khdmStrings);
		jz_Spinner.setAdapter(khdmAdapter);
		jz_Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long id) {
				// TODO Auto-generated method stub

				cursor = db.query("jz", null, "name=?", new String[] { khdmStrings.get(pos) }, null, null, null);
				if (cursor.moveToNext()) {
					jz = cursor.getString(cursor.getColumnIndex("id"));
					jjrname = cursor.getString(cursor.getColumnIndex("name"));
					jjraddr = cursor.getString(cursor.getColumnIndex("addr"));
					jjrtel = cursor.getString(cursor.getColumnIndex("tel"));
				}
				jjrname_EditText.setText(jjrname);
				jjraddr_EditText.setText(jjraddr);
				jjrtel_EditText.setText(jjrtel);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		Log.d(TAG, tm);
		cursor = db.query("yjxx", null, "tm=?", new String[] { tm }, null, null, null);
		if (cursor.moveToNext()) {
			// yjxx表中有条码对应的邮件信息
			updateBoolean = true;
			jjrname = cursor.getString(cursor.getColumnIndex("jjrname"));
			jjraddr = cursor.getString(cursor.getColumnIndex("jjraddr"));
			jjrtel = cursor.getString(cursor.getColumnIndex("jjrtel"));
			jz = cursor.getString(cursor.getColumnIndex("jz"));
			sjrname = cursor.getString(cursor.getColumnIndex("sjrname"));
			sjraddr = cursor.getString(cursor.getColumnIndex("sjraddr"));
			sjrtel = cursor.getString(cursor.getColumnIndex("sjrtel"));
			zl = cursor.getString(cursor.getColumnIndex("zl"));
			njpm = cursor.getString(cursor.getColumnIndex("ywzl"));
			bz = cursor.getString(cursor.getColumnIndex("memo"));
			bjje = cursor.getString(cursor.getColumnIndex("bjje"));
			dshk = cursor.getString(cursor.getColumnIndex("dshk"));
			yf = cursor.getString(cursor.getColumnIndex("zf"));
			bjf = cursor.getString(cursor.getColumnIndex("bjf"));
			qtf = cursor.getString(cursor.getColumnIndex("qtf"));
			// Log.d(TAG, qtf);
			zfy = cursor.getString(cursor.getColumnIndex("zfy"));
			sjrff = cursor.getString(cursor.getColumnIndex("sjrff"));
			lrrq = cursor.getString(cursor.getColumnIndex("lrrq"));
			lrsj = cursor.getString(cursor.getColumnIndex("lrsj"));
			fjfw = cursor.getInt(cursor.getColumnIndex("fjfw"));
			Log.d(TAG, "fjfw -> " + fjfw + " <-");
			if (fjfw == 1) {
				fjfw_swfd_CheckBox.setChecked(true);
			} else if (fjfw == 2) {
				fjfw_qt_CheckBox.setChecked(true);
			}

			if (jz == null || jz == "" || jz.equals("0")) {
				// jz_Spinner.setSelection(0);
				jjrname_EditText.setText(jjrname);
				jjraddr_EditText.setText(jjraddr);
				jjrtel_EditText.setText(jjrtel);
			} else {
				Cursor cursor2 = db.query("jz", null, "id=?", new String[] { jz }, null, null, null);
				String khmc = "";
				if (cursor2.moveToNext()) {
					khmc = cursor2.getString(cursor2.getColumnIndex("name"));
				}
				int position = 0;
				for (int i = 0; i < khdmStrings.size(); i++) {
					if (khmc.equals(khdmStrings.get(i))) {
						position = i;
						break;
					}
				}
				jz_Spinner.setSelection(position);
				cursor2.close();
			}

			if (njpm == null || njpm.equals("0")) {
				njpm_Spinner.setSelection(0);
			} else {
				Cursor cursor2 = db.query("ywzl", null, "id=?", new String[] { njpm }, null, null, null);
				String njpmString = "";
				if (cursor2.moveToNext()) {
					njpmString = cursor2.getString(cursor2.getColumnIndex("name"));
				}
				int position = 0;
				for (int i = 0; i < njpmStrings.size(); i++) {
					if (njpmString.equals(njpmStrings.get(i))) {
						position = i;
						break;
					}
				}
				njpm_Spinner.setSelection(position);
				cursor2.close();
			}
			sjrname_EditText.setText(sjrname);
			sjraddr_EditText.setText(sjraddr);
			sjrtel_EditText.setText(sjrtel);
			zl_EditText.setText(zl);
			bz_EditText.setText(bz);
			bjje_EditText.setText(bjje);
			dshk_EditText.setText(dshk);
			yf_EditText.setText(yf);
			bjf_EditText.setText(bjf);
			qtf_EditText.setText(qtf);
			zfy_EditText.setText(zfy);
			sjrff_EditText.setText(sjrff);

		} else {
			// yjxx表中没有条码对应的邮件信息，再查询pdxx表
			if (bundle != null && bundle.containsKey("pdid")) {
				pdid = bundle.getInt("pdid");
				jjrname = bundle.getString("jjrname");
				jjraddr = bundle.getString("jjraddr");
				jjrtel = bundle.getString("jjrtel");
				sjrname = bundle.getString("sjrname");
				sjraddr = bundle.getString("sjraddr");
				sjrtel = bundle.getString("sjrtel");
				bz = bundle.getString("memo");
				withPdxxFlag = true;
			}
			sjrname_EditText.setText(sjrname);
			sjraddr_EditText.setText(sjraddr);
			sjrtel_EditText.setText(sjrtel);
			jjrname_EditText.setText(jjrname);
			jjraddr_EditText.setText(jjraddr);
			jjrtel_EditText.setText(jjrtel);
		}
	}

	public void saveParcelForm(View view) {
		getParcelForm(view);

		// 保存到本机数据库
		saveToLocal();

		bundle.putString("jjrname", jjrname);
		bundle.putString("jjraddr", jjraddr);
		bundle.putString("jjrtel", jjrtel);
		bundle.putString("sjrname", sjrname);
		bundle.putString("sjraddr", sjraddr);
		bundle.putString("sjrtel", sjrtel);
		if (jz == null || jz.length() == 0) {
			bundle.putInt("jz", 0);
		} else {
			bundle.putInt("jz", Integer.parseInt(jz));
		}
		if (zl == null || zl == "") {
			bundle.putInt("zl", 0);
		} else {
			bundle.putInt("zl", Integer.parseInt(zl));
		}
		bundle.putString("bjje", bjje);
		bundle.putString("dshk", dshk);
		bundle.putString("yf", yf);
		bundle.putString("qtf", qtf);
		bundle.putString("bjf", bjf);
		bundle.putString("zfy", zfy);
		bundle.putString("sjrff", sjrff);
		if (njpm == null || njpm == "") {
			bundle.putInt("njpm", 0);
		} else {
			bundle.putInt("njpm", Integer.parseInt(njpm));
		}
		bundle.putString("lrrq", lrrq);
		bundle.putString("lrsj", lrsj);
		bundle.putString("memo", bz);
		bundle.putInt("lsy", Integer.parseInt(lsy));
		bundle.putShort("part", Short.parseShort(part));
		bundle.putInt("fjfw", fjfw);
		Log.d(TAG, bundle.toString());
		Intent intent = new Intent();
		intent.putExtras(bundle);
		String from = "";
		if (bundle.containsKey("from")) {
			from = bundle.getString("from");
		}

		if (from.equals("List12")) {
			setResult(RESULT_CODE, intent);// 设置resultCode，onActivityResult()中能获取到
		}

		cursor.close();
		db.close();

		finish();
	}

	private void saveToLocal() {
		// TODO Auto-generated method stub

		ContentValues values = new ContentValues();
		values.put("sjrname", sjrname);
		values.put("sjraddr", sjraddr);
		values.put("sjrtel", sjrtel);
		values.put("jjrname", jjrname);
		values.put("jjraddr", jjraddr);
		values.put("jjrtel", jjrtel);
		if (zl != null && zl.length() != 0) {
			values.put("zl", Integer.parseInt(zl));
		}
		if (bjje != null && bjje.length() != 0) {
			values.put("bjje", Integer.parseInt(bjje));
		}
		if (dshk != null && dshk.length() != 0) {
			values.put("dshk", (float) Math.round(Float.parseFloat(dshk) * 10) / 10);
		}
		if (yf != null && yf.length() != 0) {
			values.put("zf", Integer.parseInt(yf));
		}
		if (bjf != null && bjf.length() != 0) {
			values.put("bjf", Integer.parseInt(bjf));
		}
		if (sjrff != null && sjrff.length() != 0) {
			values.put("sjrff", (float) Math.round(Float.parseFloat(sjrff) * 10) / 10);
		}
		if (qtf != null && qtf.length() != 0) {
			values.put("qtf", (float) Math.round(Float.parseFloat(qtf) * 10) / 10);
		}
		if (zfy != null && zfy.length() != 0) {
			values.put("zfy", (float) Math.round(Float.parseFloat(zfy) * 10) / 10);
		}
		values.put("memo", bz);
		values.put("tm", tm);
		values.put("jz", jz);
		values.put("ywzl", njpm);
		values.put("lsy", lsy);
		values.put("part", part);
		values.put("fjfw", fjfw);

		if (updateBoolean) {
			int row = db.update("yjxx", values, "tm=?", new String[] { tm });
			if (row == 1) {
				Toast.makeText(context, "更新本机邮件信息成功！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "更新本机邮件信息失败，请检查后重试！", Toast.LENGTH_SHORT).show();
			}
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			String now = df.format(new Date());
			lrrq = now.substring(0, 10);
			lrsj = now.substring(11, 19);
			values.put("lrrq", lrrq);
			values.put("lrsj", lrsj);
			long row = db.insert("yjxx", null, values);
			if (row == -1) {
				Toast.makeText(context, "保存邮件信息到本机失败，请检查后重试！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "保存邮件信息到本机成功！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void getParcelForm(View view) {
		sjrname = sjrname_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		sjraddr = sjraddr_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		sjrtel = sjrtel_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		jjrname = jjrname_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		jjraddr = jjraddr_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		jjrtel = jjrtel_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		zl = zl_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		bjje = bjje_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		dshk = dshk_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		yf = yf_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		bjf = bjf_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		sjrff = sjrff_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		qtf = qtf_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		zfy = zfy_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		bz = bz_EditText.getText().toString().replaceAll("\r|\n", "").replaceAll("\\s*", "");
		if (fjfw_swfd_CheckBox.isChecked()) {
			fjfw = 1;
		} else if (fjfw_qt_CheckBox.isChecked()) {
			fjfw = 2;
		} else {
			fjfw = 0;
		}

	}

}
