package com.edward.navigation.UI.Presenter;



import android.util.Log;

import com.edward.navigation.Model.cache.UserCache;
import com.edward.navigation.R;
import com.edward.navigation.UI.Base.BaseActivity;
import com.edward.navigation.UI.Base.BasePresenter;
import com.edward.navigation.UI.View.IMainAtView;
import com.edward.navigation.app.AppConst;
import com.edward.navigation.app.MyApp;
import com.edward.navigation.db.DBManager;
import com.edward.navigation.manager.BroadcastManager;
import com.edward.navigation.util.LogUtils;
import com.edward.navigation.util.UIUtils;

import io.rong.imlib.RongIMClient;

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
}
