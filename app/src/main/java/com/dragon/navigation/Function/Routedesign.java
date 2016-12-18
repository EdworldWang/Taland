package com.dragon.navigation.Function;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
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
    public Handler Routehandler;
    public void dodesign(RouteSearch.FromAndTo fromandtoint, int walkMode){
        this.walkMode=walkMode;
        this.fromandto=fromandto;
        RouteSearch routeSearch = new RouteSearch(this.mActivity);
        routeSearch.setRouteSearchListener(this);
        query = new RouteSearch.WalkRouteQuery(fromandtoint, walkMode);
        routeSearch.calculateWalkRouteAsyn(query);
    }
    public void setHandler(Handler handler){
        Routehandler=handler;
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

                    Message Insnum=new Message();
                    Insnum.what=1;
                    Insnum.arg1=walkStepsList.size();
                    Routehandler.sendMessage(Insnum);//初始化数组大小
                    //表示了由多少条信息

                    for(int i=0;i<walkStepsList.size();i++){
                        mywalkstep=walkStepsList.get(i);

                        Message routeInstrction=new Message();
                        routeInstrction.what=2;
                        routeInstrction.obj=mywalkstep.getInstruction()+","+
                        mywalkstep.getDuration()+","+mywalkstep.getDistance()+",";
                        // mywalkstep.getRoad()有时为空
                        //mywalkstep.getAction在前面都有信息了
                        Routehandler.sendMessage(routeInstrction);



                        Log.i("routedesign",mywalkstep.getInstruction());

                        myLatLonPointlist=mywalkstep.getPolyline();
                        Message pointnum=new Message();
                        pointnum.what=3;
                        pointnum.arg1=i;
                        pointnum.arg2=myLatLonPointlist.size();
                        Routehandler.sendMessage(pointnum);//int
                        for(int f=0;f<myLatLonPointlist.size();f++){
                            Log.i("routedesign",myLatLonPointlist.get(f).toString());
                            Message point=new Message();
                            point.what=4;
                            point.obj=myLatLonPointlist.get(f).toString();
                            Routehandler.sendMessage(point);
                        }

                    }
                    Message msg=new Message();
                    msg.what=9;
                    //规划完成
                    Routehandler.sendMessage(msg);
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
