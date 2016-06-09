package com.allen.iguangwai;

import android.app.Application;
import android.content.Context;

/**
 * @author Allen Lin
 * @date 2016-5-25
 * @desc
 */
public class App extends Application {
	private static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();
	}

	public static Context getContext() {
		return sContext;
	}

}