package com.sdk.mysdklibrary.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.UUID;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sdk.mysdklibrary.MyApplication;
import com.sdk.mysdklibrary.MyGamesImpl;
import com.sdk.mysdklibrary.MySdkApi;
import com.sdk.mysdklibrary.Net.HttpUtils;

import com.sdk.mysdklibrary.MyApplication;
import com.sdk.mysdklibrary.Tools.Configs;

public class PhoneTool {
	public static String TAG = "PhoneTool";

	/**
	 * 安装APK
	 *
	 * @param apkFileString
	 */
	public static void notifyAndInstallApk(Activity from, String apkFileString) {
		String name = HttpUtils.getUrlFileName(apkFileString).split("\\|")[0];
		String filename = Configs.ASDKROOT + from.getPackageName() + File.separator + name;
		if (!Configs.SDEXIST) {
			filename = Configs.ASDKROOT  + name;
		}

		MLog.s("apkroot -------> " + filename);
		String command = "chmod 777 " + filename;
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File apkfile = new File(filename);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
		from.startActivityForResult(intent, 1);
	}

	/**
	 * IMSI：international mobiles subscriber
	 * identity国际移动用户号码标识，这个一般大家是不知道，GSM必须写在卡内相关文件中 MSISDN:mobile subscriber
	 * ISDN用户号码，这个是我们说的139，136那个号码； ICCID:ICC identity集成电路卡标识，这个是唯一标识一张卡片物理号码的；
	 * IMEI：international mobile Equipment identity手机唯一标识码； TelephonyManager tm
	 * = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	 * String imei = tm.getDeviceId(); //取出IMEI String tel =
	 * tm.getLine1Number(); //取出MSISDN，很可能为空(電話號碼) String imei
	 * =tm.getSimSerialNumber(); //取出ICCID String imsi =tm.getSubscriberId();
	 * //取出IMEI
	 *
	 * @return
	 */

