package com.allen.iguangwai.model;

import java.io.Serializable;
import com.allen.iguangwai.async.BaseModel;

/** ����ʵ���� */
public class Article extends BaseModel implements Serializable {
	/** ����id */
	private String id;
	/** �������� */
	private String type;
	/** ���ӱ��� */
	private String title;
	/** ���� */
	private String author;
	/** ժҪ��Ϣ */
	private String description;
	/** ����ʱ�� */
	private String publishTime;
	/** ���ӵ�һ��ͼƬ */
	private String firstPicUrl;
	/** �������� */
	private String content;

	public Article() {

	}

	public Article(String id, String type, String title, String author,
			String description, String publishTime, String firstPicUrl) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.author = author;
		this.description = description;
		this.publishTime = publishTime;
		this.firstPicUrl = firstPicUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstPicUrl() {
		return firstPicUrl;
	}

	public void setFirstPicUrl(String firstPicUrl) {
		this.firstPicUrl = firstPicUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
