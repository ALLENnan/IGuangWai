package com.allen.iguangwai.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.iguangwai.R;
import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.async.Async;
import com.allen.iguangwai.listener.AsyncPublishListener;
import com.allen.iguangwai.util.ImageUtils;

public class PublishActivity extends Activity implements OnClickListener {

	private TextView back;
	private EditText title_et;
	private EditText content_et;
	private ImageView send;
	private Async publishAsync;
	private RelativeLayout lin;// 用于判断软键盘是否弹出
	private boolean isKeyBoardShow = false, isBQViewShow = false;// 软键盘是否弹出的状态
	private boolean isADJUST_PAN = false, isADJUST_RESIZE = false;
	private ImageView ivPic;// 发帖需要发出的图片的显示
	RelativeLayout relPic;// 图片显示的布局
	private int SELECT_PICTURE = 1; // 从图库中选择图片
	private int SELECT_CAMER = 2; // 用相机拍摄照片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setADJUST_RESIZE();
		setContentView(R.layout.activity_publish);
		publishAsync = new Async(this);
		publishAsync.setQuantaAsyncListener(new AsyncPublishListener(this));
		initView();
		initclik();
	}

	private void initView() {
		back = (TextView) findViewById(R.id.pub_back);
		title_et = (EditText) findViewById(R.id.title_et);
		content_et = (EditText) findViewById(R.id.content_et);
		send = (ImageView) findViewById(R.id.send);

		back.setOnClickListener(this);
		send.setOnClickListener(this);

	}

	// 初始化页面点击事件
	public Void initclik() {
		ivPic = (ImageView) findViewById(R.id.wpost_img);
		relPic = (RelativeLayout) findViewById(R.id.wpost_imglayout);
		findViewById(R.id.wpost_remimg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						relPic.setVisibility(View.GONE);
					}
				});

		findViewById(R.id.wpost_getimg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isKeyBoardShow) {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
						}
						showSetHeadimg();
					}
				});
		// 设置添加添加图片时手机软键盘是为未弹出状态
		lin = (RelativeLayout) findViewById(R.id.wpos_layout);
		lin.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						int n = lin.getRootView().getHeight() - lin.getHeight();
						if (n > 100) {// 软键盘已弹出
							if (isBQViewShow) {

								setADJUST_RESIZE();

								isBQViewShow = false;
							}
							isKeyBoardShow = true;
						} else {// 软键盘未弹出
							isKeyBoardShow = false;
							if (isADJUST_PAN) {
								setADJUST_RESIZE();
							}
						}
					}
				});
		return null;
	}

	@Override
	protected void onResume() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

			}
		}, 300);
		super.onResume();
	}

	private void setADJUST_PAN() {
		isADJUST_RESIZE = false;
		isADJUST_PAN = true;
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

	}

	private void setADJUST_RESIZE() {
		isADJUST_RESIZE = true;
		isADJUST_PAN = false;
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

	}

	public void cancle(View v) {
		finish();
	}

	public void button(View v) {

	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();

		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	// 对添加图片功能的窗口的初始化
	private void showSetHeadimg() {
		final PopupWindow popupWindow = new PopupWindow(this);
		View v = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.dialog_changetx, null);// 设置添加图片功能窗口的xml
		popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindow.setContentView(v);
		popupWindow.setAnimationStyle(R.style.AnimationPreview);
		popupWindow.showAtLocation(new View(this), Gravity.CENTER, 0, 0);
		backgroundAlpha(0.35f);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
			}
		});
		v.findViewById(R.id.photograph).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
						// 进入相机
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(getExternalCacheDir(),
										"edtimg.jpg")));
						startActivityForResult(intent, SELECT_CAMER);
					}
				});
		v.findViewById(R.id.albums).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				// 进入图库
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, SELECT_PICTURE);
			}
		});
		v.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

	}

	@Override
	protected void onActivityResult(final int requestCode, int resultCode,
			final Intent data) {
		if (resultCode == RESULT_OK) {
			Thread thread = new Thread(new Runnable() {
				// 添加的图片的显示
				@Override
				public void run() {
					Bitmap bitmap = null;
					if (requestCode == SELECT_CAMER) {
						bitmap = BitmapFactory.decodeFile(getExternalCacheDir()
								+ "/edtimg.jpg");
					} else if (requestCode == SELECT_PICTURE) {
						Uri uri = data.getData();
						ContentResolver cr = PublishActivity.this
								.getContentResolver();
						try {
							bitmap = BitmapFactory.decodeStream(cr
									.openInputStream(uri));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					Message msg = new Message();
					msg.obj = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
					handler.sendMessage(msg);
					ImageUtils.comp(bitmap);
					//
				}
			});

			thread.start();

		} else {
			Toast.makeText(this, "选择图片失败,请重新选择", Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bitmap bitmap = (Bitmap) msg.obj;
			relPic.setVisibility(View.VISIBLE);
			ivPic.setImageBitmap(bitmap);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pub_back:
			onBackPressed();// 调用返回键
			finish();
			break;
		case R.id.send:
			send();
		}
	}

	private void send() {

		String title = title_et.getText().toString();
		String content = content_et.getText().toString();
		HashMap<String, String> taskArgs = null;
		if (!title.equals("") && !content.equals("")) {
			SimpleDateFormat time = new SimpleDateFormat("mm:ss");
			String publishTime = time.format(new java.util.Date());
			taskArgs = new HashMap<String, String>();
			taskArgs.put("id", MainActivity.user.getUsername());
			taskArgs.put("area", "学习区");
			taskArgs.put("type", "考试");
			taskArgs.put("title", title);
			taskArgs.put("description", "摘要");
			taskArgs.put("content", content);
			taskArgs.put("publishTime", publishTime);

			publishAsync.post(2, AppConfig.publishUrl, taskArgs);
			Toast.makeText(getApplicationContext(), "正在发布...",
					Toast.LENGTH_LONG).show();
		}
	}

}