	public static String getIMEI(Context con) {
		String spDeviceID = MyApplication.context.getSharedPreferences("user_info", 0).getString("device_id", "a1s2d3f4t5g6");
//		MLog.a("MySDK","spDeviceID---->"+spDeviceID);
		return spDeviceID;
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("MissingPermission")
	private static String getDeviceId(Context context) {
        //读取保存的在sd卡中的唯一标识符
        String deviceId = readDeviceID(context);
        //判断是否已经生成过,有则直接返回
        if (deviceId != null && !"".equals(deviceId)) {
        	return deviceId;
        }
        //用于生成最终的唯一标识符
        StringBuffer s = new StringBuffer();
        //获取设备的imei号，如获取到则直接返回并保存
        try {
			TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = mTelephonyMgr.getDeviceId();
			s.append(deviceId);
		} catch (Exception e) {
			MLog.a(TAG,"好吧没得到IMEI");
		}
        if (s.length() > 1) {
            //持久化操作, 进行保存到SD卡中
            saveDeviceID(deviceId, context);
            return deviceId;
        }
        try {
            //获取设备的ANDROID_ID+SERIAL硬件序列号
        	deviceId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        	String serial= Build.SERIAL;
            s.append(deviceId).append(serial);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果以上搜没有获取相应的则自己生成相应的UUID作为相应设备唯一标识符
        if (s == null || s.length() <= 0) {
            UUID uuid = UUID.randomUUID();
            deviceId = uuid.toString().replace("-", "");
            s.append(deviceId);
        }
        //为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串
        String md5 = MD5Util.getMD5String(s.toString());
        if (s.length() > 0) {
            //持久化操作, 进行保存到SD卡中
            saveDeviceID(md5, context);
        }
        return md5;
	}
	/**
     * 读取固定的文件中的内容,这里就是读取sd卡中保存的设备唯一标识符
     *
     * @param context
     * @return
     */
	private static String readDeviceID(Context context) {
        File file = getDevicesDir(context);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader in = new BufferedReader(isr);
            String deviceID = in.readLine().trim();
            in.close();
            isr.close();
            fis.close();
            return deviceID;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	//保存文件的路径
    private static final String CACHE_IMAGE_DIR = "aray/cache/devices";
    //保存的文件 采用隐藏文件的形式进行保存
    private static final String DEVICES_FILE_NAME = ".asdkDevice";
	/**
     * 统一处理设备唯一标识 保存的文件的地址
     * @param context
     * @return
     */
    private static File getDevicesDir(Context context) {
        File mCropFile = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cropdir = new File(Environment.getExternalStorageDirectory(), CACHE_IMAGE_DIR);
            if (!cropdir.exists()) {
                cropdir.mkdirs();
            }
            mCropFile = new File(cropdir, DEVICES_FILE_NAME); // 用当前时间给取得的图片命名
        } else {
            File cropdir = new File(context.getFilesDir(), CACHE_IMAGE_DIR);
            if (!cropdir.exists()) {
                cropdir.mkdirs();
            }
            mCropFile = new File(cropdir, DEVICES_FILE_NAME);
        }
        return mCropFile;
    }
    /**
     * 保存 内容到 SD卡中,  这里保存的就是 设备唯一标识符
     * @param str
     * @param context
     */
    private static void saveDeviceID(String str, Context context) {
        File file = getDevicesDir(context);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, "UTF-8");
            out.write(str);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //处理设备号
    public static void managerIMEI(final Activity activity){
    	new Thread(new Runnable() {

			@Override
			public void run() {
				//sd卡读取
				String readDeviceID = readDeviceID(activity);
				//app缓存读取
				String spDeviceID = MyApplication.context.getSharedPreferences("user_info", 0).getString("device_id", readDeviceID);

                if (readDeviceID==null||"".equals(readDeviceID)) {// sd卡缓存为空

                	if(spDeviceID==null||"".equals(spDeviceID)){//app缓存为空
                		readDeviceID = getDeviceId(activity);
                	}else{////app缓存不为空
                		readDeviceID = spDeviceID;
                		saveDeviceID(readDeviceID, activity);
                	}
                }
                MyApplication.context.getSharedPreferences("user_info", 0).edit().putString("device_id", readDeviceID).commit();
			}
		}).start();
    }
	// 取出IMSI
	@SuppressLint("MissingPermission")
	public static String getIMSI(Context con) {
		String imsi = "a1s2d3f4t5";
		try {
			TelephonyManager mTelephonyMgr = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
			imsi = mTelephonyMgr.getSubscriberId();
		} catch (Exception e) {
			MLog.a(TAG,"好吧没得到IMSI");
			return "a1s2d3f4t5";
		}
		if (("").equals(imsi) || ("null").equals(imsi) || imsi == null || imsi == "null") {
			return "a1s2d3f4t5";
		}
		return imsi;
	}

	/**
	 * 加密后的IMEI
	 *
	 * @param imei
	 * @return
	 */
	public static String getIEMI(String imei) {
		StringBuilder ji = new StringBuilder();
		StringBuilder ou = new StringBuilder();
		imei = MD5Util.getMD5String(imei);
		for (int i = 0; i < imei.length(); i++) {
			if (i % 2 == 0) {
				ou.append(imei.charAt(i));
			} else {
				ji.append(imei.charAt(i));
			}
		}

		imei = MD5Util.getMD5String(ji + "jsk412lj21j5klj362dfanbvkc59874590asfk" + ou);
		return imei;
	}

	/**
	 * 判断有木有使用cmwap代理
	 *
	 * @return
	 */
	public static boolean isProxy(Context con) {
		ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni == null || (ni != null && !ni.isAvailable())) {// 未开启网络
				return false;
			} else { // 开启了网络
				if (ni.getTypeName().equals("WIFI")) {
					System.out.println("NET mode : " + "WIFI");
					return true;
				} else {
					NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
					String netString = networkInfo.getExtraInfo();
					System.out.println("NET mode : " + netString);
					if (netString.contains("cmnet")) {
						System.out.println("NET mode : " + "cmnet");
						return true;
					} else if (netString.contains("cmwap")) {
						System.out.println("NET mode : " + "cmwap");
						return true;
					} else if (netString.contains("internet")) {
						System.out.println("NET mode : " + "internet");
						return true;
					} else {
						System.out.println("NET mode : " + "未知");
						return true;
					}
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * 返回IP地址
	 *
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断网络连接是否打开
	 *
	 * @param context
	 * @return
	 */
	public static boolean isNetConnected(Context context) {
		boolean bisConnFlag = false;
		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null) {
			bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
		}

		isProxy(context);
		System.out.println("IP address :" + getLocalIpAddress());
		return bisConnFlag;

	}

	/**
	 * Role:Telecom service providers获取手机服务商信息 需要加入权限<uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/>
	 */
	@SuppressLint("MissingPermission")
	public static String getProvidersName(Context con) {
		// String ProvidersName = "未知";
		String gateway = "3";
		try {
			TelephonyManager mTelephonyMgr = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
			// 返回唯一的用户ID;就是这张卡的编号神马的
			 String IMSI = mTelephonyMgr.getSubscriberId();
			// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
			// 后面还有10位，是不知的
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
				// ProvidersName = "中国移动";
				gateway = "0";
			} else if (IMSI.startsWith("46001")) {
				// ProvidersName = "中国联通";
				gateway = "1";
			} else if (IMSI.startsWith("46003")) {
				// ProvidersName = "中国电信";
				gateway = "2";
			}

		} catch (Exception e) {
			MLog.a(TAG,"是不是没有卡呢？");
			return gateway;

		}

		/*
		 * String operator = mTelephonyMgr.getSimOperator(); if
		 * (operator.equals("46000") || operator.equals("46002")) {
		 * ProvidersName = "中国移动"; } else if (operator.equals("46001")) {
		 * ProvidersName = "中国联通"; } else if (operator.equals("46003")) {
		 * ProvidersName = "中国电信"; }
		 *
		 * Configuration conf =
		 * AsdkActivity.asdk.getResources().getConfiguration();
		 * if(conf.mcc==460){//中国 if(conf.mnc==0 || conf.mnc==2){ ProvidersName
		 * = "中国移动"; }else if(conf.mnc==1){ ProvidersName = "中国联通"; }else
		 * if(conf.mnc==3){ ProvidersName = "中国电信"; } }
		 */

		// Configuration conf = con.getResources().getConfiguration();

		return gateway;
	}

	/**
	 * 获取网络信号类型
	 */
	public static String getNetType(Context con) {
		TelephonyManager telephonyManager = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		int networkType = telephonyManager.getNetworkType();
		if (networkType == TelephonyManager.NETWORK_TYPE_UMTS || networkType == TelephonyManager.NETWORK_TYPE_HSDPA || networkType == TelephonyManager.NETWORK_TYPE_EVDO_0 || networkType == TelephonyManager.NETWORK_TYPE_EVDO_A) {
			return "3G";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_GPRS || networkType == TelephonyManager.NETWORK_TYPE_EDGE || networkType == TelephonyManager.NETWORK_TYPE_CDMA) {
			return "2G";
		}
		return networkType + "";
	}

	/**
	 * 获取手机信息
	 */
	@SuppressLint("MissingPermission")
	public static String getPhoneInfo(Context con) {
		TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		String mtyb = Build.BRAND;// 手机品牌
		String mtype = Build.MODEL; // 手机型号
		String version = Build.VERSION.RELEASE;// 版本
		 String imei = tm.getDeviceId();// IMEI
		String imsi = tm.getSubscriberId();// IMSI
		String serviceName = tm.getSimOperatorName(); // 运营商
		String numer = tm.getLine1Number(); // 手机号码
		return imei + "|" + version + "|" + mtype + "|" + mtyb;
	}

	/**
	 * 获取手机方位是什么
	 */
	@SuppressLint("MissingPermission")
	public static String getCL(Context con) {
		String cl = "feizhou";
		CellLocation cell = null;
		try {
			TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
			cell = tm.getCellLocation(); // 手机号码
			cl = String.valueOf(cell);
		} catch (Exception e) {
			MLog.a(TAG,"好吧没得到手机方位");
			return "feizhou";
		}
		if (("").equals(String.valueOf(cell)) || ("null").equals(String.valueOf(cell)) || String.valueOf(cell) == null || String.valueOf(cell) == "null") {
			return "feizhou";
		}
		return cl;
	}

	/**
	 * 获取手机型号
	 */
	public static String getPT(Context con) {
		String mtyb = "copycat";
		String mtype = "brands";
		try {
			TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
			mtyb = Build.BRAND;// 手机品牌
			mtype = Build.MODEL; // 手机型号
		} catch (Exception e) {
			// TODO: handle exception
			MLog.a(TAG,"好吧，没得到手机型号");
			return "copycat|brands";
		}
		return mtype + "|" + mtyb;
	}
	
	public static String getMac(Context con){
		String mac = "";
		WifiManager wifi = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info;
		try {
			info = wifi.getConnectionInfo();
			mac = info.getMacAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		MLog.a(TAG, "mac:" + mac);
		return mac == null?"":mac;
		
	}

	/**
	 * 手机分辨率信息
	 * 
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context con) {

		DisplayMetrics dm = new DisplayMetrics();
		Display display = ((Activity) con).getWindowManager().getDefaultDisplay();
		display.getMetrics(dm);
		// dm = AsdkActivity.asdk.getResources().getDisplayMetrics();

		/*
		 * String str = ""; int screenWidth = dm.widthPixels; int screenHeight =
		 * dm.heightPixels; float density = dm.density; float xdpi = dm.xdpi;
		 * float ydpi = dm.ydpi; str += "The absolute width:" +
		 * String.valueOf(screenWidth) + "pixels\n"; str +=
		 * "The absolute height:" + String.valueOf(screenHeight) + "pixels\n";
		 * str += "The logical density of the display.:" +
		 * String.valueOf(density) + "\n"; str += "X dimension :" +
		 * String.valueOf(xdpi) + "pixels per inch\n"; str += "Y dimension :" +
		 * String.valueOf(ydpi) + "pixels per inch\n";
		 */
		return dm;
	}

	/**
	 * 得到UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String s = uuid.toString();
		return s;
	}

	/**
	 * 打开设置网络界面
	 */
	public static void setNetworkMethod(final Context context) {
		Builder builder = new Builder(context);

		builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				if (Build.VERSION.SDK_INT > 10) {// 判断手机系统的版本
															// 即API大于10
															// 就是3.0或以上版本
					intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				context.startActivity(intent);
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
				}
				return true;// 自己消费，不劳上层费心
			}
		});

