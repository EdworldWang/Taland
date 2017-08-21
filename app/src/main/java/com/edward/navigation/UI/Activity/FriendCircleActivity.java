package com.edward.navigation.UI.Activity;


import com.edward.navigation.UI.Base.BaseActivity;
import com.edward.navigation.UI.Presenter.FriendCircleAtPresenter;
import com.edward.navigation.UI.View.IFriendCircleAtView;

/**
 * @创建者 CSDN_LQR
 * @描述 朋友圈
 */
public class FriendCircleActivity extends BaseActivity<IFriendCircleAtView, FriendCircleAtPresenter> implements IFriendCircleAtView {

    @Override
    protected FriendCircleAtPresenter createPresenter() {
        return new FriendCircleAtPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return 0;
    }
}
