package com.allen.iguangwai.activity;

import java.util.HashMap;

import com.allen.iguangwai.R;
import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.async.Async;
import com.allen.iguangwai.listener.AsyncCommentListener;
import com.allen.iguangwai.listener.AsyncUserDataListener;
import com.allen.iguangwai.model.User;
import com.allen.iguangwai.util.BaseTools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeDataActivity extends Activity implements OnClickListener {
	User user;
	private TextView back;
	private EditText et_nickname, et_academy, et_major, et_signature;
	private Async sendUserDataAsync;
	private LinearLayout save_ly;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changedata);
		user = (User) getIntent().getSerializableExtra("user");
		init();
		show();
	}

	private void init() {
		// TODO Auto-generated method stub
		back = (TextView) findViewById(R.id.change_back);
		et_nickname = (EditText) findViewById(R.id.et_nickname);
		et_academy = (EditText) findViewById(R.id.et_academy);
		et_major = (EditText) findViewById(R.id.et_major);
		et_signature = (EditText) findViewById(R.id.et_signature);
		save_ly = (LinearLayout) this.findViewById(R.id.save_ly);
		save_ly.setOnClickListener(this);
		back.setOnClickListener(this);
		sendUserDataAsync = new Async(ChangeDataActivity.this);
		sendUserDataAsync
				.setQuantaAsyncListener(new AsyncUserDataListener(this));

	}

	private void show() {
		// TODO Auto-generated method stub
		et_nickname.setHint(user.getName());
		et_signature.setHint(user.getSignature());
		et_academy.setHint(user.getAcademy());
		et_major.setHint(user.getMajor());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.change_back:
			onBackPressed();// 调用返回键
			finish();
			break;

		case R.id.save_ly:
			
			if (!et_nickname.equals("") || !et_signature.equals("")) {
				if (!user.getUsername().equals("")) {
					MainActivity.user.setName(et_nickname.getText().toString());
					MainActivity.user.setSignature(et_signature.getText()
							.toString());

					HashMap<String, String> taskArgs2 = new HashMap<String, String>();
					taskArgs2.put("username", user.getUsername());
					taskArgs2.put("nickname", et_nickname.getText().toString());
					taskArgs2.put("signature", et_signature.getText()
							.toString());
					sendUserDataAsync.post(2, AppConfig.sendUserDataUrl,
							taskArgs2);
					//Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, "请先登录...", Toast.LENGTH_LONG).show();
				}

			}
			break;

		}
	}
}
