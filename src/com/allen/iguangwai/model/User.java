package com.allen.iguangwai.model;

import java.io.Serializable;
import com.allen.iguangwai.async.QuantaBaseModel;

/** 用户实体类 */
public class User extends QuantaBaseModel implements Serializable {

	/** 用户id(账号) */
	private String username;
	/** 真名 */
	private String realname;
	/** 昵称 */
	private String name;
	/** 性別 */
	private String gender;
	/** 学院 */
	private String academy;
	/** 专业 */
	private String major;
	/** 头像地址 */
	private String head;
	/** 个性签名 */
	private String signature;

	public User() {
	}

	public User(String username, String head) {
		this.username = username;
		this.head = head;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getAcademy() {
		return academy;
	}

	public void setAcademy(String academy) {
		this.academy = academy;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
