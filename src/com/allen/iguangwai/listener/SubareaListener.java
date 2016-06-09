package com.allen.iguangwai.listener;

import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.R;
import com.allen.iguangwai.activity.MainActivity;
import com.allen.iguangwai.fragment.TabFragment;

/*
 * 后台先做了一个分区的帖子给我(先把所有分区都读取同一个接口)
 */

public class SubareaListener implements OnClickListener {
	TabFragment tabFragment;
	HashMap<String, String> taskArgs;
	Button bt_subarea;


	public SubareaListener(TabFragment tabFragment, Button bt_subarea
		) {
		this.tabFragment = tabFragment;
		this.bt_subarea = bt_subarea;

	}

	private void getData() {
		// 下拉刷新，联网取贴子（代码实现手势下拉listview）
		HashMap<String, String> taskArgs = new HashMap<String, String>();
		taskArgs.put("Article", "share");// 选择分区
		MainActivity.articleListAsync.post(2, AppConfig.articleUrl, taskArgs);// 联网取得当前主分区的贴子列表
		tabFragment.mPullRefreshListView.onRefreshComplete();
		tabFragment.mPullRefreshListView.setRefreshing(true);

	}

	private void init_tab1() {

		MainActivity.tab1_exam.setBackgroundResource(R.drawable.bt_subarea);

		MainActivity.tab1_share.setBackgroundResource(R.drawable.bt_subarea);
		MainActivity.tab1_answer.setBackgroundResource(R.drawable.bt_subarea);
		bt_subarea.setBackgroundResource(R.drawable.bt_press_subarea);
		// MainActivity.tab1_exam.setTextAppearance(context,
		// R.style.bt_subarea);
		// MainActivity.tab1_share.setTextAppearance(context,
		// R.style.bt_subarea);
		// MainActivity.tab1_answer.setTextAppearance(context,
		// R.style.bt_subarea);
		// bt_subarea.setTextAppearance(context, R.style.bt_press_subarea);
	}

	private void init_tab2() {

		MainActivity.Tab2_dormitory
				.setBackgroundResource(R.drawable.bt_subarea);
		MainActivity.Tab2_food.setBackgroundResource(R.drawable.bt_subarea);
		MainActivity.Tab2_travel.setBackgroundResource(R.drawable.bt_subarea);
		bt_subarea.setBackgroundResource(R.drawable.bt_press_subarea);

	}

	private void init_tab3() {

		MainActivity.Tab3_academy.setBackgroundResource(R.drawable.bt_subarea);
		MainActivity.Tab3_corporation
				.setBackgroundResource(R.drawable.bt_subarea);
		MainActivity.Tab3_country.setBackgroundResource(R.drawable.bt_subarea);
		MainActivity.Tab3_school.setBackgroundResource(R.drawable.bt_subarea);
		bt_subarea.setBackgroundResource(R.drawable.bt_press_subarea);

	}

	private void init_tab4() {

		MainActivity.Tab4_guide.setBackgroundResource(R.drawable.bt_subarea);
		MainActivity.Tab4_solution.setBackgroundResource(R.drawable.bt_subarea);
		bt_subarea.setBackgroundResource(R.drawable.bt_press_subarea);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.bt_subarea_exam:
			init_tab1();
			getData();
			break;

		case R.id.bt_subarea_share:
			// View view = View.inflate(context, R.layout.activity_main, null);
			// Button bt = (Button) view.findViewById(R.id.bt_subarea_exam);
			// bt.setBackgroundResource(R.drawable.bt_subarea);
			// bt.setText("www");
			init_tab1();
			getData();
			break;
		case R.id.bt_subarea_answer:
			init_tab1();
			getData();
			break;
		case R.id.bt_subarea_dormitory:
			init_tab2();
			getData();
			break;
		case R.id.bt_subarea_food:
			init_tab2();
			getData();
			break;
		case R.id.bt_subarea_travel:
			init_tab2();
			getData();
			break;
		case R.id.bt_subarea_country:
			init_tab3();
			getData();
			break;
		case R.id.bt_subarea_academy:
			init_tab3();
			getData();
			break;
		case R.id.bt_subarea_corporation:
			init_tab3();
			getData();
			break;
		case R.id.bt_subarea_school:
			init_tab3();
			getData();
			break;
		case R.id.bt_subarea_guide:
			init_tab4();
			getData();
			break;
		case R.id.bt_subarea_solution:
			init_tab4();
			getData();
			break;
		}

	}
}
