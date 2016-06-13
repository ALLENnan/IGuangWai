package com.allen.iguangwai.listener;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.allen.iguangwai.activity.MainActivity;
import com.allen.iguangwai.async.BaseMessage;
import com.allen.iguangwai.async.Async.OnQuantaAsyncListener;
import com.allen.iguangwai.dao.ArticleDao;
import com.allen.iguangwai.fragment.TabFragment;
import com.allen.iguangwai.fragment.UserArticleFragment;
import com.allen.iguangwai.fragment.UserCommentFragment;
import com.allen.iguangwai.model.Article;

/**
 * �����۵�����   ����
 */
public class AsyncUserCommentListener implements OnQuantaAsyncListener {

	private Activity activity;

	// ArrayList<Article> articleList;

	public AsyncUserCommentListener(Activity activity) {
		this.activity = activity;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onComplete(int taskId, BaseMessage baseMessage) {
		Toast.makeText(activity, "���ڼ���..", Toast.LENGTH_SHORT).show();
		// if (baseMessage.getStatus().equals("1")) {

		try {

			UserCommentFragment.articleList = (ArrayList<Article>) baseMessage
					.getDataList("Article");
			UserArticleFragment.adapter.notifyDataSetChanged();//����
			// // ���浽���ݿ�
			// // TODO
//			ArticleDao articleDao = new ArticleDao(activity);
//			articleDao.clearAllArticle();
//			articleDao.addArticle(MainActivity.articleList);
			//
			// // Toast.makeText(activity, "���سɹ�", Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			e.printStackTrace();
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