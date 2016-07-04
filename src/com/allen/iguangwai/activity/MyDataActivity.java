package com.allen.iguangwai.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.text.TextUtils;
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
import com.allen.iguangwai.fragment.MenuFragment;
import com.allen.iguangwai.model.User;
import com.allen.iguangwai.picasso.CircleTransform;
import com.allen.iguangwai.util.LogUtil;
import com.allen.iguangwai.util.NetUtil;
import com.allen.iguangwai.widget.MyScrollView.OnScrollListener;
import com.squareup.picasso.Picasso;

/*
 * 我的资料，用来设置头像
 */
public class MyDataActivity extends Activity implements OnClickListener,
		OnScrollListener {
	private TextView back;
	private Context context;
	User user;
	private RelativeLayout title_bar;
	private TextView tv_title, name, academy, major, gender, sign;
	private LinearLayout change;
	private ImageView head;
	private PopupWindow popWindow;
	private LayoutInflater layoutInflater;
	private TextView photograph, albums;
	private LinearLayout cancel;

	public static final int PHOTOZOOM = 0; // 相册/拍照
	public static final int PHOTOTAKE = 1; // 相册/拍照
	public static final int IMAGE_COMPLETE = 2; // 结果
	public static final int CROPREQCODE = 3; // 截取
	private String photoSavePath;// 保存路径
	private String photoSaveName;// 图片名
	private String path;// 图片全路径
	ProgressDialog pd;
	String resultStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_mydata);
		// LogUtil.v("MyDataActivity-->onCreate",
		// user.getName() + user.getSignature());
		// user = (User) getIntent().getSerializableExtra("user");
		user = MainActivity.user;
//		
//		LogUtil.v("MyDataActivity-->onCreate",
//				user.getName() + user.getSignature());
		initPath();
		initView();
