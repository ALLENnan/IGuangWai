package com.allen.iguangwai.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.iguangwai.App;
import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.R;
import com.allen.iguangwai.async.Async;
import com.allen.iguangwai.listener.AsyncPublishListener;
import com.allen.iguangwai.util.ImageUtils;
import com.allen.iguangwai.util.LogUtil;
import com.allen.iguangwai.util.NetUtil;

public class PublishActivity extends Activity implements OnClickListener {
	public Context context;
	private TextView back;
	private EditText title_et;
	private EditText content_et;
	private LinearLayout send;
	private Async publishAsync;
	private RelativeLayout lin;// �����ж�������Ƿ񵯳�
	private boolean isKeyBoardShow = false, isBQViewShow = false;// ������Ƿ񵯳���״̬
	private boolean isADJUST_PAN = false, isADJUST_RESIZE = false;
	private ImageView add_pic;
	private ImageView ivPic;// ������Ҫ������ͼƬ����ʾ
	RelativeLayout relPic;// ͼƬ��ʾ�Ĳ���
	private int SELECT_PICTURE = 1; // ��ͼ����ѡ��ͼƬ
	private int SELECT_CAMER = 2; // �����������Ƭ
	private String path; // ͼƬ·��
	ProgressDialog pd;
	private String resultStr;

