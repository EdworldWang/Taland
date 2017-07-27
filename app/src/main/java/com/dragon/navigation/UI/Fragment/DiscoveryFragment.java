package com.dragon.navigation.UI.Fragment;

import com.dragon.navigation.R;
import com.dragon.navigation.UI.Activity.MainActivity;
import com.dragon.navigation.UI.Activity.ScanActivity;
import com.dragon.navigation.UI.Base.BaseFragment;
import com.dragon.navigation.UI.Presenter.DiscoveryFgPresenter;
import com.dragon.navigation.UI.View.IDiscoveryFgView;
import com.dragon.navigation.app.AppConst;
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
