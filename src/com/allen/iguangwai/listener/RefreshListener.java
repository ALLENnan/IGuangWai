package com.allen.iguangwai.listener;

import java.util.HashMap;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.allen.iguangwai.activity.MainActivity;
import com.allen.iguangwai.adapter.MainListViewAdapter;
import com.allen.iguangwai.AppConfig;

/*
 * listview 刷新的监听类
 * 
 */
public class RefreshListener implements OnRefreshListener<ListView> {
	Activity activity;
	public PullToRefreshListView mPullRefreshListView;
	private MainListViewAdapter adapter;

	public RefreshListener(Activity activity,
			PullToRefreshListView mPullRefreshListView,
			MainListViewAdapter adapter) {
		this.activity = activity;
		this.mPullRefreshListView = mPullRefreshListView;
		this.adapter = adapter;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {

		String label = DateUtils.formatDateTime(activity,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		new GetDataTask().execute();
	}

	class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				HashMap<String, String> taskArgs = new HashMap<String, String>();
				taskArgs.put("Article", "share");// 选择分区
				MainActivity.articleListAsync.post(2, AppConfig.articleUrl,
						taskArgs);// 联网取得当前主分区的贴子列表
				Thread.sleep(2000);// 刷新时间
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return null;
		}

		// onPostExecute里的result的值就是doInBackground()的返回值
		@Override
		protected void onPostExecute(String[] result) {

			adapter.notifyDataSetChanged();// 更新listview
			mPullRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

}
