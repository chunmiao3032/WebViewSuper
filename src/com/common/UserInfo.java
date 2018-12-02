package com.common;

import android.app.Application;

public class UserInfo extends Application {

	private String UserCode = "";
	private String UserName = "";
	private String Password = "";
	private String Remark = "";
	private String StartPage = "";

	 
	public void setUserCode(String userCode) {
		this.UserCode = userCode;
	}

	public String getUserCode() {
		return this.UserCode;
	}
	
	public void setUserName(String UserName) {
		this.UserName = UserName;
	}

	public String getUserName() {
		return this.UserName;
	}
	
	public void setPassword(String Password) {
		this.Password = Password;
	}

	public String getPassword() {
		return this.Password;
	}
	
	public void setRemark(String Remark) {
		this.Remark = Remark;
	}

	public String getRemark() {
		return this.Remark;
	}
	
	public void setStartPage(String StartPage) {
		this.StartPage = StartPage;
	}

	public String getStartPage() {
		return this.StartPage;
	}

}
