package com.sdk.mysdklibrary;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.sdk.mysdklibrary.Net.HttpUtils;
import com.sdk.mysdklibrary.Tools.Configs;
import com.sdk.mysdklibrary.Tools.MLog;
import com.sdk.mysdklibrary.Tools.PhoneTool;
import com.sdk.mysdklibrary.Tools.ResourceUtil;
import com.sdk.mysdklibrary.activity.LoginActivity;
import com.sdk.mysdklibrary.interfaces.InitCallBack;
import com.sdk.mysdklibrary.interfaces.LoginCallBack;
import com.sdk.mysdklibrary.localbeans.GameRoleBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MyGamesImpl {

    private static MyGamesImpl instance;
    private Activity activity;
    private Activity sdkact;

    public void setSdkact(Activity sdkact) {
        this.sdkact = sdkact;
    }

    public Activity getSdkact() {
        return sdkact;
    }

    private static CallbackManager callbackManager =null;

    private boolean isInit = false;
    private boolean isLogin = false;
    private boolean isPayShow = false;
    private boolean isLiveOn = false;
    private static SharedPreferences sharedPreferences =MyApplication.context.getSharedPreferences("user_info", 0);

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    //插件支付app 名字
    private String pluginAppName = "";

    private Application applicationContext;

    public MyGamesImpl(){

    }

    public static MyGamesImpl getInstance(){
        if(instance == null){
            instance = new MyGamesImpl();
        }
        return instance;
    }

    /**
     * 初始化sdk
     * @param context
     * @param callBack
     */
    public void initSDK(final Activity context,final InitCallBack callBack){
        String facebook_login_id = ResourceUtil.getString(context,"pg_facebook_login_id");
        MLog.a("facebook_login_id----->"+facebook_login_id);
        FacebookSdk.setApplicationId(facebook_login_id);
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        activity = context;
        if(Build.VERSION.SDK_INT>=23){
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                    ||ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE}, 1);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(700);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            boolean istop = PhoneTool.isTopActivity(context);
                            if(istop){
                                activity.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        initSDK(context,callBack);
                                    }
                                });
                                break;
                            }

                        }
                    }
                }).start();
                return;
            }else{
                HttpUtils.checkupnet(context,callBack);
            }
        }else{
            HttpUtils.checkupnet(context,callBack);
        }

    }

    /**
     *
     * @param context
     * @param callBack
     * @param type//"bind"表示游客转facebook账号
     */
    public void openfacebookLogin(Activity context,final LoginCallBack callBack,final String type){
        if (!"bind".equals(type)){
            //检查facebook的token有效性
            String token = MyGamesImpl.getSharedPreferences().getString("myths_input_token","");
            if (!token.equals("")){
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (isLoggedIn) {
                    HttpUtils.gotoAutoLoginActivity("facebook");
                    return;
                }
            }
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                String uid = loginResult.getAccessToken().getUserId();
                String token = loginResult.getAccessToken().getToken();
                HttpUtils.fblogin_check(uid,token,type);

            }
            @Override
            public void onCancel() {
                callBack.loginFail("cancel");
            }
            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                callBack.loginFail("onError");
            }
        });

        LoginManager.getInstance().logInWithReadPermissions(context, Arrays.asList("public_profile","email"));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void autoLogin(Activity context) {
        SharedPreferences sharedPreferences =MyGamesImpl.getSharedPreferences();
        String type = sharedPreferences.getString("myths_auto_type","");
        if ("guest".equals(type)){
            String name = sharedPreferences.getString("myths_youke_name","");
            if (!name.equals("")){
                HttpUtils.gotoAutoLoginActivity("guest");
                return;
            }
        }else if ("facebook".equals(type)){
            String token = sharedPreferences.getString("myths_input_token","");
            if (!token.equals("")){
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (isLoggedIn) {
                    HttpUtils.gotoAutoLoginActivity("facebook");
                    return;
                }
            }
        }
        openLogin(context);
    }
    public void openLogin(Activity context){
        Intent itn = new Intent(context, LoginActivity.class);
        itn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(itn);
    }

    public void submitRoleData(int operator, GameRoleBean gameRoleBean) {
        HttpUtils.submitRoleData(operator, gameRoleBean);
    }
}
