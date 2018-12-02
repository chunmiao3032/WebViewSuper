package com.webservice;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.basic.DateArraySerializer;
import com.basic.StringArraySerializer;
import com.common.CommonMethord;
import com.common.Global; 

public class SoapControl {
 
	static String NameSpace = "http://tempuri.org/"; 
	static String url; // ��ʱע������ʽ����ʱ��
	static String soapAction = "";

	public static SoapPrimitive ExecuteWebMethod(String MethodName, Map map,int timeout)
	{
		try
		{
			url = Global.WebServiceUrl; // ��ʱע������ʽ����ʱ��
			soapAction = NameSpace + MethodName;
			// step1 ָ��WebService�������ռ�͵��õķ�����
			SoapObject request = new SoapObject(NameSpace, MethodName);

			// step2 ���õ��÷����Ĳ���ֵ,����Ĳ������Ʋ�һ����WebServiceһ��
			if (map != null)
			{
				Set keySet = map.keySet();// ���ؼ��ļ���
				Iterator it = keySet.iterator();
				while (it.hasNext()) // ��һ�ֵ�����ʽȡ��ֵ
				{
					Object key = it.next();
					request.addProperty(key.toString(), map.get(key));
				}
			}
			// step3 ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// �����Ƿ���õ���dotNet�µ�WebService
			envelope.dotNet = true;
			// ���룬�ȼ���envelope.bodyOut = request;
			envelope.setOutputSoapObject(request);
			// step4 ����HttpTransportSE����
			MyHttpTransportSE ht = new MyHttpTransportSE(url, timeout);
			// step5 ����WebService
			ht.call(soapAction, envelope);
			// step6 ʹ��getResponse�������WebService�����ķ��ؽ��
			if (envelope.getResponse() != null)
			{
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				return response;
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return null;
		}
	}

	public static SoapObject ExecuteWebMethodReturnClass(String MethodName,Map map)
	{
		try
		{
			url = Global.WebServiceUrl; // ��ʱע������ʽ����ʱ��
			url= url.replaceAll(" ", "%20");
			soapAction = NameSpace + MethodName;
			soapAction= soapAction.replaceAll(" ", "%20");
			// step1 ָ��WebService�������ռ�͵��õķ�����
			SoapObject request = new SoapObject(NameSpace, MethodName);
			// step2 ���õ��÷����Ĳ���ֵ,����Ĳ������Ʋ�һ����WebServiceһ��
			if (map != null)
			{
				Set keySet = map.keySet();// ���ؼ��ļ���
				Iterator it = keySet.iterator();
				while (it.hasNext()) // ��һ�ֵ�����ʽȡ��ֵ
				{
					Object key = it.next();
					request.addProperty(key.toString(), map.get(key));
				}
			}
			// step3 ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// �����Ƿ���õ���dotNet�µ�WebService
			envelope.dotNet = true;
			// ���룬�ȼ���envelope.bodyOut = request;
			envelope.setOutputSoapObject(request);
			// step4 ����HttpTransportSE����
			MyHttpTransportSE ht = new MyHttpTransportSE(url, 60000);
			// step5 ����WebService
			ht.call(soapAction, envelope);
			// step6 ʹ��getResponse�������WebService�����ķ��ؽ��
			if (envelope.getResponse() != null)
			{
				SoapObject response = (SoapObject) envelope.getResponse();
				return response;
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return null;
		}
	}
	
	public static SoapPrimitive ExecuteWebMethodSubmitBatchNos(
			String billNo, int num,
	        List<String> lineNos, List<String> beginDates, 
	        List<String> beginNos, List<String> endDates, List<String> endNos,
	        String MethodName, Map map,int timeout)
	{
		try
		{
			url = Global.WebServiceUrl; // ��ʱע������ʽ����ʱ��
			soapAction = NameSpace + MethodName;
			// step1 ָ��WebService�������ռ�͵��õķ�����
			SoapObject request = new SoapObject(NameSpace, MethodName);

			// step2 ���õ��÷����Ĳ���ֵ,����Ĳ������Ʋ�һ����WebServiceһ��
			if (map != null)
			{
				Set keySet = map.keySet();// ���ؼ��ļ���
				Iterator it = keySet.iterator();
				while (it.hasNext()) // ��һ�ֵ�����ʽȡ��ֵ
				{
					Object key = it.next();
					request.addProperty(key.toString(), map.get(key));
				}
			}
			//-------------------------------------------------------------
			 PropertyInfo pInfo_lineNos = new PropertyInfo();  
			 StringArraySerializer stringArray = new StringArraySerializer();
			 for(String str : lineNos)
			 {
				 stringArray.add(str);
			 } 
			 pInfo_lineNos.setName("lineNos");
			 pInfo_lineNos.setValue(stringArray);
			 pInfo_lineNos.setType(stringArray.getClass());
			 pInfo_lineNos.setNamespace(NameSpace);
			   
		     request.addProperty(pInfo_lineNos);
		     //-------------------------------------------------------------
		     PropertyInfo pInfo_beginDates = new PropertyInfo();  
		     stringArray = new StringArraySerializer();
			 
			 for(String str : beginDates)
			 { 
				 stringArray.add(str);
			 } 
			 pInfo_beginDates.setName("beginDates");
			 pInfo_beginDates.setValue(stringArray);
			 pInfo_beginDates.setType(stringArray.getClass());
			 pInfo_beginDates.setNamespace(NameSpace);
			   
		     request.addProperty(pInfo_beginDates); 
		   //-------------------------------------------------------------
		     PropertyInfo pInfo_beginNos = new PropertyInfo();  
			 stringArray = new StringArraySerializer();
			 for(String str : beginNos)
			 {
				 stringArray.add(str);
			 } 
			 pInfo_beginNos.setName("beginNos");
			 pInfo_beginNos.setValue(stringArray);
			 pInfo_beginNos.setType(stringArray.getClass());
			 pInfo_beginNos.setNamespace(NameSpace);
			   
		     request.addProperty(pInfo_beginNos);  
 		  //-------------------------------------------------------------
		     PropertyInfo pInfo_endDates = new PropertyInfo();  
			 stringArray = new StringArraySerializer();
			 for(String str : endDates)
			 {
				 stringArray.add(str);
			 } 
			 pInfo_endDates.setName("endDates");
			 pInfo_endDates.setValue(stringArray);
			 pInfo_endDates.setType(stringArray.getClass());
			 pInfo_endDates.setNamespace(NameSpace);
			   
		     request.addProperty(pInfo_endDates);  
		      
		   //-------------------------------------------------------------
		     PropertyInfo pInfo_endNos = new PropertyInfo();  
			 stringArray = new StringArraySerializer();
			 for(String str : endNos)
			 {
				 stringArray.add(str);
			 } 
			 pInfo_endNos.setName("endNos");
			 pInfo_endNos.setValue(stringArray);
			 pInfo_endNos.setType(stringArray.getClass());
			 pInfo_endNos.setNamespace(NameSpace);
			   
		     request.addProperty(pInfo_endNos);   
 		  //-------------------------------------------------------------
			
			// step3 ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			// �����Ƿ���õ���dotNet�µ�WebService
			envelope.dotNet = true;
			// ���룬�ȼ���envelope.bodyOut = request;
			envelope.setOutputSoapObject(request);
			// step4 ����HttpTransportSE����
			MyHttpTransportSE ht = new MyHttpTransportSE(url, timeout);
			// step5 ����WebService
			ht.call(soapAction, envelope);
			// step6 ʹ��getResponse�������WebService�����ķ��ؽ��
			if (envelope.getResponse() != null)
			{
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				return response;
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return null;
		}
	}
	 
}
