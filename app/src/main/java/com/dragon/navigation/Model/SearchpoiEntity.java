package com.dragon.navigation.Model;

/**
 * Created by EdwardPC on 2016/11/29.
 */
public class SearchpoiEntity {
    private String poiName;
    private String poiDes;
    private int distance;



    // 暂无数据属性
    private boolean isNoData = false;
    private int height;

    public SearchpoiEntity(String poiName,String poiDes,int distance) {
            this.poiName=poiName;
            this.poiDes=poiDes;
            this.distance=distance;
    }
    public SearchpoiEntity() {
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
