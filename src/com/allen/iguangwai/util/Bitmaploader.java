//package com.allen.iguangwai.util;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.ref.SoftReference;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.WeakHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import com.allen.iguangwai.AppException;
//import com.allen.iguangwai.clip.ClipZoomImageView;
//
///**
// * 异步线程加载图片工具类
// */
//public class Bitmaploader {
//
//	private static HashMap<String, SoftReference<Bitmap>> cache;// 图片缓存
//	private static ExecutorService pool;
//	// imageViews记录当前ImageView控件对应的图片地址，用来防止异步线程加载图片时候ImageView控件显示的图片与实际图片地址对应的图片不符，出现错乱
//	private static Map<ImageView, String> imageViews;
//	private Bitmap defaultBmp;
//
//	static {
//		cache = new HashMap<String, SoftReference<Bitmap>>();
//		pool = Executors.newFixedThreadPool(5); // 固定线程池
//		imageViews = Collections
//				.synchronizedMap(new WeakHashMap<ImageView, String>());
//	}
//
//	public Bitmaploader(Bitmap def) {
//		this.defaultBmp = def;
//	}
//
//	public void loadBitmap(BaseAdapter adapter, String url,
//			ImageView imageView, int width, int height, boolean isCircle) {
//		Log.v("myLog", "头像地址" + url);
//		if (!url.equals("0")) {
//			imageViews.put(imageView, url);
//			Bitmap bitmap = getBitmapFromCache(url);// 从缓存中获取图片
//			adapter.notifyDataSetChanged();
//			if (bitmap != null) {
//				// 显示缓存图片
//				if (width > 0 && height > 0) {
//					// 指定显示图片的高宽
//					bitmap = Bitmap.createScaledBitmap(bitmap, width, height,
//							true);
//				}
//				if (isCircle) {
//					bitmap = ClipZoomImageView.getCircleBitmap(bitmap);
//				}
//				imageView.setImageBitmap(bitmap);
//				adapter.notifyDataSetChanged();
//				Log.v("myLog", "已从缓存中取出头像" + url);
//			} else {
//				// 加载SD卡中的图片缓存
//				String filename = FileUtils.getFileName(url);
//				String filepath = imageView.getContext().getFilesDir()
//						+ File.separator + filename;
//				File file = new File(filepath);
//				if (file.exists()) {
//					// 显示SD卡中的图片缓存
//					Bitmap bmp = ImageUtils.getBitmap(imageView.getContext(),
//							filename);
//					if (width > 0 && height > 0) {
//						bmp = Bitmap.createScaledBitmap(bmp, width, height,
//								true);
//					}
//					if (isCircle) {
//						bmp = ClipZoomImageView.getCircleBitmap(bmp);
//					}
//					imageView.setImageBitmap(bmp);
//					adapter.notifyDataSetChanged();
//					Log.v("myLog", "已从sd卡中取出头像" + url);
//				} else {
//					// 线程加载网络图片
//					// imageView.setImageBitmap(defaultBmp);
//					queueJob(adapter, url, imageView, width, height, isCircle);
//				}
//			}
//		} else {
//			imageView.setImageBitmap(defaultBmp);
//		}
//	}
//
//	/**
//	 * 加载图片-可指定显示图片的高宽
//	 * 
//	 * @param url
//	 * @param imageView
//	 * @param width
//	 * @param height
//	 */
//	public void loadBitmap(String url, ImageView imageView, int width,
//			int height, boolean isCircle) {
//		Log.v("myLog", "头像地址" + url);
//		if (!url.equals("0")) {
//			imageViews.put(imageView, url);
//			Bitmap bitmap = getBitmapFromCache(url);// 从缓存中获取图片
//
//			if (bitmap != null) {
//				// 显示缓存图片
//				if (width > 0 && height > 0) {
//					// 指定显示图片的高宽
//					bitmap = Bitmap.createScaledBitmap(bitmap, width, height,
//							true);
//				}
//				if (isCircle) {
//					bitmap = ClipZoomImageView.getCircleBitmap(bitmap);
//				}
//				imageView.setImageBitmap(bitmap);
//				Log.v("myLog", "已从缓存中取出头像" + url);
//			} else {
//				// 加载SD卡中的图片缓存
//				String filename = FileUtils.getFileName(url);
//				String filepath = imageView.getContext().getFilesDir()
//						+ File.separator + filename;
//				File file = new File(filepath);
//				if (file.exists()) {
//					// 显示SD卡中的图片缓存
//					Bitmap bmp = ImageUtils.getBitmap(imageView.getContext(),
//							filename);
//					if (width > 0 && height > 0) {
//						bmp = Bitmap.createScaledBitmap(bmp, width, height,
//								true);
//					}
//					if (isCircle) {
//						bmp = ClipZoomImageView.getCircleBitmap(bmp);
//					}
//					imageView.setImageBitmap(bmp);
//					Log.v("myLog", "已从sd卡中取出头像" + url);
//				} else {
//					// 线程加载网络图片
//					// imageView.setImageBitmap(defaultBmp);
//					queueJob(url, imageView, width, height, isCircle);
//				}
//			}
//		} else {
//			imageView.setImageBitmap(defaultBmp);
//		}
//	}
//
//	/**
//	 * 从缓存中获取图片
//	 * 
//	 * @param url
//	 */
//	public Bitmap getBitmapFromCache(String url) {
//		Bitmap bitmap = null;
//		if (cache.containsKey(url)) {
//			bitmap = cache.get(url).get();
//		}
//		return bitmap;
//	}
//
//	/**
//	 * 从网络中加载图片
//	 * 
//	 * @param url
//	 * @param imageView
//	 * @param width
//	 * @param height
//	 */
//	public void queueJob(final String url, final ImageView imageView,
//			final int width, final int height, boolean isCircle) {
//		/* Create handler in UI thread. */
//		final Handler handler = new Handler() {
//			public void handleMessage(Message msg, boolean isCircle) {
//				String tag = imageViews.get(imageView);
//				if (tag != null && tag.equals(url)) {
//					if (msg.obj != null) {
//						Bitmap bmp = (Bitmap) msg.obj;
//						if (isCircle) {
//							bmp = ClipZoomImageView.getCircleBitmap(bmp);
//						}
//						imageView.setImageBitmap(bmp);
//						try {
//							// 向SD卡中写入图片缓存
//							ImageUtils.saveImage(imageView.getContext(),
//									FileUtils.getFileName(url),
//									(Bitmap) msg.obj);
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					} else {
//						Log.v("myLog", "服务器异常");
//					}
//				}
//			}
//		};
//
//		pool.execute(new Runnable() {
//			public void run() {
//				Message message = Message.obtain();
//				message.obj = downloadBitmap(url, width, height);
//				handler.sendMessage(message);
//
//			}
//		});
//	}
//
//	public void queueJob(BaseAdapter adapter, final String url,
//			final ImageView imageView, final int width, final int height,
//			boolean isCircle) {
//		/* Create handler in UI thread. */
//		final Handler handler = new Handler() {
//			public void handleMessage(Message msg, boolean isCircle,
//					BaseAdapter adapter) {
//				String tag = imageViews.get(imageView);
//				if (tag != null && tag.equals(url)) {
//					if (msg.obj != null) {
//						Bitmap bmp = (Bitmap) msg.obj;
//						if (isCircle) {
//							bmp = ClipZoomImageView.getCircleBitmap(bmp);
//						}
//						imageView.setImageBitmap(bmp);
//						adapter.notifyDataSetChanged();
//						try {
//							// 向SD卡中写入图片缓存
//							ImageUtils.saveImage(imageView.getContext(),
//									FileUtils.getFileName(url),
//									(Bitmap) msg.obj);
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					} else {
//						Log.v("myLog", "服务器异常");
//					}
//				}
//			}
//		};
//
//		pool.execute(new Runnable() {
//			public void run() {
//				Message message = Message.obtain();
//				message.obj = downloadBitmap(url, width, height);
//				handler.sendMessage(message);
//
//			}
//		});
//	}
//
//	/**
//	 * 下载图片-可指定显示图片的高宽
//	 * 
//	 * @param url
//	 * @param width
//	 * @param height
//	 */
//	private Bitmap downloadBitmap(String url, int width, int height) {
//		Bitmap bitmap = null;
//		try {
//			// http加载图片
//			if (getNetBitmap(url) != null) {
//				bitmap = getNetBitmap(url);
//				if (width > 0 && height > 0) {
//					// 指定显示图片的高宽
//					bitmap = Bitmap.createScaledBitmap(bitmap, width, height,
//							true);
//				}
//				// 放入缓存
//				cache.put(url, new SoftReference<Bitmap>(bitmap));
//				return bitmap;
//			}
//		} catch (AppException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static Bitmap getNetBitmap(String url) throws AppException {
//
//		HttpURLConnection conn = null;
//		Bitmap bitmap = null;
//		InputStream inputStream = null;
//		URL picurl;
//		try {
//			picurl = new URL(url);
//			conn = (HttpURLConnection) picurl.openConnection();
//			// conn.setDoInput(true);
//			// conn.connect();
//			inputStream = conn.getInputStream();
//			bitmap = BitmapFactory.decodeStream(inputStream);
//			return bitmap;
//
//		} catch (Exception e) {
//
//		} finally {
//
//			conn.disconnect();
//		}
//		return null;
//
//	}
//}