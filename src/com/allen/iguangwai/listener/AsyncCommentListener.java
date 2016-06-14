package com.allen.iguangwai.listener;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.allen.iguangwai.activity.ContentActivity;
import com.allen.iguangwai.activity.SearchActivity;
import com.allen.iguangwai.async.BaseMessage;
import com.allen.iguangwai.async.Async.OnQuantaAsyncListener;
import com.allen.iguangwai.dao.ArticleDao;
import com.allen.iguangwai.model.Article;
import com.allen.iguangwai.model.Comment;

/**
 * 异步http请求监听器 监听请求事件
 */
public class AsyncCommentListener implements OnQuantaAsyncListener {

	private Activity activity;

	// ArrayList<Comment> commentList;

	public AsyncCommentListener(Activity activity) {
		this.activity = activity;

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onComplete(int taskId, BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		// Toast.makeText(activity, "正在加载评论..", Toast.LENGTH_SHORT).show();
		// if (baseMessage.getStatus().equals("1")) {

		Log.v("AsyncCommentListener", "评论data: " + baseMessage.toString());
		if (baseMessage.getStatus().equals("1")) {
			try {

				ArrayList<Comment> temp = (ArrayList<Comment>) baseMessage
						.getDataList("Comment");
				ContentActivity.commentList = temp;
				ContentActivity.adapter.notifyDataSetChanged();// 更新listview
				if(temp.size()==0){
				Toast.makeText(activity, "暂无评论", Toast.LENGTH_LONG).show();}
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
			Toast.makeText(activity, "暂无评论", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onComplete(int taskId) {
		Log.v("AsyncCommentListener", "评论data onComplete");
//		Toast.makeText(activity, "暂无评论", Toast.LENGTH_SHORT).show();
		Toast.makeText(activity, "请求成功！", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNetWorkError(int taskId, String errorMsg) {
		Log.v("AsyncCommentListener", "评论data onNetWorkError ");
		Toast.makeText(activity, "网络访问错误", Toast.LENGTH_SHORT).show();
	}
}