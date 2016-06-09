package com.allen.iguangwai.util;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;

/** ��ȡ��Ļ��С�Ĺ����� */
public class BaseTools {
	/** ��ȡ��Ļ�Ŀ�� */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/** ��ȡ��Ļ�ĸ߶� */
	public final static int getWindowsHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/** ��ȡ��Ļ���ܶ� */
	public final static float getWindowsDensity(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	/** ��ȡ��ǰʱ�� */
	@SuppressLint("SimpleDateFormat") public final static String getCurrentTime() {
		SimpleDateFormat time = new SimpleDateFormat("MM-dd HH:mm");// ��ʽ��ʱ��
		String publishTime = time.format(new java.util.Date());
		return publishTime;
	}
}
