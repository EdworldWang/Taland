//package com.dragon.navigation;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationListener;
//
///**
// * This file created by dragon on 2016/10/7 22:11,
// * belong to com.dragon.navigation .
// */
//public class GetGPS {
//
//    //经纬度
//    private  double latitude;
//    private  double longitude;
//
//    private LocationManagerProxy mAMapLocationManager;
//    private AMap aMap;
//    public  GetGPS(Context context ){ //构造传入
//        aMap = new AMap();
//        mAMapLocationManager = LocationManagerProxy.getInstance(context);
//        mAMapLocationManager.requestLocationUpdates(
//                LocationProviderProxy.AMapNetwork, 5000, 10, aMap );
//
//    }
//    class AMap implements AMapLocationListener {
//
//        @Override
//        public void onLocationChanged(Location location) {
//        }
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//        @Override
//        public void onProviderEnabled(String provider) {
//        }
//        @Override
//        public void onProviderDisabled(String provider) {
//        }
//        //获取经纬度
//        @Override
//        public void onLocationChanged(AMapLocation location) {
//
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//
//        }
//    }
//    public void destroyAMapLocationListener() { //取消经纬度监听
//        mAMapLocationManager.removeUpdates(aMap);
//        mAMapLocationManager.destory();
//        mAMapLocationManager = null;
//
//    }
//}