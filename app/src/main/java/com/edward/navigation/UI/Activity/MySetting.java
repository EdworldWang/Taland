package com.edward.navigation.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.edward.navigation.R;

/**
 * This file created by dragon on 2016/9/29 20:57,
 * belong to com.edward.navigation .
 */
public class MySetting extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_setting);
        TextView test=(TextView)findViewById(R.id.test);

    }
}