//		LogUtil.v("MyDataActivity-->onCreate",
//				user.getName() + user.getSignature());
//		if (condition) {
//			
//		}
		setUserInfo();
	}

	private void setUserInfo() {
		name.setText(user.getName());
		LogUtil.v("MyDataActivity-->setUserInfo",
				user.getName() + user.getSignature());
		academy.setText(user.getAcademy());
		major.setText(user.getMajor());
		gender.setText(user.getGender());
		if (user.getSignature()!=null&&!user.getSignature().equals("")) {
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
		head = (ImageView) this.findViewById(R.id.head);// 头像
		head.setOnClickListener(this);
		back.setOnClickListener(this);
		change.setOnClickListener(this);
		if (!user.getHead().equals("0")) {// 用户User类的getHead为0则为默认头像,不执行此方法
			// Bitmaploader bitmapTools = new Bitmaploader(
			// BitmapFactory.decodeResource(this.getResources(),
			// R.drawable.default_image));
			// bitmapTools.loadBitmap(user.getHead(), head, 150, 150, true);//
			// 异步加载头像\
			// Toast.makeText(this, "地址" + user.getHead(), Toast.LENGTH_LONG)
			// .show();
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

	private void initPath() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"ClipHeadPhoto/cache");
		if (!file.exists())
			file.mkdirs();
		// 图片的保存路径
		photoSavePath = Environment.getExternalStorageDirectory()
				+ "/ClipHeadPhoto/cache/";
		// 图片名
		photoSaveName = System.currentTimeMillis() + ".png";
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.head:
			showPopupWindow(head);
			break;
		case R.id.data_back:
			onBackPressed();// 调用返回键
			finish();
			break;
		case R.id.change:
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("user", user);// 传递对象
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

	// 选择改头像的小窗口的初始化
	public void initPop(View view) {
		photograph = (TextView) view.findViewById(R.id.photograph);// 拍照
		albums = (TextView) view.findViewById(R.id.albums);// 相册
		cancel = (LinearLayout) view.findViewById(R.id.cancel);// 取消
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
	 * 图片选择及拍照结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		Uri uri = null;
		switch (requestCode) {
		case PHOTOZOOM:// 相册
			if (data == null) {
				return;
			}
			uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index);// 图片在的路径
			Intent intent3 = new Intent(MyDataActivity.this, ClipActivity.class);
			intent3.putExtra("path", path);
			startActivityForResult(intent3, IMAGE_COMPLETE);
			break;
		case PHOTOTAKE:// 拍照
			path = photoSavePath + photoSaveName;
			uri = Uri.fromFile(new File(path));
			Intent intent2 = new Intent(MyDataActivity.this, ClipActivity.class);
			intent2.putExtra("path", path);
			startActivityForResult(intent2, IMAGE_COMPLETE);
			break;
		case IMAGE_COMPLETE:// 结果，上传图片到服务器
			final String temppath = data.getStringExtra("path");
			head.setImageBitmap(getLoacalBitmap(temppath));
			// TODO
			MenuFragment.head.setImageBitmap(getLoacalBitmap(temppath));
			user.setHead(temppath);
			path = temppath;

//			pd = ProgressDialog.show(this, null, "正在上传头像到服务器...");
			new Thread(uploadHeadRunnable).start();
			LogUtil.v("MyDataActivity", "uploadHeadRunnable");

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

	// 这个方法也是跟图片裁剪的页面有关
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

	Runnable uploadHeadRunnable = new Runnable() {

		@Override
		public void run() {

			Map<String, String> textParams = new HashMap<String, String>();
			Map<String, File> fileparams = new HashMap<String, File>();

			try {
				// 创建一个URL对象
				URL url = new URL(AppConfig.sendUserDataUrl);
				textParams = new HashMap<String, String>();
				fileparams = new HashMap<String, File>();
				// 要上传的图片文件
				if (path != null) {
					LogUtil.v("path", path);
					File file = new File(path);
					fileparams.put("pic", file);
				}
				
				textParams.put("username", MainActivity.user.getUsername());
				LogUtil.v("username", MainActivity.user.getUsername());
				// textParams.put("area", "考試");
				// textParams.put("type", "学习区");
				// textParams.put("title", title);
				// textParams.put("description", "摘要");
				// textParams.put("content", content);
				// textParams.put("publishTime", publishTime);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				// 设置不使用缓存（容易出现问题）
				conn.setUseCaches(false);
				// 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
				conn.setRequestProperty("ser-Agent", "Fiddler");
				// 设置contentType
				conn.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + NetUtil.BOUNDARY);
				OutputStream os = conn.getOutputStream();
				DataOutputStream ds = new DataOutputStream(os);
				NetUtil.writeStringParams(textParams, ds);
				NetUtil.writeFileParams(fileparams, ds);
				NetUtil.paramsEnd(ds);
				os.close();

				int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
				LogUtil.v("MyDataActivity", "uploadHeadRunnable " + "响应码"
						+ code);
				if (code == 200) {// 返回的响应码200,是成功
					// 得到网络返回的输入流
					InputStream is = conn.getInputStream();
					resultStr = NetUtil.readString(is);
					handler.sendEmptyMessage(1);
//					LogUtil.v("MyDataActivity", "uploadHeadRunnable "
//							+ "响应码200" + resultStr);
				} else {
					// InputStream is = conn.getInputStream();
					// resultStr = NetUtil.readString(is);
					InputStream is = conn.getInputStream();
					resultStr = NetUtil.readString(is);
					LogUtil.v("PublishActivity--uploadImageRunnable", "网络请求失败");
					handler.sendEmptyMessage(2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
//				pd.dismiss();
				try {
					LogUtil.v("MyDataActivity", resultStr);
					JSONObject jsonObject = new JSONObject(resultStr);
					if (jsonObject.optString("status").equals("1")) {
						// Toast.makeText(context, "设置头像成功",
						// Toast.LENGTH_SHORT).show();
						LogUtil.v("PublishActivity-->handleMessage", "设置头像成功");
					} else {
						// Toast.makeText(context, "设置头像失败",
						// Toast.LENGTH_SHORT).show();
						LogUtil.v("PublishActivity-->handleMessage", "设置头像失败");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 2:
//				pd.dismiss();
				// Toast.makeText(context, "网络请求失败", Toast.LENGTH_SHORT).show();
				LogUtil.v("PublishActivity-->handleMessage", "网络请求失败");
				break;
			}

		}
	};

}
