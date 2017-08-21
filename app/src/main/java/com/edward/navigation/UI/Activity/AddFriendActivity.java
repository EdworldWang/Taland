package com.edward.navigation.UI.Activity;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.edward.navigation.Model.cache.UserCache;
import com.edward.navigation.R;
import com.edward.navigation.UI.Base.BaseActivity;
import com.edward.navigation.UI.Presenter.AddFriendAtPresenter;
import com.edward.navigation.UI.View.IAddFriendAtView;
import com.edward.navigation.util.UIUtils;

import butterknife.BindView;


/**
 * @创建者 CSDN_LQR
 * @描述 添加朋友界面
 */

public class AddFriendActivity extends BaseActivity<IAddFriendAtView, AddFriendAtPresenter> implements IAddFriendAtView {

    @BindView(R.id.llSearchUser)
    LinearLayout mLlSearchUser;
    @BindView(R.id.tvAccount)
    TextView mTvAccount;

    @Override
    public void initView() {
        setToolbarTitle(UIUtils.getString(R.string.add_friend));
        mTvAccount.setText(UserCache.getId() + "");
    }

    @Override
    public void initListener() {
        mLlSearchUser.setOnClickListener(v -> jumpToActivity(SearchUserActivity.class));
    }

    @Override
    protected AddFriendAtPresenter createPresenter() {
        return new AddFriendAtPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_add_friend;
    }
}
