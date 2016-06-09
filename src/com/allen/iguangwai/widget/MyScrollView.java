package com.allen.iguangwai.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
public class MyScrollView extends ScrollView {
	
	public MyScrollView(Context context) {
		super(context);
	}
	
    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

	private OnScrollListener onScrollListener;
	private int lastScrollY;
	
	
	/**
	 * 设置滚动接口
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int scrollY = MyScrollView.this.getScrollY();
			if(lastScrollY != scrollY){
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			}
			if(onScrollListener != null){
				onScrollListener.onScroll(scrollY);
			}
			
		};

	}; 

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_MOVE:
	         handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			break;
		}
		return super.onTouchEvent(ev);
	}


	/**
	 * 滚动的回调接口
	 */
	public interface OnScrollListener{
		public void onScroll(int scrollY);
	}
	
	

}
