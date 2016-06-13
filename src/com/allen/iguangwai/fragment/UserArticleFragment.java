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
//我发过的帖子
public class UserArticleFragment extends Fragment {

	/** 上拉刷新listview */
	public PullToRefreshListView mPullRefreshListView;
	public static UserArticleAdapter adapter;
	public static ArrayList<Article> articleList = new ArrayList<Article>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_tab, null);
		mPullRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		adapter = new UserArticleAdapter(getActivity());// new 一个自定义adapter
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
				getActivity(), mPullRefreshListView, adapter));// 设置刷新监听
		mPullRefreshListView.setOnItemClickListener(new ItemClickListener(
				getActivity(), 1));// 设置item监听
		KLog.v("log3");
	}

}
