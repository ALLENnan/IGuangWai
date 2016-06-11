package com.allen.iguangwai.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.allen.iguangwai.R;
import com.allen.iguangwai.activity.LoginActivity;
import com.allen.iguangwai.activity.MyAcyActivity;
import com.allen.iguangwai.activity.MyDataActivity;
import com.allen.iguangwai.activity.MyMsyActivity;
import com.allen.iguangwai.model.User;
import com.allen.iguangwai.picasso.CircleTransform;
import com.squareup.picasso.Picasso;

/*
 * SlidingMenu填充的Fragment
 */
public class MenuFragment extends Fragment implements OnClickListener {
	private User user;
	public static ImageView head;
	private TextView user_name;
	private LinearLayout mydata_bt, mymsg_bt, myacy_bt, offline_bt;
	Activity activity;
	private View mView;

	public MenuFragment() {

	}

	public MenuFragment(Activity activity, User user) {
		this.activity = activity;
		this.user = user;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			initView(inflater, container);
		}
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initHear();
		mydata_bt.setOnClickListener(this);
		mymsg_bt.setOnClickListener(this);
		myacy_bt.setOnClickListener(this);
		offline_bt.setOnClickListener(this);
	}

	private void initView(LayoutInflater inflater, ViewGroup container) {
		mView = inflater.inflate(R.layout.fragment_drawer, container, false);
		head = (ImageView) mView.findViewById(R.id.menu_head);
		user_name = (TextView) mView.findViewById(R.id.user_name);
		mydata_bt = (LinearLayout) mView.findViewById(R.id.mydata_bt);
		mymsg_bt = (LinearLayout) mView.findViewById(R.id.mymsg_bt);
		myacy_bt = (LinearLayout) mView.findViewById(R.id.myacy_bt);
		offline_bt = (LinearLayout) mView.findViewById(R.id.offline_bt);

	}

	/** 初始化头像 */
	private void initHear() {
		// TODO Auto-generated method stub
		// 游客不执行此方法

		if (!user.getUsername().equals("")) {
			user_name.setText(user.getRealname());// 设置真名

			if (!user.getHead().equals("0")) {// 用户User类的getHead为0则为默认头像,不执行此方法
//				Bitmaploader bitmapTools = new Bitmaploader(
//						BitmapFactory.decodeResource(this.getResources(),
//								R.drawable.default_image));
//				bitmapTools.loadBitmap(user.getHead(), head, 100, 100, true);// 异步加载头像

				Picasso.with(getActivity()).load(user.getHead())
						.placeholder(R.drawable.head).error(R.drawable.head)
						
						.transform(new CircleTransform()).into(head);
			}
		}
		// else {
		// Picasso.with(getActivity())
		// .load("http://img3.duitang.com/uploads/blog/201405/03/20140503112526_VVGQE.thumb.700_0.jpeg")
		// .transform(new CircleTransform())
		// .into(head);
		// }

	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.mydata_bt:
			Bundle bundle = new Bundle();
			bundle.putSerializable("user", this.user);// 传递user对象
			intent.putExtras(bundle);
			intent.setClass(getActivity(), MyDataActivity.class);
			startActivity(intent);
			break;
		case R.id.mymsg_bt:
			intent.setClass(getActivity(), MyMsyActivity.class);
			startActivity(intent);
			break;
		case R.id.myacy_bt:
			intent.setClass(getActivity(), MyAcyActivity.class);
			startActivity(intent);
			break;
		case R.id.offline_bt:
			intent.setClass(getActivity(), LoginActivity.class);
			startActivity(intent);
			getActivity().finish();
			break;
		}

	}

}
