package com.dragon.navigation.UI.Activity;

import com.alibaba.fastjson.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.dragon.navigation.Control.Data;
import com.dragon.navigation.R;
import com.dragon.navigation.View.HTMLSpirit;
import com.dragon.navigation.Connect.HttpMethod;
import com.dragon.navigation.util.ToastUtil;

public class UserActivity extends Activity {
    EditText account;
    EditText textpassword;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    TextInputLayout usernameWrapper;
    TextInputLayout passwordWrapper;

    private ProgressDialog progDialog = null;// 登陆时进度条
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usernameWrapper = (TextInputLayout) findViewById(R.id.TextInputUsername);
        passwordWrapper = (TextInputLayout) findViewById(R.id.TextInputPassword);
        usernameWrapper.isInEditMode();
        passwordWrapper.isInEditMode();
        usernameWrapper.setHint("Email");
        passwordWrapper.setHint("Password");
        account=(EditText) findViewById(R.id.EditText_Account);
        textpassword=(EditText) findViewById(R.id.EditText_Password);
        Button login=(Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                hideKeyboard();

                String username = usernameWrapper.getEditText().getText().toString();
                String password = passwordWrapper.getEditText().getText().toString();
                if (!validateEmail(username)) {
                    usernameWrapper.setError("Not a valid email address!");
                   // usernameWrapper.setErrorTextAppearance();

                } else if (!validatePassword(password)) {
                    passwordWrapper.setError("Not a valid password!");
                } else {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    //doLogin();
                    MyThread1 one = new MyThread1();
                    one.flag = "0";
                    one.start();
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        });
        Button register=(Button) findViewById(R.id.btn_register);
        register.setOnClickListener(l);




        System.out.println("ip"+GetHostIp());
        Handler handler=new Handler();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//对用户名，是否为邮箱进行判定，密码长度必须大于5
    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean validatePassword(String password) {
        return password.length() > 5;
    }



