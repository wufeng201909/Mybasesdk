package com.sdk.mysdklibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdk.mysdklibrary.MyApplication;
import com.sdk.mysdklibrary.MyGamesImpl;
import com.sdk.mysdklibrary.MySdkApi;
import com.sdk.mysdklibrary.Tools.PhoneTool;
import com.sdk.mysdklibrary.Tools.ResourceUtil;

import androidx.annotation.Nullable;

public class AutoLoginActivity extends Activity implements View.OnClickListener {
    int change_acc,auto_cancle,acc_bind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int lay_id = ResourceUtil.getLayoutId(this,"myths_autoview");
        setContentView(lay_id);

        String type = getIntent().getStringExtra("type");

        int acc_id = ResourceUtil.getId(this,"acc_tv");
        change_acc = ResourceUtil.getId(this,"change_acc");
        auto_cancle = ResourceUtil.getId(this,"auto_cancle");
        acc_bind= ResourceUtil.getId(this,"acc_bind");
        int ll_lay=ResourceUtil.getId(this,"ll_lay");
        int ll_lay_face=ResourceUtil.getId(this,"ll_lay_face");
        int load_log=ResourceUtil.getId(this,"load_log");
        LinearLayout lo_bg_guest = (LinearLayout)findViewById(ll_lay);
        TextView lo_bg_facebook = (TextView)findViewById(ll_lay_face);
        TextView load_tv = (TextView)findViewById(load_log);

        TextView acc_tv = (TextView)findViewById(acc_id);
        LinearLayout change_acc_tv = (LinearLayout)findViewById(change_acc);
        TextView auto_cancle_tv = (TextView)findViewById(auto_cancle);
        TextView acc_bind_tv = (TextView)findViewById(acc_bind);
        change_acc_tv.setOnClickListener(this);
        auto_cancle_tv.setOnClickListener(this);
        acc_bind_tv.setOnClickListener(this);

        PhoneTool.auto_Login_Animator(load_tv,0.0f,720.0f,3000);

        SharedPreferences sharedPreferences = MyGamesImpl.getSharedPreferences();
        String acc="";
        if ("guest".equals(type)){
            acc_tv.setVisibility(View.VISIBLE);
            acc_bind_tv.setVisibility(View.VISIBLE);
            lo_bg_facebook.setVisibility(View.GONE);
            acc=sharedPreferences.getString("myths_youke_name","");
            CharSequence charSequence;
            String str1 = acc_tv.getText().toString()+"<font color = '#00BFFF'>"+acc+"</font>";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                charSequence = Html.fromHtml(str1, Html.FROM_HTML_MODE_LEGACY);
            } else {
                charSequence = Html.fromHtml(str1);
            }
            acc_tv.setText(charSequence);
        }else if("facebook".equals(type)){
            lo_bg_guest.setBackground(null);
            lo_bg_facebook.setVisibility(View.VISIBLE);
            acc_tv.setVisibility(View.GONE);
            acc_bind_tv.setVisibility(View.GONE);
        }
        //3秒后自动登录
        PhoneTool.autoLogin(this,3,type);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        PhoneTool.setAutoLogin_time_milliseconds(-1);
        if (id==change_acc){
            Intent itn = new Intent(this, LoginActivity.class);
            itn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(itn);
            this.finish();
        }else if (id==auto_cancle){
            this.finish();
            MySdkApi.getLoginCallBack().loginFail("login_cancle");
        }else if (id==acc_bind){
            this.finish();
            MyGamesImpl.getInstance().openfacebookLogin(MySdkApi.getMact(),MySdkApi.getLoginCallBack(),"bind");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            PhoneTool.setAutoLogin_time_milliseconds(-1);
            this.finish();
            MySdkApi.getLoginCallBack().loginFail("login_cancle");
        }
        return super.onKeyDown(keyCode, event);
    }
}
