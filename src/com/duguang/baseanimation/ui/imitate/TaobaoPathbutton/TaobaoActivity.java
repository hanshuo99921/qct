package com.duguang.baseanimation.ui.imitate.TaobaoPathbutton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.duguang.baseanimation.ui.base.BaseActivity;
import com.example.qct.Feedback;
import com.example.qct.List12;
import com.example.qct.MainSetting;
import com.example.qct.Main_info;
import com.example.qct.R;
import com.example.qct.ReceiptActivity;
import com.example.qct.ReceiveActivity;
import com.example.qct.util.UpdateAppManager;

/**
 * 高仿淘宝按钮主页面
 * 
 * @author
 * 
 */
public class TaobaoActivity extends BaseActivity {

	private ComposerLayout clayout;
	private Context context;
	private static final String TAG = "TaobaoActivity";
	
	private UpdateAppManager updateManager;

	@Override
	public void setView() {
		setContentView(R.layout.activity_imitate_taobao);
		context = this;
		
		updateManager = new UpdateAppManager(context);  
        updateManager.checkUpdateInfo(); 
	}

	@Override
	public void initView() {
		// 引用控件
		clayout = (ComposerLayout) findViewById(R.id.test);
		// clayout.init(new int[] { R.drawable.composer_camera,
		// R.drawable.composer_music, R.drawable.composer_place,
		// R.drawable.composer_sleep, R.drawable.composer_thought,
		// R.drawable.composer_with }, R.drawable.composer_button,
		// R.drawable.composer_icn_plus, ComposerLayout.CENTERBOTTOM, 180,
		// 300);
		clayout.init(new int[] { R.drawable.ic_main_info, R.drawable.ic_main_get, R.drawable.ic_main_receipt,R.drawable.ic_main_query,
				R.drawable.ic_main_receive, R.drawable.ic_main_feedback, R.drawable.ic_main_setting },
				R.drawable.composer_button, R.drawable.composer_icn_plus, ComposerLayout.CENTERBOTTOM, 180, 300);
	}

	@Override
	public void setListener() {
		// 点击事件监听，100+0对应composer_camera，100+1对应composer_music……如此类推你有机个按钮就加几个按钮都行。
		OnClickListener clickit = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (v.getId() == 100 + 0) {
					intent.setClass(context, Main_info.class);
					startActivity(intent);
				} else if (v.getId() == 100 + 1) {
					intent.setClass(context, List12.class);
					startActivity(intent);
				}else if (v.getId() == 100 + 2) {
					intent.setClass(context, ReceiptActivity.class);
					startActivity(intent);
				} else if (v.getId() == 100 + 3) {
					intent.setClass(context, com.example.qct.Query.class);
					startActivity(intent);
				} else if (v.getId() == 100 + 4) {
					intent.setClass(context, ReceiveActivity.class);
					startActivity(intent);
				} else if (v.getId() == 100 + 5) {
					intent.setClass(context, Feedback.class);
					startActivity(intent);
				} else if (v.getId() == 100 + 6) {
					intent.setClass(context, MainSetting.class);
					startActivity(intent);
				}
			}
		};
		clayout.setButtonsOnClickListener(clickit);

		// 下面几个句纯粹测试下父控件,实际开发是可以去掉
		// 下面呢幾句純粹攞嚟測試下父控件點唔點倒，實際用嘅時候可以去掉。
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlparent);
		rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(TaobaoActivity.this, "父控件可以點擊的哦!!!",
				// 0).show();
				// System.out.println("父控件可以點擊就即系冇吡截咗。");
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			new AlertDialog.Builder(context).setIcon(R.drawable.alert_dialog_icon)
					.setTitle(R.string.alert_dialog_two_buttons_title3)
					.setPositiveButton(R.string.alert_dialog_ok2, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							/* User clicked OK so do some stuff */
							finish();
						}
					}).setNegativeButton(R.string.alert_dialog_cancel2, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							/* User clicked Cancel so do some stuff */
						}
					}).create().show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}
