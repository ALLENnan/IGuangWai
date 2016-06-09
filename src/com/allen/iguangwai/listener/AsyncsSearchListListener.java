package com.allen.iguangwai.listener;

import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.allen.iguangwai.activity.SearchActivity;
import com.allen.iguangwai.async.QuantaAsync.OnQuantaAsyncListener;
import com.allen.iguangwai.async.QuantaBaseMessage;
import com.allen.iguangwai.model.Article;

public class AsyncsSearchListListener implements OnQuantaAsyncListener {
	private Activity activity;

	public AsyncsSearchListListener(Activity activity) {
		this.activity = activity;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onComplete(int taskId, QuantaBaseMessage baseMessage) {
		// TODO Auto-generated method stub
		// Toast.makeText(activity, "���ڼ���..", Toast.LENGTH_SHORT).show();
		// if (baseMessage.getStatus().equals("1")) {
		if (baseMessage.getStatus().equals("1")) {
			try {
				ArrayList<Article> temp = (ArrayList<Article>) baseMessage
						.getDataList("Article");
				SearchActivity.articleList = temp;
				SearchActivity.adapter.notifyDataSetChanged();// ����listview
				// Toast.makeText(activity, "���سɹ�..",
				// Toast.LENGTH_SHORT).show();

				// ArticleDao articleDao = new ArticleDao(activity);
				// articleDao.clearAllArticle();
				// articleDao.addArticle(articleList);
				// Toast.makeText(activity, "���سɹ�", Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (baseMessage.getStatus().equals("0")) {
			Toast.makeText(activity, "��ƥ������...", Toast.LENGTH_SHORT).show();
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
