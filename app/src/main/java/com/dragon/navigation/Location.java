package com.dragon.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;

/**
 * This file created by dragon on 2016/9/20 15:46,
 * belong to com.dragon.navigation .
 */
public class Location extends Activity implements LocationSource,AMapLocationListener {
    //    定位相关类
    private static final String TAG="Location";
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private ViewStub pic_sub;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;
    private TextView textView1;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        rgLocationMode.setOnCheckedChangeListener(this);
//        cbAddress.setOnCheckedChangeListener(this);
//        cbCacheAble.setOnCheckedChangeListener(this);
//        btLocation.setOnClickListener(this);
    }



    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        //此函数一直更新
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                textView1.setText(amapLocation.getCity());
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
//                mLocationErrText.setVisibility(View.GONE);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//                ToastUtil.show(Camera2.this, amapLocation.getAddress());//一直显示
                Toast.makeText(Location.this, amapLocation.getAddress(), Toast.LENGTH_SHORT).show();
                Log.e("information",amapLocation.getAddress()+amapLocation.getProvince()+ amapLocation.getCity()+amapLocation.getDistrict());
//                pic_sub.setVisibility(View.VISIBLE);

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
//                mLocationErrText.setVisibility(View.VISIBLE);
//                mLocationErrText.setText(errText);
            }
        }
    }
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
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

    /**
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

}
