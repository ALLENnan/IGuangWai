package com.allen.iguangwai.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.iguangwai.R;
import com.allen.iguangwai.activity.ContentActivity;
import com.allen.iguangwai.activity.LoginActivity;
import com.allen.iguangwai.activity.MainActivity;
import com.allen.iguangwai.activity.OtherDataActivity;
import com.allen.iguangwai.async.Async;
import com.allen.iguangwai.model.Comment;
import com.allen.iguangwai.model.User;
import com.allen.iguangwai.picasso.CircleTransform;
import com.socks.library.KLog;
import com.squareup.picasso.Picasso;

public class CommentListviewAdapter extends BaseAdapter implements
		OnClickListener {
	private Context context;
	LayoutInflater myInflater;
	AllViewHolder holder;

	// Bitmaploader bitmapTools;

	// ArrayList<Article> articleList;

	public CommentListviewAdapter(Context context) {
		this.context = context;
		// this.articleList=articleList;
		myInflater = LayoutInflater.from(context);
		// this.bitmapTools = new Bitmaploader(BitmapFactory.decodeResource(
		// context.getResources(), R.drawable.head));

	}

	@Override
	public int getCount() {
		return ContentActivity.commentList.size();
	}

	@Override
	public Object getItem(int position) {
		return ContentActivity.commentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Comment comment = ContentActivity.commentList.get(position);
		if (convertView == null) {
			convertView = myInflater.inflate(R.layout.comment_list_item, null);
			holder = new AllViewHolder();
			holder.commentHead = (ImageView) convertView
					.findViewById(R.id.commentHead);
			holder.nickname = (TextView) convertView
					.findViewById(R.id.nickname);
			holder.comment = (TextView) convertView.findViewById(R.id.comment);
			convertView.setTag(holder);

		} else {

			holder = (AllViewHolder) convertView.getTag();
		}
		// holder.title_tv.setText(mSchool.getSchoolId());
		// holder.desc_tv.setText("" + mSchool.getSchoolName());

		holder.nickname.setText(comment.getCommentnickname());
		holder.comment.setText(comment.getComment());
		String imgURL = comment.getHead();

		// bitmapTools
		// .loadBitmap(this, imgURL, holder.commentHead, 100, 100, true);
		Picasso.with(context).load(imgURL).placeholder(R.drawable.head)
				.error(R.drawable.head)

				.transform(new CircleTransform()).into(holder.commentHead);
		holder.commentHead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String username = comment.getCommentusername();
				Intent intent = new Intent();

				intent.putExtra("username", username);
				intent.setClass(context, OtherDataActivity.class);
				context.startActivity(intent);
				// Async loginAsync = new Async();

			}
		});
		return convertView;
	}

	class AllViewHolder {
		ImageView commentHead;
		TextView nickname;
		TextView comment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commentHead:

			break;

		default:
			break;
		}

	}
}
