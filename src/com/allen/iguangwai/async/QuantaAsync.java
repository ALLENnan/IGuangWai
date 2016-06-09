package com.allen.iguangwai.async;


import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

/**
 * 异步任务类，由该类管理所有的异步任务，并进行发放
 * @author wangjiewen
 *
 */
public class QuantaAsync{

	private static ExecutorService taskPool = Executors.newCachedThreadPool();
	private QuantaHandler quantaHandler = null;
	private Context context;
	
	public QuantaAsync(Context context){
		this.context = context;
		this.quantaHandler = new QuantaHandler();
	}
	
	/**
	 * 外部接口
	 * @param quantaAsyncListener
	 */
	public void setQuantaAsyncListener(OnQuantaAsyncListener quantaAsyncListener) {
		this.quantaHandler.setQuantaAsyncListener(quantaAsyncListener);
	}
	
	/**
	 * execute http post task
	 * @param taskId 标示任务的id, 在回调的时�?�可以判断是哪个任务完成�?
	 * @param taskUrl
	 * @param taskArgs post的参数，键�?�对的形�?
	 */
	public void post(int taskId, String taskUrl, HashMap<String, String> taskArgs){
		try{
			taskPool.execute(new QuantaTaskThread(context, quantaHandler, taskId, taskUrl, taskArgs, 0));
		}catch (Exception e){
			taskPool.shutdown();
		}
		
	}
	
	/**
	 * execute http get task
	 * @param taskId 标示任务的id, 在回调的时�?�可以判断是哪个任务完成�?
	 * @param taskUrl
	 */
	public void get(int taskId, String taskUrl){
		try{
			taskPool.execute(new QuantaTaskThread(context, quantaHandler, taskId, taskUrl, null, 0));
		}catch (Exception e){
			taskPool.shutdown();
		}
	}
	
	/**
	 * 设置回调的接�?
	 * @author wangjiewen
	 *
	 */
	public interface OnQuantaAsyncListener{
		
		/**
		 * 网络任务成功完成，并且返回了数据
		 * @param taskId
		 * @param baseMessage
		 */
		public void onComplete(int taskId, QuantaBaseMessage baseMessage);
		
		/**
		 * 网络任务成功完成，但不需要返回数�?
		 * @param taskId
		 */
		public void onComplete(int taskId);
		
		/**
		 * 网络错误
		 * @param taskId
		 * @param errorMsg
		 */
		public void onNetWorkError(int taskId, String errorMsg);
	}
}
