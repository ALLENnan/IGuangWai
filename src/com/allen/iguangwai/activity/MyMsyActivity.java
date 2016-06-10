package com.allen.iguangwai.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.allen.iguangwai.R;
import com.allen.iguangwai.adapter.MyMsyItemAdapter;
import com.allen.iguangwai.widget.SlideCutListView;
import com.allen.iguangwai.widget.SlideCutListView.RemoveListener;

/*
 * 我的私信
 */
public class MyMsyActivity extends Activity implements RemoveListener,
		OnClickListener {
	private TextView back;
	private SlideCutListView slideCutListView;
	private MyMsyItemAdapter adapter;
	private ArrayList<String> dataSourceList = new ArrayList<String>();
	private ArrayList<String> dataSourceList1 = new ArrayList<String>();
	private ArrayList<Object> dataSourceList2 = new ArrayList<Object>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_mymsy);
		init();
	}

	private void init() {
		slideCutListView = (SlideCutListView) this
				.findViewById(R.id.slideCutListView);
		slideCutListView.setRemoveListener(this);
		back = (TextView) findViewById(R.id.msy_back);
		back.setOnClickListener(this);
		dataSourceList.add("启南");
		dataSourceList.add("梓杰");
		dataSourceList.add("阳温");
		dataSourceList.add("宇耕");
		dataSourceList1.add("Android工程师");
		dataSourceList1.add("Web后台工程师");
		dataSourceList1.add("Web后台工程师");
		dataSourceList1.add("产品经理兼设计师");
		dataSourceList2.add(R.drawable.head_first);
		dataSourceList2.add(R.drawable.head_second);
		dataSourceList2.add(R.drawable.head_first);
		dataSourceList2.add(R.drawable.head_second);
		adapter = new MyMsyItemAdapter(this);
		adapter.setData(dataSourceList, dataSourceList1, dataSourceList2);
		slideCutListView.setAdapter(adapter);

		slideCutListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}

		});
	}

	public void removeItem(int position) {
		SlideCutListView.isSlide = false;
		SlideCutListView.itemView.findViewById(R.id.tv_coating).setVisibility(
				View.VISIBLE);
		dataSourceList.remove(position);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msy_back:
			onBackPressed();// 调用返回键
			finish();
			break;

		}
	}

}
