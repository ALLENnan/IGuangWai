package com.allen.iguangwai.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.iguangwai.R;


public class MyMsyItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<String> datas;
	private ArrayList<String> datas1;
	private ArrayList<Object> datas2;

	public MyMsyItemAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public void setData(ArrayList<String> datas, ArrayList<String> datas1,
			ArrayList<Object> datas2) {
		this.datas = datas;
		this.datas1 = datas1;
		this.datas2 = datas2;

	}

	public int getCount() {
		return datas.size();
	}

	public Object getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.mymsylistview_item, null);
			holder.textView = (TextView) convertView.findViewById(R.id.tv_item);
			holder.textView1 = (TextView) convertView
					.findViewById(R.id.tv1_item);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.iv_item);
			holder.coating = (TextView) convertView
					.findViewById(R.id.tv_coating);
			holder.functions = (TextView) convertView
					.findViewById(R.id.tv_functions);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView.setText(datas.get(position));
		holder.textView1.setText(datas1.get(position));
		holder.imageView.setBackgroundResource((Integer) datas2.get(position));
		holder.coating.setVisibility(View.VISIBLE);

		holder.functions.setClickable(false);

		return convertView;
	}

	public final class ViewHolder {
		public TextView textView;
		public TextView textView1;
		public TextView coating;
		public TextView functions;
		public ImageView imageView;

	}

}
