package com.allen.iguangwai.async;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.util.Log;


public class QuantaAppUtil {

	/**
	 * md5 加密
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		MessageDigest algorithm = null;
		try {
			algorithm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (algorithm != null) {
			algorithm.reset();
			algorithm.update(str.getBytes());
			byte[] bytes = algorithm.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte b : bytes) {
				hexString.append(Integer.toHexString(0xFF & b));
			}
			return hexString.toString();
		}
		return "";

	}

	/**
	 * 首字母大�?
	 * @param str
	 * @return
	 */
	public static String ucfirst(String str) {
		if (str != null && str != "") {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		return str;
	}

	/**
	 * �? EntityUtils.toString() 添加 gzip 解压功能
	 * @param entity
	 * @param defaultCharset
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String gzipToString(final HttpEntity entity,
			final String defaultCharset) throws IOException, ParseException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		InputStream instream = entity.getContent();
		if (instream == null) {
			return "";
		}
		// gzip logic start
		if (entity.getContentEncoding().getValue().contains("gzip")) {
			instream = new GZIPInputStream(instream);
		}
		// gzip logic end
		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"HTTP entity too large to be buffered in memory");
		}
		int i = (int) entity.getContentLength();
		if (i < 0) {
			i = 4096;
		}
		String charset = EntityUtils.getContentCharSet(entity);
		if (charset == null) {
			charset = defaultCharset;
		}
		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}
		Reader reader = new InputStreamReader(instream, charset);
		CharArrayBuffer buffer = new CharArrayBuffer(i);
		try {
			char[] tmp = new char[1024];
			int l;
			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}

	/**
	 * �? EntityUtils.toString() 添加 gzip 解压功能
	 * @param entity
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String gzipToString(final HttpEntity entity)
			throws IOException, ParseException {
		return gzipToString(entity, null);
	}

	public static SharedPreferences getSharedPreferences(Context ctx) {
		return ctx.getSharedPreferences(QuantaConfig.SHARE_PREFERENCE_NAME,	Context.MODE_PRIVATE);
	}

	public static SharedPreferences getSharedPreferences(Service service) {
		return service.getSharedPreferences(QuantaConfig.SHARE_PREFERENCE_NAME,	Context.MODE_PRIVATE);
	}


	/**
	 * 将http json 格式转为QuantaMessage形式
	 * @param jsonStr
	 * @return
	 */
	public static QuantaBaseMessage getMessage(String jsonStr) {
		QuantaBaseMessage message = new QuantaBaseMessage();
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonStr);
			if (jsonObject != null) {
				message.setData(jsonObject.getString("data"));
				message.setInfo(jsonObject.getString("info"));
				message.setStatus(jsonObject.getString("status"));
			}
		} catch (JSONException e) {
			message = null;
			// throw new Exception("Json format error");
			Log.e("QuantaAppUtil-->getMessage", "Json format error"+"---:"+jsonStr);
			e.printStackTrace();
		} catch (Exception e) {
			message = null;
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * Model 数组转化�? Map 列表
	 * @param data
	 * @param fields
	 * @return
	 */
	public static List<? extends Map<String, ?>> dataToList(
			List<? extends QuantaBaseModel> data, String[] fields) {
		ArrayList<HashMap<String, ?>> list = new ArrayList<HashMap<String, ?>>();
		for (QuantaBaseModel item : data) {
			list.add((HashMap<String, ?>) dataToMap(item, fields));
		}
		return list;
	}

	/**
	 * Model 转化�? Map
	 * @param data
	 * @param fields
	 * @return
	 */
	public static Map<String, ?> dataToMap(QuantaBaseModel data, String[] fields) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			for (String fieldName : fields) {
				Field field = data.getClass().getDeclaredField(fieldName);
				field.setAccessible(true); // have private to be accessable
				map.put(fieldName, field.get(data));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 把从数据库查询的数据转为model的形�?
	 * 
	 * @param className 模型的类名，不需要再加包�?
	 * @param mapList
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<? extends QuantaBaseModel> hashMapToModel(String className, ArrayList<HashMap<String, String>> mapList)	throws Exception {

		String modelClassName = QuantaConfig.MODEL_PACKAGE + "." + className;
		ArrayList<QuantaBaseModel> modelList = new ArrayList<QuantaBaseModel>();

		for (HashMap<String, String> temp : mapList) {
			QuantaBaseModel modelObj = (QuantaBaseModel) Class.forName(modelClassName)
					.newInstance();
			Class<? extends QuantaBaseModel> modelClass = modelObj.getClass();
			Iterator<String> it = temp.keySet().iterator();
			while (it.hasNext()) {
				String varField = it.next();
				String varValue = temp.get(varField);
				Field field = modelClass.getDeclaredField(varField);
				field.setAccessible(true); // have private to be accessable
				field.set(modelObj, varValue);
			}
			modelList.add(modelObj);
		}
		return modelList;
	}

	/**
	 * 把jsonObject转为hashmap的格�?
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static HashMap<String, String> jsonObject2HashMap(
			JSONObject jsonObject) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		Iterator<String> iterator = jsonObject.keys();

		try {
			while (iterator.hasNext()) {
				String key = iterator.next();
				String value = jsonObject.getString(key);
				hashMap.put(key, value);

			}
		} catch (Exception e) {
			Log.w("apputil", "jsonobject2hasmap");
		}
		return hashMap;
	}

	/**
	 * 把url返回的json格式转换为arrayList<Hash<key,map>>形式
	 * 
	 * @param jsonArray
	 * @return arrayList
	 */
	@SuppressWarnings("null")
	public static ArrayList<HashMap<String, String>> jsonArray2ArrayList(
			JSONArray jsonArray) {
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
		JSONObject jsonObject = null;
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				jsonObject = jsonArray.getJSONObject(i);
				@SuppressWarnings("unchecked")
				Iterator<String> iterator = jsonObject.keys();
				while (iterator.hasNext()) {
					String key = iterator.next();
					String value = jsonObject.getString(key);
					hashMap.put(key, value);
				}
				arrayList.add(hashMap);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("json", "json转换为arrayList时�?�错�?");
		} catch (Exception e) {
			Log.e("json", "错误");
		}
		return arrayList;
	}

	/**
	 * 判断int是否为空
	 * @param v
	 * @return
	 */
	public static boolean isEmptyInt(int v) {
		Integer t = new Integer(v);
		return t == null ? true : false;
	}

	/**
	 * 获取毫秒�?
	 * @return
	 */
	public static long getTimeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取耗费内存
	 * @return
	 */
	public static long getUsedMemory() {
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		return total - free;
	}
	
	/**
	 * 复制对象
     * Copy obj to desc.
     * 
     */
    public static void copyProperties(Object desc, Object obj){
        Class<?> descClass = desc.getClass();
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                String getMethodName = "get"+toFirstLetterUpperCase(name);
                String setMethodName = "set"+toFirstLetterUpperCase(name);
                try{
                	Object value = objClass.getMethod(getMethodName).invoke(obj);
                	descClass.getMethod(setMethodName, value.getClass()).invoke(desc, value);
                }catch(Exception e){
                }
            }
        } catch (Exception e) {
            
        }
    }
    
    public static String toFirstLetterUpperCase(String str) {
    	if(str == null || str.length() < 2){
    		return str;
    	}
        String firstLetter = str.substring(0, 1).toUpperCase();
        return firstLetter + str.substring(1, str.length());
    }
    
    
    public static String getNowDate() {
		StringBuffer sbBuffer = new StringBuffer();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sbBuffer.append(format.format(Calendar.getInstance().getTime()));
		return sbBuffer.toString();
	}

    /**
     * 获得文件的后�?�?
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName){
		String[] arr = fileName.split("\\.");
		if (arr.length <= 1) {
			return "";
		}else{
			return arr[arr.length-1].toLowerCase();
		}
	}
    
    /**
     * 生成cover的名�?
     * @param url
     * @return
     */
    public static String getCoverName(String url){
    	String cacheKey = QuantaAppUtil.md5(url);
		String fileName = cacheKey + "." + QuantaAppUtil.getFileExt(url);
		return fileName;
    }
    
}