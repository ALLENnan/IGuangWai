package com.allen.iguangwai.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.R;
import com.allen.iguangwai.async.Async;
import com.allen.iguangwai.fragment.MenuFragment;
import com.allen.iguangwai.listener.AsyncOtherDataListener;
import com.allen.iguangwai.model.User;
import com.allen.iguangwai.picasso.CircleTransform;
import com.allen.iguangwai.widget.MyScrollView.OnScrollListener;
import com.squareup.picasso.Picasso;

/*
 * 我的资料，用来设置头像
 */
public class OtherDataActivity extends Activity implements OnClickListener {

	private TextView back;
	User user;
	private RelativeLayout title_bar;
	private TextView tv_title, name, academy, major, gender, sign;
	private ImageView head;

	public final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			user = (User) msg.obj;
			setUserInfo();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otherdata);
		String username = (String) getIntent().getStringExtra("username");
		// user = MainActivity.user;
		initView();
		Async getOtherDataAsync = new Async(this);
		getOtherDataAsync.setQuantaAsyncListener(new AsyncOtherDataListener(
				this, handler));
		HashMap<String, String> taskArgs = new HashMap<String, String>();
		taskArgs.put("username", username);
		getOtherDataAsync.post(2, AppConfig.getOtherDataUrl, taskArgs);
		// setUserInfo();
	}

	private void setUserInfo() {
		name.setText(user.getName());
		academy.setText(user.getAcademy());
		major.setText(user.getMajor());
		gender.setText(user.getGender());
		if (!user.getSignature().equals("")) {
			sign.setText(user.getSignature());
		}
		if (!user.getHead().equals("0")) {// 用户User类的getHead为0则为默认头像,不执行此方法
			if (user.getHead().contains("http")) {
				Picasso.with(this).load(user.getHead())
						.placeholder(R.drawable.head).error(R.drawable.head)
						.transform(new CircleTransform()).into(head);
			} else {
				File file = new File(user.getHead());
				Picasso.with(this).load(file).placeholder(R.drawable.head)
						.error(R.drawable.head)
						.transform(new CircleTransform()).into(head);
			}

		}
	}

	private void initView() {

		title_bar = (RelativeLayout) this.findViewById(R.id.title_bar);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		back = (TextView) this.findViewById(R.id.data_back);
		name = (TextView) this.findViewById(R.id.name_value);
		academy = (TextView) this.findViewById(R.id.academy_value);
		major = (TextView) findViewById(R.id.major_value);
		gender = (TextView) findViewById(R.id.gender_value);
		sign = (TextView) findViewById(R.id.sign_value);
		head = (ImageView) this.findViewById(R.id.head);// 头像
		// head.setOnClickListener(this);
		back.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.data_back:
			onBackPressed();// 调用返回键
			finish();
			break;

		}
	}

}
