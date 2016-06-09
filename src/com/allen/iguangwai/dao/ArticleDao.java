package com.allen.iguangwai.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.allen.iguangwai.db.SQLHelper;
import com.allen.iguangwai.model.Article;

/*
 * article数据库操作类
 */
public class ArticleDao implements ArticleDaoInface {
	private SQLiteDatabase db;
	private SQLHelper helper;
	private Context context;

	public ArticleDao(Context context) {
		this.context = context;
		helper = new SQLHelper(context);
		db = helper.getWritableDatabase();
	}

	// 添加article
	@Override
	public void addArticle(Article artile) {

		ContentValues values = new ContentValues();
		values.put("id", artile.getId());
		values.put("title", artile.getTitle());
		values.put("description", artile.getDescription());
		values.put("publishTime", artile.getPublishTime());
		values.put("author", artile.getAuthor());
		values.put("type", artile.getType());
		values.put("firstPicUrl", artile.getFirstPicUrl());
		db.insert(SQLHelper.TABLE_ARTICLE, null, values);

	}

	// 添加article列表
	public void addArticle(ArrayList<Article> articleList) {
		for (int i = 0; i < articleList.size(); i++) {
			addArticle(articleList.get(i));
		}
		Close();
//		Toast.makeText(context, "数据库添加成功", Toast.LENGTH_LONG).show();
	}

	@Override
	public void deleteArticle(String whereClause, String[] whereArgs) {

		db.delete(SQLHelper.TABLE_ARTICLE, whereClause, whereArgs);
		Close();
	}

	@Override
	public void updateArticle(ContentValues values, String whereClause,
			String[] whereArgs) {
		db.update(SQLHelper.TABLE_ARTICLE, values, whereClause, whereArgs);
		Close();
	}

	@Override
	public ArrayList<Article> getArticleList() {
		// TODO Auto-generated method stub
		ArrayList<Article> articlelist = new ArrayList<Article>();

		Cursor cursor = db.query(SQLHelper.TABLE_ARTICLE,
				SQLHelper.ALL_STRINGS, null, null, null, null, null);

		while (cursor.moveToNext()) {
			Article article = new Article();

			article.setId(cursor.getString(cursor.getColumnIndex(SQLHelper.ID)));
			article.setType(cursor.getString(cursor
					.getColumnIndex(SQLHelper.TYPE)));
			article.setTitle(cursor.getString(cursor
					.getColumnIndex(SQLHelper.TITLE)));
			article.setAuthor(cursor.getString(cursor
					.getColumnIndex(SQLHelper.AUTHOR)));
			article.setDescription(cursor.getString(cursor
					.getColumnIndex(SQLHelper.DESCRIPTION)));
			article.setPublishTime(cursor.getString(cursor
					.getColumnIndex(SQLHelper.PUBLISHTIME)));
			article.setFirstPicUrl(cursor.getString(cursor
					.getColumnIndex(SQLHelper.FIRSTPICURL)));
			articlelist.add(article);
			Log.v("myLog", "id" + article.getId());
		}
//		Toast.makeText(context, "从数据库得到数据", Toast.LENGTH_LONG).show();
		return articlelist;

	}

	public void Close() {
		db.close();
		helper.close();
	}

	public void clearAllArticle() {
		String sql = "DELETE FROM " + SQLHelper.TABLE_ARTICLE + ";";
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql);
//		Toast.makeText(context, "清除数据库", Toast.LENGTH_LONG).show();
	}
	//
	// private void revertSeq() {
	// String sql = "update sqlite_sequence set seq=0 where name='"
	// + SQLHelper.TABLE_ARTICLE + "'";
	// SQLiteDatabase db = helper.getWritableDatabase();
	// db.execSQL(sql);
	// }

}
