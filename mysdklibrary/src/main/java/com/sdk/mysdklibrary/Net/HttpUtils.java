package com.sdk.mysdklibrary.Net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.android.vending.billing.IInAppBillingService;
import com.sdk.mysdklibrary.MyApplication;
import com.sdk.mysdklibrary.MyGamesImpl;
import com.sdk.mysdklibrary.MySdkApi;
import com.sdk.mysdklibrary.Tools.AESSecurity;
import com.sdk.mysdklibrary.Tools.Configs;
import com.sdk.mysdklibrary.Tools.FilesTool;
import com.sdk.mysdklibrary.Tools.MLog;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.sdk.mysdklibrary.Tools.PhoneTool;
import com.sdk.mysdklibrary.Tools.ToastUtils;
import com.sdk.mysdklibrary.activity.AutoLoginActivity;
import com.sdk.mysdklibrary.activity.MyAsdkActivity;
import com.sdk.mysdklibrary.activity.PayActivity;
import com.sdk.mysdklibrary.interfaces.GetorderCallBack;
import com.sdk.mysdklibrary.interfaces.InitCallBack;
import com.sdk.mysdklibrary.interfaces.LoginCallBack;
import com.sdk.mysdklibrary.localbeans.GameRoleBean;
import com.sdk.mysdklibrary.localbeans.OrderInfo;
import com.sdk.mysdklibrary.volley.Request.Method;
import com.sdk.mysdklibrary.volley.RequestQueue;
import com.sdk.mysdklibrary.volley.Response.ErrorListener;
import com.sdk.mysdklibrary.volley.Response.Listener;
import com.sdk.mysdklibrary.volley.VolleyError;
import com.sdk.mysdklibrary.volley.toolbox.JsonObjectRequest;
import com.sdk.mysdklibrary.volley.toolbox.JsonRequest;
import com.sdk.mysdklibrary.volley.toolbox.Volley;


public class HttpUtils {
	/** 文件下载失败 */
	public final static int FILEDOWNERR = 1;
	/** 下载重连次数 */
	public final static int RECONNECTNUM = 5;
	/** 代理地址 */
	public static Proxy mProxy = null;
	public static String cookiestat = null;
	public final static String KEY = "48fhd5748sayuh12";
	private static String sTag = "HttpUtils";

