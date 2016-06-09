package com.allen.iguangwai.adapter;

import com.allen.iguangwai.R;
import com.allen.iguangwai.activity.MainActivity;
import com.allen.iguangwai.model.Article;
import com.allen.iguangwai.util.Bitmaploader;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * 主界面的listview adapter
 */
public class MainListViewAdapter extends BaseAdapter {
	private Context context;
	LayoutInflater myInflater;
	AllViewHolder holder;
	Bitmaploader bitmapTools;

	public MainListViewAdapter(Context context) {
		this.context = context;
		myInflater = LayoutInflater.from(context);
		this.bitmapTools = new Bitmaploader(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.default_image));
	}

	@Override
	public int getCount() {
		return MainActivity.articleList.size();
	}

	@Override
	public Object getItem(int position) {
		return MainActivity.articleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 取出model类数据
		Log.v("myLog", "getView");
		// School mSchool = MainActivity.schoolList.get(position);
		Article mArticle = MainActivity.articleList.get(position);
		if (convertView == null) {
			convertView = myInflater.inflate(R.layout.list_item, null);
			holder = new AllViewHolder();
			Log.v("myLog", "getView2");
			holder.item_iv = (ImageView) convertView.findViewById(R.id.item_iv);
			holder.title_tv = (TextView) convertView
					.findViewById(R.id.title_tv);
			holder.desc_tv = (TextView) convertView.findViewById(R.id.desc_tv);
			convertView.setTag(holder);

		} else {

			holder = (AllViewHolder) convertView.getTag();
		}
		// holder.title_tv.setText(mSchool.getSchoolId());
		// holder.desc_tv.setText("" + mSchool.getSchoolName());

		holder.title_tv.setText(mArticle.getTitle());
		holder.desc_tv.setText(mArticle.getDescription());
		String imgURL = mArticle.getFirstPicUrl();

		bitmapTools.loadBitmap(this, imgURL, holder.item_iv, 180, 180, false);
		this.notifyDataSetChanged();
		return convertView;
	}

	class AllViewHolder {
		ImageView item_iv;
		TextView title_tv;
		TextView desc_tv;
	}

}
