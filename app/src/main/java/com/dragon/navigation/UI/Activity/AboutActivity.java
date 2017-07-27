package com.dragon.navigation.UI.Activity;


import com.dragon.navigation.R;
import com.dragon.navigation.UI.Base.BaseActivity;
import com.dragon.navigation.UI.Base.BasePresenter;

/**
 * @创建者 CSDN_LQR
 * @描述 关于界面
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_about;
    }
}
