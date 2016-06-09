package com.allen.iguangwai.activity;

import java.io.File;

import com.allen.iguangwai.clip.ClipImageLayout;
import com.allen.iguangwai.fragment.MenuFragment;
import com.allen.iguangwai.util.FileUtils;
import com.allen.iguangwai.util.ImageUtils;
import com.allen.iguangwai.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/*
 * ºÙ«–ÕºœÒActivity
 */
public class ClipActivity extends Activity {
	private ClipImageLayout mClipImageLayout;
	private String path;
	private ProgressDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// »•µÙ±ÍÃ‚¿∏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clipimage);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle("«Î…‘∫Û...");
		path = getIntent().getStringExtra("path");
		if (TextUtils.isEmpty(path) || !(new File(path).exists())) {
			Toast.makeText(this, "Õº∆¨º”‘ÿ ß∞‹", Toast.LENGTH_SHORT).show();
			return;
		}
		Bitmap bitmap = ImageUtils.convertToBitmap(path, 600, 600);
		if (bitmap == null) {
			Toast.makeText(this, "Õº∆¨º”‘ÿ ß∞‹", Toast.LENGTH_SHORT).show();
			return;
		}
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
		mClipImageLayout.setBitmap(bitmap);
		((Button) findViewById(R.id.id_action_clip))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						loadingDialog.show();
						new Thread(new Runnable() {
							@Override
							public void run() {
								Bitmap bitmap = mClipImageLayout.clip();
								String path = Environment
										.getExternalStorageDirectory()
										+ "/ClipHeadPhoto/cache/"
										+ System.currentTimeMillis() + ".png";
								// œÚSDø®÷––¥»ÎÕº∆¨ª∫¥Ê
								ImageUtils.savePhotoToSDCard(bitmap, path);
								
								loadingDialog.dismiss();
								Intent intent = new Intent();
								intent.putExtra("path", path);
								setResult(RESULT_OK, intent);
								finish();
							}
						}).start();
					}
				});
	}
}
