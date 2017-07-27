/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2014 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

package com.dragon.navigation.UI.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dragon.navigation.R;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;


public class ActivitySplashScreen extends Activity
{
//    启动页延迟显示时间
    private static long SPLASH_MILLIS = 5950;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        无标题并全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        inflate就相当于将一个xml中定义的布局找出来，对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
            R.layout.splash_screen, null, false);
        
        addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT));
//        作用：接收子线程发送来的消息并配合主线程来更新UI


             initPermissionGen();


    }

   void initPermissionGen() {

       //Android 6.0权限管理
       PermissionGen.with(this)
               .addRequestCode(200)
               .permissions(
                       //电话通讯录
                       Manifest.permission.GET_ACCOUNTS,
                       Manifest.permission.READ_PHONE_STATE,
                       //位置
                       Manifest.permission.ACCESS_FINE_LOCATION,
                       Manifest.permission.ACCESS_COARSE_LOCATION,
                       Manifest.permission.ACCESS_FINE_LOCATION,
                       //相机、麦克风
                       Manifest.permission.RECORD_AUDIO,
                       Manifest.permission.WAKE_LOCK,
                       Manifest.permission.CAMERA,
                       //存储空间
                       Manifest.permission.WRITE_EXTERNAL_STORAGE,
                       Manifest.permission.WRITE_SETTINGS
               )
               .request();
   }
    @PermissionSuccess(requestCode=200)
    public void successOpenCamera(){

        //Dlog.debug("open camera success");
        Toast.makeText(this,"open camera success",Toast.LENGTH_SHORT).show();

    }
    @PermissionFail(requestCode=200)
    public void failOpenCamera(){
        Toast.makeText(this,"Camera permission is not granted",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                           int[] grantResults) {

        PermissionGen.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(ActivitySplashScreen.this,
                        SplashActivity.class);
                startActivity(intent);
            }

        }, SPLASH_MILLIS);
    }



}
