package com.allen.iguangwai.listener;

import java.util.HashMap;

import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.async.QuantaAsync;
import com.allen.iguangwai.async.QuantaAsync.OnQuantaAsyncListener;
import com.allen.iguangwai.async.QuantaBaseMessage;
import com.allen.iguangwai.model.Article;

public class AsyncSendCommentListener implements OnQuantaAsyncListener {
	private Activity activity;
	Article article;
	BaseAdapter adapter;
	QuantaAsync CommentAsync;

	// ArrayList<Comment> commentList;

	public AsyncSendCommentListener(Activity activity, Article article,
			QuantaAsync CommentAsync, BaseAdapter adapter) {
		this.activity = activity;
		this.article = article;
		this.CommentAsync = CommentAsync;
		this.adapter = adapter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onComplete(int taskId, QuantaBaseMessage baseMessage) {

		if (baseMessage.getStatus().equals("1")) {
			try {
				Toast.makeText(activity, "发表成功", Toast.LENGTH_SHORT).show();
				HashMap<String, String> taskArgs3 = new HashMap<String, String>();

				taskArgs3.put("Comment", article.getId());
				CommentAsync.post(2, AppConfig.getCommentUrl, taskArgs3);
				adapter.notifyDataSetChanged();
				// ArrayList<Comment> temp = (ArrayList<Comment>) baseMessage
				// .getDataList("Comment");
				// ContentActivity.commentList = temp;
				// ContentActivity.adapter.notifyDataSetChanged();// 更新listview
				// Toast.makeText(activity, "返回成功..",
				// Toast.LENGTH_SHORT).show();

				// ArticleDao articleDao = new ArticleDao(activity);
				// articleDao.clearAllArticle();
				// articleDao.addArticle(articleList);
				// Toast.makeText(activity, "返回成功", Toast.LENGTH_SHORT).show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (baseMessage.getStatus().equals("0")) {
			Toast.makeText(activity, "评论失败", Toast.LENGTH_SHORT).show();
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
