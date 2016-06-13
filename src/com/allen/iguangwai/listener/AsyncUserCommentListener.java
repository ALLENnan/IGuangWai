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
 * 我评论的帖子   监听
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
		Toast.makeText(activity, "正在加载..", Toast.LENGTH_SHORT).show();
		// if (baseMessage.getStatus().equals("1")) {

		try {

			UserCommentFragment.articleList = (ArrayList<Article>) baseMessage
					.getDataList("Article");
			UserArticleFragment.adapter.notifyDataSetChanged();//更新
			// // 保存到数据库
			// // TODO
//			ArticleDao articleDao = new ArticleDao(activity);
//			articleDao.clearAllArticle();
//			articleDao.addArticle(MainActivity.articleList);
			//
			// // Toast.makeText(activity, "返回成功", Toast.LENGTH_SHORT).show();

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