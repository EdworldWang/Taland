package com.dragon.navigation.UI.View;


import android.widget.Button;
import android.widget.EditText;

import com.dragon.navigation.widget.ClearWriteEditText;

public interface IRegisterAtView {

    ClearWriteEditText getEtNickName();

    ClearWriteEditText getEtPhone();

    ClearWriteEditText getEtPwd();

    ClearWriteEditText getEtVerifyCode();

    Button getBtnSendCode();
}
