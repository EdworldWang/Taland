package com.edward.navigation.UI.Presenter;

import android.text.TextUtils;


import com.edward.navigation.Model.cache.UserCache;
import com.edward.navigation.Model.exception.ServerException;
import com.edward.navigation.R;
import com.edward.navigation.UI.Activity.MainActivity;
import com.edward.navigation.UI.Base.BaseActivity;
import com.edward.navigation.UI.Base.BasePresenter;
import com.edward.navigation.UI.View.ILoginAtView;
import com.edward.navigation.api.ApiRetrofit;
import com.edward.navigation.app.AppConst;
import com.edward.navigation.util.LogUtils;
import com.edward.navigation.util.UIUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginAtPresenter extends BasePresenter<ILoginAtView> {

    public LoginAtPresenter(BaseActivity context) {
        super(context);
    }

    public void login() {
        String phone = getView().getEtPhone().getText().toString().trim();
        String pwd = getView().getEtPwd().getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_not_empty));
           // getView().getEtPhone().setShakeAnimation();
            UIUtils.setShakeAnimation(getView().getTextInputPhone(),3);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            UIUtils.showToast(UIUtils.getString(R.string.password_not_empty));
            getView().getEtPwd().setShakeAnimation();
            return;
        }
        if (pwd.contains(" ")) {
            UIUtils.showToast(UIUtils.getString(R.string.password_not_contain_blank));
            getView().getEtPwd().setShakeAnimation();
            return;
        }
        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        ApiRetrofit.getInstance().login(AppConst.REGION, phone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginResponse -> {
                    int code = loginResponse.getCode();
                    mContext.hideWaitingDialog();
                    if (code == 200) {
                        UIUtils.showToast(UIUtils.getString(R.string.login_success));
                        UserCache.save(loginResponse.getResult().getId(), phone, loginResponse.getResult().getToken());
                        mContext.jumpToActivityAndClearTask(MainActivity.class);
                        mContext.finish();
                    } else {
                        loginError(new ServerException(UIUtils.getString(R.string.login_error) + code));
                    }
                }, this::loginError);
    }

    private void loginError(Throwable throwable) {
        LogUtils.e(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
        mContext.hideWaitingDialog();
    }
}
