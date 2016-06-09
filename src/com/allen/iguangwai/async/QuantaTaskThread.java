package com.allen.iguangwai.async;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

/**
 * 具体任务的异步线程，进行http请求
 * @author wangjiewen
 *
 */
public class QuantaTaskThread implements Runnable{
	
	// 任务完成状�??
	public static final int TASK_COMPLETE = 1000;
	public static final int NETWORK_ERROR = 1001;
	
	private Context context = null;
	private QuantaHandler quantaHandler = null;
	private int taskId = 0;
	private String taskUrl;
	private HashMap<String, String> taskArgs;
	private int delayTime = 0;

	/**
	 * 
	 * @param context
	 * @param quantaHandler
	 * @param taskId
	 * @param taskUrl
	 * @param taskArgs
	 * @param delayTime
	 */
	public QuantaTaskThread(Context context, QuantaHandler quantaHandler, int taskId, String taskUrl, HashMap<String, String> taskArgs, int delayTime) {
		this.context = context;
		this.quantaHandler = quantaHandler;
		this.taskId = taskId;
		this.taskUrl = taskUrl;
		this.taskArgs = taskArgs;
		this.delayTime = delayTime;
	}

	@Override
	public void run() {
		try {
			String httpResult = null;
			// set delay time
			if (this.delayTime > 0){
				Thread.sleep(this.delayTime);
			}
			try {
				QuantaHttpClient quantaHttpClient = new QuantaHttpClient(this.context, this.taskUrl);
				if (this.taskArgs == null) {
					httpResult = quantaHttpClient.get();
				}else{
					httpResult = quantaHttpClient.post(this.taskArgs);
				}
				sendMessage(TASK_COMPLETE, taskId, httpResult);
			} catch (Exception e) {
				e.printStackTrace();
				sendMessage(NETWORK_ERROR, taskId, e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage(NETWORK_ERROR, taskId, e.getMessage());
		}
	}
	
	/**
	 * 发�?�消息给handler处理
	 * @param what 任务完成与否
	 * @param taskId 当前任务的id
	 * @param data 任务完成之后的数�?
	 */
	private void sendMessage(int what, int taskId, String data){
		Bundle bundle = new Bundle();
		bundle.putString("data", data);
		bundle.putInt("taskId", taskId);
		Message msg = new Message();
		msg.what = what;
		msg.setData(bundle);
		quantaHandler.sendMessage(msg);
	}
	
	
	
	
}
	
	
