package com.common;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.os.Handler;

import com.basic.BankResultOK;
import com.basic.OtherLinkResult;

public class Global extends Application {

	public static String _ConnString4Data = "/sdcard/Android/data/WebView/data.db";

//	金斗云    9988 - webview_jdy  - webdb1    
//	完美金融  9987 - webview_wmjr - webdb2    
//	卡神通    9986 - webview_kst  - webdb3    
//	中创科技  9985 - webview_zckj - webdb4    
//	鑫易金融  9984 - webview_xyjr - webdb5    
	
	// webdb_super 测试科技-全民一体机
	public static String WebServiceUrl = "http://211.149.157.146:9888/WebService.asmx";
	public static String updateFileString = "http://211.149.157.146:9888/WebView.apk";
	
	// webdb1     金斗云
//	public static String WebServiceUrl = "http://211.149.157.146:9988/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9988/WebView.apk";
	
	// webdb2   完美金融
//	public static String WebServiceUrl = "http://211.149.157.146:9987/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9987/WebView.apk";
	
	// webdb3  卡神通 
//	public static String WebServiceUrl = "http://211.149.157.146:9986/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9986/WebView.apk";
	
	// webdb4  中创科技
//	public static String WebServiceUrl = "http://211.149.157.146:9985/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9985/WebView.apk";
	
	// webdb5  鑫易金融 
//	public static String WebServiceUrl = "http://211.149.157.146:9984/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9984/WebView.apk";

	public static int SendDataTime = 20000;
	public static String deviceId = ""; // 设备id
	public static String clientVersion = "";// 版本

	public static String ID = ""; // 用户id
	
	public static String UserCode = ""; // 用户编码
    
	public static String UserName = ""; // 用户名称
	public static String Password = ""; // 用户密码
	public static String Remark = ""; // 用户备注
	public static String IsAdmin = ""; // 是否是管理员

	public static String StartPage = "";// 起始页图片 链接

	// public static List<SaleBillDo> _ModelList = new
	// ArrayList<SaleBillDo>();//所有已扫描未提交的销货单
	public static String Version;

	public static String City;// 城市
	/**
	 * SysLink1 入门篇链接-只维护链接即可 SysLink2 进阶篇链接-只维护链接即可 SysLink3 完胜篇链接-只维护链接即可
	 * SysLink4 最新口子链接-只维护链接即可 SysLink5 信用卡攻略链接-只维护链接即可 SysLink6 企业查询链接-只维护链接即可
	 * SysLink7 征信查询链接-只维护链接即可
	 */
	//public static List<OtherLinkResult> OutlinksList;// 存储用到的外部网站链接

	public static List<BankResultOK> BankList;

 
}
