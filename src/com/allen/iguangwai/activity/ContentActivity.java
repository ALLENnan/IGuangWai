package com.allen.iguangwai.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.iguangwai.R;
import com.allen.iguangwai.adapter.CommentListviewAdapter;
import com.allen.iguangwai.AppConfig;
import com.allen.iguangwai.async.Async;
import com.allen.iguangwai.listener.AsyncCommentListener;
import com.allen.iguangwai.listener.AsyncSendCommentListener;
import com.allen.iguangwai.model.Article;
import com.allen.iguangwai.model.Comment;
import com.allen.iguangwai.picasso.CircleTransform;
import com.allen.iguangwai.util.BaseTools;
import com.squareup.picasso.Picasso;

/*
 * 帖子正文Activity
 */
public class ContentActivity extends Activity implements OnClickListener {
	private TextView back;
	private ImageView share, image_content;
	private TextView content, title, publishTime, author, tv_newcomment;
	private ListView commentListView;
	private Button bt_getComment, bt_send;// 加载评论button、发送button
	private ScrollView scrollview_content;
	private EditText et_comment;// 评论编辑框
	Async CommentAsync, sendCommentAsync;
	Article article;
	public static ArrayList<Comment> commentList = new ArrayList<Comment>();// 评论列表
	public static CommentListviewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		article = (Article) getIntent().getSerializableExtra("article");
		initView();
		showView();
	}

	private void initView() {
		scrollview_content = (ScrollView) this
				.findViewById(R.id.scrollview_content);
		scrollview_content.smoothScrollTo(0, 0);// 把ScrollView滚动至最顶端
		back = (TextView) this.findViewById(R.id.con_back);
		title = (TextView) this.findViewById(R.id.title);
		author = (TextView) this.findViewById(R.id.author);
		publishTime = (TextView) this.findViewById(R.id.publishtime);
		image_content = (ImageView) this.findViewById(R.id.image_content);
		content = (TextView) this.findViewById(R.id.content);
		tv_newcomment = (TextView) this.findViewById(R.id.tv_newcomment);
		share = (ImageView) this.findViewById(R.id.share);
		commentListView = (ListView) this.findViewById(R.id.listview_comment);
		bt_getComment = (Button) this.findViewById(R.id.bt_getComment);
		et_comment = (EditText) this.findViewById(R.id.et_comment);
		bt_send = (Button) this.findViewById(R.id.bt_send);

		/*
		 * 给listview设置adapter
		 */
		adapter = new CommentListviewAdapter(this);
		commentListView.setAdapter(adapter);
		/*
		 * 取评论异步框架
		 */
		CommentAsync = new Async(ContentActivity.this);
		CommentAsync.setQuantaAsyncListener(new AsyncCommentListener(this));
		/*
		 * 发表评论异步框架
		 */
		sendCommentAsync = new Async(ContentActivity.this);
		sendCommentAsync.setQuantaAsyncListener(new AsyncSendCommentListener(
				this, article, CommentAsync, adapter));

		back.setOnClickListener(this);
		share.setOnClickListener(this);
		bt_getComment.setOnClickListener(this);
		bt_send.setOnClickListener(this);

	}

	private void showView() {
		title.setText(article.getTitle());
		author.setText(article.getAuthor());
		publishTime.setText(article.getPublishTime());
		content.setText(article.getContent());

		int width = BaseTools.getWindowsWidth(this);

		// Bitmaploader bitmapTools = new Bitmaploader(
		// BitmapFactory.decodeResource(this.getResources(),
		// R.drawable.default_image));
		// bitmapTools.loadBitmap(article.getFirstPicUrl(), image_content,
		// width - 40, width - 40, false);// 异步加载头像

		Picasso.with(this).load(article.getFirstPicUrl())
				.placeholder(R.drawable.default_image).error(R.drawable.default_image)
				.into(image_content);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.con_back:
			onBackPressed();// 调用返回键
			finish();
			break;
		/** 分享 */
		case R.id.share:
			shareContent(article.getTitle() + '\n' + article.getPublishTime()
					+ '\n' + article.getContent());
			break;
		/** 加载评论 */
		case R.id.bt_getComment:
			Toast.makeText(this, "正在加载...", Toast.LENGTH_SHORT).show();
			HashMap<String, String> taskArgs = new HashMap<String, String>();
			taskArgs.put("Comment", article.getId());
			CommentAsync.post(2, AppConfig.getCommentUrl, taskArgs);
			bt_getComment.setVisibility(View.GONE);
			tv_newcomment.setVisibility(View.VISIBLE);
			commentListView.setVisibility(View.VISIBLE);// 显示评论的listview
			break;
		/** 发送评论 */
		case R.id.bt_send:
			String commentStr = et_comment.getText().toString();
			// 发评论的接口

			// postid：帖子的id号
			// comment：评论的内容
			// commentusername：评论者的账号
			// commentdate：评论日期（具体到时和分）
			if (!commentStr.equals("")) {
				if (!MainActivity.user.getUsername().equals("")) {
					HashMap<String, String> taskArgs2 = new HashMap<String, String>();
					taskArgs2.put("postid", article.getId());
					taskArgs2.put("comment", commentStr);
					taskArgs2.put("commentusername",
							MainActivity.user.getUsername());
					taskArgs2.put("commentdate", BaseTools.getCurrentTime());

					sendCommentAsync.post(2, AppConfig.sendCommentUrl,
							taskArgs2);

				} else {
					Toast.makeText(this, "请先登录...", Toast.LENGTH_LONG).show();
				}

			}

			break;
		}
	}

	/*
	 * 分享帖子到其他
	 */

	private void shareContent(String shareContent) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
		intent.putExtra(Intent.EXTRA_TEXT, shareContent);
		startActivity(Intent.createChooser(intent, article.getTitle()));
	}

	// @Override
	// public boolean onTouch(View arg0, MotionEvent event) {
	// // TODO Auto-generated method stub
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_UP:
	// doOnBorderListener();
	// break;
	// }
	// return false;
	// }

	// private void doOnBorderListener() {
	// // TODO Auto-generated method stub
	// if (scrollview_content != null
	// && scrollview_content.getChildAt(0).getMeasuredHeight() <=
	// scrollview_content
	// .getScrollY() + scrollview_content.getHeight()) {
	// bt_getComment.setVisibility(View.GONE);
	// }
	//
	// }
}