    private OnClickListener l=new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_login:
                    MyThread1 one = new MyThread1();
                    one.flag = "0";
                    one.start();
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    break;
                case R.id.btn_register:
                    MyThread1 two= new MyThread1();
                    two.flag = "1";
                    two.start();
                    Message msg2 = new Message();
                    msg2.what = 5;
                    handler.sendMessage(msg2);
                    break;

            }

        }

    };


    public class MyThread1 extends Thread {
        String flag;
        @Override
        public void run() {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            postone(flag);
            System.out.println("ip"+GetHostIp());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("so quick");
            }
        }


    }

    /**
     * Created by EdwardPC on 2016/12/20.
     */



    private void postone(String flag){
        //HttpMethod.httpUrlConnPost(account.getText().toString(),textpassword.getText().toString());
        String accountstr=account.getText().toString();
        String passwordstr=textpassword.getText().toString();
        System.out.println("accountstr"+accountstr+"	passwordstr"+passwordstr+"\n");
        Map<String,String> params = new HashMap<String,String>();
        JSONObject entity=new JSONObject();

        entity.put("name",accountstr);
        entity.put("password",passwordstr);
        entity.put("flag",flag);
		/*  entity.put("name", "znbn");
		  entity.put("password", "znbp");*/
        String strUrlPath ="http://59.110.139.192/AR/server.php";
        //   String strUrlPath ="http://172.29.30.37/AR/server.php?name=梁宇";
        String strResult= HttpMethod.sendPost(strUrlPath, entity.toJSONString());
        String myanswer= HTMLSpirit.delHTMLTag(strResult);
        System.out.println("strResult"+strResult+"\n"+"myanswer"+myanswer+"\n");
        //    System.out.println(entity.toJSONString());
        //    System.out.println("postone");
       switch(myanswer){
           case "200":
               Message msg3=new Message();
               msg3.what=1;
               handler.sendMessage(msg3);
           Intent intent = new Intent(UserActivity.this,
                   Main.class);
               UserActivity.this.finish();
           startActivity(intent);
               Data.name=accountstr;
               break;
           case "110":
               Message msg=new Message();
               msg.what=3;
               handler.sendMessage(msg);
               try {
                   Thread.sleep(1000);
               }catch(InterruptedException e){
               }
               Message msg2=new Message();
               msg2.what=4;
               handler.sendMessage(msg2);
               break;
           case "666":
               System.out.println("??????");
               Message msg233=new Message();
                msg233.what=6;
               handler.sendMessage(msg233);
               try {
                   Thread.sleep(500);
               }catch(InterruptedException e){
               }
               Message msg6=new Message();
               msg6.what=4;
               handler.sendMessage(msg6);
               break;
           case "233":
               Message msg666=new Message();
               msg666.what=7;
               handler.sendMessage(msg666);
               break;

       }
//
//	      JSONObject j = JSONObject.parseObject(strResult);
        //      System.out.println(j.toString()+"HERE");
    }



    private void post(){
        System.out.println("ע��");
        Map<String,String> params = new HashMap<String,String>();

        JSONObject entity=new JSONObject();

        entity.put("barcode", "6921294398434");
        entity.put("mid", "100107550000109");

        //����������·��
        String strUrlPath ="http://cashier2.wizarpos.com/wizarposOpen-server/v1_0/product/merchandise";
        String strResult=HttpMethod.sendPost(strUrlPath, entity.toJSONString());
        System.out.println(entity.toJSONString());
        System.out.println("post�ɹ�");
        System.out.println(strResult);
        JSONObject j = JSONObject.parseObject(strResult);
        System.out.println(j.toString());


    }



    private void questmember(){

        System.out.println("��ѯ");
        JSONObject entity=new JSONObject();


        entity.put("mid", "100107550000001");

        entity.put("openId", "oC_nujvuTLA38nsAjINPeNcwSpDU"); //
        entity.put("cardNo", "99107550000100000012"); //����

        //����������·��
        String strUrlPath ="http://train.wizarpos.com/wizarposOpen-server/v1_0/wechart/info/query";
        String strResult=HttpMethod.sendPost(strUrlPath, entity.toJSONString());
        System.out.println(entity.toJSONString());
        System.out.println("post�ɹ�");
        System.out.println(strResult);
        JSONObject j = JSONObject.parseObject(strResult);
        System.out.println(j.toString());


    }
    //��ֵ��Ա��
    private void membermoney(){
        System.out.println("��ֵ");
        JSONObject entity=new JSONObject();
        entity.put("mid", "100107550000001");
        entity.put("tranAmount", "200"); //��ֵ���
        entity.put("cardNo", "99107550000100000012"); //����
        entity.put("extraAmount", "20"); //���ӽ�����
        //����������·��
        String strUrlPath ="http://train.wizarpos.com/wizarposOpen-server/v1_0/wechart/charge";
        String strResult=HttpMethod.sendPost(strUrlPath, entity.toJSONString());
        System.out.println(entity.toJSONString());
        System.out.println("post�ɹ�");
        System.out.println(strResult);
        JSONObject j = JSONObject.parseObject(strResult);
        System.out.println(j.toString());


    }

    //���Ļ�Ա����
    private void memberjifen(){
        System.out.println("���Ļ���");
        JSONObject entity=new JSONObject();
        entity.put("mid", "100107550000001");
        entity.put("tranAmount", "200"); //��ֵ���
        entity.put("cardNo", "99107550000100000012"); //����

        //����������·��
        String strUrlPath ="http://train.wizarpos.com/wizarposOpen-server/v1_0/wechart/integral/use";
        String strResult=HttpMethod.sendPost(strUrlPath, entity.toJSONString());
        System.out.println(entity.toJSONString());
        System.out.println("post�ɹ�");
        System.out.println(strResult);
        JSONObject j = JSONObject.parseObject(strResult);
        System.out.println(j.toString());


    }
    //��ѯ��Ա����
    private void memberqurey(){
        System.out.println("��ѯ����");
        JSONObject entity=new JSONObject();
        entity.put("mid", "100107550000001");
        entity.put("tranTime", "200");
        entity.put("cardNo", "0000140000001791"); //����
        entity.put("count", "2");

        //����������·��
        String strUrlPath ="http://train.wizarpos.com/wizarposOpen-server/v1_0//wechart/integral/detail/";
        String strResult=HttpMethod.sendPost(strUrlPath, entity.toJSONString());
        System.out.println(entity.toJSONString());
        System.out.println("post�ɹ�");
        System.out.println(strResult);
        JSONObject j = JSONObject.parseObject(strResult);
        System.out.println(j.toString());
    }




    //��ѯ����
    private void qureykaquan(){
        System.out.println("��ѯ����");
        JSONObject entity=new JSONObject();
        entity.put("mid", "100107550000001");
        entity.put("tranTime", "2014-12-18 18:28:54");
        entity.put("cardNo", "99107550000100000012"); //����
        entity.put("count", "2");
        //����������·��
        String strUrlPath ="http://train.wizarpos.com/wizarposOpen-server/v1_0/wechart/ticket/usedetail";
        String strResult=HttpMethod.sendPost(strUrlPath, entity.toJSONString());
        System.out.println(entity.toJSONString());
        System.out.println("post�ɹ�");
        System.out.println(strResult);
        JSONObject j = JSONObject.parseObject(strResult);
        System.out.println(j.toString());


    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public class mymessage{
        String word;
        @Override
        public String toString() {
            return word;
        }
    }

    public static String GetHostIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        } catch (Exception e) {
        }
        return null;
    }

    private void showProgressDialog(String text) {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage(text);
        progDialog.show();
    }

    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    showProgressDialog("正在登陆");
                    break;
                case 2:
                    showProgressDialog("登陆成功");
                    ToastUtil.show(UserActivity.this,"登陆成功");
                    break;
                case 3:
                    showProgressDialog("登陆失败");
                    ToastUtil.show(UserActivity.this,"账号或者密码错误");
                    break;
                case 4:
                    dissmissProgressDialog();
                    break;
                case 5:
                    showProgressDialog("正在注册");
                    break;
                case 6:
                    showProgressDialog("注册成功");
                    break;
                case 7:
                    showProgressDialog("注册失败");
                    ToastUtil.show(UserActivity.this,"用户名已经存在");
                    break;

            }
            super.handleMessage(msg);
        }

    };


}
