package com.edward.navigation.UI.View;

import android.support.design.widget.TextInputLayout;

import com.edward.navigation.widget.ClearWriteEditText;

/**
 * @创建者 CSDN_LQR
 * @描述 登录界面的View
 */
public interface ILoginAtView {

    ClearWriteEditText getEtPhone();

    ClearWriteEditText getEtPwd();

    TextInputLayout getTextInputPhone();

    TextInputLayout getTextInputPassword();
}
