package com.sdk.mysdklibrary.localbeans;

public class ConfigBean {
	private int appId;
	private String appKey;
	private String url;
	private int isDebug = 0;
	private String sdkVersion;
	private String loginJson;

	private String paraSecret;

	public String getLoginJson() {
		return loginJson;
	}
	public void setLoginJson(String loginJson) {
		this.loginJson = loginJson;
	}
	public int getIsDebug() {
		return isDebug;
	}
	public void setIsDebug(int isDebug) {
		this.isDebug = isDebug;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getAppKey() {
		return appKey==null?"":appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getUrl() {
		return url==null?"":url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSdkVersion() {
		return sdkVersion==null?"":sdkVersion;
	}
	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	public String getParaSecret() {
		return paraSecret;
	}

	public void setParaSecret(String paraSecret) {
		this.paraSecret = paraSecret;
	}
}
