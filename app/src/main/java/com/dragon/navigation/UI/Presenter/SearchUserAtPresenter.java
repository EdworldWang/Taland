package com.dragon.navigation.UI.Presenter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;


import com.dragon.navigation.Model.response.GetUserInfoByIdResponse;
import com.dragon.navigation.Model.response.GetUserInfoByPhoneResponse;
import com.dragon.navigation.R;
import com.dragon.navigation.UI.Activity.UserInfoActivity;
import com.dragon.navigation.UI.Base.BaseActivity;
import com.dragon.navigation.UI.Base.BasePresenter;
import com.dragon.navigation.UI.View.ISearchUserAtView;
import com.dragon.navigation.api.ApiRetrofit;
import com.dragon.navigation.app.AppConst;
import com.dragon.navigation.util.LogUtils;
import com.dragon.navigation.util.RegularUtils;
import com.dragon.navigation.util.UIUtils;

import io.rong.imlib.model.UserInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchUserAtPresenter extends BasePresenter<ISearchUserAtView> {

    public SearchUserAtPresenter(BaseActivity context) {
        super(context);
    }

    public void searchUser() {
        String content = getView().getEtSearchContent().getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            UIUtils.showToast(UIUtils.getString(R.string.content_no_empty));
            return;
        }

        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        if (RegularUtils.isMobile(content)) {
            ApiRetrofit.getInstance().getUserInfoFromPhone(AppConst.REGION, content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getUserInfoByPhoneResponse -> {
                        mContext.hideWaitingDialog();
                        if (getUserInfoByPhoneResponse.getCode() == 200) {
                            GetUserInfoByPhoneResponse.ResultEntity result = getUserInfoByPhoneResponse.getResult();
                            UserInfo userInfo = new UserInfo(result.getId(), result.getNickname(), Uri.parse(result.getPortraitUri()));
                            Intent intent = new Intent(mContext, UserInfoActivity.class);
                            intent.putExtra("userInfo", userInfo);
                            mContext.jumpToActivity(intent);
                        } else {
                            getView().getRlNoResultTip().setVisibility(View.VISIBLE);
                            getView().getLlSearch().setVisibility(View.GONE);
                        }
                    }, this::loadError);
        } else {
            ApiRetrofit.getInstance().getUserInfoById(content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getUserInfoByIdResponse -> {
                        mContext.hideWaitingDialog();
                        if (getUserInfoByIdResponse.getCode() == 200) {
                            GetUserInfoByIdResponse.ResultEntity result = getUserInfoByIdResponse.getResult();
                            UserInfo userInfo = new UserInfo(result.getId(), result.getNickname(), Uri.parse(result.getPortraitUri()));
                            Intent intent = new Intent(mContext, UserInfoActivity.class);
                            intent.putExtra("userInfo", userInfo);
                            mContext.jumpToActivity(intent);
                        } else {
                            getView().getRlNoResultTip().setVisibility(View.VISIBLE);
                            getView().getLlSearch().setVisibility(View.GONE);
                        }
                    }, this::loadError);
        }
    }

    private void loadError(Throwable throwable) {
        mContext.hideWaitingDialog();
        LogUtils.sf(throwable.getLocalizedMessage());
    }
}
