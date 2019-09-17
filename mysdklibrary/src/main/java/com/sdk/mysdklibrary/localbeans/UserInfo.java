package com.sdk.mysdklibrary.localbeans;

/**
 * 数据库用户表信息
 * @author Administrator
 *
 */
public class UserInfo {
	private int userId ;
	private String userName;
	private String password;
	private int accountType;
	private int userType;
	private String thirdPartyId;
	private String email;
	private String nickName;
	private String clientDate;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName==null?"":userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password==null?"":password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getThirdPartyId() {
		return thirdPartyId==null?"":thirdPartyId;
	}
	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}
	public String getEmail() {
		return email==null?"":email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickName() {
		return nickName==null?"":nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setClientDate(String clientDate){
		this.clientDate = clientDate;
	}
	public String getClientDate(){
		return clientDate==null?"":clientDate;
	}
}