		builder.show();
	}
	private static AlertDialog alertDialog = null;
	/**
	 * 弹出等待框
	 * 
	 */
	public static void onCreateDialog(final Activity activity, String title, String msg) {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				Builder builder = new Builder(activity);
				//需要使用相对布局
				RelativeLayout view = new RelativeLayout(activity);
				RelativeLayout.LayoutParams  par = new RelativeLayout.LayoutParams(45, 45);
				view.setLayoutParams(par);
				

				ImageView iv2 = new ImageView(activity);
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(45, 45);
				iv2.setLayoutParams(param);
				Bitmap bm = FilesTool.getBitmap("myths_load_2", 1);
				BitmapDrawable draw = new BitmapDrawable(bm);
				iv2.setBackground(draw);
				
				ObjectAnimator animator=ObjectAnimator.ofFloat(iv2,"rotation",0F,360F);
				animator.setDuration(1500);
				animator.setRepeatCount(ObjectAnimator.INFINITE);
				animator.setRepeatMode(ObjectAnimator.RESTART);
				animator.start();
				
				view.addView(iv2);

				alertDialog =builder.create();
				alertDialog.show();
				alertDialog.setCancelable(false);
				alertDialog.getWindow().setContentView(view);
			}
		});
	}
	public static void disDialog() {
		if(alertDialog!=null){
			alertDialog.dismiss();
		}

	}

	/**
	 * 查看有木有当前显示
	 * 
	 * @param dialog
	 * @return
	 */
	public static Object isShowing(Dialog dialog) {
		if (dialog != null) {
			if (dialog.isShowing()) {
				return 1;
			} else {
				return null;
			}
		}
		return null;
	}


	private static String acc_file_name = "asdk.acc";
	//应美（欣欣h5）
