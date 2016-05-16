package com.aishangte.zmj.meishiwu.utils;

import android.content.SharedPreferences;
import cn.sharesdk.tpl.UserInfo;

public class ZmjConstant {

	public static String meiShiWuDownLoadUrl = "http://1.zmj520.sinaapp.com";


	public static String getPersonalLiWuCollectionUrl = "http://1.zmj520.sinaapp.com/servlet/GetPersonalLiWuCollection";
	public static String getPersonalShiHeCollectionUrl = "http://1.zmj520.sinaapp.com/servlet/GetShiHeCollection";
	public static String getPersonalMeiShiMeiWenCollectionUrl = "http://1.zmj520.sinaapp.com/servlet/GetPersonalMeiShiMeiWen";

	public static String getVersionInfoUrl = "http://1.zmj520.sinaapp.com/servlet/CheckVersion";
	
	public static String sendOpinionsUrl = "http://1.zmj520.sinaapp.com/servlet/OpinionsFeedBack";

	public static String deletePersonalLiWuCollectionUrl = "http://1.zmj520.sinaapp.com/servlet/DeleteLiWuCollection";
	public static String deletePersonalShiHeCollectionUrl = "http://1.zmj520.sinaapp.com/servlet/DeleteShiHeCollection";
	public static String deletePersonalMeiShiMeiWenCollectionUrl = "http://1.zmj520.sinaapp.com/servlet/DeleteMeiShiMeiWenCollection";
	
	public static String delteUserInfoFromServersUrl = "http://1.zmj520.sinaapp.com/servlet/DeleteUserInfoFromTable";
	
	
	public static String loginUrl = "http://1.zmj520.sinaapp.com/servlet/LoginCheck";
	
	// 已经显示了隐藏的framelayout的view数量 初始为0
	public static int LiWuAlreadyShowFrameLayoutNumbersofView = 0;
	// 当前总共显示的view的数量 初始为 4
	public static int LiWuTotalViewShowNumbers = 4;
	
	
	public static int ShiHeAlreadyShowFrameLayoutNumbersofView = 0;
	public static int ShiHeTotalViewShowNumbers = 4;
	
	public static int MeiShiMeiWenAlreadyShowFrameLayoutNumbersofView = 0;
	public static int MeiShiMeiWenTotalViewShowNumbers = 4;
	
	public static int whichFragmentIsDoingDeleteOperation = 100;
	
	
	public static String imagePath;
	
	
	public static UserInfo userInfo = new UserInfo();
	
	
	public static SharedPreferences preference;
	public static SharedPreferences.Editor editor;
	



}