	String title;
	String content;
	String publishTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setADJUST_RESIZE();
		setContentView(R.layout.activity2_publish);
		publishAsync = new Async(this);
		publishAsync.setQuantaAsyncListener(new AsyncPublishListener(this));
		initView();
		// initclik();
	}

	private void initView() {
		back = (TextView) findViewById(R.id.pub_back);
		title_et = (EditText) findViewById(R.id.title_et);
		content_et = (EditText) findViewById(R.id.content_et);
		send = (LinearLayout) findViewById(R.id.send);
		add_pic = (ImageView) findViewById(R.id.add_pic);
		add_pic.setOnClickListener(this);
		back.setOnClickListener(this);
		send.setOnClickListener(this);

	}

	// ��ʼ��ҳ�����¼�
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
		// ����������ͼƬʱ�ֻ��������Ϊδ����״̬
		lin = (RelativeLayout) findViewById(R.id.wpos_layout);
		lin.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						int n = lin.getRootView().getHeight() - lin.getHeight();
						if (n > 100) {// ������ѵ���
							if (isBQViewShow) {

								setADJUST_RESIZE();

								isBQViewShow = false;
							}
							isKeyBoardShow = true;
						} else {// �����δ����
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
	 * ���������Ļ�ı���͸����
	 * 
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();

		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	// �����ͼƬ���ܵĴ��ڵĳ�ʼ��
	private void showSetHeadimg() {
		final PopupWindow popupWindow = new PopupWindow(this);
		View v = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.dialog_changetx, null);// �������ͼƬ���ܴ��ڵ�xml
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
						// �������
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
				// ����ͼ��
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
				// ��ӵ�ͼƬ����ʾ
				@Override
				public void run() {
					Bitmap bitmap = null;
					if (requestCode == SELECT_CAMER) {
						path = getExternalCacheDir() + "/edtimg.jpg";
						LogUtil.v("PublishActivity-->path", path + "");
						bitmap = BitmapFactory.decodeFile(path);
						// ѹ��
						bitmap = ImageUtils.comp(bitmap);
						ImageUtils.savePhotoToSDCard(bitmap, path);

					} else if (requestCode == SELECT_PICTURE) {
						Uri uri = data.getData();

						LogUtil.v("PublishActivity-->uri", uri + "");
						// Toast.makeText(getApplicationContext(), "uri" + uri,
						// Toast.LENGTH_LONG).show();
						ContentResolver resolver = PublishActivity.this
								.getContentResolver();

						// bitmap = MediaStore.Images.Media.getBitmap(
						// resolver, uri);

						// String[] proj = { MediaStore.Images.Media.DATA };
						// Cursor cursor = managedQuery(uri, proj, null, null,
						// null);
						// int column_index = cursor
						// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						// cursor.moveToFirst();
						// path = cursor.getString(column_index);
						// LogUtil.v("PublishActivity-->path", path + "");

						try {
							bitmap = BitmapFactory.decodeStream(resolver
									.openInputStream(uri));
							// ѹ��
							bitmap = ImageUtils.comp(bitmap);
							path = getExternalCacheDir() + "/picture.jpg";
							ImageUtils.savePhotoToSDCard(bitmap, path);
							LogUtil.v("PublishActivity-->path", path + "");

						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					Message msg = new Message();
					msg.what = 0;
					msg.obj = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
					handler.sendMessage(msg);
					// ImageUtils.comp(bitmap);
					//
				}
			});

			thread.start();

		} else {
			Toast.makeText(this, "ѡ��ͼƬʧ��,������ѡ��", Toast.LENGTH_SHORT).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Bitmap bitmap = (Bitmap) msg.obj;
				// relPic.setVisibility(View.VISIBLE);
				// ivPic.setImageBitmap(bitmap);
				add_pic.setImageBitmap(bitmap);
				break;
			case 1:
				pd.dismiss();
				try {
					JSONObject jsonObject = new JSONObject(resultStr);
					if (jsonObject.optString("status").equals("1")) {
						Toast.makeText(context, "�����ɹ�", Toast.LENGTH_SHORT)
								.show();
						LogUtil.v("PublishActivity-->handleMessage", "�����ɹ�");
					} else {
						Toast.makeText(context, "����ʧ��", Toast.LENGTH_SHORT);
						LogUtil.v("PublishActivity-->handleMessage", "����ʧ��");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				pd.dismiss();
				Toast.makeText(context, "���粻����", Toast.LENGTH_SHORT).show();
				LogUtil.v("PublishActivity-->handleMessage", "���粻����");
				break;
			}

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pub_back:
			onBackPressed();// ���÷��ؼ�
			finish();
			break;
		case R.id.send:
			LogUtil.v("PublishActivity-->onclick", "send");
			send();
			break;
		case R.id.add_pic:
			if (isKeyBoardShow) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
			showSetHeadimg();
			break;
		}
	}

	private void send() {
		pd = ProgressDialog.show(this, null, "���ڷ������ӣ����Ժ�...");
		title = title_et.getText().toString();
		content = content_et.getText().toString();
		// HashMap<String, Object> taskArgs = null;
		if (!title.equals("") && !content.equals("")) {
			SimpleDateFormat time = new SimpleDateFormat("mm:ss");
			publishTime = time.format(new java.util.Date());
			// taskArgs = new HashMap<String, Object>();
			// taskArgs.put("id", MainActivity.user.getUsername());
			// taskArgs.put("area", "ѧϰ��");
			// taskArgs.put("type", "����");
			// taskArgs.put("title", title);
			// taskArgs.put("description", "ժҪ");
			// taskArgs.put("content", content);
			// taskArgs.put("publishTime", publishTime);
			// taskArgs.put("pic", new File(path));
			//
			// publishAsync.post(2, AppConfig.publishUrl, taskArgs);
			// Toast.makeText(getApplicationContext(), "���ڷ���...",
			// Toast.LENGTH_LONG).show();
		}
		new Thread(uploadImageRunnable).start();
		LogUtil.v("PublishActivity", "uploadImageRunnable");
	}

	Runnable uploadImageRunnable = new Runnable() {

		@Override
		public void run() {

			// if (TextUtils.isEmpty(imgUrl)) {
			// Toast.makeText(mContext, "��û�������ϴ���������·����", Toast.LENGTH_SHORT)
			// .show();
			// return;
			// }

			Map<String, String> textParams = new HashMap<String, String>();
			Map<String, File> fileparams = new HashMap<String, File>();

			try {
				// ����һ��URL����
				URL url = new URL(AppConfig.publishUrl);
				textParams = new HashMap<String, String>();
				fileparams = new HashMap<String, File>();
				// Ҫ�ϴ���ͼƬ�ļ�
				if (path != null) {

					File file = new File(path);
					fileparams.put("pic", file);
				}
				textParams.put("id", MainActivity.user.getUsername());
				textParams.put("area", "��ԇ");
				textParams.put("type", "ѧϰ��");
				textParams.put("title", title);
				textParams.put("description", "ժҪ");
				textParams.put("content", content);
				textParams.put("publishTime", publishTime);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				// ���ò�ʹ�û��棨���׳������⣩
				conn.setUseCaches(false);
				// �ڿ�ʼ��HttpURLConnection�����setRequestProperty()����,��������HTML�ļ�ͷ
				conn.setRequestProperty("ser-Agent", "Fiddler");
				// ����contentType
				conn.setRequestProperty("Content-Type",
						"multipart/form-data; boundary=" + NetUtil.BOUNDARY);
				OutputStream os = conn.getOutputStream();
				DataOutputStream ds = new DataOutputStream(os);
				NetUtil.writeStringParams(textParams, ds);
				NetUtil.writeFileParams(fileparams, ds);
				NetUtil.paramsEnd(ds);
				os.close();

				int code = conn.getResponseCode(); // ��Internet��ȡ��ҳ,��������,����ҳ��������ʽ������
				LogUtil.v("PublishActivity--uploadImageRunnable", "��Ӧ��" + code);
				if (code == 200) {// ���ص���Ӧ��200,�ǳɹ�
					// �õ����緵�ص�������
					InputStream is = conn.getInputStream();
					resultStr = NetUtil.readString(is);
					handler.sendEmptyMessage(1);
					LogUtil.v("PublishActivity--uploadImageRunnable", "��Ӧ��200"
							+ resultStr);
				} else {
					// InputStream is = conn.getInputStream();
					// resultStr = NetUtil.readString(is);
					LogUtil.v("PublishActivity--uploadImageRunnable", "����URIʧ��");
					handler.sendEmptyMessage(2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

}
