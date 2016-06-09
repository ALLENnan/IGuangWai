package com.allen.iguangwai.util;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;

/** 获取屏幕大小的工具类 */
public class BaseTools {
	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/** 获取屏幕的高度 */
	public final static int getWindowsHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/** 获取屏幕的密度 */
	public final static float getWindowsDensity(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	/** 获取当前时间 */
	@SuppressLint("SimpleDateFormat") public final static String getCurrentTime() {
		SimpleDateFormat time = new SimpleDateFormat("MM-dd HH:mm");// 格式化时间
		String publishTime = time.format(new java.util.Date());
		return publishTime;
	}
}
