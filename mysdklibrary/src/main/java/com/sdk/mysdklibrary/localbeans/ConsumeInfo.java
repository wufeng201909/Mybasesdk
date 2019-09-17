package com.sdk.mysdklibrary.localbeans;

public class ConsumeInfo {
	private int userId;             //用户ID
	private int appId;              //应用ID
	private String transactionId;   //交易流水
	private String platformCoin;	//平台币
	private String productDesc;		//商品描述
	private int status;			//200成功，错误请见错误列表
	private String clientTime;		//客户端时间
	
	public ConsumeInfo(){
		
	}
	
	public ConsumeInfo(int userId,int appId,String transactionId,String platformCoin,String productDesc,int status,String clientTime){
		this.userId = userId;
		this.appId = appId;
		this.transactionId = transactionId;
		this.platformCoin = platformCoin;
		this.productDesc = productDesc;
		this.status = status;
		this.clientTime = clientTime;
	}

	public final int getUserId() {
		return userId;
	}

	public final void setUserId(int userId) {
		this.userId = userId;
	}

	public final int getAppId() {
		return appId;
	}

	public final void setAppId(int appId) {
		this.appId = appId;
	}

	public final String getTransactionId() {
		return transactionId==null?"":transactionId;
	}

	public final void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public final String getPlatformCoin() {
		return platformCoin==null?"":platformCoin;
	}

	public final void setPlatformCoin(String platformCoin) {
		this.platformCoin = platformCoin;
	}

	public final String getProductDesc() {
		return productDesc==null?"":productDesc;
	}

	public final void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public final int getStatus() {
		return status;
	}

	public final void setStatus(int status) {
		this.status = status;
	}

	public final String getClientTime() {
		return clientTime==null?"":clientTime;
	}

	public final void setClientTime(String clientTime) {
		this.clientTime = clientTime;
	}
	
	
}
