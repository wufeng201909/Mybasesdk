package com.sdk.mysdklibrary.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.sdk.mysdklibrary.MyApplication;
import com.sdk.mysdklibrary.MyGamesImpl;
import com.sdk.mysdklibrary.MySdkApi;
import com.sdk.mysdklibrary.Net.HttpUtils;
import com.sdk.mysdklibrary.Tools.MLog;
import com.sdk.mysdklibrary.Tools.ResourceUtil;
import com.sdk.mysdklibrary.activity.Adaper.MyGridViewAdapter;
import com.sdk.mysdklibrary.interfaces.GetorderCallBack;
import com.sdk.mysdklibrary.localbeans.OrderInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class PayActivity extends Activity implements View.OnClickListener {
    int pay_cancle_id,pay_submit_id;
    //支付列表
//    private String paydata = "[{\"url\":\"myths_mycard\",\"pay_id\":\"1\",\"remark\":\"Mycard\"}," +
//            "{\"url\":\"myths_molpay\",\"pay_id\":\"2\",\"remark\":\"MOL pay\"}," +
//            "{\"url\":\"myths_googlepay\",\"pay_id\":\"3\",\"remark\":\"Google pay\"}]";
    //只有googlepay
    private String paydata = "[{\"url\":\"myths_googlepay\",\"pay_id\":\"3\",\"remark\":\"Google pay\"}]";
    GridView gv = null;
    JSONArray jsonarr=null;
    int position=0;

    ArrayList<String[]> list_kefu = new ArrayList<String[]>();

    static IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int lay_id = ResourceUtil.getLayoutId(this,"myths_rechargeview");
        setContentView(lay_id);

        MyGamesImpl.getInstance().setSdkact(this);

        String data = getIntent().getStringExtra("data");
        //绑定google play服务
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        int gv_id = ResourceUtil.getId(this,"pay_list");
        gv = (GridView)findViewById(gv_id);
        int game_name_id = ResourceUtil.getId(this,"game_name");
        ((TextView)findViewById(game_name_id)).setText(MyApplication.getAppContext().getGameArgs().getName()+"：");
        int game_product_id = ResourceUtil.getId(this,"game_product");
        ((TextView)findViewById(game_product_id)).setText(MyApplication.getAppContext().getOrderinfo().getProductname());
        int pro_price_id = ResourceUtil.getId(this,"pro_price");
        TextView pro_price_tv = (TextView)findViewById(pro_price_id);
        pro_price_tv.setText(MyApplication.getAppContext().getOrderinfo().getAmount()+pro_price_tv.getText().toString());

        pay_cancle_id = ResourceUtil.getId(this,"pay_cancle");
        ((TextView)findViewById(pay_cancle_id)).setOnClickListener(this);
        pay_submit_id = ResourceUtil.getId(this,"pay_tv");
        ((TextView)findViewById(pay_submit_id)).setOnClickListener(this);



        try {
//            jsonarr= new JSONArray(paydata);
            jsonarr = new JSONObject(data).getJSONArray("data");
            for (int i = 0; i < jsonarr.length(); i++) {
                list_kefu.add(new String[]{jsonarr.getJSONObject(i).getString("url"),jsonarr.getJSONObject(i).getString("pay_id"),jsonarr.getJSONObject(i).getString("remark")});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final MyGridViewAdapter ad = new MyGridViewAdapter(this,list_kefu);
        gv.setAdapter(ad);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int paylistitem_id = ResourceUtil.getDrawableId(PayActivity.this,"myths_pay_choose");
//                view.setBackgroundResource(paylistitem_id);
                position = i;
                MLog.a("position-------->"+position);
                getpayorder();
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                gv.setSelection(0);
                ad.notifyDataSetChanged();
//                gv.requestFocusFromTouch();
                gv.setSelection(0);
            }
        },100);


    }

    private void getpayorder() {
        HttpUtils.getpayorder(list_kefu.get(position)[1],new GetorderCallBack() {
            @Override
            public void callback(final String orderid,final String feepoint) {
                PayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        googlepay(orderid,feepoint);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==pay_cancle_id){
            this.finish();
            MySdkApi.getMpaycallBack().payFail("pay_cancle");
        }else if (id==pay_submit_id){
            getpayorder();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            this.finish();
            MySdkApi.getMpaycallBack().payFail("pay_cancle4");
        }
        return super.onKeyDown(keyCode, event);
    }

    public  void googlepay(String orderid,String feepoint){
        try {
            Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                    feepoint, "inapp", orderid);
            int response = buyIntentBundle.getInt("RESPONSE_CODE");
            MLog.a("response----->"+response);
            if (response==0){
                PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                try {
                    startIntentSenderForResult(pendingIntent.getIntentSender(),
                            2707, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                            Integer.valueOf(0));
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }else{
                MySdkApi.getMpaycallBack().payFail("pay_error");
                this.finish();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2707) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
            MLog.a("responseCode----->"+responseCode);
            MLog.a("purchaseData----->"+purchaseData);
            if("0".equals(responseCode)){
                if (resultCode == RESULT_OK) {
                    try {
                        JSONObject jo = new JSONObject(purchaseData);
                        String sku = jo.getString("productId");
                        String order = jo.getString("developerPayload");
                        String purchaseToken = jo.getString("purchaseToken");
                        HttpUtils.consumePurchase(mService,purchaseToken);
                        HttpUtils.consumePurchaseSDK(order,purchaseData,dataSignature);
                        MySdkApi.getMpaycallBack().payFinish();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        MySdkApi.getMpaycallBack().payFail("pay_cancle3");
                    }
                    this.finish();
                }else{
                    MySdkApi.getMpaycallBack().payFail("pay_cancle2");
                    this.finish();
                }
            }else{
                MySdkApi.getMpaycallBack().payFail("pay_cancle1");
                this.finish();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }


}
