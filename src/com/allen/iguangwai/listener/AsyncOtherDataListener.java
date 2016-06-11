package com.allen.iguangwai.listener;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.allen.iguangwai.activity.MainActivity;
import com.allen.iguangwai.activity.OtherDataActivity;
import com.allen.iguangwai.activity.SearchActivity;
import com.allen.iguangwai.async.Async.OnQuantaAsyncListener;
import com.allen.iguangwai.async.BaseMessage;
import com.allen.iguangwai.model.Article;
import com.allen.iguangwai.model.Content;
import com.allen.iguangwai.model.User;

public class AsyncOtherDataListener implements OnQuantaAsyncListener {
	private Activity activity;
	Handler handler;

	public AsyncOtherDataListener(Activity activity, Handler handler) {
		this.activity = activity;
		this.handler = handler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onComplete(int taskId, BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		Toast.makeText(activity, "正在加载..", Toast.LENGTH_SHORT).show();
		// if (baseMessage.getStatus().equals("1")) {

		try {
			User user = (User) baseMessage.getData("User");
			Message message = Message.obtain();
			message.obj = user;
			handler.sendMessage(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onComplete(int taskId) {

		Toast.makeText(activity, "请求成功！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNetWorkError(int taskId, String errorMsg) {

		Toast.makeText(activity, "网络错误！", Toast.LENGTH_SHORT).show();
	}
}
