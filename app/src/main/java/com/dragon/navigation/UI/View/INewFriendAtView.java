package com.dragon.navigation.UI.View;


import android.widget.LinearLayout;

import com.lqr.recyclerview.LQRRecyclerView;

public interface INewFriendAtView {
    LinearLayout getLlNoNewFriend();

    LinearLayout getLlHasNewFriend();

    LQRRecyclerView getRvNewFriend();
}
