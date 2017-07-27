package com.dragon.navigation.UI.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dragon.navigation.R;

/**
 * This file created by dragon on 2016/9/19 9:13,
 * belong to com.dragon.navigation .
 * 引导页面构建
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
//        是否首次进入
    boolean mFirst = isFirstEnter(SplashActivity.this, SplashActivity.this.getClass().getName());
    if(mFirst)
            mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY,5000);
    else
            mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY,5000);
    }
    //****************************************************************
    // 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
    //****************************************************************
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private boolean isFirstEnter(Context context, String className){
        if(context==null || className==null||"".equalsIgnoreCase(className))
            return false;
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
                .getString(KEY_GUIDE_ACTIVITY, "");//取得所有类名 如 com.my.MainActivity
        if(mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }
//    Handler
    private final static int SWITCH_MAINACTIVITY = 1000;
    private final static int SWITCH_GUIDACTIVITY = 1001;
    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case SWITCH_MAINACTIVITY:
                    Intent mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this,UserActivity.class);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();
                    break;
                case SWITCH_GUIDACTIVITY:
                    mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this,GuideActivity.class);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
