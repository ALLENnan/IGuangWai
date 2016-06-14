package com.allen.iguangwai.activity;

import com.allen.iguangwai.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/*
 * 欢迎界面
 */
public class WelcomeActivity extends Activity {
	private AlphaAnimation start_anima;
	private View welcomeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		welcomeView = View.inflate(this, R.layout.welcome, null);
		setContentView(welcomeView);
		initData();
	}

	private void initData() {
		start_anima = new AlphaAnimation(0.6f, 1.0f);// 设置透明度渐变动画
		start_anima.setDuration(4000);// 设置动画持续时间
		welcomeView.startAnimation(start_anima);

		start_anima.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				redirectTo();
			}
		});
	}

	private void redirectTo() {
		startActivity(new Intent(getApplicationContext(), LoginActivity.class));
		finish();
	}
}
