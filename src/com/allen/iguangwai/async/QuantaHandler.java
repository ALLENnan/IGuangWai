package com.allen.iguangwai.async;

import com.allen.iguangwai.async.QuantaAsync.OnQuantaAsyncListener;

import android.os.Handler;
import android.os.Message;

/**
 * 具体的操作处理，以及对界面的回调
 * @author wangjiewen
 *
 */
public class QuantaHandler extends Handler{

	OnQuantaAsyncListener quantaAsyncListener = null;
	
	public QuantaHandler(){
		
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
		case QuantaTaskThread.TASK_COMPLETE:
			taskId = msg.getData().getInt("taskId");
			result = msg.getData().getString("data");
			if (result != null && !result.equals("")) {
				this.quantaAsyncListener.onComplete(taskId, QuantaAppUtil.getMessage(result));
			}else{
				this.quantaAsyncListener.onComplete(taskId);
			}
			break;
			
		case QuantaTaskThread.NETWORK_ERROR:
			taskId = msg.getData().getInt("taskId");
			result = msg.getData().getString("data");
			this.quantaAsyncListener.onNetWorkError(taskId, result);
			break;

		default:
			break;
		}
	}
}
