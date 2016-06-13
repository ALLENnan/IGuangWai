package com.allen.iguangwai.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.allen.iguangwai.model.Article;
import com.allen.iguangwai.widget.KeywordsFlow;
import com.allen.iguangwai.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFlyActivity extends Activity implements OnClickListener {
//	public static final String[] keywords = { "QQ", "Sodino", "APK", "GFW",
//			"铅笔",//
//			"短信", "桌面精灵", "MacBook Pro", "平板电脑", "雅诗兰黛",//
//			"卡西欧 TR-100", "笔记本", "SPY Mouse", "Thinkpad E40", "捕鱼达人",//
//			"内存清理", "地图", "导航", "闹钟", "主题",//
//			"通讯录", "播放器", "CSDN leak", "安全", "3D",//
//			"美女", "天气", "4743G", "戴尔", "联想",//
//			"欧朋", "浏览器", "愤怒的小鸟", "mmShow", "网易公开课",//
//			"iciba", "油水关系", "网游App", "互联网", "365日历",//
//			"脸部识别", "Chrome", "Safari", "中国版Siri", "A5处理器",//
//			"iPhone4S", "摩托 ME525", "魅族 M9", "尼康 S2500" };
	private KeywordsFlow keywordsFlow;
	private Button changeBtn;
	private LinearLayout back;
	private List<String> keywords=new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchfly);

		changeBtn = (Button) findViewById(R.id.change_button);
		back = (LinearLayout) findViewById(R.id.back);
		changeBtn.setOnClickListener(this);
		back.setOnClickListener(this);
		keywordsFlow = (KeywordsFlow) findViewById(R.id.frameLayout1);
		keywordsFlow.setDuration(800l);
		keywordsFlow.setOnItemClickListener(this);
		// 添加
		if (MainActivity.articleList!=null) {
		    ArrayList<Article> articleList = MainActivity.articleList;
			for (int i = 0; i < articleList.size(); i++) {
				keywords.add(articleList.get(i).getTitle());
			}
		}
		else {
			keywords.add("暂无话题");
		}
	
		feedKeywordsFlow(keywordsFlow, keywords);
		keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
	}

	private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, List<String> keywords) {
		Random random = new Random();
		for (int i = 0; i < KeywordsFlow.MAX; i++) {
			int ran = random.nextInt(keywords.size());
			String tmp = keywords.get(ran);
			keywordsFlow.feedKeyword(tmp);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == changeBtn) {
			keywordsFlow.rubKeywords();
			// keywordsFlow.rubAllViews();
			feedKeywordsFlow(keywordsFlow, keywords);
			keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
		} else if (v == back) {
			// keywordsFlow.rubKeywords();
			// // keywordsFlow.rubAllViews();
			// feedKeywordsFlow(keywordsFlow, keywords);
			// keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
			onBackPressed();// 调用返回键
			finish();
		} else if (v instanceof TextView) {
			String keyword = ((TextView) v).getText().toString();
			// Intent intent = new Intent();
			// intent.setAction(Intent.ACTION_VIEW);
			// intent.addCategory(Intent.CATEGORY_DEFAULT);
			// intent.setData(Uri.parse("http://www.google.com.hk/#q=" +
			// keyword));
			// startActivity(intent);
			Log.e("Search", keyword);

			Intent intent = new Intent();
			intent.putExtra("keyword", keyword);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}