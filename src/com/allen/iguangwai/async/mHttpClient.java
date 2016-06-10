package com.allen.iguangwai.async;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class mHttpClient {

	// compress strategy
	final private static int CS_NONE = 0;
	final private static int CS_GZIP = 1;

	// logic variables
	private String apiUrl;
	private HttpParams httpParams;
	private DefaultHttpClient httpClient;
	private int timeoutConnection = 20000;
	private int timeoutSocket = 20000;
	private int compress = CS_NONE;// 设置zip与否
	private Context context = null;

	private static CookieStore cookieStore = null;

	// charset default utf8
	private String charset = HTTP.UTF_8;

	public mHttpClient(Context context, String url) {
		this.context = context;
		initClient(url);
	}

	public mHttpClient(String url, String charset, int compress) {
		initClient(url);
		this.charset = charset; // set charset
		this.compress = compress; // set strategy
	}

	private void initClient(String url) {
		// initialize API URL
		this.apiUrl = url;

		// set client timeout
		httpParams = new BasicHttpParams();
		HttpConnectionParams
				.setConnectionTimeout(httpParams, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeoutSocket);
		// init client
		httpClient = new DefaultHttpClient(httpParams);
	}

	/**
	 * 使用代理
	 */
	public void useWap() {// 此处是�?�择wap的上网方�?
		HttpHost proxy = new HttpHost("10.0.0.172", 80, "http");
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				proxy);
	}

	/**
	 * http get 方法
	 * 
	 * @return
	 * @throws Exception
	 */
	public String get() throws Exception {
		try {
			HttpGet httpGet = headerFilter(new HttpGet(this.apiUrl));
			debugMemory(this.apiUrl);

			// 添加cookie
			addCookies();
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String httpResult = resultFilter(httpResponse.getEntity());
				// 保存cookie
				saveCookies();
				debugMemory(httpResult);
				return httpResult;
			} else {
				return null;
			}
		} catch (ConnectTimeoutException e) {
			throw new Exception("网络超时");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("网络错误");
		}
	}

	/**
	 * http post 方法
	 * 
	 * @param urlParams
	 * @return
	 * @throws Exception
	 */
	public String post(HashMap<String, String> urlParams) throws Exception {
		try {
			HttpPost httpPost = headerFilter(new HttpPost(this.apiUrl));
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			// get post parameters
			Iterator<String> it = urlParams.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				String value = urlParams.get(key);
				postParams.add(new BasicNameValuePair(key, value));
			}
			// set data charset
			if (this.charset != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(postParams,
						this.charset));
			} else {
				httpPost.setEntity(new UrlEncodedFormEntity(postParams));
			}

			debugMemory(this.apiUrl);
			debugMemory(postParams.toString());

			// 添加cookie
			addCookies();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String httpResult = resultFilter(httpResponse.getEntity());

				// 保存cookie
				saveCookies();
				return httpResult;
			} else {
				return null;
			}
		} catch (ConnectTimeoutException e) {
			throw new Exception("网络超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构�?�Get头部
	 * 
	 * @param httpGet
	 * @return
	 */
	private HttpGet headerFilter(HttpGet httpGet) {
		switch (this.compress) {
		case CS_GZIP:
			httpGet.addHeader("Accept-Encoding", "gzip");
			break;
		default:
			break;
		}
		return httpGet;
	}

	/**
	 * 构�?�Post头部
	 * 
	 * @param httpPost
	 * @return
	 */
	private HttpPost headerFilter(HttpPost httpPost) {
		switch (this.compress) {
		case CS_GZIP:
			httpPost.addHeader("Accept-Encoding", "gzip");
			break;
		default:
			break;
		}
		return httpPost;
	}

	// 把response的HttpEntity处理成string格式
	private String resultFilter(HttpEntity entity) {
		String result = null;
		try {
			switch (this.compress) {
			case CS_GZIP:
				result = AppUtil.gzipToString(entity);
				break;
			default:
				result = EntityUtils.toString(entity);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将返回的cookie保存起来
	 * 
	 * @param resp
	 */
	private void saveCookies() {
		cookieStore = httpClient.getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
		try {
			JSONArray jsonCookieArr = new JSONArray();
			for (Cookie cookie : cookies) {
				JSONObject obj = new JSONObject("{}");
				obj.put("name", cookie.getName());
				obj.put("value", cookie.getValue());
				obj.put("path", cookie.getPath());
				obj.put("domain", cookie.getDomain());
				obj.put("expiryDate", cookie.getExpiryDate() == null ? ""
						: cookie.getExpiryDate());
				jsonCookieArr.put(obj);

			}

			// 持久化cookie
			String cookieStr = jsonCookieArr.toString();
			SharedPreferences sp = AppUtil
					.getSharedPreferences(this.context);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(Config.PERSISTENT_COOKIE, cookieStr);
			editor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给http头部增加Cookie
	 * 
	 * @param request
	 */
	private void addCookies() {
		if (cookieStore != null) {
			// 清除过期的cookie
			cookieStore.clearExpired(new Date());
			httpClient.setCookieStore(cookieStore);
		} else {
			SharedPreferences sp = AppUtil
					.getSharedPreferences(this.context);
			String cookieStr = sp.getString(Config.PERSISTENT_COOKIE,
					null);
			if (cookieStr == null) {
				return;
			}
			cookieStore = this.httpClient.getCookieStore();
			try {
				JSONArray jsonArray = new JSONArray(cookieStr);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					String name = obj.getString("name");
					String value = obj.getString("value");
					String path = obj.getString("path");
					String domain = obj.getString("domain");
					String expiryDate = obj.getString("expiryDate");

					BasicClientCookie cookie = new BasicClientCookie(name,
							value);
					cookie.setDomain(domain);
					cookie.setPath(path);
					if (expiryDate != null && !expiryDate.equals("")) {
						SimpleDateFormat format = new SimpleDateFormat(
								"EEE MMM dd HH:mm:ss 'GMT' yyyy", Locale.US);
						Date date = format.parse(expiryDate);
						cookie.setExpiryDate(date);
					}
					cookieStore.addCookie(cookie);
				}

				cookieStore.clearExpired(new Date());
				httpClient.setCookieStore(cookieStore);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void debugMemory(String tag) {
		try {
			String method = Thread.currentThread().getStackTrace()[3]
					.getMethodName();
			Log.w(this.getClass().getSimpleName() + ":" + method, tag + ":"
					+ AppUtil.getUsedMemory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}