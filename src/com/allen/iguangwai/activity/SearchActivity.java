package com.allen.iguangwai.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.allen.iguangwai.R;
import com.allen.iguangwai.adapter.SearchListviewAdapter;
import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.async.QuantaAsync;
import com.allen.iguangwai.listener.AsyncsSearchListListener;
import com.allen.iguangwai.listener.ItemClickListener;
import com.allen.iguangwai.model.Article;

/*
 * 搜索
 */
public class SearchActivity extends Activity implements OnClickListener {
	private TextView back;
	private EditText search_input;
	private ListView listview_search;
	public static ArrayList<Article> articleList = new ArrayList<Article>();
	private QuantaAsync SearchAsync;
	public static SearchListviewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		SearchAsync = new QuantaAsync(SearchActivity.this);
		SearchAsync.setQuantaAsyncListener(new AsyncsSearchListListener(this));

		init();

	}

	private void init() {
		back = (TextView) this.findViewById(R.id.search_back);
		//
		listview_search = (ListView) this.findViewById(R.id.listview_search);
		search_input = (EditText) this.findViewById(R.id.search_input);

		search_input.setOnClickListener(this);
		back.setOnClickListener(this);
		findViewById(R.id.searchView).setOnClickListener(this);// 整个界面的监听

		adapter = new SearchListviewAdapter(this);// new一个自定义adapter

		listview_search.setAdapter(adapter);

		listview_search.setOnItemClickListener(new ItemClickListener(this, 2));// 设置item监听

		search_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				search();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_back:
			onBackPressed();// 调用返回键
			finish();
			break;
		case R.id.searchView:
			// 隐藏键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			break;
		}
	}

	public void search() {
		String key = search_input.getText().toString().trim();
		HashMap<String, String> taskArgs = null;
		if (!key.equals("")) {
			taskArgs = new HashMap<String, String>();

			taskArgs.put("Search", key);
			SearchAsync.post(2, AppConfig.SearchUrl, taskArgs);

		}
	}
}
