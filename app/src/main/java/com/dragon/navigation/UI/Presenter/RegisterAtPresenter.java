package com.dragon.navigation.UI.Presenter;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;


import com.dragon.navigation.Model.cache.UserCache;
import com.dragon.navigation.Model.exception.ServerException;
import com.dragon.navigation.Model.response.LoginResponse;
import com.dragon.navigation.Model.response.MobVerifyCodeResponse;
import com.dragon.navigation.Model.response.RegisterResponse;
import com.dragon.navigation.R;
import com.dragon.navigation.UI.Activity.LoginActivity;
import com.dragon.navigation.UI.Activity.MainActivity;
import com.dragon.navigation.UI.Base.BaseActivity;
import com.dragon.navigation.UI.Base.BasePresenter;
import com.dragon.navigation.UI.View.IRegisterAtView;
import com.dragon.navigation.api.ApiRetrofit;
import com.dragon.navigation.app.AppConst;
import com.dragon.navigation.app.MyApp;
import com.dragon.navigation.util.LogUtils;
import com.dragon.navigation.util.RegularUtils;
import com.dragon.navigation.util.UIUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RegisterAtPresenter extends BasePresenter<IRegisterAtView> {

    int time = 0;
    private Timer mTimer;
    private Subscription mSubscription;
    private static TimerTask mTask;
    public RegisterAtPresenter(BaseActivity context) {
        super(context);
    }
    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                LogUtils.i("回调完成");
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    LogUtils.i("提交验证码成功");
                    // sendSuccess();
                    //UIUtils.showToast("提交验证码成功");
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                    if(result == SMSSDK.RESULT_COMPLETE) {
                        boolean smart = (Boolean)data;
                        if(smart) {
                            LogUtils.i("通过智能验证");
                            UIUtils.showToast("您的号码已经通过智能验证");
                            //通过智能验证
                        } else {
                            LogUtils.i("请等待短信");
                            //依然走短信验证
                        }
                    }
                    LogUtils.i("获取验证码成功");
                    changeSendCodeBtn();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                    //返回支持发送验证码的国家列表
                    //ArrayList<HashMap<String,Object>>
                    LogUtils.i(data.toString());
                }
            }else{
                ((Throwable)data).printStackTrace();
            }
        }
    };
    public void sendCode() {
        String phone = getView().getEtPhone().getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_not_empty));
            return;
        }

        if (!RegularUtils.isMobile(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_format_error));
            return;
        }

        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        ApiRetrofit.getInstance().checkPhoneAvailable(AppConst.REGION, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(checkPhoneResponse -> {
                    int code = checkPhoneResponse.getCode();
                    if (code == 200) {
                        //MyApp extend Application
                        SMSSDK.initSDK(MyApp.getContext(), "1dbe108d9bf00", "480cdb08f1c1c6bd0dc351e1c3c505a5");//3.0之前SMSSDK版本的初始化
                        //3.0版本之后的初始化看这里（包括3.0）
                        SMSSDK.registerEventHandler(eh); //注册短信回调
                        SMSSDK.getVerificationCode(AppConst.REGION, phone);

                        //  SMSSDK.getSupportedCountries();
                      //  return ApiRetrofit.getInstance().sendCode(AppConst.REGION, phone);
                    } else {
                        sendCodeError(new ServerException(UIUtils.getString(R.string.send_code_error) + R.string.phone_not_available));
                       //return Observable.error(new ServerException(UIUtils.getString(R.string.phone_not_available)));
                    }
                    mContext.hideWaitingDialog();
                  /*
                    if (code == 200) {
                        changeSendCodeBtn();
                    } else {
                        sendCodeError(new ServerException(UIUtils.getString(R.string.send_code_error)));
                    }*/
                }, this::sendCodeError);
    }

    private void sendCodeError(Throwable throwable) {
        mContext.hideWaitingDialog();
        LogUtils.e(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }
    private void sendSuccess() {
        mContext.hideWaitingDialog();
//        UIUtils.showToast("提交验证码成功");
    }

    private void changeSendCodeBtn() {
        //开始1分钟倒计时
        //每一秒执行一次Task
        //取消绑定，可以绑定多个
        mSubscription = Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
            time = 60;
            //修改源代码bug,在定时操作后会生成新的timertask使，时间下降得更快
            if(mTask == null) {
                mTask = new TimerTask() {
                    @Override
                    public void run() {
                        subscriber.onNext(--time);
                    }
                };
            }
            mTimer = new Timer();
            mTimer.schedule(mTask, 0, 1000);//每一秒执行一次Task
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> {
                    if (getView().getBtnSendCode() != null) {
                        if (time >= 0) {
                            getView().getBtnSendCode().setEnabled(false);
                            getView().getBtnSendCode().setText(time + "");
                        } else {
                            getView().getBtnSendCode().setEnabled(true);
                            getView().getBtnSendCode().setText(UIUtils.getString(R.string.send_code_btn_normal_tip));
                        }
                    } else {
                        mTimer.cancel();
                    }
                }, throwable -> LogUtils.sf(throwable.getLocalizedMessage()));
    }

    public void register() {
        String phone = getView().getEtPhone().getText().toString().trim();
        String password = getView().getEtPwd().getText().toString().trim();
        String nickName = getView().getEtNickName().getText().toString().trim();
        String code = getView().getEtVerifyCode().getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_not_empty));
            UIUtils.setShakeAnimation(getView().getEtPhone(),3);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            UIUtils.showToast(UIUtils.getString(R.string.password_not_empty));
            UIUtils.setShakeAnimation(getView().getEtPwd(),3);
            return;
        }
        if (password.contains(" ")) {
            UIUtils.showToast(UIUtils.getString(R.string.password_not_contain_blank));
            UIUtils.setShakeAnimation(getView().getEtPwd(),3);
            return;
        }
        if (TextUtils.isEmpty(nickName)) {
            UIUtils.showToast(UIUtils.getString(R.string.nickname_not_empty));
            UIUtils.setShakeAnimation(getView().getEtNickName(),3);
            return;
        }
        if (nickName.contains(" ")) {
            UIUtils.showToast(UIUtils.getString(R.string.nickname_not_contain_blank));
            UIUtils.setShakeAnimation(getView().getEtNickName(),3);
            return;
        }
        if (TextUtils.isEmpty(code)) {
            UIUtils.showToast(UIUtils.getString(R.string.vertify_code_not_empty));
            UIUtils.setShakeAnimation(getView().getEtVerifyCode(),3);
            return;
        }
      //  SMSSDK.submitVerificationCode(AppConst.REGION, phone,code);
        ApiRetrofit.getInstance().verifyCode(AppConst.REGION, phone, code)
                //将VerifyCodeResponse替换成MobVerifyCodeResponse
                .flatMap(new Func1<MobVerifyCodeResponse, Observable<RegisterResponse>>() {
                    @Override
                    public Observable<RegisterResponse> call(MobVerifyCodeResponse MobverifyCodeResponse) {
                        int status = MobverifyCodeResponse.getStatus();
                        if (status == 200) {
                            SMSSDK.unregisterEventHandler(eh);
                                return ApiRetrofit.getInstance().register(nickName, password,MobverifyCodeResponse.getResult().getVerification_token());
                            // return ApiRetrofit.getInstance().register(nickName, password, verifyCodeResponse.getResult().getVerification_token());
                        } else {
                           // return Observable.error(new ServerException(UIUtils.getString(R.string.vertify_code_error) + status));
                            return   Observable.error(new ServerException(MobverifyCodeResponse.getError()+status));
                        }
                    }
                })
                .flatMap(new Func1<RegisterResponse, Observable<LoginResponse>>() {
                    @Override
                    public Observable<LoginResponse> call(RegisterResponse registerResponse) {
                        int code = registerResponse.getCode();
                        if (code == 200) {
                            return ApiRetrofit.getInstance().login(AppConst.REGION, phone, password);
                        } else {
                            return Observable.error(new ServerException(UIUtils.getString(R.string.register_error) + code));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginResponse -> {
                    int responseCode = loginResponse.getCode();
                    if (responseCode == 200) {
                        UserCache.save(loginResponse.getResult().getId(), phone, loginResponse.getResult().getToken());
                        mContext.jumpToActivityAndClearTask(MainActivity.class);
                        mContext.finish();
                    } else {
                        UIUtils.showToast(UIUtils.getString(R.string.login_error));
                        mContext.jumpToActivity(LoginActivity.class);
                    }
                }, this::registerError);
    }

    private void registerError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }

    public void unsubscribe() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

}
