package com.example.qct;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qct.util.Tpdxx;
import com.example.service.impl.PdxxServiceImpl;

public class ScrollView1 extends ActionBarActivity {

	private static final String TAG = "ScrollView1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scroll_view1);

		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PdxxServiceImpl pdxxService = new PdxxServiceImpl(
						getApplication());
				pdxxService.clear();
			}

		});
		LinearLayout lin = (LinearLayout) findViewById(R.id.layout);
		lin.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LayoutInflater mflater = LayoutInflater.from(this);

		PdxxServiceImpl pdxxService = new PdxxServiceImpl(getApplication());

		List<Tpdxx> pdxxs = pdxxService.find(1, 100);
		int i = 0;
		for (final Tpdxx t : pdxxs) {
			i = i + 1;
			TextView textView = (TextView) new TextView(this);
			String str = i + " ";
			str += "联系人：" + t.getJjrname() + " 联系电话：" + t.getJjrtel();
			textView.setText(str);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lin.addView(textView, p);

			Button buttonView = new Button(this);
			buttonView.setText("地址：" + t.getJjraddr() + "\n派单ID：" + t.getPdid()
					+ "\n备注：" + t.getMemo() + "\n下发时间：" + t.getXfrq() + " "
					+ t.getXfsj());
			lin.addView(buttonView, p);
			buttonView.setOnClickListener(new Button.OnClickListener() {// 创建监听
						public void onClick(View v) {
							Intent intent = new Intent();

							Button bv = (Button) v;

							// Intent可以在不同的应用程序的Activity发送数据

							DemoApplication myApp = ((DemoApplication) getApplicationContext());// 获得我们的应用程序MyApp
							myApp.setGlobalVariable((String) bv.getText());

							Bundle bundle = new Bundle();
							bundle.putInt("id", t.getId());
							bundle.putString("pdid", t.getPdid() + "");
							bundle.putString("jjrname", t.getJjrname());

							intent.putExtras(bundle);
							// intent.putExtra("pdid", 1).putExtra("jjraddr",
							// "蛔蛔急口令不");

							intent.setClass(ScrollView1.this, List12.class);// 从哪里跳到哪里
							intent.addFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							startActivity(intent);

						}

					});

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scroll_view1, menu);
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
}
