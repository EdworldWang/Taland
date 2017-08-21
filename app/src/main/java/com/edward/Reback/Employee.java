package com.edward.Reback;

import android.util.Log;

/**
 * Created by EdwardPC on 2016/12/17.
 */
public class Employee {
    ListviewCallBack callBack;
    public Employee(ListviewCallBack callBack){
        this.callBack=callBack;
    }
    public void doWork(){
        Log.i("Employee","dowork");
        callBack.makeList();
    }
}
