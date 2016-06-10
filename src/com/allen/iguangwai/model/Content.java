package com.allen.iguangwai.model;

import com.allen.iguangwai.async.BaseModel;

public class Content extends BaseModel {
	/** Ìù×Óid */
	private String id;
	/** Ìù×ÓÄÚÈİ */
	private String content;

	public Content() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
