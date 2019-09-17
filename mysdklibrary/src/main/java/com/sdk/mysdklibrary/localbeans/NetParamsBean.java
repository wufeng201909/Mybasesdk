package com.sdk.mysdklibrary.localbeans;

public class NetParamsBean {
	private String verifyCode;		//验证码
	private String newPassword;		//新密码
	private String bindAccountName; //绑定邮箱
	
	private int operator;			//1=初绑定手机（判断PASSWORD,） 2=绑定手机（不判断password）     注册\注册接口时：0=register 1=login 2=fast register
	private int createRole;			//0=非创角 1=创角 
	private String lastTime;		//服务器返回的时间戳（yyyy-MM-dd HH:mm:ss）
	

	private String cardNumber; //卡号
	private String cardPassword; //卡密
	
	private String paymentUrl;//web支付服务器返回url
	
	private boolean isAutoLogin;//是否是自动登录
	private String bindEmail;//待绑定邮箱
	
	//下列为支付和登录完成后打点需要的数据
	private boolean isFirstLogin;
//	private String gameOrderId;
	private Double money;
	private String currency;
	
	//社会化分享
	private String platform;
	private String description;
	
	private String exInfo ="";//额外信息
	private String thirdPartyId = "";//第三方用户Id
	



    private int type;                         //0=创角色 1=进入游戏 2=角色等级升级
	private String roleId;                    //角色ID
	private String roleName;                  //角色名称
	private int level;                     //角色等级
	private int vipLevel;                     //VIP等级
	private String roleCreateTime;            //角色创建时间
	private String gameOrderId;               //游戏订单ID
	private String gameZoneId;                //游戏区服ID
	private int gameCoin;
	private String gameExt;
	private String productName;				  //商品id

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getNewPassword() {
		return newPassword==null?"":newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getBindAccountName() {
		return bindAccountName==null?"":bindAccountName;
	}

	public void setBindAccountName(String bindAccountName) {
		this.bindAccountName = bindAccountName;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	public int getCreateRole() {
		return createRole;
	}

	public void setCreateRole(int createRole) {
		this.createRole = createRole;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardPassword() {
		return cardPassword;
	}

	public void setCardPassword(String cardPassword) {
		this.cardPassword = cardPassword;
	}

	public String getPaymentUrl() {
		return paymentUrl;
	}

	public void setPaymentUrl(String paymentUrl) {
		this.paymentUrl = paymentUrl;
	}

	public boolean isAutoLogin() {
		return isAutoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		isAutoLogin = autoLogin;
	}

	public String getBindEmail() {
		return bindEmail==null?"":bindEmail;
	}

	public void setBindEmail(String bindEmail) {
		this.bindEmail = bindEmail;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		isFirstLogin = firstLogin;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExInfo() {
		return exInfo==null?"":exInfo;
	}

	public void setExInfo(String exInfo) {
		this.exInfo = exInfo;
	}

	public String getThirdPartyId() {
		return thirdPartyId==null?"":thirdPartyId;
	}

	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRoleId() {
		return roleId==null?"":roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName==null?"":roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getRoleCreateTime() {
		return roleCreateTime==null?"":roleCreateTime;
	}

	public void setRoleCreateTime(String roleCreateTime) {
		this.roleCreateTime = roleCreateTime;
	}

	public String getGameOrderId() {
		return gameOrderId;
	}

	public void setGameOrderId(String gameOrderId) {
		this.gameOrderId = gameOrderId;
	}

	public String getGameZoneId() {
		return gameZoneId==null?"":gameZoneId;
	}

	public void setGameZoneId(String gameZoneId) {
		this.gameZoneId = gameZoneId;
	}

	public int getGameCoin() {
		return gameCoin;
	}

	public void setGameCoin(int gameCoin) {
		this.gameCoin = gameCoin;
	}

	public String getGameExt() {
		return gameExt;
	}

	public void setGameExt(String gameExt) {
		this.gameExt = gameExt;
	}

	public String getProductName() {
		return productName==null?"":productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
