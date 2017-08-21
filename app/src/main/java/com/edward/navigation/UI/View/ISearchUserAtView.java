package com.edward.navigation.UI.View;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public interface ISearchUserAtView {

    EditText getEtSearchContent();

    RelativeLayout getRlNoResultTip();

    LinearLayout getLlSearch();
}
