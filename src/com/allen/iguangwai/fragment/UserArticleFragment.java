package com.allen.iguangwai.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.socks.library.KLog;
import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.R;
import com.allen.iguangwai.activity.MainActivity;
import com.allen.iguangwai.adapter.MainListViewAdapter;
import com.allen.iguangwai.adapter.UserArticleAdapter;
import com.allen.iguangwai.async.Async;
import com.allen.iguangwai.listener.AsyncUserArticleListener;
import com.allen.iguangwai.listener.ItemClickListener;
import com.allen.iguangwai.listener.RefreshListener;
import com.allen.iguangwai.model.Article;
//�ҷ���������
public class UserArticleFragment extends Fragment {

	/** ����ˢ��listview */
	public PullToRefreshListView mPullRefreshListView;
	public static UserArticleAdapter adapter;
	public static ArrayList<Article> articleList = new ArrayList<Article>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_tab, null);
		mPullRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		adapter = new UserArticleAdapter(getActivity());// new һ���Զ���adapter
		mPullRefreshListView.setAdapter(adapter);
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		http();
		setListviewListener();
	}

	private void http() {
		Async userArticleAsync = new Async(getActivity());
		userArticleAsync.setQuantaAsyncListener(new AsyncUserArticleListener(
				getActivity()));
		HashMap<String, String> taskArgs = new HashMap<String, String>();
		taskArgs.put("username", MainActivity.user.getUsername());
		userArticleAsync.post(2, AppConfig.getUserArticle, taskArgs);
	}

	private void setListviewListener() {

		mPullRefreshListView.setOnRefreshListener(new RefreshListener(
				getActivity(), mPullRefreshListView, adapter));// ����ˢ�¼���
		mPullRefreshListView.setOnItemClickListener(new ItemClickListener(
				getActivity(), 1));// ����item����
		KLog.v("log3");
	}

}
