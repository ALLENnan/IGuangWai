package com.allen.iguangwai.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.allen.iguangwai.R;
import com.allen.iguangwai.model.User;
import com.allen.iguangwai.util.Bitmaploader;
import com.allen.iguangwai.widget.MyScrollView.OnScrollListener;

/*
 * �ҵ����ϣ���������ͷ��
 */
public class MyDataActivity extends Activity implements OnClickListener,
		OnScrollListener {
	private TextView back;
	User user;
	private RelativeLayout title_bar;
	private TextView tv_title, name, academy, major, gender, sign;
	private LinearLayout change;
	private ImageView head;
	private PopupWindow popWindow;
	private LayoutInflater layoutInflater;
	private TextView photograph, albums;
	private LinearLayout cancel;

	public static final int PHOTOZOOM = 0; // ���/����
	public static final int PHOTOTAKE = 1; // ���/����
	public static final int IMAGE_COMPLETE = 2; // ���
	public static final int CROPREQCODE = 3; // ��ȡ
	private String photoSavePath;// ����·��
	private String photoSaveName;// ͼƬ��
	private String path;// ͼƬȫ·��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydata);
		user = (User) getIntent().getSerializableExtra("user");
		initPath();
		initView();
		setUserInfo();
	}

	private void setUserInfo() {
		// TODO Auto-generated method stub
		name.setText(user.getName());
		academy.setText(user.getAcademy());
		major.setText(user.getMajor());
		gender.setText(user.getGender());
		if (!user.getSignature().equals("")) {
			sign.setText(user.getSignature());
		}
	}

	private void initView() {

		title_bar = (RelativeLayout) this.findViewById(R.id.title_bar);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		back = (TextView) this.findViewById(R.id.data_back);
		change = (LinearLayout) this.findViewById(R.id.change);
		name = (TextView) this.findViewById(R.id.name_value);
		academy = (TextView) this.findViewById(R.id.academy_value);
		major = (TextView) findViewById(R.id.major_value);
		gender = (TextView) findViewById(R.id.gender_value);
		sign = (TextView) findViewById(R.id.sign_value);
		head = (ImageView) this.findViewById(R.id.head);// ͷ��
		head.setOnClickListener(this);
		back.setOnClickListener(this);
		change.setOnClickListener(this);
		if (!user.getHead().equals("0")) {// �û�User���getHeadΪ0��ΪĬ��ͷ��,��ִ�д˷���
			Bitmaploader bitmapTools = new Bitmaploader(
					BitmapFactory.decodeResource(this.getResources(),
							R.drawable.default_image));
			bitmapTools.loadBitmap(user.getHead(), head, 150, 150, true);// �첽����ͷ��\

		}

	}

	private void initPath() {
		// TODO Auto-generated method stub
		File file = new File(Environment.getExternalStorageDirectory(),
				"ClipHeadPhoto/cache");
		if (!file.exists())
			file.mkdirs();
		// ͼƬ�ı���·��
		photoSavePath = Environment.getExternalStorageDirectory()
				+ "/ClipHeadPhoto/cache/";
		// ͼƬ��
		photoSaveName = System.currentTimeMillis() + ".png";
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.head:
			showPopupWindow(head);
			break;
		case R.id.data_back:
			onBackPressed();// ���÷��ؼ�
			finish();
			break;
		case R.id.change:
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("user", user);// ���ݶ���
			intent.putExtras(bundle);
			intent.setClass(MyDataActivity.this, ChangeDataActivity.class);
			startActivity(intent);
		}
	}

	@SuppressWarnings("deprecation")
	private void showPopupWindow(View parent) {
		if (popWindow == null) {
			layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.pop_select_photo, null);
			popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			initPop(view);
		}
		popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	// ѡ���ͷ���С���ڵĳ�ʼ��
	public void initPop(View view) {
		photograph = (TextView) view.findViewById(R.id.photograph);// ����
		albums = (TextView) view.findViewById(R.id.albums);// ���
		cancel = (LinearLayout) view.findViewById(R.id.cancel);// ȡ��
		photograph.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				photoSaveName = String.valueOf(System.currentTimeMillis())
						+ ".png";
				Uri imageUri = null;
				Intent openCameraIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
				openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION,
						0);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(openCameraIntent, PHOTOTAKE);
			}
		});
		albums.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();
				Intent openAlbumIntent = new Intent(Intent.ACTION_PICK, null);
				openAlbumIntent
						.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
				startActivityForResult(openAlbumIntent, PHOTOZOOM);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				popWindow.dismiss();

			}
		});
	}

	/**
	 * ͼƬѡ�����ս��
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		Uri uri = null;
		switch (requestCode) {
		case PHOTOZOOM:// ���
			if (data == null) {
				return;
			}
			uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index);// ͼƬ�ڵ�·��
			Intent intent3 = new Intent(MyDataActivity.this, ClipActivity.class);
			intent3.putExtra("path", path);
			startActivityForResult(intent3, IMAGE_COMPLETE);
			break;
		case PHOTOTAKE:// ����
			path = photoSavePath + photoSaveName;
			uri = Uri.fromFile(new File(path));
			Intent intent2 = new Intent(MyDataActivity.this, ClipActivity.class);
			intent2.putExtra("path", path);
			startActivityForResult(intent2, IMAGE_COMPLETE);
			break;
		case IMAGE_COMPLETE:
			final String temppath = data.getStringExtra("path");
			head.setImageBitmap(getLoacalBitmap(temppath));
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	// �������Ҳ�Ǹ�ͼƬ�ü���ҳ���й�
	@Override
	public void onScroll(int scrollY) {
		if (scrollY < 30) {
			tv_title.setVisibility(View.GONE);
			title_bar.setBackgroundColor(Color.parseColor("#00000000"));
		} else {
			tv_title.setVisibility(View.VISIBLE);
			title_bar.setBackgroundColor(Color.parseColor("#18B4ED"));
		}
	}

}
