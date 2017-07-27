package com.dragon.navigation.UI.Activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.dragon.navigation.R;
import com.dragon.navigation.UI.Base.BaseActivity;
import com.dragon.navigation.UI.Base.BasePresenter;
import com.dragon.navigation.api.ApiRetrofit;
import com.dragon.navigation.app.AppConst;
import com.dragon.navigation.db.DBManager;
import com.dragon.navigation.db.model.Friend;
import com.dragon.navigation.manager.BroadcastManager;
import com.dragon.navigation.util.LogUtils;
import com.dragon.navigation.util.PinyinUtils;
import com.dragon.navigation.util.UIUtils;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @创建者 CSDN_LQR
 * @描述 设备备注界面
 */

public class SetAliasActivity extends BaseActivity {

    private String mFriendId;
    private Friend mFriend;

    @BindView(R.id.btnToolbarSend)
    Button mBtnToolbarSend;
    @BindView(R.id.etAlias)
    EditText mEtAlias;

    @Override
    public void init() {
        mFriendId = getIntent().getStringExtra("userId");
    }

    @Override
    public void initView() {
        if (TextUtils.isEmpty(mFriendId)) {
            finish();
            return;
        }

        mBtnToolbarSend.setVisibility(View.VISIBLE);
        mBtnToolbarSend.setText(UIUtils.getString(R.string.complete));
    }

    @Override
    public void initData() {
        mFriend = DBManager.getInstance().getFriendById(mFriendId);
        if (mFriend != null)
            mEtAlias.setText(mFriend.getDisplayName());
        mEtAlias.setSelection(mEtAlias.getText().toString().trim().length());
    }

    @Override
    public void initListener() {
        mEtAlias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnToolbarSend.setEnabled(mEtAlias.getText().toString().trim().length() > 0 ? true : false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBtnToolbarSend.setOnClickListener(v -> {
            String displayName = mEtAlias.getText().toString().trim();
            if (TextUtils.isEmpty(displayName)) {
                UIUtils.showToast(UIUtils.getString(R.string.alias_no_empty));
                return;
            }

            showWaitingDialog(UIUtils.getString(R.string.please_wait));
            ApiRetrofit.getInstance().setFriendDisplayName(mFriendId, displayName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(setFriendDisplayNameResponse -> {
                        if (setFriendDisplayNameResponse.getCode() == 200) {
                            UIUtils.showToast(UIUtils.getString(R.string.change_success));

                            //更新本地好友数据库
                            mFriend.setDisplayName(displayName);
                            mFriend.setDisplayNameSpelling(PinyinUtils.getPinyin(displayName));
                            DBManager.getInstance().saveOrUpdateFriend(mFriend);
                            BroadcastManager.getInstance(SetAliasActivity.this).sendBroadcast(AppConst.UPDATE_FRIEND);
                            BroadcastManager.getInstance(SetAliasActivity.this).sendBroadcast(AppConst.CHANGE_INFO_FOR_USER_INFO);

                            finish();

                        } else {
                            UIUtils.showToast(UIUtils.getString(R.string.change_fail));
                        }
                        hideWaitingDialog();
                    }, this::changeError);
        });
    }

    private void changeError(Throwable throwable) {
        hideWaitingDialog();
        LogUtils.sf(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_set_alias;
    }
}
