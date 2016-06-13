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
//			"Ǧ��",//
//			"����", "���澫��", "MacBook Pro", "ƽ�����", "��ʫ����",//
//			"����ŷ TR-100", "�ʼǱ�", "SPY Mouse", "Thinkpad E40", "�������",//
//			"�ڴ�����", "��ͼ", "����", "����", "����",//
//			"ͨѶ¼", "������", "CSDN leak", "��ȫ", "3D",//
//			"��Ů", "����", "4743G", "����", "����",//
//			"ŷ��", "�����", "��ŭ��С��", "mmShow", "���׹�����",//
//			"iciba", "��ˮ��ϵ", "����App", "������", "365����",//
//			"����ʶ��", "Chrome", "Safari", "�й���Siri", "A5������",//
//			"iPhone4S", "Ħ�� ME525", "���� M9", "�῵ S2500" };
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
		// ���
		if (MainActivity.articleList!=null) {
		    ArrayList<Article> articleList = MainActivity.articleList;
			for (int i = 0; i < articleList.size(); i++) {
				keywords.add(articleList.get(i).getTitle());
			}
		}
		else {
			keywords.add("���޻���");
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
			onBackPressed();// ���÷��ؼ�
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