package com.sdk.mysdklibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.sdk.mysdklibrary.Net.HttpUtils;
import com.sdk.mysdklibrary.Tools.FilesTool;
import com.sdk.mysdklibrary.Tools.MLog;
import com.sdk.mysdklibrary.Tools.PhoneTool;
import com.sdk.mysdklibrary.activity.LoginActivity;
import com.sdk.mysdklibrary.activity.PayActivity;
import com.sdk.mysdklibrary.interfaces.InitCallBack;
import com.sdk.mysdklibrary.interfaces.LoginCallBack;
import com.sdk.mysdklibrary.interfaces.PayCallBack;
import com.sdk.mysdklibrary.localbeans.GameArgs;
import com.sdk.mysdklibrary.localbeans.GameRoleBean;
import com.sdk.mysdklibrary.localbeans.OrderInfo;

import java.util.Arrays;

public class MySdkApi {


    private static Activity mact = null;
    private static LoginCallBack mlogincallBack;
    private static PayCallBack mpaycallBack;

    public static LoginCallBack getLoginCallBack() {
        return mlogincallBack;
    }

    public static PayCallBack getMpaycallBack() {
        return mpaycallBack;
    }

    public static Activity getMact() {
        return mact;
    }

    public static void setDebug(boolean isdebug){
        MLog.setDebug(isdebug);
    }
    public static void initSDK(final Activity context,String cpid,String gameid,String key, final InitCallBack callBack){
        mact = context;
        //处理设备号
        PhoneTool.managerIMEI(context);
        GameArgs gameargs = new GameArgs();
        String gamename = MyApplication.getAppContext().getApplicationInfo().loadLabel(MyApplication.getAppContext().getPackageManager()).toString();
        gameargs.setCpid(cpid);
        gameargs.setGameno(gameid);
        gameargs.setKey(key);
        gameargs.setName(gamename);
        gameargs.setPublisher(FilesTool.getPublisherString()[0]);

        MLog.s("service Publisher -------> " + gameargs.getPublisher());
        MLog.s("service cpidid -------> " + cpid);
        MLog.s("service gameid -------> " + gameid);
        MLog.s("service gamekey-------> " + key);
        MLog.s("service gamename------> " + gamename);
        MyApplication.getAppContext().setGameArgs(gameargs);

        MyGamesImpl.getInstance().initSDK(context, callBack);
    }

    /**
     * activity onResume
     */
    public static void onResume(){

        //MythsGamesImpl.getInstance().onResume();
    }

    /**
     * activity onPause
     */
    public static void onPause(){

        //MythsGamesImpl.getInstance().onPause();
    }

    /**
     * activity onDestory
     */
    public static void onDestory(){

        //MythsGamesImpl.getInstance().destorySDK();
    }

    /**
     * activity onActivityResult
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data){

        MyGamesImpl.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    /**
     * activity onSaveInstanceState google登录需要用到
     * @param outState
     */
    public static void onSaveInstanceState(Bundle outState){
     //   MythsGamesImpl.getInstance().onSaveInstanceState(outState);
    }

    /**
     * 在游戏Activity中的onBackPressed方法中调用
     * @param context
     */
    public static void onBackPressed(Activity context){
     //   MythsGamesImpl.getInstance().onBackPressed(context);
    }

    /**
     * 选择登录
     * @param context
     * @param callBack
     * @param mode//1游客2facebook
     */
    public static void chooseLogin(Activity context,LoginCallBack callBack,int mode){
        mlogincallBack = callBack;
        if (mode==1){
            HttpUtils.fastlogin(context);
        }else if (mode==2){
            MyGamesImpl.getInstance().openfacebookLogin(context,callBack,"");
        }

    }

    /**
     *自动登录
     * @param context
     * @param callBack
     */
    public static void autoLogin(Activity context,LoginCallBack callBack){
        mlogincallBack = callBack;
        MyGamesImpl.getInstance().autoLogin(context);
    }



    /**
     * 登录接口，呼出登录界面
     * @param context
     * @param callBack
     */
    public static void openLogin(Activity context,final LoginCallBack callBack){
        mlogincallBack = callBack;
        MyGamesImpl.getInstance().openLogin(context);

    }

    /**
     * 启动支付 -- 打开支付界面
     * @param context
     * @param callBack
     */
    public static void startPay(Activity context, OrderInfo orderinfo, PayCallBack callBack){
        mpaycallBack = callBack;

        MyApplication.getAppContext().setOrderinfo(orderinfo);

        HttpUtils.getPayList();

    }

    /**
     * 打开个人中心
     */
    public static void openAccountCenter(Activity context) {
    //    MythsGamesImpl.getInstance().openAccountCenter(context);
    }

    /**
     * 打开客服中心
     */
    public static void openCustomerCenter(Activity context) {
   //     MythsGamesImpl.getInstance().openCustomerCenter(context);
    }

    /**
     * 0=create role 1=enter game 2=role level
     * @param operator
     * @param gameRoleBean
     */
    public static void submitRoleData(int operator, GameRoleBean gameRoleBean) {
        MyGamesImpl.getInstance().submitRoleData(operator, gameRoleBean);
    }


    /**
     * 注册广播接收器，在添加Fb分享、邀请好友、点赞功能时在初始化SDK时调用
     * @param context
     */
    public static void registerBroadcastReceiver(Activity context){
    //    MythsGamesImpl.getInstance().registerBroadcastReceiver(context);
    }

    public static void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults){
   //     MythsGamesImpl.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    /**
     * 隐藏悬浮窗
     */
    public static void hideFloat(){
    }

    /**
     * 显示悬浮窗
     */
    public static void showFloat(){
    }
}
