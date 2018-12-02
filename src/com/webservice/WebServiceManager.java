package com.webservice;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import android.R.integer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.format.Time;
import android.util.Log;

import com.basic.AddQuotaResult;
import com.basic.BankResult;
import com.basic.BankResultOK;
import com.basic.CardDetailsResult;
import com.basic.CardLogResult;
import com.basic.CardResult;
import com.basic.LineLoanResult;
import com.basic.LoanResult;
import com.basic.OtherAppResult;
import com.basic.OtherLinkResult;
import com.common.CommonMethord;
import com.common.Global;

import android.content.SharedPreferences;

public class WebServiceManager {
	
	/**
	 * У���û��Ƿ����
	 * @return
	 */
	public static boolean WriteLog(String val1,String val2,String val3,String val4,String val5)
	{
		try {
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("desc", val1);
			map.put("userName", val2);
			map.put("password", val3);
			map.put("deviceId", val4);
			map.put("clientVersion", val5);

			SoapPrimitive so = SoapControl.ExecuteWebMethod(
					"WriteLog", map,20000);
			if (so != null) {
				String rlt = so.toString();
				if(rlt.equals("success"))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch(Exception ex)
		{
			return false;
		}
		return false;
		
	}
	
	/**
	 * У���û��Ƿ����
	 * @return
	 */
	public static boolean checkUserCloseDate()
	{
		try {
			Map<String, Object> map = new HashMap<String, Object>();  
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapPrimitive so = SoapControl.ExecuteWebMethod(
					"checkUserCloseDate", map,20000);
			if (so != null) {
				String rlt = so.toString();
				if(rlt.equals("true"))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch(Exception ex)
		{
			return false;
		}
		return false;
		
	}

	// ��ȡ�����ⲿApp
		public static List<OtherAppResult> getOtherApp(String type) {
			List<OtherAppResult> list = new ArrayList<OtherAppResult>();

			try {
				Map<String, Object> map = new HashMap<String, Object>(); 
				map.put("type", type);
				map.put("userName", Global.UserCode);
				map.put("password", Global.Password);
				map.put("deviceId", Global.deviceId);
				map.put("clientVersion", Global.clientVersion);

				SoapObject so = SoapControl.ExecuteWebMethodReturnClass(
						"getOtherApp", map);
				if (so != null) {
					String Success = so.getPropertyAsString("Success");
					if (Success.equals("true")) {
						SoapObject soap_AssetResults = (SoapObject) so
								.getProperty("OtherAppResults");
						int ic1 = soap_AssetResults.getPropertyCount();
						for (int i = 0; i < ic1; i++) {
							SoapObject soap_Asset = (SoapObject) soap_AssetResults
									.getProperty(i);
							OtherAppResult ado = new OtherAppResult();

							ado.OTHER_APP_NAME = soap_Asset.getProperty(
									"OTHER_APP_NAME").toString();
							ado.OTHER_APP_DESC = soap_Asset.getProperty(
									"OTHER_APP_DESC").toString();

							ado.OTHER_APP_IMG_DATA = soap_Asset.getProperty(
									"OTHER_APP_IMG_DATA").toString();
							ado.OTHER_APP_URL = soap_Asset.getProperty(
									"OTHER_APP_URL").toString();
							ado.OTHER_APP_PACKAGE_NAME = soap_Asset.getProperty(
									"OTHER_APP_PACKAGE_NAME").toString();


							list.add(ado);
						}

					}
				}
			} catch (Exception ex) {
				return list;
			}
			return list;
		}
	
	
	// ��ȡ���д���
	public static List<AddQuotaResult> getAddQuota(String currentLastIdx) {
		List<AddQuotaResult> list = new ArrayList<AddQuotaResult>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("currentLastIdx", currentLastIdx);
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass(
					"getAddQuota", map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("AddQuotaResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						AddQuotaResult ado = new AddQuotaResult();

						ado.INCREASE_QUOTA_NAME = soap_Asset.getProperty(
								"INCREASE_QUOTA_NAME").toString();
						ado.INCREASE_QUOTA_DESC = soap_Asset.getProperty(
								"INCREASE_QUOTA_DESC").toString();

						ado.INCREASE_QUOTA_IMG_DATA = soap_Asset.getProperty(
								"INCREASE_QUOTA_IMG_DATA").toString();
						ado.INCREASE_QUOTA_URL = soap_Asset.getProperty(
								"INCREASE_QUOTA_URL").toString();
						ado.INCREASE_QUOTA_METHORD_URL = soap_Asset.getProperty(
								"INCREASE_QUOTA_METHORD_URL").toString();


						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}

	// ��ȡ���д���
	public static List<LoanResult> getLoan(String currentLastIdx) {
		List<LoanResult> list = new ArrayList<LoanResult>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("currentLastIdx", currentLastIdx);
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass("getLoan",
					map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("LoanResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						LoanResult ado = new LoanResult();

						ado.LOAN_NAME = soap_Asset.getProperty("LOAN_NAME")
								.toString();
						ado.LOAN_DESC = soap_Asset.getProperty("LOAN_DESC")
								.toString();

						ado.LOAN_IMG_DATA = soap_Asset.getProperty(
								"LOAN_IMG_DATA").toString();
						ado.LOAN_URL = soap_Asset.getProperty("LOAN_URL")
								.toString();

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}

	// ��ȡ��������
	public static List<LineLoanResult> getLineLoan(String city,
			String loanType, String currentLastIdx) {
		List<LineLoanResult> list = new ArrayList<LineLoanResult>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("city", city);
			map.put("loanType", loanType);
			map.put("currentLastIdx", currentLastIdx);
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass(
					"getLineLoan", map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("LineLoanResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						LineLoanResult ado = new LineLoanResult();

						ado.LINE_LOAN_TYPE = soap_Asset.getProperty(
								"LINE_LOAN_TYPE").toString();
						ado.LINE_LOAN_NAME = soap_Asset.getProperty(
								"LINE_LOAN_NAME").toString();
						ado.LINE_LOAN_DESC = soap_Asset.getProperty(
								"LINE_LOAN_DESC").toString();
						ado.LINE_LOAN_DESC1 = soap_Asset.getProperty(
								"LINE_LOAN_DESC1").toString();// ��Ϣ��
						ado.LINE_LOAN_DESC2 = soap_Asset.getProperty(
								"LINE_LOAN_DESC2").toString();// ���������
						ado.LINE_LOAN_DESC3 = soap_Asset.getProperty(
								"LINE_LOAN_DESC3").toString();// ������Ч��
						ado.LINE_LOAN_IMG_DATA = soap_Asset.getProperty(
								"LINE_LOAN_IMG_DATA").toString();
						ado.LINE_LOAN_URL = soap_Asset.getProperty(
								"LINE_LOAN_URL").toString();
						ado.APPLY_USER_COUNT = soap_Asset.getProperty(
								"APPLY_USER_COUNT").toString();// ��������

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}

	// ��ȡ���п���Ϣ
	public static List<CardResult> getCards(String city, String bank,
			String level, String cardOrg, String ages, String currentLastIdx) {
		List<CardResult> list = new ArrayList<CardResult>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("city", city);
			map.put("bank", bank);
			map.put("level", level);
			map.put("cardOrg", cardOrg);
			map.put("ages", ages);
			map.put("currentLastIdx", currentLastIdx);
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass("getCard",
					map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("CardResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						CardResult ado = new CardResult();
						ado.ID = soap_Asset.getProperty("ID").toString();
						ado.BANK_ID = soap_Asset.getProperty("BANK_ID")
								.toString();
						ado.CARD_NAME = soap_Asset.getProperty("CARD_NAME")
								.toString();
						ado.CARD_HUMAN_AGES = soap_Asset.getProperty(
								"CARD_HUMAN_AGES").toString();// ���������
						ado.CARD_DESC1 = soap_Asset.getProperty("CARD_DESC1")
								.toString();// ��Ϣ��
						ado.CARD_DESC2 = soap_Asset.getProperty("CARD_DESC2")
								.toString();// ���������
						ado.CARD_DESC3 = soap_Asset.getProperty("CARD_DESC3")
								.toString();// ������Ч��
						ado.CARD_DESC4 = soap_Asset.getProperty("CARD_DESC4")
								.toString();// ���ȼ�
						ado.CARD_DESC5 = soap_Asset.getProperty("CARD_DESC5")
								.toString();// ������
						ado.CARD_DESC6 = soap_Asset.getProperty("CARD_DESC6")
								.toString();// ����֯
						ado.CARD_DESC7 = soap_Asset.getProperty("CARD_DESC7")
								.toString();// ע������
						ado.CARD_TITLE_DESC1 = soap_Asset.getProperty(
								"CARD_TITLE_DESC1").toString();// ��������
						ado.CARD_TITLE_DESC2 = soap_Asset.getProperty(
								"CARD_TITLE_DESC2").toString();// ��������
						ado.CARD_TITLE_DESC3 = soap_Asset.getProperty(
								"CARD_TITLE_DESC3").toString();// ��������
						ado.CARD_IMG_DATA = soap_Asset.getProperty(
								"CARD_IMG_DATA").toString();
						ado.CARD_URL = soap_Asset.getProperty("CARD_URL")
								.toString();
						ado.APPLY_USER_COUNT = soap_Asset.getProperty(
								"APPLY_USER_COUNT").toString();// ��������
						ado.IS_NEW_USER = soap_Asset.getProperty("IS_NEW_USER")
								.toString(); // �ʺ��»��쿨������һ����û�����ڿͻ���
						ado.IS_FAST_GET = soap_Asset.getProperty("IS_FAST_GET")
								.toString(); // �׻��쿨�����ź���û�����ÿ��ͻ���
						ado.IS_HIGH_QUOTAR = soap_Asset.getProperty(
								"IS_HIGH_QUOTAR").toString();// �߶�Ȱ쿨�����ź��������ÿ��ͻ���

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}

	// ��ȡ���п���Ϣ
	public static List<CardResult> getCards(String city, String bank,
			String level, String cardOrg, String ages, String currentLastIdx,
			String IsNewUser, String IsFastGet, String IsHighQuotar) {
		List<CardResult> list = new ArrayList<CardResult>();

		try {
			if(city == null)
			{
				city= "";
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("city", city);
			map.put("bank", bank);
			map.put("level", level);
			map.put("cardOrg", cardOrg);
			map.put("ages", ages);
			map.put("currentLastIdx", currentLastIdx);
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);
			map.put("IsNewUser", IsNewUser);
			map.put("IsFastGet", IsFastGet);
			map.put("IsHighQuotar", IsHighQuotar);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass("getCard",
					map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("CardResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						CardResult ado = new CardResult();
						ado.ID = soap_Asset.getProperty("ID").toString();
						ado.BANK_ID = soap_Asset.getProperty("BANK_ID")
								.toString();
						ado.CARD_NAME = soap_Asset.getProperty("CARD_NAME")
								.toString();
						ado.CARD_HUMAN_AGES = soap_Asset.getProperty(
								"CARD_HUMAN_AGES").toString();// ���������
						ado.CARD_DESC1 = soap_Asset.getProperty("CARD_DESC1")
								.toString();// ��Ϣ��
						ado.CARD_DESC2 = soap_Asset.getProperty("CARD_DESC2")
								.toString();// ���������
						ado.CARD_DESC3 = soap_Asset.getProperty("CARD_DESC3")
								.toString();// ������Ч��
						ado.CARD_DESC4 = soap_Asset.getProperty("CARD_DESC4")
								.toString();// ���ȼ�
						ado.CARD_DESC5 = soap_Asset.getProperty("CARD_DESC5")
								.toString();// ������
						ado.CARD_DESC6 = soap_Asset.getProperty("CARD_DESC6")
								.toString();// ����֯
						ado.CARD_DESC7 = soap_Asset.getProperty("CARD_DESC7")
								.toString();// ע������
						ado.CARD_TITLE_DESC1 = soap_Asset.getProperty(
								"CARD_TITLE_DESC1").toString();// ��������
						ado.CARD_TITLE_DESC2 = soap_Asset.getProperty(
								"CARD_TITLE_DESC2").toString();// ��������
						ado.CARD_TITLE_DESC3 = soap_Asset.getProperty(
								"CARD_TITLE_DESC3").toString();// ��������
						ado.CARD_IMG_DATA = soap_Asset.getProperty(
								"CARD_IMG_DATA").toString();
						ado.CARD_URL = soap_Asset.getProperty("CARD_URL")
								.toString();
						ado.APPLY_USER_COUNT = soap_Asset.getProperty(
								"APPLY_USER_COUNT").toString();// ��������
						ado.IS_NEW_USER = soap_Asset.getProperty("IS_NEW_USER")
								.toString(); // �ʺ��»��쿨������һ����û�����ڿͻ���
						ado.IS_FAST_GET = soap_Asset.getProperty("IS_FAST_GET")
								.toString(); // �׻��쿨�����ź���û�����ÿ��ͻ���
						ado.IS_HIGH_QUOTAR = soap_Asset.getProperty(
								"IS_HIGH_QUOTAR").toString();// �߶�Ȱ쿨�����ź��������ÿ��ͻ���

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}

	// ��ȡ����������Ϣ
	public static List<OtherLinkResult> getLinks(String linkName) {
		List<OtherLinkResult> list = new ArrayList<OtherLinkResult>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("linkName", linkName);
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass(
					"getOtherLinks", map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("OtherLinkResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						OtherLinkResult ado = new OtherLinkResult();
						ado.ID = soap_Asset.getProperty("ID").toString();
						ado.LINK_NAME = soap_Asset.getProperty("LINK_NAME")
								.toString();
						ado.LINK_DESC = soap_Asset.getProperty("LINK_DESC")
								.toString();
						ado.LINK_URL = soap_Asset.getProperty("LINK_URL")
								.toString();// ���������
						ado.LINK_IMG_DATA = soap_Asset.getProperty(
								"LINK_IMG_DATA").toString();// ��Ϣ��
//						ado.REMARK = soap_Asset.getProperty("REMARK")
//								.toString();

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}

	// ��ȡ���п���Ϣ
	public static List<BankResult> getBanks() {
		List<BankResult> list = new ArrayList<BankResult>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass("getBanks",
					map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("BankResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						BankResult ado = new BankResult();

						ado.BANK_NAME1 = soap_Asset.getProperty("BANK_NAME1")
								.toString();
						ado.BANK_DESC1 = soap_Asset.getProperty("BANK_DESC1")
								.toString();
						ado.BANK_IMG_DATA1 = soap_Asset.getProperty(
								"BANK_IMG_DATA1").toString();
						ado.BANK_URL1 = soap_Asset.getProperty("BANK_URL1")
								.toString();

						String BANK_NAME2 = soap_Asset
								.getProperty("BANK_NAME2").toString();
						if (BANK_NAME2.trim().contains("anyType")) {
							BANK_NAME2 = "";
						}
						ado.BANK_NAME2 = BANK_NAME2;

						String BANK_DESC2 = soap_Asset
								.getProperty("BANK_DESC2").toString();
						if (BANK_DESC2.trim().contains("anyType")) {
							BANK_DESC2 = "";
						}
						ado.BANK_DESC2 = BANK_DESC2;

						ado.BANK_IMG_DATA2 = soap_Asset.getProperty(
								"BANK_IMG_DATA2").toString();
						ado.BANK_URL2 = soap_Asset.getProperty("BANK_URL2")
								.toString();

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}

	public static List<BankResultOK> getBanksOK() {
		List<BankResultOK> list = new ArrayList<BankResultOK>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass(
					"getBanksOK", map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("BankResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						BankResultOK ado = new BankResultOK();

						ado.BANK_NAME = soap_Asset.getProperty("BANK_NAME")
								.toString();
						String BANK_DESC = soap_Asset.getProperty("BANK_DESC")
								.toString();
						if (BANK_DESC.trim().contains("anyType")) {
							BANK_DESC = "";
						}
						ado.BANK_DESC = BANK_DESC;
						ado.BANK_IMG_DATA = soap_Asset.getProperty(
								"BANK_IMG_DATA").toString();
						ado.BANK_URL = soap_Asset.getProperty("BANK_URL")
								.toString();

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}
	
	public static List<BankResultOK> getBanksOKCity() {
		List<BankResultOK> list = new ArrayList<BankResultOK>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);
			map.put("city", Global.City);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass(
					"getBanksOK", map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("BankResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						BankResultOK ado = new BankResultOK();

						ado.BANK_NAME = soap_Asset.getProperty("BANK_NAME")
								.toString();
						String BANK_DESC = soap_Asset.getProperty("BANK_DESC")
								.toString();
						if (BANK_DESC.trim().contains("anyType")) {
							BANK_DESC = "";
						}
						ado.BANK_DESC = BANK_DESC;
						ado.BANK_IMG_DATA = soap_Asset.getProperty(
								"BANK_IMG_DATA").toString();
						ado.BANK_URL = soap_Asset.getProperty("BANK_URL")
								.toString();

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}

	// ��ȡ���п���ϸ��Ϣ
	public static List<CardDetailsResult> getCardDetails(String cardID) {
		List<CardDetailsResult> list = new ArrayList<CardDetailsResult>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cardID", cardID);
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass(
					"getCardDetails", map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("CardDetailsResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						CardDetailsResult ado = new CardDetailsResult();
						ado.CARD_DESC1 = soap_Asset.getProperty("CARD_DESC1")
								.toString();
						ado.CARD_DESC2 = soap_Asset.getProperty("CARD_DESC2")
								.toString();
						ado.DESC_TYPE = soap_Asset.getProperty("DESC_TYPE")
								.toString();// ��Ϣ��

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}

	// ��ȡ���п���Ϣ
	public static String[] getBanks(String city) {
		String[] banks = null;
		List<OtherLinkResult> list = new ArrayList<OtherLinkResult>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("city", city);
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass("getBanks",
					map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("OtherLinkResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						OtherLinkResult ado = new OtherLinkResult();
						ado.ID = soap_Asset.getProperty("ID").toString();
						ado.LINK_NAME = soap_Asset.getProperty("LINK_NAME")
								.toString();
						ado.LINK_DESC = soap_Asset.getProperty("LINK_DESC")
								.toString();
						ado.LINK_URL = soap_Asset.getProperty("LINK_URL")
								.toString();// ���������
						ado.LINK_IMG_DATA = soap_Asset.getProperty(
								"LINK_IMG_DATA").toString();// ��Ϣ��
						ado.REMARK = soap_Asset.getProperty("REMARK")
								.toString();

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return banks;
		}
		return banks;
	}

	// �ύ��������־
	public static String submitCardLog(String bankname,String cardname) {
		String Success = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bankname", bankname);
		map.put("cardname", cardname);
		map.put("userName", Global.UserCode);
		map.put("password", Global.Password);
		map.put("deviceId", Global.deviceId);
		map.put("clientVersion", Global.clientVersion);

		SoapPrimitive so = SoapControl.ExecuteWebMethod("submitCardLog", map,
				Global.SendDataTime);
		if (so != null) {
			Success = so.toString();
		}

		return Success;
	}
	
	//��ȡ������־
	public static List<CardLogResult> getCardLog() {
		List<CardLogResult> list = new ArrayList<CardLogResult>();

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userName", Global.UserCode);
			map.put("password", Global.Password);
			map.put("deviceId", Global.deviceId);
			map.put("clientVersion", Global.clientVersion);

			SoapObject so = SoapControl.ExecuteWebMethodReturnClass(
					"getCardLog", map);
			if (so != null) {
				String Success = so.getPropertyAsString("Success");
				if (Success.equals("true")) {
					SoapObject soap_AssetResults = (SoapObject) so
							.getProperty("CardLogResults");
					int ic1 = soap_AssetResults.getPropertyCount();
					for (int i = 0; i < ic1; i++) {
						SoapObject soap_Asset = (SoapObject) soap_AssetResults
								.getProperty(i);
						
						CardLogResult ado = new CardLogResult();

						ado.BANK_NAME = soap_Asset.getProperty("BANK_NAME").toString(); 
						ado.CARD_NAME = soap_Asset.getProperty("CARD_NAME").toString();
						ado.USER_NAME = soap_Asset.getProperty("USER_NAME").toString();
						ado.DATE = soap_Asset.getProperty("DATE").toString();

						list.add(ado);
					}

				}
			}
		} catch (Exception ex) {
			return list;
		}
		return list;
	}
	
	
	

	// ��ȡ����
	public static String GetServerDate() {
		Time t = new Time();
		t.setToNow(); // ȡ��ϵͳʱ�䡣
		String year = String.valueOf(t.year).substring(0, 2);
		return year;
	}

}
