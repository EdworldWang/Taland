package com.dragon.navigation.Model.cache;


import android.util.Log;

import com.dragon.navigation.app.AppConst;
import com.dragon.navigation.db.DBManager;
import com.dragon.navigation.util.SPUtils;
import com.dragon.navigation.util.UIUtils;

/**
 * @创建者 CSDN_LQR
 * @描述 用户缓存
 */
public class UserCache {

    public static String getId() {
        return SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.ID, "");
    }

    public static String getPhone() {
        return SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.PHONE, "");
    }

    public static String getToken() {
        Log.i("token",SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.TOKEN, ""));
        return SPUtils.getInstance(UIUtils.getContext()).getString(AppConst.User.TOKEN, "");
    }

    public static void save(String id, String account, String token) {
        SPUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.ID, id);
        SPUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.PHONE, account);
        SPUtils.getInstance(UIUtils.getContext()).putString(AppConst.User.TOKEN, token);
    }

    public static void clear() {
        SPUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.ID);
        SPUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.PHONE);
        SPUtils.getInstance(UIUtils.getContext()).remove(AppConst.User.TOKEN);
        DBManager.getInstance().deleteAllUserInfo();
    }

}
