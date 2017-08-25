package com.edward.navigation.UI.Presenter;



import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.edward.navigation.Model.cache.UserCache;
import com.edward.navigation.Model.response.GetUserInfoByIdResponse;
import com.edward.navigation.R;
import com.edward.navigation.UI.Base.BaseActivity;
import com.edward.navigation.UI.Base.BasePresenter;
import com.edward.navigation.UI.View.IMainAtView;
import com.edward.navigation.api.ApiRetrofit;
import com.edward.navigation.app.AppConst;
import com.edward.navigation.app.MyApp;
import com.edward.navigation.db.DBManager;
import com.edward.navigation.db.model.Friend;
import com.edward.navigation.manager.BroadcastManager;
import com.edward.navigation.util.LogUtils;
import com.edward.navigation.util.UIUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainAtPresenter extends BasePresenter<IMainAtView> {

    public MainAtPresenter(BaseActivity context) {
        super(context);
        Log.i("MainAtPresenter","in");
        connect(UserCache.getToken());

        //同步所有用户信息
        DBManager.getInstance().getAllUserInfo();
    }


    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(String token) {
        Log.i("MainAtPresenter",UIUtils.getContext().getApplicationInfo().packageName
        + "         " + MyApp.getCurProcessName(UIUtils.getContext()));
        if (UIUtils.getContext().getApplicationInfo().packageName.equals(MyApp.getCurProcessName(UIUtils.getContext()))) {
            Log.i("MainAtPresenter","iamhere");
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    Log.i("MainAtPresenter","ontokenincorrect");
                    LogUtils.e("--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtils.e("--onSuccess---" + userid);
                    Log.i("MainAtPresenter","success");
                    BroadcastManager.getInstance(mContext).sendBroadcast(AppConst.UPDATE_CONVERSATIONS);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    LogUtils.e("--onError" + errorCode);
                    Log.i("MainAtPresenter","失联");
                    UIUtils.showToast(UIUtils.getString(R.string.disconnect_server));
                }
            });
        }
    }


    private UserInfo mUserInfo;
    private boolean isFirst = true;
    public void loadUserInfo() {
        mUserInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
        if (mUserInfo == null || isFirst) {
            isFirst = false;
            ApiRetrofit.getInstance().getUserInfoById(UserCache.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getUserInfoByIdResponse -> {
                        if (getUserInfoByIdResponse != null && getUserInfoByIdResponse.getCode() == 200) {
                            GetUserInfoByIdResponse.ResultEntity result = getUserInfoByIdResponse.getResult();

                            mUserInfo = new UserInfo(UserCache.getId(), result.getNickname(), Uri.parse(result.getPortraitUri()));
                            if (TextUtils.isEmpty(mUserInfo.getPortraitUri().toString())) {
                                mUserInfo.setPortraitUri(Uri.parse(DBManager.getInstance().getPortraitUri(mUserInfo)));
                            }

                            DBManager.getInstance().saveOrUpdateFriend(new Friend(mUserInfo.getUserId(), mUserInfo.getName(), mUserInfo.getPortraitUri().toString()));
                            fillView();
                        }
                    }, this::loadError);
        } else {
            fillView();
        }
    }

    public void refreshUserInfo() {
        UserInfo userInfo = DBManager.getInstance().getUserInfo(UserCache.getId());
        if (userInfo == null) {
            loadUserInfo();
        } else {
            mUserInfo = userInfo;
        }
    }

    public void fillView() {
        if (mUserInfo != null) {
            Glide.with(mContext).load(mUserInfo.getPortraitUri()).centerCrop().into(getView().getToolHead());
           /* getView().getTvAccount().setText(UIUtils.getString(R.string.my_chat_account, mUserInfo.getUserId()));
            getView().getTvName().setText(mUserInfo.getName());*/
        }
    }

    private void loadError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

}
