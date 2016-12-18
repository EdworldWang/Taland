package com.dragon.navigation;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.dragon.navigation.Control.Data;
import com.dragon.navigation.Control.Util;
import com.dragon.navigation.Function.Routedesign;
import com.dragon.navigation.Model.SearchpoiEntity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdwardPC on 2016/12/17.
 */
public class fragmenttwo extends Fragment {
    Handler myhandler;
    SearchpoiEntity destinationEntity;
    LatLonPoint destinationpoint;
    ArrayList<String> Instruction=new ArrayList<String>();
    ArrayList<String> Distance=new ArrayList<String>();
    ArrayList<String> Duration=new ArrayList<String>();
    ArrayList<String> ListLatLonPoint=new ArrayList<String>();
    float Alldistance=0;
    float bearing=0;
    private int[] Instructionnum;
    private float rotatez=0;
    private Location next;

    private Location Locationdestination;
    private Location   Locationhere;
    private float prebearing=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.guide_view, container, false);

    }
    public void setHandler(Handler mainhandler){
        myhandler=mainhandler;
    }
    public void onStart() {
        super.onStart();
        destinationEntity=Data.SelectedEntity;
        destinationpoint=destinationEntity.getMyLatLonPoint();
      next=new Location("next");
        Locationhere=new Location("LocHere");
        Routedesign mydesign=new Routedesign(getActivity());
        mydesign.setHandler(fraghandler);
        mydesign.dodesign(new RouteSearch.FromAndTo(new LatLonPoint(Data.locationhere.getLatitude(),
                Data.locationhere.getLongitude()),destinationpoint
                ),0);
    }

    private void initview(){
           View guide= getFragmentManager().findFragmentByTag("guide").getView();
       TextView distance= (TextView)guide.findViewById(R.id.distance);//距离
        TextView alldistanceview=(TextView)guide.findViewById(R.id.alldistance);
        TextView needtime=(TextView)guide.findViewById(R.id.needtime);
        TextView arrivetime=(TextView)guide.findViewById(R.id.arrivetime);
        TextView guidedescription=(TextView)guide.findViewById(R.id.guidedescription);
       guidedescription.setText(Instruction.get(0));
        for(int i=0;i<Distance.size();i++){
            Alldistance+=Float.parseFloat(Distance.get(i));
        }
        String[] nextdes=ListLatLonPoint.get(0).split(",");
      // new Double.parseDouble(nextdes[0])
       distance.setText(Distance.get(0)+"米");
        needtime.setText(Calculatetime(Duration.get(0)));
        alldistanceview.setText("约"+(int)Alldistance+"米");


        doarrow();
    }
    Handler fraghandler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    Instructionnum=new int[msg.arg1];
                    break;
                case 2:
                    String[] message=msg.obj.toString().split(",");
                    Instruction.add(message[0]);
                    Duration.add(message[1]);
                    Distance.add(message[2]);
                    break;
                case 3:
                    Instructionnum[msg.arg1]=msg.arg2;
                    break;
                case 4:
                    ListLatLonPoint.add(msg.obj.toString());
                    break;
                case 9:
                    initview();
                    break;

            }
            super.handleMessage(msg);
        }

    };
    public void onResume(){
        super.onResume();
    }
    public void onStop(){
        super.onStop();
    }
    public void onDestroy(){
        super.onDestroy();
    }
    private class TimeThread extends Thread{
        public void run(){
            while (true){
                try {
                    Thread.sleep(2000);
                }catch(InterruptedException e){

                }
            }
        }

    }
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }
    private String Calculatetime(String ss){
        Float fs=Float.parseFloat(ss);
        if(fs<60){
            return "1分钟内";
        }else{
            return "约"+(int)(fs/60)+"分钟";
        }
    }
    private void doarrow(){
        Locationhere.setLatitude(ArPoiSearch.here.latitude);
        Locationhere.setLongitude(ArPoiSearch.here.longitude);
        String[]  Pointmessage=  ListLatLonPoint.get(0).split(",");
        next.setLatitude(Float.parseFloat(Pointmessage[0]));
        next.setLongitude(Float.parseFloat(Pointmessage[0]));
        final float startBearing =  next.bearingTo(Locationhere);
        Data.bearing = Util.positiveModulo(startBearing - Data.currentAzimuth,
                360);
        //    float realbearing=(-(Data.bearing-180)-Data.currentAzimuth)%360;
        float realbearing=(180-Data.bearing-Data.currentAzimuth+360)%360;
        Data.realbearing=realbearing;
        ValueAnimator animatorbearing = ValueAnimator.ofFloat(prebearing,realbearing);
        animatorbearing.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotatez = (float)animation.getAnimatedValue();
                MainRenderer.setbearing(rotatez);
            }
        });
        prebearing=realbearing;
        animatorbearing.setDuration(250);
        animatorbearing.setInterpolator(new AccelerateInterpolator());
        animatorbearing.start();
    }

    public void remove(){
        Instruction.clear();
        Distance.clear();
        Duration.clear();
        ListLatLonPoint.clear();
    }
}