	/**
	 * 封装请求头信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public static URLConnection headMethod(String urls, int flag) {
		URL url = null;
		URLConnection urlConn = null;
		detectProxy();
		try {
			url = new URL(urls);
			if (mProxy != null) {
				urlConn = (HttpURLConnection) url.openConnection(mProxy);
			} else {
				urlConn = (HttpURLConnection) url.openConnection();
			}
		} catch (MalformedURLException e1) {
			url = null;
			e1.printStackTrace();
		} catch (IOException e) {
			urlConn = null;
			e.printStackTrace();
		}
		if (url != null && urlConn != null) {


			urlConn=addhttphead(urlConn);


		}
		return urlConn;
	}
	public static URLConnection addhttphead(URLConnection urlConn){
		urlConn.addRequestProperty("moby_auth", PhoneTool.getIEMI(PhoneTool.getIMEI(MyApplication.getAppContext())));//--参数签名,由moby_imei加密而来
		MLog.a("moby_auth："+PhoneTool.getIEMI(PhoneTool.getIMEI(MyApplication.getAppContext())));
		urlConn.addRequestProperty("moby_imei", PhoneTool.getIMEI(MyApplication.getAppContext()));//--手机串号,在ios平台下似是mac地址
		MLog.a("moby_imei："+PhoneTool.getIMEI(MyApplication.getAppContext()));
		urlConn.addRequestProperty("moby_sdk", "android");//--开发环境,表明用户手机操作系统的环境s60_5th/android/iphone
		urlConn.addRequestProperty("moby_op", PhoneTool.getProvidersName(MyApplication.getAppContext()));//--用户运营商,表明用户网络的提供商
		MLog.a("moby_op："+PhoneTool.getProvidersName(MyApplication.getAppContext()));
		urlConn.addRequestProperty("moby_ua", PhoneTool.getPT(MyApplication.getAppContext()));//--用户代理,表明用户手机硬件/软件描述
		MLog.a("moby_ua："+PhoneTool.getPT(MyApplication.getAppContext()));
		urlConn.addRequestProperty("moby_pn", PhoneTool.getCL(MyApplication.context));//手机方位
		MLog.a("moby_pn："+PhoneTool.getCL(MyApplication.getAppContext()));
		urlConn.addRequestProperty("moby_imsi", PhoneTool.getIMSI(MyApplication.context));//
		MLog.a("moby_imsi："+PhoneTool.getIMSI(MyApplication.getAppContext()));
		urlConn.addRequestProperty("moby_mac" , PhoneTool.getMac(MyApplication.context));
		MLog.a("moby_mac："+PhoneTool.getMac(MyApplication.getAppContext()));
		urlConn.addRequestProperty("moby_gameid",MyApplication.getAppContext().getGameArgs().getCpid()+MyApplication.getAppContext().getGameArgs().getGameno());
		MLog.a("moby_gameid："+MyApplication.getAppContext().getGameArgs().getCpid()+MyApplication.getAppContext().getGameArgs().getGameno());
		urlConn.addRequestProperty("moby_bv", Configs.BV);//--平台版本,表明客户端的平台版本 MLog.s("SVSV -----> " +
		MLog.a("moby_bv："+Configs.BV);
		urlConn.addRequestProperty("moby_sv", Configs.SV);//包版本,表用用户上层lua的版本
		MLog.a("moby_sv："+Configs.SV);
		urlConn.addRequestProperty("moby_pb", MyApplication.getAppContext().getGameArgs().getPublisher());//--渠道标识,表明用户来源(从哪个合作方而来)
		MLog.a("moby_pb："+MyApplication.getAppContext().getGameArgs().getPublisher());
		urlConn.addRequestProperty("moby_accid",MyApplication.getAppContext().getGameArgs().getAccount_id()); //用户账号id,用户登录返回的用户账号,如果还未登录则为空
		MLog.a("moby_accid："+MyApplication.getAppContext().getGameArgs().getAccount_id());
		urlConn.addRequestProperty("moby_sessid",MyApplication.getAppContext().getGameArgs().getSession_id());//登录会话id,全局唯一,表明用户登录的会话id
		MLog.a("moby_sessid："+MyApplication.getAppContext().getGameArgs().getSession_id());
		urlConn.addRequestProperty("cpid",MyApplication.getAppContext().getGameArgs().getCpid());//商户ID
		MLog.a("cpid："+MyApplication.getAppContext().getGameArgs().getCpid());
		urlConn.setConnectTimeout(15000); urlConn.setReadTimeout(15000);

		return urlConn;
	}
	/**
	 * 封装请求头信息
	 * 
	 * @return
	 * @throws IOException
	 */
	// public static URLConnection headWebMethod(String urls) {
	// URL url = null;
	// URLConnection urlConn = null;
	// try {
	// url = new URL(urls);
	// if(mProxy!=null){
	// urlConn=(HttpURLConnection)url.openConnection(mProxy);
	// }else{
	// urlConn = (HttpURLConnection)url.openConnection();
	// }
	// } catch (MalformedURLException e1) {
	// url = null;
	// e1.printStackTrace();
	// } catch (IOException e) {
	// urlConn = null;
	// e.printStackTrace();
	// }
	// if (url != null && urlConn != null) {
	// // 找准程序入口
	// LuaState Luastate = MyApplication.getAppContext().getmLuaState();
	// synchronized (Luastate) {
	// Luastate.getGlobal("utils");
	// int index = Luastate.getTop();
	// Luastate.getField(index, "webgamehttphead");
	// Luastate.pushJavaObject(urlConn);
	// LuaTools.dbcall(Luastate, 1, 0);
	// }
	// }
	// return urlConn;
	// }

	/**
	 * 得到最原始的连接，没有任何封装
	 * 
	 * @return
	 * @throws IOException
	 */
	// public static URLConnection headMethodRaw(String urls) {
	// URL url = null;
	// URLConnection urlConn = null;
	// detectProxy();
	// try {
	// url = new URL(urls);
	// if (mProxy != null) {
	// urlConn = (HttpURLConnection) url.openConnection(mProxy);
	// } else {
	// urlConn = (HttpURLConnection) url.openConnection();
	// }
	//
	// } catch (MalformedURLException e1) {
	// url = null;
	// e1.printStackTrace();
	// } catch (IOException e) {
	// urlConn = null;
	// e.printStackTrace();
	// }
	// return urlConn;
	// }

