package com.sdk.mysdklibrary.interfaces;

import com.sdk.mysdklibrary.localbeans.UserInfo;

public interface LoginCallBack {
	void loginSuccess(String uid, String token);
	void loginFail(String msg);
}
