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

//	����    9988 - webview_jdy  - webdb1    
//	��������  9987 - webview_wmjr - webdb2    
//	����ͨ    9986 - webview_kst  - webdb3    
//	�д��Ƽ�  9985 - webview_zckj - webdb4    
//	���׽���  9984 - webview_xyjr - webdb5    
	
	// webdb_super ���ԿƼ�-ȫ��һ���
	public static String WebServiceUrl = "http://211.149.157.146:9888/WebService.asmx";
	public static String updateFileString = "http://211.149.157.146:9888/WebView.apk";
	
	// webdb1     ����
//	public static String WebServiceUrl = "http://211.149.157.146:9988/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9988/WebView.apk";
	
	// webdb2   ��������
//	public static String WebServiceUrl = "http://211.149.157.146:9987/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9987/WebView.apk";
	
	// webdb3  ����ͨ 
//	public static String WebServiceUrl = "http://211.149.157.146:9986/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9986/WebView.apk";
	
	// webdb4  �д��Ƽ�
//	public static String WebServiceUrl = "http://211.149.157.146:9985/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9985/WebView.apk";
	
	// webdb5  ���׽��� 
//	public static String WebServiceUrl = "http://211.149.157.146:9984/WebService.asmx";
//	public static String updateFileString = "http://211.149.157.146:9984/WebView.apk";

	public static int SendDataTime = 20000;
	public static String deviceId = ""; // �豸id
	public static String clientVersion = "";// �汾

	public static String ID = ""; // �û�id
	
	public static String UserCode = ""; // �û�����
    
	public static String UserName = ""; // �û�����
	public static String Password = ""; // �û�����
	public static String Remark = ""; // �û���ע
	public static String IsAdmin = ""; // �Ƿ��ǹ���Ա

	public static String StartPage = "";// ��ʼҳͼƬ ����

	// public static List<SaleBillDo> _ModelList = new
	// ArrayList<SaleBillDo>();//������ɨ��δ�ύ��������
	public static String Version;

	public static String City;// ����
	/**
	 * SysLink1 ����ƪ����-ֻά�����Ӽ��� SysLink2 ����ƪ����-ֻά�����Ӽ��� SysLink3 ��ʤƪ����-ֻά�����Ӽ���
	 * SysLink4 ���¿�������-ֻά�����Ӽ��� SysLink5 ���ÿ���������-ֻά�����Ӽ��� SysLink6 ��ҵ��ѯ����-ֻά�����Ӽ���
	 * SysLink7 ���Ų�ѯ����-ֻά�����Ӽ���
	 */
	//public static List<OtherLinkResult> OutlinksList;// �洢�õ����ⲿ��վ����

	public static List<BankResultOK> BankList;

 
}