	/*********************************************** GET *****************************************************/
	/**
	 * GET协议请求
	 */
	public static HttpURLConnection getMethod(String urls) {
		HttpURLConnection urlConn = null;
		MLog.s(urls);
		urlConn = (HttpURLConnection) headMethod(urls, 0);
		if (urlConn != null) {
			// 设置以GET方式
			try {
				urlConn.setRequestMethod("GET");
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
			// 是否跟随重定向
			urlConn.setInstanceFollowRedirects(true);
		}
		return urlConn;
	}



	/**
	 * GET json字符串
	 * 
	 * @param urls
	 * @param params
	 * @param encode
	 * @return
	 */
	public static String getJsonString(String urls, Map<String, String> params, String encode) {
		StringBuffer buffer = new StringBuffer();
		StringBuilder pars = null;
		if (params != null && !params.isEmpty()) {
			pars = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				try {
					pars.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), encode)).append('&');
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			pars.deleteCharAt(pars.length() - 1);
		}
		if (pars != null) {
			urls += pars.toString();
		}
		HttpURLConnection urlConn = getMethod(urls);
		try {

			if (urlConn.getResponseCode() == 200) {
				String temp = null;
				InputStream in = urlConn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
				while ((temp = br.readLine()) != null) {
					buffer.append(temp);
				}
				br.close();
				in.close();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}


	/**
	 * 检查代理，是否cnwap接入
	 */
	private static void detectProxy() {
		ConnectivityManager cm = (ConnectivityManager) MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isAvailable() && ni.getType() == ConnectivityManager.TYPE_MOBILE) {
			String proxyHost = android.net.Proxy.getDefaultHost();
			int port = android.net.Proxy.getDefaultPort();
			MLog.s("proxyHost =========> " + proxyHost);
			MLog.s("proxyPort =========> " + port);
			if (proxyHost != null) {
				final InetSocketAddress sa = new InetSocketAddress(proxyHost, port);
				mProxy = new Proxy(Proxy.Type.HTTP, sa);
				return;
			}
		}
		mProxy = null;
	}


	/************************************************** POST ****************************************************/

