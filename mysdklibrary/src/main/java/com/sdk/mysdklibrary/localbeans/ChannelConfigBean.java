package com.sdk.mysdklibrary.localbeans;

public class ChannelConfigBean {
	private String appsFlyer;
	private String talkingdata;
	private String facebook;
	private String charboostAppId;
	private String charboostAppSignature;
	private String eway;
	private String mobvistaSDKAppId;
	private String adMobId ;//google
	private String adMobValue;

	public String getAppsFlyer() {
		return appsFlyer==null?"":appsFlyer;
	}
	public void setAppsFlyer(String appsFlyer) {
		this.appsFlyer = appsFlyer;
	}
	public String getTalkingdata() {
		return talkingdata==null?"":talkingdata;
	}
	public void setTalkingdata(String talkingdata) {
		this.talkingdata = talkingdata;
	}
	public String getFacebook() {
		return facebook==null?"":facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getCharboostAppId() {
		return charboostAppId==null?"":charboostAppId;
	}
	public void setCharboostAppId(String charboostAppId) {
		this.charboostAppId = charboostAppId;
	}
	public String getCharboostAppSignature() {
		return charboostAppSignature==null?"":charboostAppSignature;
	}
	public void setCharboostAppSignature(String charboostAppSignature) {
		this.charboostAppSignature = charboostAppSignature;
	}
	public String getEway() {
		return eway==null?"":eway;
	}
	public void setEway(String eway) {
		this.eway = eway;
	}
	public String getMobvistaSDKAppId() {
		return mobvistaSDKAppId==null?"":mobvistaSDKAppId;
	}
	public void setMobvistaSDKAppId(String mobvistaSDKAppId) {
		this.mobvistaSDKAppId = mobvistaSDKAppId;
	}
	public String getAdMobId() {
		return adMobId==null?"":adMobId;
	}
	public void setAdMobId(String adMobId) {
		this.adMobId = adMobId;
	}
	public String getAdMobValue() {
		return adMobValue==null?"":adMobValue;
	}
	public void setAdMobValue(String adMobValue) {
		this.adMobValue = adMobValue;
	}
}
