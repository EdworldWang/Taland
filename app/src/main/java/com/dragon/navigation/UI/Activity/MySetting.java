package com.dragon.navigation.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Window;
import android.widget.TextView;

import com.dragon.navigation.Control.Control;
import com.dragon.navigation.Control.Data;
import com.dragon.navigation.R;

/**
 * This file created by dragon on 2016/9/29 20:57,
 * belong to com.dragon.navigation .
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
