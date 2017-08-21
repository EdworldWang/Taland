package com.edward.navigation.Function;

import android.app.Activity;
import android.util.Log;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * Created by EdwardPC on 2016/11/29.
 */
public class ResGeoCoding implements GeocodeSearch.OnGeocodeSearchListener {
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private RegeocodeQuery query;
    private Activity mActivity;
    public ResGeoCoding(Activity activity) {
        this.mActivity= activity;
    }

    public void doSearch(LatLonPoint latLonPointdes){
        query=new RegeocodeQuery(latLonPointdes,200,GeocodeSearch.AMAP);
        geocoderSearch =new GeocodeSearch(this.mActivity);
        geocoderSearch.setOnGeocodeSearchListener(this);
        geocoderSearch.getFromLocationAsyn(query);
    }
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
           if(rCode==1000){
               if(result != null && result.getRegeocodeAddress()!=null
                       &&result.getRegeocodeAddress().getFormatAddress()!=null){
                    addressName = result.getRegeocodeAddress().getFormatAddress()+"附近";
                       Log.i("ResGeoCoding",addressName);
               }else{
                   Log.i("ResGeoCoding","null");
               }
           }else{
               Log.i("ResGeoCoding","error");
           }
    }

    public void onGeocodeSearched(GeocodeResult var1, int var2){

    }
    public String getDescription(){
        return addressName;
    }

}
