package com.dragon.navigation;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.dragon.navigation.Control.Control;
import com.dragon.navigation.Control.Data;
import com.dragon.navigation.util.MyTextView;
import com.dragon.navigation.util.Servicetype;

/**
 * This file created by dragon on 2016/9/29 20:55,
 * belong to com.dragon.navigation .
 * 因本人无法解决此类服务的返回值问题，所以这种设计方式放弃（dragon20161006）
 */

public class MLocation implements LocationSource,AMapLocationListener {

    private Activity mActivity;
    private MyTextView mCurrentCity;
    private Bundle mSavedInstanceState;
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    public String resultLocation="";
    private boolean flagCurrentLocation;
    private boolean FlagCurrentCity;

    private Handler Locationhandler;
    private String mCity;

    private static int FINSHLOCATION=9;

//    构造函数
    public  MLocation(){

    }
    //    构造函数
    public MLocation(Activity mActivity,Bundle mSavedInstanceState,MyTextView currentCity){
        this.mActivity = mActivity;
        this.mSavedInstanceState =mSavedInstanceState;
        this.mCurrentCity = currentCity;
        this.flagCurrentLocation = true;

    }
    public void setHandler(Handler mainhandler){
        Locationhandler=mainhandler;
    }
//初始化
    public void initLoction(){
        mapView = new MapView(this.mActivity);
//        mapView = (MapView)findViewById(R.id.map);
        mapView.onCreate(this.mSavedInstanceState);
        init();
    }
    private void init(){
        if(aMap==null){
            aMap=mapView.getMap();
            aMap.setLocationSource(this);// 设置定位监听
//            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);//定位模式
        }
    }

    public void onResume(){
        mapView.onResume();
    }
    public void onPause(){
        mapView.onPause();
    }
    protected void onDestroy() {
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        //此函数一直更新
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度

                amapLocation.getAccuracy();//获取精度信息

                //用于搜索定位后距离的运算
                LatLng here=new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                ArPoiSearch.here=here;
                Data.locationhere.setLatitude(amapLocation.getLatitude());
                Data.locationhere.setLongitude(amapLocation.getLongitude());

//                mLocationErrText.setVisibility(View.GONE);;
                mListener.onLocationChanged(amapLocation);//
                resultLocation=amapLocation.getAddress();
                if(flagCurrentLocation) {
                    mCurrentCity.setText(resultLocation);
                }
                flagCurrentLocation=false;
//                Toast.makeText(this.mActivity, amapLocation.getCity(), Toast.LENGTH_SHORT).show();

                Message msg=new Message();
                msg.what=FINSHLOCATION;
                Log.e("information", "Aoi name" + amapLocation.getAoiName() + "address" + amapLocation.getAddress() + amapLocation.getProvince()
                        + amapLocation.getCity() + amapLocation.getDistrict());
                Locationhandler.sendMessage(msg);



            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);

            }
        }
    }
/*
*
     * 激活定位

*/

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this.mActivity);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

/*
*
     * 停止定位

*/

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }




}
