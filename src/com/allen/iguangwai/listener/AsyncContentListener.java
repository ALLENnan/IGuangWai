package com.allen.iguangwai.listener;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.allen.iguangwai.activity.MainActivity;
import com.allen.iguangwai.activity.SearchActivity;
import com.allen.iguangwai.async.Async.OnQuantaAsyncListener;
import com.allen.iguangwai.async.BaseMessage;
import com.allen.iguangwai.model.Article;
import com.allen.iguangwai.model.Content;

public class AsyncContentListener implements OnQuantaAsyncListener {
	private Activity activity;
	private int activityId;

	public AsyncContentListener(Activity activity, int activityId) {
		this.activity = activity;
		this.activityId = activityId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onComplete(int taskId, BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		Toast.makeText(activity, "正在加载..", Toast.LENGTH_SHORT).show();
		// if (baseMessage.getStatus().equals("1")) {

		try {
			Content content = (Content) baseMessage.getData("Content");
			Article article = findArticle(content);// 通过从content的id找到对应的article,并将content的内容set进article
			Message message = Message.obtain();
			message.obj = article;
			ItemClickListener.handler.sendMessage(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 找到对应article
	 */

	private Article findArticle(Content content) {
		Article article = null;
		ArrayList<Article> articleList = null;
		if (activityId == 1) {
			articleList = MainActivity.articleList;
		}
		if (activityId == 2) {
			articleList = SearchActivity.articleList;
		}
		for (int i = 0; i < articleList.size(); i++) {

			article = articleList.get(i);
			Log.v("myLog", "id" + article.getId() + content.getContent());
			Log.v("myLog", "con_id" + content.getId());
			if (article.getId().equals(content.getId())) {
				article.setContent(content.getContent());
				break;
			}
		}
		return article;
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
