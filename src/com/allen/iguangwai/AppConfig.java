package com.allen.iguangwai;

/**
 * 应用程序配置类：用于保存相关信息及设置
 */
public class AppConfig {

	public static String USERNAME;
	public static String ip1="http://192.168.203.84/";
	public static String ip2="http://192.168.203.90/";
	// http://192.168.203.84/
	// http://192.168.203.90/

	public static final String loginUrl = ip1+"igdufs1/index.php/home/AndroidApi/login";
	public static final String articleUrl = ip1+"igdufs1/index.php/home/AndroidApi/getArticle";
	public static final String articleUrl2 = ip1+"igdufs1/index.php/home/AndroidApi/";
	public static final String getOtherDataUrl = ip1+"igdufs1/index.php/home/AndroidApi/getUser";
	public static final String contentUrl = ip1+"igdufs1/index.php/home/AndroidApi/getContent";
	public static final String publishUrl = ip1+"igdufs1/index.php/home/AndroidApi/postUserArticle";
	public static final String SearchUrl = ip1+"igdufs1/index.php/home/AndroidApi/findArticle";
	public static final String getCommentUrl = ip1+"igdufs1/index.php/home/AndroidApi/getComment";
	public static final String sendCommentUrl =ip1+ "igdufs1/index.php/home/AndroidApi/postComment";
	public static final String sendUserDataUrl = ip1+"igdufs1/index.php/home/AndroidApi/updateUserInfo";
	public static final String getUserArticle = ip1+"igdufs1/index.php/home/AndroidApi/getUserArticle";
	public static final String getUserComment = ip1+"igdufs1/index.php/home/AndroidApi/getUserComment";
	/*
	 * public static final String loginUrl =
	 * "igdufs/index.php/home/Appsystemloadjson/jsontest";
	 * 
	 * public static final String articleUrl =
	 * "igdufs/index.php/home/Appstudypostsjson/postsjson"
	 * ; public static final String articleUrl2 =
	 * "igdufs/index.php/home/Appstudypostsjson/postsjson"
	 * ;
	 * 
	 * public static final String contentUrl =
	 * "igdufs/index.php/home/Appsentpostjson/postcontentjson"
	 * ; public static final String publishUrl =
	 * "igdufs/index.php/home/Appsentpostjson/sentpostjson"
	 * ; public static final String SearchUrl =
	 * "igdufs/index.php/home/Appstudypostsjson/searchjson"
	 * ; public static final String getCommentUrl =
	 * "igdufs/index.php/home/Appcommentjson/commentjson";
	 * public static final String sendCommentUrl =
	 * "igdufs/index.php/home/Appcommentjson/sentcommentjson"
	 * ; public static final String sendUserDataUrl =
	 * "igdufs/index.php/home/Appuserinfojson/userinfojson"
	 * ;
	 */
}