	/**
	 * POST协议请求
	 */
	public static String postMethod(String urls, Map<String, String> params, String encode) {
		HttpURLConnection urlConn = null;
		MLog.s("POST ARGS: \n" + params);
		StringBuffer buffer = new StringBuffer();
		StringBuilder pars = null;
		if (params != null && !params.isEmpty()) {
			pars = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				try {
					pars.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), encode)).append('&');
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			pars.deleteCharAt(pars.length() - 1);
		}
		try {
			urlConn = (HttpURLConnection) headMethod(urls, 0);
			// 因为这个是post请求,设立需要设置为true
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			// 设置以POST方式
			urlConn.setRequestMethod("POST");

			// Post 请求不能使用缓存
			urlConn.setUseCaches(false);
			// 是否跟随重定向
			urlConn.setInstanceFollowRedirects(true);

			/****************************** 提交配置 **************************************/
			// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
			// 要注意的是connection.getOutputStream会隐含的进行connect。
			// urlConn.connect();
			// DataOutputStream流

			if (pars != null && !pars.equals("")) {
				byte[] by = pars.toString().getBytes();
				// byte[] by = AESSecurity.encrypt(pars.toString(),
				// MyApplication.getAppContext().getGameArgs().getKey()).getBytes();
				// 配置数据长度
				urlConn.setRequestProperty("Content-Length", String.valueOf(by.length));
				// 参数输出流
				DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
				// 将要上传的内容写入流中
				out.write(by);
				// 刷新、关闭
				out.flush();
				out.close();
			}

			if (urlConn.getResponseCode() == 200) {
				String temp = null;
				InputStream in = urlConn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
				while ((temp = br.readLine()) != null) {
					buffer.append(temp);
				}

				br.close();
				in.close();
			}
		} catch (ConnectTimeoutException e) {// 连接超时
			e.printStackTrace();
			return null;
		} catch (SocketTimeoutException e) {// 读取超时
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return buffer.toString();
	}

	// 显示网络上的图片
	public static Bitmap getbitmap(String imageUri) {
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 写入文件
	 */
	public static void method1(String conent) {
		File dr = new File(Configs.ASDKROOT + "tmp/");
		if (!dr.exists()) {
			dr.mkdirs();
		}
		File file = new File(dr + File.separator + "响应时间.txt");
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			out.write(conent + "\r\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * POST协议请求
	 */
	public synchronized static String postMethod(String urls, String params, String encode) {
		HttpURLConnection urlConn = null;
		MLog.a(sTag,"Start  POST------>  " + urls);
		MLog.a(sTag,"POST ARGS: ------>  " + params);
		// 判断是否使用以下3个协议
		boolean isconstant = false;
		if (urls.contains("update") || urls.contains("getaddr") || urls.contains("submitbug") || urls.contains("replace")) {
			isconstant = true;
		} else {
			isconstant = false;
		}

		StringBuffer buffer = new StringBuffer();
		try {
			urlConn = (HttpURLConnection) headMethod(urls, 0);
			// 因为这个是post请求,设立需要设置为true
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			// 设置以POST方式
			urlConn.setRequestMethod("POST");

			// Post 请求不能使用缓存
			urlConn.setUseCaches(false);
			// 是否跟随重定向
			urlConn.setInstanceFollowRedirects(true);

			/****************************** 提交配置 **************************************/
			// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
			// 要注意的是connection.getOutputStream会隐含的进行connect。
			// urlConn.connect();
			// DataOutputStream流
			if (params != null && !params.equals("")) {
				if (isconstant) {
					params = AESSecurity.constantEncryptionResult(params, KEY);
				} else {
					params = AESSecurity.encryptionResult(params);
				}
				byte[] by = params.getBytes();
				// byte[] by = AESSecurity.encrypt(params,
				// MyApplication.getAppContext().getGameArgs().getKey()).getBytes();

				// 配置数据长度
				urlConn.setRequestProperty("Content-Length", String.valueOf(by.length));
				// 参数输出流
				DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
				// 将要上传的内容写入流中
				out.write(by);
				// 刷新、关闭
				out.flush();
				out.close();
			}

			int result = urlConn.getResponseCode();
			
			if (result == 200) {
				String temp = null;
				InputStream in = urlConn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
				while ((temp = br.readLine()) != null) {
					buffer.append(temp);
				}
				br.close();
				in.close();
			} else {
				return "exception" + result;
			}
		} catch (ConnectTimeoutException e) {// 连接超时
			e.printStackTrace();
			return "exception ConnectTimeout";
		} catch (SocketTimeoutException e) {// 读取超时
			e.printStackTrace();
			return "exception SocketTimeout";
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "exception MalformedURL";
		} catch (IOException e) {
			e.printStackTrace();
			return "exception IO";
		} catch (Exception e) {
			e.printStackTrace();
			return "exception";
		}
		MLog.s("End Http Request");
		MLog.s("result："+buffer.toString());
		if (buffer.toString() == null || buffer.toString().contains("error") || buffer.toString().contains("exception") || buffer.toString().contains("Fatal")) {
			MLog.a(sTag,buffer.toString());
			return "exception Net";
		}
		if (isconstant) {
			return AESSecurity.constantdecryptResult(buffer.toString(), KEY);
		}
		return AESSecurity.decryptResult(buffer.toString());
	}

	/**
	 * POST协议请求
	 */
	public static String postWebMethod(String urls, int flag) {
		MLog.s("post请求 5");
		HttpURLConnection urlConn = null;
		StringBuffer buffer = new StringBuffer();
		try {
			urlConn = (HttpURLConnection) headMethod(urls, flag);
			// 因为这个是post请求,设立需要设置为true
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			// 设置以POST方式
			urlConn.setRequestMethod("POST");

			// Post 请求不能使用缓存
			urlConn.setUseCaches(false);
			// 是否跟随重定向
			urlConn.setInstanceFollowRedirects(true);

			/****************************** 提交配置 **************************************/
			urlConn.setRequestProperty("Content-Type", "text/plain");

			byte[] by = "900 Success".getBytes();
			// 配置数据长度
			// urlConn.setRequestProperty("Content-Length",String.valueOf(by.length));
			// 参数输出流
			DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());
			// 将要上传的内容写入流中
			out.write(by);
			// 刷新、关闭
			out.flush();
			out.close();

			// 已主动发起response了
			// MLog.s("post请求 52----> "+urlConn.getContentType());

			int result = urlConn.getResponseCode();
			if (result == 200) {
				String temp = null;
				InputStream in = urlConn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
				while ((temp = br.readLine()) != null) {
					buffer.append(temp);
				}
				br.close();
				in.close();
			} else {
				return "error" + result;
			}
		} catch (ConnectTimeoutException e) {// 连接超时
			e.printStackTrace();
			return "exception ConnectTimeout";
		} catch (SocketTimeoutException e) {// 读取超时
			e.printStackTrace();
			return "exception SocketTimeout";
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "exception MalformedURL";
		} catch (IOException e) {
			e.printStackTrace();
			return "exception IO";
		}
		MLog.s("postresult--->" + buffer.toString());
		if (buffer.toString() == null || buffer.toString() == "") {
			return buffer.toString();
		}
		return AESSecurity.decryptResult(buffer.toString());
	}

	
	
	static RequestQueue requestQueue = null;
	/**
	 * http对外发送接口
	 * 
	 * @param urls
	 * @param params
	 * @param encode
	 */
	public static void startPost(final String urls, final String params, String encode) {
		MLog.s("Start Http Request");
		MLog.a("MySDK", urls);
		MLog.a("MySDK", params);
//		String publisher = FilesTool.getPublisherStringContent();
		if(true){
			// 判断是否使用以下3个协议
			boolean isconstant = false;
			if (urls.contains("update") || urls.contains("getaddr") || urls.contains("submitbug")) {
				isconstant = true;
			} else {
				isconstant = false;
			}
			String params_ =null;
			if (params != null && !params.equals("")) {
				if (isconstant) {
					params_ = AESSecurity.constantEncryptionResult(params, KEY);
				} else {
					params_ = AESSecurity.encryptionResult(params);
				}
			}
			
			requestQueue = (requestQueue ==null)? Volley.newRequestQueue(MyApplication.context):requestQueue;
			JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(isconstant,Method.POST, urls, params_, new Listener<JSONObject>() {

				@Override
				public void onResponse(JSONObject response) {
					callback(urls,response.toString(),params);
				}

			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError error) {
					callback(urls,"exception",params);
				}
			});
			requestQueue.add(jsonRequest);
		}else{
//			HttpAsynTask async = new HttpAsynTask();
//			async.execute(urls, params, encode);
		}
	}
	
	private static void callback(String urls, String response, String params) {
		String flag = null;
		if (urls.contains("=")) {
			int sh = urls.lastIndexOf("=");
			flag = urls.substring(sh + 1, urls.length());
		} else {
			flag = "httptest";
		}

		analyseData(flag, response, params);
//		synchronized (MyApplication.getAppContext().getmLuaState()) {
//			MyApplication.getAppContext().httpback.httpcallback(flag, response, params);
//		}
	}

	private static void analyseData(String flag, String response, String params) {
		PhoneTool.disDialog();

		boolean errorweb=false;
		if (response==null||response.equals("")||response.contains("error")||response.contains("exception")||response.contains("Fatal")){
			errorweb = true;
		}
		if(errorweb){
			if (flag.contains("paylist")||flag.contains("asdkpay")){
				MySdkApi.getMpaycallBack().payFail("data_error");
				MySdkApi.getMact().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Activity act = MyGamesImpl.getInstance().getSdkact();
						if (act!=null){
							act.finish();
						}
					}
				});
			}
		}else if (flag.equals("paylist")){
			JSONObject data = null;
			try {
				data = new JSONObject(response);
				String code = data.getString("code");
				if (code.equals("0")){
//					JSONArray result = data.getJSONArray("data");
					gotoPayActivity(response);
				}else if (code.equals("-1")){
					MySdkApi.getMpaycallBack().payFail(data.getString("msg"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}else if (flag.equals("asdkpay")){
			JSONObject data = null;
			try {
				data = new JSONObject(response);
				String code = data.getString("code");
				if (code.equals("0")){
//					JSONArray result = data.getJSONArray("data");
					String orderid = data.getJSONObject("data").getString("orderid");
					String feepoint = "";
					try{
						feepoint = data.getJSONObject("data").getString("feepoint");
					} catch (JSONException e){
						e.printStackTrace();
					}
					try{
						Configs.gp_url = data.getJSONObject("data").getString("url");
					} catch (JSONException e){
						e.printStackTrace();
					}

					m_callBack.callback(orderid,feepoint);
				}else if (code.equals("-1")){
					MySdkApi.getMpaycallBack().payFail(data.getString("msg"));
					MySdkApi.getMact().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Activity act = MyGamesImpl.getInstance().getSdkact();
							if (act!=null){
								act.finish();
							}
						}
					});
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private static void gotoPayActivity(final String data){
		MySdkApi.getMact().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				PhoneTool.disDialog();
				Intent itn = new Intent(MySdkApi.getMact(), PayActivity.class);
				itn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				itn.putExtra("data",data);
				MySdkApi.getMact().startActivity(itn);
			}
		});

	}


	public static int getAppCode(){
		try {
			PackageInfo info = MyApplication.context.getPackageManager().getPackageInfo(MyApplication.context.getPackageName(), 0);
			return info.versionCode; // 版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	public static void sendRequestWithHttpURLConnection(final int step) 
	{
		new Thread(new Runnable() 
		{
		    @Override
		    public void run() 
		    {
		    	try 
		    	{
		    		String url = "";
		    		String Publisher = FilesTool.getPublisherStringContent();
		    		if(Publisher.startsWith("asdk")&&Publisher.contains("zbsg")){
		    			url = "http://sg2log.gotechgames.com:5201/ActiveGame";
		    		}else if(Publisher.startsWith("huawei")&&Publisher.contains("xxsg2")){
		    			url = "http://sg2log.gotechgames.com:5201/ActiveGame";
		    		}
		    		URL myUrl = new URL(url);
		    		HttpURLConnection httpURLConnection = (HttpURLConnection)myUrl.openConnection();
		    		 
		    		 httpURLConnection.setRequestMethod("POST");
		    		 httpURLConnection.setDoOutput(true);
		    		 httpURLConnection.setDoInput(true);
		    		 httpURLConnection.setUseCaches(false);
		    		 httpURLConnection.setConnectTimeout(10000);
		    		 httpURLConnection.setReadTimeout(10000);
		    		 httpURLConnection.setRequestProperty("Content-type","application/x-www-form-urlencoded");
		    		 httpURLConnection.connect();
		    		 OutputStream outputStream = httpURLConnection.getOutputStream();
		    		 String param = String.format("channel=%s&deviceId=%s&step=%d&version=%s&sign=%s", getChannel(), getAndroidID(), step,getAppVersion(),"");
		    		 byte[] postData = param.getBytes();
		    		 DataOutputStream dos = new DataOutputStream(outputStream);
		    		 dos.write(postData);
		             dos.flush();
		             dos.close();
		             if (httpURLConnection.getResponseCode() == 200) 
		             {
		            	 Log.i("Unity","Active Step OK");
		             }
		             else
		             {
		            	 Log.i("Unity",String.format("Active Step OK Fail %d",httpURLConnection.getResponseCode()));
		             }
		             httpURLConnection.disconnect();
				} 
		    	catch (Exception e) 
				{
		    		Log.i("Unity", e.toString());
				}
		    }
		}).start();
	}
	public static String getChannel()
	{
		String channel = "";
		PackageManager pm = MyApplication.context.getPackageManager();
		ApplicationInfo appInfo = null;
		try
		{
			appInfo = pm.getApplicationInfo(MyApplication.context.getPackageName(), PackageManager.GET_META_DATA);
			channel = appInfo.metaData.getString("DC_CHANNEL");
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return channel;
	}
	public static String m_AndriodID = null;
	public static String getAndroidID() 
    {
		if(m_AndriodID == null)
		{
			String szAndroidID = Secure.getString(MyApplication.context.getContentResolver(), Secure.ANDROID_ID);
			m_AndriodID = szAndroidID;
		}
		return m_AndriodID;
    }
	public static String getAppVersion() {
		String versionName = "";
		try {
			PackageManager pm = MyApplication.context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(MyApplication.context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "0.0.0";
			}
		} catch (Exception e) {
			Log.e("getAppVersion", "getAppVersion Exception", e);
		}
		return versionName;
	}

	/**
	 * 获取url中文件名
	 *
	 * @param urls
	 * @return
	 */
	public static String getUrlFileName(String urls) {
		String name = urls;
		if (name.contains("/")) {
			String[] arr = urls.split("/");
			name = arr[arr.length - 1];
		}
		if (name.contains("?")) {
			name = name.split("\\?")[0];
		}
		return name;
	}

	public static void checkupnet(final Activity context, final InitCallBack callBack) {
		//网络请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				int appcode= HttpUtils.getAppCode();
				JSONObject param = null;
				String param_json=  "";
				try {
					param = new JSONObject();
					param.put("version_appVersion", appcode+"");
				} catch (JSONException e2) {
					e2.printStackTrace();
				}
				param_json = param.toString();
				MLog.a("param_json:"+param_json);
				String checkupdata = HttpUtils.postMethod(Configs.initurl+"?gameparam=replace", param_json, "utf-8");
				if(checkupdata!=null){
					if(checkupdata.contains("error")||checkupdata.contains("Fatal")||checkupdata.contains("exception")){
						if (Configs.initurl.equals(Configs.initurl1)){
							Configs.initurl = Configs.initurl2;
							checkupnet(context,callBack);
						}
					}else{
						JSONObject jo1 = null;
						try {
							jo1 = new JSONObject(checkupdata);
							if ("4".equals(jo1.getString("code"))) {
								MLog.a("has no update");
								initsdk(context,callBack);
							}else if ("3".equals(jo1.getString("code"))){
								Intent itn = new Intent(context, MyAsdkActivity.class);
								itn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								Bundle bu = new Bundle();
								bu.putInt("result", 3);
								itn.putExtras(bu);
								context.startActivity(itn);
							}else{
								callBack.initSuccess(false);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}else{
					callBack.initSuccess(false);
				}
			}
		}).start();

	}

	public static void initsdk(final Activity context, final InitCallBack callBack) {
		String initdata = HttpUtils.postMethod(Configs.initurl+"?gameparam=getaddr", "", "utf-8");
		if(initdata!=null){
			if(initdata.contains("error")||initdata.contains("Fatal")||initdata.contains("exception")){
				callBack.initSuccess(false);
			}else{
				JSONObject initjs = null;
				try {
					initjs = new JSONObject(initdata);
					Configs.accountserver = initjs.getJSONObject("data").getString("accsvrurl");
					Configs.payserver = initjs.getJSONObject("data").getString("payurl");

					Configs.othersdkextdata1=initjs.getJSONObject("data").getString("othersdkextdata1");
					Configs.othersdkextdata2=initjs.getJSONObject("data").getString("othersdkextdata2");
					Configs.othersdkextdata3=initjs.getJSONObject("data").getString("othersdkextdata3");
					Configs.othersdkextdata4=initjs.getJSONObject("data").getString("othersdkextdata4");
					Configs.othersdkextdata5=initjs.getJSONObject("data").getString("othersdkextdata5");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				callBack.initSuccess(true);
			}
		}else{
			callBack.initSuccess(false);
		}
	}

	public static void fastlogin(final Activity context) {
		PhoneTool.onCreateDialog(context,"温馨提示","正在请求````");

		new Thread(new Runnable() {
			@Override
			public void run() {
				SharedPreferences sharedPreferences =MyGamesImpl.getSharedPreferences();
				String name = sharedPreferences.getString("myths_youke_name","");
				if (!name.equals("")){
					gotoAutoLoginActivity("guest");
					return;
				}
				String fastdata = HttpUtils.postMethod(Configs.accountserver+"gameparam=accautoreg", "", "utf-8");
				JSONObject fastdatajs = null;
				try {
					fastdatajs = new JSONObject(fastdata);
					if ("0".equals(fastdatajs.getString("code"))){
						String account=fastdatajs.getJSONObject("data").getString("account");
						String password=fastdatajs.getJSONObject("data").getString("password");

						sharedPreferences.edit().putString("myths_youke_name",account).commit();
						sharedPreferences.edit().putString("myths_youke_password",password).commit();

						acclogin();

					}else {
						String msg=fastdatajs.getString("msg");
						ToastUtils.Toast(msg);
						MySdkApi.getLoginCallBack().loginFail(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					MySdkApi.getLoginCallBack().loginFail("");
				}
				PhoneTool.disDialog();
			}
		}).start();

	}

	/**
	 *
	 * @param type//"guest"游客自动登录；"facebook"facebook自动登录
	 */
	public static void gotoAutoLoginActivity(final String type){
		MySdkApi.getMact().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				PhoneTool.disDialog();
				Intent itn = new Intent(MySdkApi.getMact(), AutoLoginActivity.class);
				itn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				itn.putExtra("type",type);
				MySdkApi.getMact().startActivity(itn);
			}
		});

	}

	public static void acclogin() {
		PhoneTool.onCreateDialog(MySdkApi.getMact(),"温馨提示","正在请求````");
		new Thread(new Runnable() {
			@Override
			public void run() {
				String name = MyGamesImpl.getSharedPreferences().getString("myths_youke_name","");
				String password = MyGamesImpl.getSharedPreferences().getString("myths_youke_password","");
				JSONObject param = null;
				String param_json=  "";
				try {
					param = new JSONObject();
					param.put("username", name);
					param.put("userpassword", password);
				} catch (JSONException e2) {
					e2.printStackTrace();
				}
				param_json = param.toString();
				String logindata = HttpUtils.postMethod(Configs.accountserver+"gameparam=acclogin", param_json, "utf-8");
				JSONObject logindatajs = null;
				try {
					logindatajs = new JSONObject(logindata);
					if ("0".equals(logindatajs.getString("code"))){
						String accountid = logindatajs.getJSONObject("data").getJSONObject("account").getString("accountid");
						String sessionid = logindatajs.getJSONObject("data").getJSONObject("account").getString("sessionid");
						MyGamesImpl.getSharedPreferences().edit().putString("myths_youke_accountid",accountid).commit();
						//保存登录类型用于自动登录
						MyGamesImpl.getSharedPreferences().edit().putString("myths_auto_type","guest").commit();

						MySdkApi.getLoginCallBack().loginSuccess(accountid,sessionid);
						//保存登录信息
						MyApplication.getAppContext().getGameArgs().setAccount_id(accountid);
						MyApplication.getAppContext().getGameArgs().setSession_id(sessionid);
					}else{
						String msg=logindatajs.getString("msg");
						ToastUtils.Toast(msg);
						MySdkApi.getLoginCallBack().loginFail(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					MySdkApi.getLoginCallBack().loginFail("");
				}
				PhoneTool.disDialog();
			}
		}).start();;

	}
	//fblogin
	public static void fblogin_check(final String uid ,final String token,final String type) {
		PhoneTool.onCreateDialog(MySdkApi.getMact(),"温馨提示","正在请求````");
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject param = null;
				String param_json=  "";
				String url = Configs.accountserver+"gameparam=setfbaccount";
				try {
					param = new JSONObject();
					param.put("fbid", uid);
					param.put("input_token", token);
					if ("bind".equals(type)){
						url = Configs.accountserver+"gameparam=guesttofbaccount";
						String name = MyGamesImpl.getSharedPreferences().getString("myths_youke_name","");
						String accountid = MyGamesImpl.getSharedPreferences().getString("myths_youke_accountid","");
						param.put("accountid", accountid);
						param.put("guestusername", name);
					}
				} catch (JSONException e2) {
					e2.printStackTrace();
				}
				param_json = param.toString();
				String fblogindata = HttpUtils.postMethod(url, param_json, "utf-8");
				JSONObject fbloginjs = null;
				try {
					fbloginjs = new JSONObject(fblogindata);
					String code= fbloginjs.getString("code");
					if ("0".equals(code)||"1".equals(code)){//1表示已有facebook账号，绑定失败，直接登录facebook账号
						if ("bind".equals(type)){
							if ("1".equals(code)){
								String msg=fbloginjs.getString("msg");
								ToastUtils.Toast(msg);
							}else{//绑定facebook成功，清楚游客信息
								MyGamesImpl.getSharedPreferences().edit().putString("myths_youke_name","").commit();
								MyGamesImpl.getSharedPreferences().edit().putString("myths_youke_password","").commit();
								MyGamesImpl.getSharedPreferences().edit().putString("myths_youke_accountid","").commit();
							}
						}

						//保存facebook登录信息
						MyGamesImpl.getSharedPreferences().edit().putString("myths_fbid",uid).commit();
						MyGamesImpl.getSharedPreferences().edit().putString("myths_input_token",token).commit();

						//保存登录类型用于自动登录
						MyGamesImpl.getSharedPreferences().edit().putString("myths_auto_type","facebook").commit();

						String accountid = fbloginjs.getJSONObject("data").getString("accountid");
						String sessionid = fbloginjs.getJSONObject("data").getString("sessionid");
						MySdkApi.getLoginCallBack().loginSuccess(accountid,sessionid);

						//保存登录信息
						MyApplication.getAppContext().getGameArgs().setAccount_id(accountid);
						MyApplication.getAppContext().getGameArgs().setSession_id(sessionid);

					} else{
						String msg=fbloginjs.getString("msg");
						ToastUtils.Toast(msg);
						MySdkApi.getLoginCallBack().loginFail(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					MySdkApi.getLoginCallBack().loginFail("");
				}
				PhoneTool.disDialog();
			}
		}).start();;
	}

	//您必须对应用内商品发送消耗请求，然后 Google Play 才能允许再次购买
	public static void consumePurchase(final IInAppBillingService mService,final String purchaseToken) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int response = mService.consumePurchase(3, MyApplication.context.getPackageName(), purchaseToken);
					MLog.a("consumePurchase------>"+response);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void getPayList() {
		HttpUtils.startPost(Configs.payserver+"gameparam=paylist","","utf-8");
		PhoneTool.onCreateDialog(MySdkApi.getMact(),"温馨提示","正在请求````");
	}

	static GetorderCallBack m_callBack;
	public static void getpayorder(String paytypeid, GetorderCallBack callBack) {
		m_callBack=callBack;
		JSONObject param = null;
		String param_json=  "";
		OrderInfo orderinfo=MyApplication.getAppContext().getOrderinfo();
		try {
			param = new JSONObject();
			param.put("sum", orderinfo.getAmount());
			param.put("callbackurl", orderinfo.getPayurl());
			param.put("paytypeid",paytypeid);
			param.put("customorderid", orderinfo.getTransactionId());
			param.put("accountid", MyApplication.getAppContext().getGameArgs().getAccount_id());
			param.put("custominfo", orderinfo.getExtraInfo());
			param.put("desc", orderinfo.getProductname());
			param.put("bundleid", MyApplication.context.getPackageName());
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		param_json = param.toString();
		PhoneTool.onCreateDialog(MyGamesImpl.getInstance().getSdkact(),"温馨提示","正在请求````");
		HttpUtils.startPost(Configs.payserver+"gameparam=asdkpay",param_json,"utf-8");

	}

	//gp支付验证
	public static void consumePurchaseSDK(final String order,final String purchaseData, final String dataSignature) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject param_js = new JSONObject();
				try {
					param_js.put("orderid", order);
					param_js.put("ticket", purchaseData);
					param_js.put("sign", dataSignature);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				String param = param_js.toString();
				final String result = HttpUtils.postMethod(Configs.gp_url, param, "utf-8");
				MLog.a(result);
			}
		}).start();;
	}

	//上报角色信息
	public static void submitRoleData(final int operator, final GameRoleBean gameRoleBean) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject param_js = new JSONObject();
				try {
					param_js.put("serverId", gameRoleBean.getGameZoneId());
					param_js.put("serverName", gameRoleBean.getGameZoneName());
					param_js.put("roleId", gameRoleBean.getRoleId());
					param_js.put("roleName", gameRoleBean.getRoleName());
					param_js.put("roleLevel", gameRoleBean.getRoleLevel());
					param_js.put("vipLevel", gameRoleBean.getVipLevel());
					param_js.put("RoleCTime", gameRoleBean.getRoleCTime());
					param_js.put("operator", operator);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				String param = param_js.toString();
				final String result = HttpUtils.postMethod(Configs.accountserver+"gameparam=usergameinfo", param, "utf-8");
				MLog.a(result);
			}
		}).start();
	}
}
