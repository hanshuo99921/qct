package com.example.qct;

import java.util.HashMap;

import android.app.Application;

public class DemoApplication extends Application {
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private String globalVariable;
	
	public String getGlobalVariable() {
		return globalVariable;
	}

	public void setGlobalVariable(String globalVariable) {
		this.globalVariable = globalVariable;
	}

	public void put(String key, Object value) {
		map.put(key, value);
	}

	public Object get(String key) {
		return map.get(key);
	}

}
