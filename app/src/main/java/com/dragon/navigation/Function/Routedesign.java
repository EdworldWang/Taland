package com.dragon.navigation.Function;

import android.app.Activity;
import android.util.Log;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import java.util.List;

/**
 * Created by EdwardPC on 2016/12/4.
 */
public class Routedesign implements RouteSearch.OnRouteSearchListener {
    private Activity mActivity;
    private int walkMode;
    private RouteSearch.FromAndTo fromandto;
    private RouteSearch.WalkRouteQuery query;

    private WalkRouteResult routeResult;
    private List<WalkPath> walkPathsList;
    private WalkPath mywalkpath;
    private List<WalkStep> walkStepsList;
    private WalkStep mywalkstep;
    private List<LatLonPoint> myLatLonPointlist;
    public Routedesign(Activity activity) {
        this.mActivity= activity;
    }
    public void dodesign(RouteSearch.FromAndTo fromandtoint, int walkMode){
        this.walkMode=walkMode;
        this.fromandto=fromandto;
        RouteSearch routeSearch = new RouteSearch(this.mActivity);
        routeSearch.setRouteSearchListener(this);
        query = new RouteSearch.WalkRouteQuery(fromandtoint, walkMode);
        routeSearch.calculateWalkRouteAsyn(query);
    }

    public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
        //解析result获取数据结果
        if(rCode==1000){
            if(result != null && result.getWalkQuery()!=null){
                if (result.getWalkQuery().equals(query)) {// 是否是同一条
                    routeResult = result;
                    walkPathsList = routeResult.getPaths();
                    mywalkpath=walkPathsList.get(0);
                    walkStepsList=mywalkpath.getSteps();
                    for(int i=0;i<walkStepsList.size();i++){
                        mywalkstep=walkStepsList.get(i);
                        Log.i("routedesign",mywalkstep.getInstruction());
                        myLatLonPointlist=mywalkstep.getPolyline();
                        for(int f=0;f<myLatLonPointlist.size();f++){
                            Log.i("routedesign",myLatLonPointlist.get(f).toString());
                        }

                    }
                }
            }else{
                Log.i("dfd","null");
            }
        }else{
            Log.i("dfd","error");
        }
    }

   public void onBusRouteSearched(BusRouteResult var1, int var2){

   }

    public void onDriveRouteSearched(DriveRouteResult var1, int var2){

    }

}
