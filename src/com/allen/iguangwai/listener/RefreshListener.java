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
 * listview ˢ�µļ�����
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
				taskArgs.put("Article", "share");// ѡ�����
				MainActivity.articleListAsync.post(2, AppConfig.articleUrl,
						taskArgs);// ����ȡ�õ�ǰ�������������б�
				Thread.sleep(2000);// ˢ��ʱ��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return null;
		}

		// onPostExecute���result��ֵ����doInBackground()�ķ���ֵ
		@Override
		protected void onPostExecute(String[] result) {

			adapter.notifyDataSetChanged();// ����listview
			mPullRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

}
