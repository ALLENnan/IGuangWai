package com.allen.iguangwai.dao;

import java.util.List;
import java.util.Map;

import com.allen.iguangwai.model.Article;

import android.content.ContentValues;
/*
 * Article���ݿ�����ӿ�
 */
public interface ArticleDaoInface {
	public void addArticle(Article article);

	public void deleteArticle(String whereClause, String[] whereArgs);

	public void updateArticle(ContentValues values, String whereClause,
			String[] whereArgs);

	public List<Article> getArticleList();

	public void clearAllArticle();

}
