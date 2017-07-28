package com.dragon.navigation.UI.View;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.dragon.navigation.widget.ClearWriteEditText;

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
