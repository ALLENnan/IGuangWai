package com.allen.iguangwai.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * 图片操作工具包
 */
public class ImageUtils {

	/**
	 * 写图片文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录
	 */
	public static void saveImage(Context context, String fileName, Bitmap bitmap)
			throws IOException {
		saveImage(context, fileName, bitmap, 100);
	}

	public static void saveImage(Context context, String fileName,
			Bitmap bitmap, int quality) throws IOException {
		if (bitmap == null || fileName == null || context == null)
			return;

		FileOutputStream fos = context.openFileOutput(fileName,
				Context.MODE_PRIVATE);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, quality, stream);
		byte[] bytes = stream.toByteArray();
		fos.write(bytes);
		fos.close();
	}

	/**
	 * 获取bitmap
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(Context context, String fileName) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = context.openFileInput(fileName);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 保存图片到sd卡
	 * 
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static void savePhotoToSDCard(Bitmap photoBitmap, String path,
			String photoName) {
		if (checkSDCardAvailable()) {
			File photoFile = new File(path, photoName);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (Exception e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void savePhotoToSDCard(Bitmap photoBitmap, String path) {
		if (checkSDCardAvailable()) {
			File photoFile = new File(path);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (Exception e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 检查sd卡
	 * 
	 * @return
	 */
	public static boolean checkSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 根据路径加载bitmap
	 * 
	 * @param path
	 * @param w
	 * @param h
	 */
	public static final Bitmap convertToBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 设置为ture只获取图片大小
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
			// 返回为空
			BitmapFactory.decodeFile(path, opts);
			int width = opts.outWidth;
			int height = opts.outHeight;
			float scaleWidth = 0.f, scaleHeight = 0.f;
			if (width > w || height > h) {
				// 缩放
				scaleWidth = ((float) width) / w;
				scaleHeight = ((float) height) / h;
			}
			opts.inJustDecodeBounds = false;
			float scale = Math.max(scaleWidth, scaleHeight);
			opts.inSampleSize = (int) scale;
			WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
					BitmapFactory.decodeFile(path, opts));
			Bitmap bMapRotate = Bitmap.createBitmap(weak.get(), 0, 0, weak
					.get().getWidth(), weak.get().getHeight(), null, true);
			if (bMapRotate != null) {
				return bMapRotate;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		Log.e("compressImage", baos.toByteArray().length / 1024 + "  kb");
		while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		Log.e("compressImage", baos.toByteArray().length / 1024 + "  kb");
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm);// 把ByteArrayInputStream数据生成图片
		try {
			File f = new File(Environment.getExternalStorageDirectory(),
					"edtimg.jpg");
			f.createNewFile();

			FileOutputStream fOut = new FileOutputStream(f);
			// Log.e("-----", bitmap.getHeight() + "_____" + bitmap.getWidth()
			// + "________" + bitmap.getByteCount()+ "________" +
			// bitmap.getRowBytes());
			// bitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
			// Log.e("======", bitmap.getHeight() + "_____" + bitmap.getWidth()
			// + "________" + bitmap.getByteCount()+ "________" +
			// bitmap.getRowBytes());
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e1) {

			e1.printStackTrace();
		}

		return bitmap;
	}

	public static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		Log.e("comp", baos.toByteArray().length / 1024 + "  kb");
		if (baos.toByteArray().length / 1024 > 200) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		Log.e("comp2", baos.toByteArray().length / 1024 + "  kb");
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// DisplayMetrics metrics = new DisplayMetrics();
		// activity.getWindowManager().getDefaultDisplay()
		// .getMetrics(metrics);
		// float hh = metrics.heightPixels;//这里设置高度为800f
		// float ww = metrics.heightPixels;//这里设置宽度为480f
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

}
