package com.allen.iguangwai.listener;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.allen.iguangwai.activity.ContentActivity;
import com.allen.iguangwai.activity.SearchActivity;
import com.allen.iguangwai.async.QuantaBaseMessage;
import com.allen.iguangwai.async.QuantaAsync.OnQuantaAsyncListener;
import com.allen.iguangwai.dao.ArticleDao;
import com.allen.iguangwai.model.Article;
import com.allen.iguangwai.model.Comment;

/**
 * �첽http��������� ���������¼�
 */
public class AsyncCommentListener implements OnQuantaAsyncListener {

	private Activity activity;

	// ArrayList<Comment> commentList;

	public AsyncCommentListener(Activity activity) {
		this.activity = activity;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onComplete(int taskId, QuantaBaseMessage baseMessage) {
		// TODO Auto-generated method stub
		// Toast.makeText(activity, "���ڼ�������..", Toast.LENGTH_SHORT).show();
		// if (baseMessage.getStatus().equals("1")) {

		Log.v("myLog", "����data" + baseMessage.getData());
		if (baseMessage.getStatus().equals("1")) {
			try {
	
				ArrayList<Comment> temp = (ArrayList<Comment>) baseMessage
						.getDataList("Comment");
				ContentActivity.commentList = temp;
				ContentActivity.adapter.notifyDataSetChanged();// ����listview
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
			Toast.makeText(activity, "��������", Toast.LENGTH_SHORT).show();
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