package com.allen.iguangwai.listener;

import android.app.Activity;
import android.widget.Toast;

import com.allen.iguangwai.async.Async.OnQuantaAsyncListener;
import com.allen.iguangwai.async.BaseMessage;

public class AsyncUserDataListener implements OnQuantaAsyncListener {
	private Activity activity;

	public AsyncUserDataListener(Activity activity) {
		this.activity = activity;

	}

	@Override
	public void onComplete(int taskId, BaseMessage baseMessage) {
		Toast.makeText(activity, baseMessage.getData(), Toast.LENGTH_LONG)
				.show();
		if (baseMessage.getStatus().equals("1")) {
			Toast.makeText(activity, "����ɹ�", Toast.LENGTH_LONG).show();

		}
		if (baseMessage.getStatus().equals("0")) {
			Toast.makeText(activity, "����ʧ��", Toast.LENGTH_LONG).show();

		}
	}

	@Override
	public void onComplete(int taskId) {

		Toast.makeText(activity, "����ɹ���", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNetWorkError(int taskId, String errorMsg) {

		Toast.makeText(activity, "�������", Toast.LENGTH_SHORT).show();
	}
}
