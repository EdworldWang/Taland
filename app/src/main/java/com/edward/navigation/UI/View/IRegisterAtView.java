package com.edward.navigation.UI.View;


import android.widget.Button;

import com.edward.navigation.widget.ClearWriteEditText;

public interface IRegisterAtView {

    ClearWriteEditText getEtNickName();

    ClearWriteEditText getEtPhone();

    ClearWriteEditText getEtPwd();

    ClearWriteEditText getEtVerifyCode();

    Button getBtnSendCode();
}
