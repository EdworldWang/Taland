package com.edward.navigation.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * This file created by dragon on 2016/10/6 22:40,
 * belong to com.edward.navigation .
 */
public class MyTextView  extends TextView {
    public MyTextView(Context con) {
        super(con);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean isFocused() {
        return true;
    }
}
