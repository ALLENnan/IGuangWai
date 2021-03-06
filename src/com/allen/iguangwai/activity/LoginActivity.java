package com.allen.iguangwai.activity;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.iguangwai.R;
import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.async.Async;
import com.allen.iguangwai.listener.AsyncLoginListener;
import com.allen.iguangwai.model.User;

/*
 * 登录
 */
public class LoginActivity extends Activity implements View.OnClickListener {

	private TextView tv_tourlogin;
	private EditText edit_username;
	private EditText edit_password;
	private Button bt_login;
	private Async loginAsync;
	public static ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 去除title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉Activity上面的状态栏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		loginAsync = new Async(LoginActivity.this);

		findViews();
		setlisteners();

	}

	private void findViews() {
		// TODO Auto-generated method stub
		tv_tourlogin = (TextView) findViewById(R.id.tv_tourlogin);
		tv_tourlogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		edit_username = (EditText) findViewById(R.id.edit_username);
		edit_password = (EditText) findViewById(R.id.edit_password);
		bt_login = (Button) findViewById(R.id.bt_login);
	}

	private void setlisteners() {
		bt_login.setOnClickListener(this);

		tv_tourlogin.setOnClickListener(this);
		loginAsync.setQuantaAsyncListener(new AsyncLoginListener(this));

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_login:
			String str_username = edit_username.getText().toString();
			String str_password = edit_password.getText().toString();
			HashMap<String, String> taskArgs = null;
			if (!str_username.equals("") && !str_password.equals("")) {
				taskArgs = new HashMap<String, String>();

				taskArgs.put("username", str_username);
				taskArgs.put("password", str_password);
				Log.v("myLog", str_password);
				Log.v("myLog", str_username);
				loginAsync.post(2, AppConfig.loginUrl, taskArgs);
				pd = ProgressDialog.show(this, null, "正在登录，请稍候...");
			}
			else if(str_username.equals("")){
				Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			}else if(str_password.equals("")){
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			}
			
			// Toast.makeText(this, "正在登录，请稍候...", Toast.LENGTH_SHORT).show();
//			pd = ProgressDialog.show(this, null, "正在登录，请稍候...");
			break;

		case R.id.tv_tourlogin:
			Toast.makeText(this, "正在进入，请稍候...", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			User user = new User("", "0");
			bundle.putSerializable("user", user);// 传递对象
			intent.putExtras(bundle);
			intent.setClass(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			pd = ProgressDialog.show(this, null, "正在进入，请稍候...");
			break;
		}
	}

}