//	private static String acc_file_name = "asdk-xxh5.acc";
	//17wan
//	private static String acc_file_name = "yiqwan.acc";
	//单机网游
//	private static String acc_file_name = "asdk-djwy.acc";
	public static ArrayList<String> getAccs(){
		boolean isHaveAcc = true;
		ArrayList<String> arr = new ArrayList<String>();
		StringBuffer strbuf = new StringBuffer();
		
		try {
			DesUtils des = new DesUtils("leemenz");
			
			File path = Environment.getExternalStorageDirectory();
			File file = new File(path.getAbsolutePath()+File.separator+Configs.ASDK+File.separator);
			if(!file.exists()){
				file.mkdir();
			}
			File accfile = new File(file.getAbsoluteFile()+File.separator+acc_file_name);
			if(!accfile.exists()){
				try {
					accfile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			BufferedReader br = new BufferedReader(new FileReader(accfile));
			String content = null;
			while ((content=br.readLine())!=null) {
				strbuf.append(content);
			}
			br.close();
			if(strbuf.length()==0){
				isHaveAcc = false;
			}else{
				String[] list = strbuf.toString().split("\\|");
				for (String item : list) {
					String str = des.decrypt(item);
					if(str.split("\\@").length>1){
						arr.add(str);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!isHaveAcc){
			Toast.makeText(MyApplication.context, "无账号记录", Toast.LENGTH_SHORT).show();
			return null;
		}else{
			return arr;
		}
	}
	public static void deleteAcc(String acc){
		FileWriter fw =null;
		try {
			DesUtils des = new DesUtils("leemenz");
			
			File path = Environment.getExternalStorageDirectory();
			File file = new File(path.getAbsolutePath()+File.separator+Configs.ASDK+File.separator);
			if(!file.exists()){
				try {
					file.mkdir();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			File accfile = new File(file.getAbsoluteFile()+File.separator+acc_file_name);
			if(!accfile.exists()){
				try {
					accfile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			Reader rd = new FileReader(accfile);
			BufferedReader br = new BufferedReader(rd);
			String content = br.readLine();
			br.close();
			if(content!=null && !"".equals(content)){
				String[] list = content.split("\\|");
				for (int i = 0;i<list.length;i++) {
					String acc_item =des.decrypt(list[i]);
					if(acc_item.split("\\@").length>1){
						if(acc_item.equals(acc)){
							content = content.replace(list[i]+"|", "");
							break;
						}else{
						}
					}
				}
			}
			fw = new FileWriter(accfile);
			fw.write(content);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * 保存帐号密码到本地
	 * @param acc
	 * @param password
	 */
	public static void saveAccount(String acc,String password){
		
		String myacc= acc+"@"+password;
		FileWriter fw =null;
		StringBuffer strbuf1 = new StringBuffer();
		StringBuffer strbuf2 = new StringBuffer();
		try {
			DesUtils des = new DesUtils("leemenz");
			
			File path = Environment.getExternalStorageDirectory();
			File file = new File(path.getAbsolutePath()+File.separator+Configs.ASDK+File.separator);
			if(!file.exists()){
				try {
					file.mkdir();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			File accfile = new File(file.getAbsoluteFile()+File.separator+acc_file_name);
			if(!accfile.exists()){
				try {
					accfile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			Reader rd = new FileReader(accfile);
			BufferedReader br = new BufferedReader(rd);
			String content = null;
			while ((content=br.readLine())!=null) {
				strbuf1.append(content);
			}
			br.close();
			strbuf2.append(des.encrypt(myacc)+"|");
			if(strbuf1.toString()!=null && !"".equals(strbuf1.toString())){
				String[] list = strbuf1.toString().split("\\|");
				for (int i = 0;i<list.length;i++) {
					String acc_item =des.decrypt(list[i]);
					if(acc_item.split("\\@").length>1){
						if(acc_item.split("\\@")[0].equals(acc)){
						}else{
							strbuf2.append(list[i]+"|");
						}
					}
				}
			}
			fw = new FileWriter(accfile);
			fw.write(strbuf2.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
//		try {
//			String str= acc+"@"+password;
//			DesUtils des = new DesUtils("leemenz");
//			System.out.println("加密前的字符：" + str);
//			System.out.println("加密后的字符：" + des.encrypt(str));
//		    System.out.println("解密后的字符：" + des.decrypt(des.encrypt(str)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}//自定义密钥   
//		Toast.makeText(MyApplication.context, "保存成功", Toast.LENGTH_SHORT).show();
	}
	

	/**
	 * 保存游客账号到本地
	 * @param acc
	 * @param password
	 * @param accid 
	 */
	public static void savevstAccount(String acc,String password, String accid){
		String myacc= acc+"@"+password+"@"+accid;
		FileWriter fw =null;
		try {
			DesUtils des = new DesUtils("leemenz");
			
			File path = Environment.getExternalStorageDirectory();
			File file = new File(path.getAbsolutePath()+File.separator+Configs.ASDK+File.separator);
			if(!file.exists()){
				try {
					file.mkdir();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			File accfile = new File(file.getAbsoluteFile()+File.separator+"asdk.accvst");
			if(!accfile.exists()){
				try {
					accfile.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
			fw = new FileWriter(accfile);
			fw.write(des.encrypt(myacc));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 获取手机号码
	 * **/
	public static TelephonyManager getPhoneManager(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// String resutl= telephonyManager.getLine1Number();
		return telephonyManager;
	}
	
	/**
	 * 复制内容到剪切板
	 * 
	 * @param context
	 * @param number
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("deprecation")
	public static void copy(Context context, String number) {
		if (Build.VERSION.SDK_INT < 11) {
			android.text.ClipboardManager clip = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			clip.setText(number);
		} else {
			ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			clip.setText(number);
		}
		Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show();
	}
	
	public static InputFilter[] inputFilter(int length){
		return new InputFilter[]{new InputFilter.LengthFilter(length)};
	}
	private static int autoLogin_time_milliseconds = -1;
	public static void setAutoLogin_time_milliseconds(
			int autoLogin_time_milliseconds) {
		PhoneTool.autoLogin_time_milliseconds = autoLogin_time_milliseconds;
	}
	public static void autoLogin(final Activity activity,int time_secends,final String type){
		autoLogin_time_milliseconds = time_secends*1000;
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (autoLogin_time_milliseconds>0) {
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
						autoLogin_time_milliseconds = -1;
						Thread.currentThread().interrupt();
					}
					autoLogin_time_milliseconds--;
					autoLogin_time_milliseconds--;
				}
				if(autoLogin_time_milliseconds==0){//登录
					MySdkApi.getMact().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							try {
								activity.finish();
							}catch (Exception e){
							}

						}
					});
					if ("guest".equals(type)){
						HttpUtils.acclogin();
					}else if("facebook".equals(type)) {
						String acc = MyGamesImpl.getSharedPreferences().getString("myths_fbid","");;
						String token = MyGamesImpl.getSharedPreferences().getString("myths_input_token","");
						HttpUtils.fblogin_check(acc,token,"");
					}
				}
			}
		}).start();
	}

	@SuppressWarnings("deprecation")
	public static boolean isTopActivity(Activity act)  
    {  
        boolean isTop = false;  
        ActivityManager am = (ActivityManager)act.getSystemService(Context.ACTIVITY_SERVICE);  
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;  
        String name = act.getClass().getName();
        if (cn.getClassName().contains(name))  
        {  
            isTop = true;  
        }  
        return isTop;  
    }

	@SuppressLint("ObjectAnimatorBinding")
	public static void auto_Login_Animator(Object ob,float start,float end,long time){
		ObjectAnimator animator=ObjectAnimator.ofFloat(ob,"rotation",start,end);
		animator.setDuration(time);
		animator.setRepeatCount(ObjectAnimator.INFINITE);
		animator.setRepeatMode(ObjectAnimator.RESTART);
		animator.start();
	}

}
