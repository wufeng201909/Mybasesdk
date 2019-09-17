package com.sdk.mysdklibrary.Tools;

public class Configs {
	/** 随版本发布的BV */
	public static String BV = "20190909";
	/** 随版本发布的BV */
	public static String SV = "20190909";
	/** use XPKG */
	public static boolean USEXPKG = true;
	/** 要解析数据头 */
	public static boolean ALLUSED = true;
	/** 数据包目录 */
	public static String ASDK = "outsea_sdk";
	/** 数据包目录 */
//	public static String ASDK = "out_sdk";
	/** SD是否存在 */
	public static boolean SDEXIST = false;
	/** SD剩余大小 */
	public static long SDSIZE = 0;
	/** ASDK根目录 */
	public static String ASDKROOT = null;
	/** 注册成功码 */
	public static int REGISTERSUCCESS = 100;
	/** 注册失败码 */
	public static int REGISTERFAILURE = 101;
	/** 登陆成功码 */
	public static int LOGINSUCCESS = 102;
	/** 登陆失败码 */
	public static int LOGINFAILURE = 103;
	/** 充值成功码 */
	public static int CHARGESUCCESS = 104;
	/** 充值失败码 */
	public static int CHARGEAILURE = 105;
	/** 8种基本数据类型 */
	public static Class<Double> tdouble = Double.TYPE;
	public static Class<Float> tfloat = Float.TYPE;
	public static Class<Long> tlong = Long.TYPE;
	public static Class<Integer> tint = Integer.TYPE;
	public static Class<Short> tshort = Short.TYPE;
	public static Class<Character> tchar = Character.TYPE;
	public static Class<Byte> tbyte = Byte.TYPE;
	public static Class<Boolean> tboolean = Boolean.TYPE;
	final public static String initurl1 = "http://updatehk.mythsgame.com:8082/init.php";
	final public static String initurl2 = "http://updatehk.mythsgames.com:8082/init.php";
	public static String initurl = initurl1;
	/** 对象类型 */
	public static Class<String> tstring = String.class;

    public static String accountserver="";
	public static String payserver="";
	public static String othersdkextdata1="";
	public static String othersdkextdata2="";
	public static String othersdkextdata3="";
	public static String othersdkextdata4="";
	public static String othersdkextdata5="";
	public static String gp_url="";
}
