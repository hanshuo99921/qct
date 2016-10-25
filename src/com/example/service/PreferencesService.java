package com.example.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesService {
	private Context context;

	public PreferencesService(Context context) {
		this.context = context;
	}

	/**
	 * 保存参数
	 * 
	 * @param ID
	 *            登录ID
	 * @param password
	 *            登录密码
	 */
	public void save(String id, String password) {
		SharedPreferences preferences = context.getSharedPreferences("qct",
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("id", id);
		editor.putString("password", password);
		editor.commit();
	}

	/**
	 * 获取各项配置参数
	 * 
	 * @return
	 */
	public Map<String, String> getPreferences() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = context.getSharedPreferences("qct",
				Context.MODE_PRIVATE);
		params.put("id", preferences.getString("id", ""));
		params.put("password", preferences.getString("password", ""));
		return params;
	}
}
