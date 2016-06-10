package com.allen.iguangwai.async;

import com.allen.iguangwai.async.Async.OnQuantaAsyncListener;

import android.os.Handler;
import android.os.Message;

/**
 * 具体的操作处理，以及对界面的回调
 * @author wangjiewen
 *
 */
public class mHandler extends Handler{

	OnQuantaAsyncListener quantaAsyncListener = null;
	
	public mHandler(){
		
	}
	
	public void setQuantaAsyncListener(OnQuantaAsyncListener quantaAsyncListener) {
		this.quantaAsyncListener = quantaAsyncListener;
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		int taskId;
		String result;
		switch (msg.what) {
		case TaskThread.TASK_COMPLETE:
			taskId = msg.getData().getInt("taskId");
			result = msg.getData().getString("data");
			if (result != null && !result.equals("")) {
				this.quantaAsyncListener.onComplete(taskId, AppUtil.getMessage(result));
			}else{
				this.quantaAsyncListener.onComplete(taskId);
			}
			break;
			
		case TaskThread.NETWORK_ERROR:
			taskId = msg.getData().getInt("taskId");
			result = msg.getData().getString("data");
			this.quantaAsyncListener.onNetWorkError(taskId, result);
			break;

		default:
			break;
		}
	}
}
