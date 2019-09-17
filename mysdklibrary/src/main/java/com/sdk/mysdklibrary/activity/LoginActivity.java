package com.sdk.mysdklibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.sdk.mysdklibrary.MySdkApi;
import com.sdk.mysdklibrary.Net.HttpUtils;
import com.sdk.mysdklibrary.Tools.Configs;
import com.sdk.mysdklibrary.Tools.PhoneTool;
import com.sdk.mysdklibrary.Tools.ResourceUtil;

import androidx.annotation.Nullable;

public class LoginActivity extends Activity implements View.OnClickListener {
    int cancle_id,fastlogin_id,fblogin_id,policy_con_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int lay_id = ResourceUtil.getLayoutId(this,"myths_loginview");
        setContentView(lay_id);
        cancle_id = ResourceUtil.getId(this,"myths_cancle");
        fastlogin_id = ResourceUtil.getId(this,"fastlogin_icon");
        fblogin_id = ResourceUtil.getId(this,"fblogin_icon");
        policy_con_id = ResourceUtil.getId(this,"policy_con");

        TextView cancle = (TextView)findViewById(cancle_id);
        TextView fastlogin = (TextView)findViewById(fastlogin_id);
        TextView fblogin = (TextView)findViewById(fblogin_id);
        TextView policy = (TextView)findViewById(policy_con_id);
        cancle.setOnClickListener(this);
        fastlogin.setOnClickListener(this);
        fblogin.setOnClickListener(this);
        policy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==cancle_id){//关闭
            LoginActivity.this.finish();
            MySdkApi.getLoginCallBack().loginFail("login_cancle");
        }else if (id==fastlogin_id){//快速进入
            HttpUtils.fastlogin(MySdkApi.getMact());
            this.finish();
        }else if (id==fblogin_id) {//facebook
            MySdkApi.chooseLogin(MySdkApi.getMact(),MySdkApi.getLoginCallBack(),2);
            this.finish();
        }else if (id==policy_con_id) {
            Intent intent = new Intent(this,YXWebActivity.class);
            intent.putExtra("user_protocol_url", Configs.othersdkextdata1);
            this.startActivity(intent);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            this.finish();
            MySdkApi.getLoginCallBack().loginFail("login_cancle");
        }
        return super.onKeyDown(keyCode, event);
    }
}
