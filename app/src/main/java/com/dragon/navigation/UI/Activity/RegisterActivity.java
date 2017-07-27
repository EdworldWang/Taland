package com.dragon.navigation.UI.Activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dragon.navigation.R;
import com.dragon.navigation.UI.Base.BaseActivity;
import com.dragon.navigation.UI.Presenter.RegisterAtPresenter;
import com.dragon.navigation.UI.View.IRegisterAtView;
import com.dragon.navigation.util.UIUtils;

import java.util.HashMap;

import butterknife.BindView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;


public class RegisterActivity extends BaseActivity<IRegisterAtView, RegisterAtPresenter> implements IRegisterAtView {

    @BindView(R.id.etNick)
    EditText mEtNick;
    @BindView(R.id.vLineNick)
    View mVLineNick;

    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.vLinePhone)
    View mVLinePhone;

    @BindView(R.id.etPwd)
    EditText mEtPwd;
    @BindView(R.id.ivSeePwd)
    ImageView mIvSeePwd;
    @BindView(R.id.vLinePwd)
    View mVLinePwd;

    @BindView(R.id.etVerifyCode)
    EditText mEtVerifyCode;
    @BindView(R.id.btnSendCode)
    Button mBtnSendCode;
    @BindView(R.id.vLineVertifyCode)
    View mVLineVertifyCode;

    @BindView(R.id.btnRegister)
    Button mBtnRegister;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mBtnRegister.setEnabled(canRegister());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void initListener() {
//        SMSSDK.initSDK(RegisterActivity.this, "1dbe108d9bf00", "480cdb08f1c1c6bd0dc351e1c3c505a5");
//        //打开注册页面
//        RegisterPage registerPage = new RegisterPage();
//        registerPage.setRegisterCallback(new EventHandler() {
//            public void afterEvent(int event, int result, Object data) {
//// 解析注册结果
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    @SuppressWarnings("unchecked")
//                    //回调完成
//                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                    String country = (String) phoneMap.get("country");
//                    String phone = (String) phoneMap.get("phone");
//                    //Log.i("success","回调完成");
//
//// 提交用户信息（此方法可以不调用）
//                    //registerUser(country, phone);
//                }else if(result == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
//                    Log.i("success","提交验证码成功");
//                }
//            }
//        });
//        registerPage.show(RegisterActivity.this.getApplicationContext());
//        EventHandler eh=new EventHandler(){
//
//            @Override
//            public void afterEvent(int event, int result, Object data) {
//
//                if (result == SMSSDK.RESULT_COMPLETE) {
//                    //回调完成
//                    Log.i("success","注册回调成功");
//                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                        //提交验证码成功
//                        Log.i("success","提交验证码成功");
//                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
//                        //获取验证码成功
//                        Log.i("success","获取验证码成功");
//                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
//                        //返回支持发送验证码的国家列表
//                        Log.i("支持的国家",data.toString());
//                    }
//                }else{
//                    ((Throwable)data).printStackTrace();
//                }
//            }
//        };
//        SMSSDK.registerEventHandler(eh); //注册短信回调
        mEtNick.addTextChangedListener(watcher);
        mEtPwd.addTextChangedListener(watcher);
        mEtPhone.addTextChangedListener(watcher);
        mEtVerifyCode.addTextChangedListener(watcher);

        mEtNick.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mVLineNick.setBackgroundColor(UIUtils.getColor(R.color.green0));
            } else {
                mVLineNick.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });
        mEtPwd.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mVLinePwd.setBackgroundColor(UIUtils.getColor(R.color.green0));
            } else {
                mVLinePwd.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });
        mEtPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mVLinePhone.setBackgroundColor(UIUtils.getColor(R.color.green0));
            } else {
                mVLinePhone.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });
        mEtVerifyCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mVLineVertifyCode.setBackgroundColor(UIUtils.getColor(R.color.green0));
            } else {
                mVLineVertifyCode.setBackgroundColor(UIUtils.getColor(R.color.line));
            }
        });

        mIvSeePwd.setOnClickListener(v -> {

            if (mEtPwd.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }

            mEtPwd.setSelection(mEtPwd.getText().toString().trim().length());
        });

        mBtnSendCode.setOnClickListener(v -> {
            if (mBtnSendCode.isEnabled()) {
                mPresenter.sendCode();
            }
        });

        mBtnRegister.setOnClickListener(v -> {
            mPresenter.register();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    private boolean canRegister() {
        int nickNameLength = mEtNick.getText().toString().trim().length();
        int pwdLength = mEtPwd.getText().toString().trim().length();
        int phoneLength = mEtPhone.getText().toString().trim().length();
        int codeLength = mEtVerifyCode.getText().toString().trim().length();
        if (nickNameLength > 0 && pwdLength > 0 && phoneLength > 0 && codeLength > 0) {
            return true;
        }
        return false;
    }

    @Override
    protected RegisterAtPresenter createPresenter() {
        return new RegisterAtPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    public EditText getEtNickName() {
        return mEtNick;
    }

    @Override
    public EditText getEtPhone() {
        return mEtPhone;
    }

    @Override
    public EditText getEtPwd() {
        return mEtPwd;
    }

    @Override
    public EditText getEtVerifyCode() {
        return mEtVerifyCode;
    }

    @Override
    public Button getBtnSendCode() {
        return mBtnSendCode;
    }
}
