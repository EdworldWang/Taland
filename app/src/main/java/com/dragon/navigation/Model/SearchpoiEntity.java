package com.dragon.navigation.Model;

import com.amap.api.maps.model.LatLng;

/**
 * Created by EdwardPC on 2016/11/29.
 */
public class SearchpoiEntity {
    private String poiName;
    private String poiDes;
    private int distance;
    private String poiType;
    private LatLng myLatlng;
    private float firstbearing;
    //只要知道初始的bearing就好，后面的bearing不用计算，
    // 根据当前的转向旋转而变化就好，moveview的移动也是这样

    // 暂无数据属性
    private boolean isNoData = false;
    private int height;

    public SearchpoiEntity(String poiName,String poiType,int distance,String poiDes,LatLng myLatlng) {
            this.poiName=poiName;
            this.poiType=poiType;
            this.distance=distance;
            this.poiDes=poiDes;
            this.myLatlng=myLatlng;
    }
    public SearchpoiEntity() {
    }


    public String getPoiType() {
        return poiType;
    }

    public void setPoiType(String poiType) {
        this.poiType = poiType;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isNoData() {
        return isNoData;
    }

    public void setNoData(boolean noData) {
        isNoData = noData;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setPoiDes(String poiDes) {
        this.poiDes = poiDes;
    }

    public String getPoiDes() {
        return poiDes;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getPoiName() {
        return poiName;
    }
}
