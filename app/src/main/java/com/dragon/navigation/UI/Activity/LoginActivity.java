package com.dragon.navigation.UI.Activity;


import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.dragon.navigation.R;
import com.dragon.navigation.UI.Base.BaseActivity;
import com.dragon.navigation.UI.Presenter.LoginAtPresenter;
import com.dragon.navigation.UI.View.ILoginAtView;
import com.dragon.navigation.widget.ClearWriteEditText;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * @创建者 Edward
 * @Date: 2017/07/27
 * @描述 登录界面
 */
public class LoginActivity extends BaseActivity<ILoginAtView, LoginAtPresenter> implements ILoginAtView {

    private boolean  PasswordVisible = false;
    @BindView(R.id.ed_img_backgroud)
    ImageView mImg_Background;

    @BindView(R.id.ed_login_phone)
    ClearWriteEditText mEtPhone;

    @BindView(R.id.ed_login_password)
    ClearWriteEditText mEtPwd;

    @BindView(R.id.psd_eye)
    ImageView mPsd_eye;
    @OnClick(R.id.psd_eye)
    public void ChangePasVisible(View v){
        if(PasswordVisible){
            mEtPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else {
            //原本不可见
            mEtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        PasswordVisible = !PasswordVisible;
    }

    @BindView(R.id.ed_login_sign)
    Button mBtnLogin;


    @BindView(R.id.TextInputPhone)
    TextInputLayout mTextInputPhone;

    @BindView(R.id.TextInputPassword)
    TextInputLayout mTextInputPassword;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mBtnLogin.setEnabled(canLogin());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void initView(){
       // mEtPwd.IsPassword(true);
       /* UIUtils.showToast(mEtPwd.getmClearAndEyeDrawable().getIntrinsicHeight()+"   "+
                        mEtPwd.getmClearAndEyeDrawable().getIntrinsicWidth());*/
        //版本5.0以上实现  沉浸式状态栏（状态栏透明，且位置腾出给下面）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                mImg_Background.startAnimation(animation);
            }
        }, 200);

    }

    @Override
    public void initListener() {
        mEtPwd.addTextChangedListener(watcher);
        mEtPhone.addTextChangedListener(watcher);
        mBtnLogin.setOnClickListener(v -> mPresenter.login());
    }

    private boolean canLogin() {
        int pwdLength = mEtPwd.getText().toString().trim().length();
        int phoneLength = mEtPhone.getText().toString().trim().length();
        if (pwdLength > 0 && phoneLength > 0) {
            return true;
        }
        return false;
    }


    @Override
    protected LoginAtPresenter createPresenter() {
        return new LoginAtPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_login_imstyle;
    }

    @Override
    public ClearWriteEditText getEtPhone() {
        return mEtPhone;
    }

    @Override
    public ClearWriteEditText getEtPwd() {
        return mEtPwd;
    }

    @Override
    public TextInputLayout getTextInputPhone(){return mTextInputPhone;}

    @Override
    public TextInputLayout getTextInputPassword(){return mTextInputPassword;}
}