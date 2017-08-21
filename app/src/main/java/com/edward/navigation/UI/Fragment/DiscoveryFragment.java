package com.edward.navigation.UI.Fragment;

import com.edward.navigation.R;
import com.edward.navigation.UI.Activity.MainActivity;
import com.edward.navigation.UI.Activity.ScanActivity;
import com.edward.navigation.UI.Base.BaseFragment;
import com.edward.navigation.UI.Presenter.DiscoveryFgPresenter;
import com.edward.navigation.UI.View.IDiscoveryFgView;
import com.edward.navigation.app.AppConst;
import com.lqr.optionitemview.OptionItemView;

import butterknife.BindView;

/**
 * @创建者 CSDN_LQR
 * @描述 发现界面
 */
public class DiscoveryFragment extends BaseFragment<IDiscoveryFgView, DiscoveryFgPresenter> implements IDiscoveryFgView {

    @BindView(R.id.oivScan)
    OptionItemView mOivScan;
    @BindView(R.id.oivShop)
    OptionItemView mOivShop;
    @BindView(R.id.oivGame)
    OptionItemView mOivGame;

    @Override
    public void initListener() {
        mOivScan.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToActivity(ScanActivity.class));
        mOivShop.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.JD));
        mOivGame.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.GAME));
    }

    @Override
    protected DiscoveryFgPresenter createPresenter() {
        return new DiscoveryFgPresenter((MainActivity) getActivity());
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_discovery;
    }
}
