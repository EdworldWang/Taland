package com.edward.navigation.UI.Presenter;

import android.net.Uri;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.edward.navigation.Model.cache.UserCache;
import com.edward.navigation.Model.response.GetUserInfoByIdResponse;
import com.edward.navigation.R;
import com.edward.navigation.UI.Base.BaseActivity;
import com.edward.navigation.UI.Base.BasePresenter;
import com.edward.navigation.UI.View.IMainFgView;
import com.edward.navigation.api.ApiRetrofit;
import com.edward.navigation.db.DBManager;
import com.edward.navigation.db.model.Friend;
import com.edward.navigation.util.LogUtils;
import com.edward.navigation.util.UIUtils;

import io.rong.imlib.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFgPresenter extends BasePresenter<IMainFgView> {

    public MainFgPresenter(BaseActivity context) {
        super(context);
    }
}
