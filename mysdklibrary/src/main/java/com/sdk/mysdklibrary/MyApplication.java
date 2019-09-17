package com.sdk.mysdklibrary;

import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager.LayoutParams;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEventFailure;
import com.adjust.sdk.AdjustEventSuccess;
import com.adjust.sdk.LogLevel;
import com.adjust.sdk.OnEventTrackingFailedListener;
import com.adjust.sdk.OnEventTrackingSucceededListener;
import com.sdk.mysdklibrary.Tools.Configs;
import com.sdk.mysdklibrary.Tools.FilesTool;
import com.sdk.mysdklibrary.Tools.MLog;
import com.sdk.mysdklibrary.localbeans.GameArgs;
import com.sdk.mysdklibrary.localbeans.OrderInfo;

public class MyApplication extends  Application {

	public static Context context;
	private static MyApplication myself;
	/** 游戏信息 */
	private GameArgs gameargs = null;
	private OrderInfo orderinfo = null;

	public void setOrderinfo(OrderInfo orderinfo) {
		this.orderinfo = orderinfo;
	}

	public OrderInfo getOrderinfo() {
		return orderinfo;
	}

	/** 游戏实例表 */
	private Map<String, GameArgs> gamemap = null;


	public String re1 = "";
	public String re2 = "";
	public String re3 = "";

	public MyApplication(Context contex) {
		super();
		if (context == null) {
			context = this;
		}
	}

	public MyApplication() {
		super();
		if (context == null) {
			context = this;
		}
	}

	@Override
	public void onCreate() {
		System.out.println("111111111111111111111");
		
		MyCrashHandler mCrashHandler=MyCrashHandler.getInstance();
		mCrashHandler.init(this);
		
		super.onCreate();

		//初始化Adjust
		String appToken = "2fm9gkqubvpc";
		String environment = AdjustConfig.ENVIRONMENT_SANDBOX;//test
		String environment1 = AdjustConfig.ENVIRONMENT_PRODUCTION;//develop
		AdjustConfig config = new AdjustConfig(this, appToken, environment);
		config.setLogLevel(LogLevel.VERBOSE);

		// Set event success tracking delegate.
		config.setOnEventTrackingSucceededListener(new OnEventTrackingSucceededListener() {
			@Override
			public void onFinishedEventTrackingSucceeded(AdjustEventSuccess eventSuccessResponseData) {
				Log.d("MySDK", "Event success callback called!");
				Log.d("MySDK", "Event success data: " + eventSuccessResponseData.toString());
			}
		});

		// Set event failure tracking delegate.
		config.setOnEventTrackingFailedListener(new OnEventTrackingFailedListener() {
			@Override
			public void onFinishedEventTrackingFailed(AdjustEventFailure eventFailureResponseData) {
				Log.d("MySDK", "Event failure callback called!");
				Log.d("MySDK", "Event failure data: " + eventFailureResponseData.toString());
			}
		});
		config.setSendInBackground(true);
		Adjust.onCreate(config);


		registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

		if (context == null) {
			context = this;
		}
		myself = this;
		checkSD();

	}

	private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
		@Override
		public void onActivityCreated(Activity activity, Bundle bundle) {

		}

		@Override
		public void onActivityStarted(Activity activity) {

		}

		@Override
		public void onActivityResumed(Activity activity) {
			Adjust.onResume();
		}

		@Override
		public void onActivityPaused(Activity activity) {
			Adjust.onPause();
		}

		@Override
		public void onActivityStopped(Activity activity) {

		}

		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

		}

		@Override
		public void onActivityDestroyed(Activity activity) {

		}

		//...
	}

	@Override
	public void attachBaseContext(Context base) {
	    super.attachBaseContext(base);

	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		// 防止内存泄露，清理相关数据务必调用SDK结束接口

	}

	/**
	 * 栈顶活动名称
	 * 
	 * @return
	 */
	public ComponentName getTopActivity() {
		ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null) {
			return (runningTaskInfos.get(0).topActivity);
		} else {
			return null;
		}
	}

	/**
	 * 加载SD卡相关信息
	 */
	public void checkSD() {
		FilesTool.ExistSDCard();
		long size = FilesTool.getSDFreeSize();

		MLog.a("SDFreeSize----->" + size+"MB");
		System.out.println("Configs.ASDKROOT----------------->"+ Configs.ASDKROOT);
	}

	/**
	 * 得到当前全局游戏实体
	 * 
	 * @return
	 */
	public GameArgs getGameArgs() {
		return gameargs;
	}

	/**
	 * 设置当前全局游戏实体
	 * 
	 * @param gameargs
	 */
	public void setGameArgs(GameArgs gameargs) {
		this.gameargs = gameargs;
	}

	/**
	 * 将游戏参数放入哈希表
	 * 
	 * @param key
	 * @param g
	 */
	public void putGameArgsMap(String key, GameArgs g) {
		gamemap.put(key, g);
	}


	/**
	 *
	 * @return
	 */
	public Map<String, GameArgs> getGamemap() {
		return gamemap;
	}

	public static MyApplication getAppContext() {
		return myself;
	}

}
