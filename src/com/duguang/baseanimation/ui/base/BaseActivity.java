package com.duguang.baseanimation.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.duguang.baseanimation.ui.AboutActivity;
import com.example.qct.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.umeng.socialize.controller.UMWXHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;

/**
 * ���е�Activity�̳еĻ���Activity,������ActionBar�˵�
 * @author duguang
 * ���͵�ַ:http://blog.csdn.net/duguang77
 */
public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView();
		initView();
		setListener();
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * ���ò����ļ�
	 */
	public abstract void setView();

	/**
	 * ��ʼ�������ļ��еĿؼ�
	 */
	public abstract void initView();

	/**
	 * ���ÿؼ��ļ���
	 */
	public abstract void setListener();
	
	private void initData() {
		SocializeConstants.APPKEY = "52c4c16956240bce2e08eeb0";
		// ����������Activity��������³�Ա����
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
		                                                                             
		// ���÷�������
		mController.setShareContent("Android�����߱ر�BaseAnimationӦ��,һЩ��Ҫ��Ч���ܹ������ҵ�,����ӵ��Լ���Ӧ����,���߲��͵�ַ:http://blog.csdn.net/duguang77");
		
		
		// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
		String appID = "wx88818f8c48a95eb4";
		// ΢��ͼ�ķ����������һ��url 
		String contentUrl = "http://www.umeng.com/social";
		// ���΢��ƽ̨������1Ϊ��ǰActivity, ����2Ϊ�û������AppID, ����3Ϊ�������������ת����Ŀ��url
		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(this,appID, contentUrl);
		//���÷������
		wxHandler.setWXTitle("Android�����߱ر�BaseAnimation");
		// ֧��΢������Ȧ
		UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(this,appID, contentUrl) ;
		circleHandler.setCircleTitle("һЩ��Ҫ��Ч���ܹ������ҵ�,����ӵ��Լ���Ӧ����,BaseAnimation������Ŷ...");
		
	//  ����1Ϊ��ǰActivity�� ����2Ϊ�û������������ʱ��ת����Ŀ���ַ
		mController.getConfig().supportQQPlatform(this, "http://www.umeng.com/social");   
		
		mController.getConfig().setSsoHandler(new QZoneSsoHandler(this));
		
		//������Ѷ΢��SSO handler
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.optionsmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_about:
			Intent intent = new Intent(BaseActivity.this,AboutActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.small_2_big, R.anim.fade_out);
			return true;
		case R.id.menu_feedback:
			FeedbackAgent agent = new FeedbackAgent(this);
		    agent.startFeedbackActivity();
		    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		    return true;
		case R.id.menu_share:
			final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
			mController.openShare(BaseActivity.this, false);
			 overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",RequestType.SOCIAL);
			    
	    /**ʹ��SSO��Ȩ����������´��� */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}
	
	
	
	
	
}
