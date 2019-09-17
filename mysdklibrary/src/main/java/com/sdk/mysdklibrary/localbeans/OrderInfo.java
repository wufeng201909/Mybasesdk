package com.sdk.mysdklibrary.localbeans;

public class OrderInfo {
	private String transactionId="";   //交易流水
	private String amount="";			//金额
	private String productname="";        //道具
	private String feepoint="";             //计费点
	private String extrainfo="";      //客户端支付时间
	private String payurl="";      //客户端支付时间


	public OrderInfo(){
		
	}
	
	public OrderInfo(String transactionId, String amount,String product_name,String fee_point, String extra_info,String pay_url){
		this.transactionId = transactionId;
		this.amount = amount;
		this.productname = product_name;
		this.feepoint = fee_point;
		this.extrainfo = extra_info;
		this.payurl = pay_url;
	}


	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String product_name) {
		this.productname = product_name;
	}

	public String getFeepoint() {
		return feepoint;
	}

	public void setFeepoint(String fee_point) {
		this.feepoint = fee_point;
	}

	public String getExtraInfo() {
		return extrainfo;
	}

	public void setExtraInfo(String extra_info) {
		this.extrainfo = extra_info;
	}

	public void setPayurl(String payurl) {
		this.payurl = payurl;
	}

	public String getPayurl() {
		return payurl;
	}
}
