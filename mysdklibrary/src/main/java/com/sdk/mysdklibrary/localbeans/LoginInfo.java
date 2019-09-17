package com.sdk.mysdklibrary.localbeans;

/**
 * 数据库登录信息
 * @author joson
 *
 */
public class LoginInfo {
	private int appId;
	private int userId;
	private String clientDate;
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getClientDate() {
		return clientDate==null?"":clientDate;
	}
	public void setClientDate(String clientDate) {
		this.clientDate = clientDate;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
