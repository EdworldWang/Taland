package com.dragon.navigation.UI.View;


import android.widget.EditText;

import com.lqr.recyclerview.LQRRecyclerView;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public interface ISessionAtView {

    BGARefreshLayout getRefreshLayout();

    LQRRecyclerView getRvMsg();

    EditText getEtContent();
}
