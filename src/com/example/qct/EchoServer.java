package com.example.qct;

import android.util.Log;

public class EchoServer implements Runnable {

	private static final String TAG = "MainActivity";
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.d(TAG,"This is a echo server. The current time is "
				+ System.currentTimeMillis() + ".");

	}

}
