package com.dragon.navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.dragon.navigation.Control.Control;
import com.dragon.navigation.Control.Data;
import com.dragon.navigation.Control.Util;
import com.dragon.navigation.Function.Routedesign;
import com.dragon.navigation.util.NewWidget;
import com.dragon.navigation.util.Servicetype;
import com.dragon.navigation.util.scrollerlayout;

/**
 * Created by EdwardPC on 2016/12/9.
 */
public class fragmentone extends Fragment {
    private scrollerlayout[] layoutarray=new scrollerlayout[10];
    private  NewWidget[]  widgetarray=new NewWidget[10];
    private FragmentManager  fm;
    private FragmentTransaction transaction;
    private RelativeLayout isee;
    private RefreshviewThread   refreshviewthread;
    private  static final int UPDATE_VIEW = 1;
    public boolean ThreadIsCreated=false;

    public static int SelectID=-1;
    public static boolean IsSelected=false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.moveview, container, false);

    }

    @Override
    public void onStart(){
        super.onStart();
        initnewview(10);
        if(ThreadIsCreated==false) {
            refreshviewthread = new RefreshviewThread();
            refreshviewthread.start();
            ThreadIsCreated = true;
        }
    }

    public void onStop(){
        super.onStop();
    }
    public void onDestroy(){
        super.onDestroy();
        ThreadIsCreated=false;
    }
    public void initnewview(int size){
         fm = getFragmentManager();
       transaction = fm.beginTransaction();
        TextView ucan= (TextView) fm.findFragmentByTag("yourname").
                getView().findViewById(R.id.www);
        ucan.setText("woailuo");
        ucan.invalidate();
        isee= (RelativeLayout) fm.findFragmentByTag("yourname").
                getView().findViewById(R.id.contentwidget);
        FrameLayout ican=(FrameLayout)View.inflate(getActivity(), R.layout.blanklayout,
                null);
        isee.removeAllViews();
        //每次调用start都会先清除里面的view
        /*ican.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));*/
        for(int i=0;i<size;i++){
            layoutarray[i]=new scrollerlayout(getActivity());
            //  layoutarray[i].addView(widgetarray[i]);
        }
        for(int i=0;i<size;i++){
            LinearLayout.LayoutParams blue = new LinearLayout.LayoutParams(
                    200+30* Data.AroundpoiList.get(i).getPoiName().length(), 150);
            widgetarray[i]=new NewWidget(getActivity());
            //  widgetarray[i].setContent(String.valueOf(Data.AroundpoiList.get(i).getDistance()));
            widgetarray[i].setContent(size+"");
            widgetarray[i].setContentBackgroundColor(R.color.red);
            widgetarray[i].setTitle(Data.AroundpoiList.get(i).getPoiName());
            widgetarray[i].setTitleBackgroundColor(R.color.ivory);
            widgetarray[i].setTextSize(40);
            widgetarray[i].setTextColor(Color.WHITE);
            widgetarray[i].setLayoutParams(blue);
            widgetarray[i].setId(i);
            widgetarray[i].setmType(Data.AroundpoiList.get(i).getPoiDes());
            layoutarray[i].addView(widgetarray[i]);
            //   Log.i("dfds",i+"    "+layoutarray[i].getId()+"   "+widgetarray[i].getId());
            isee.addView(layoutarray[i], new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            layoutarray[i].smoothScrollBy(-500,-500,2000);
            widgetarray[i].setOnClickListener(new OnScrollerClick());

        }
        /*TextureView textureView=new TextureView(getActivity());
        ican.addView(textureView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));*/
        //mActivity.addContentView(ucan, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        //      ViewGroup.LayoutParams.MATCH_PARENT));

        //ican.bringToFront();
    }


    public class OnScrollerClick implements View.OnClickListener{
        public void onClick(View v){
            int id=v.getId();
            for (int i=0;i<widgetarray.length;i++){
                if(widgetarray[i].getId()==id){
                      /*  widgetarray[i].setTitle(widgetarray[i].getmType());
                        widgetarray[i].invalidate();*/
                    //widget做的改变需要用incalidate()才会改变
                    //解释了之前为什么settitle没有改变的原因
                  Data.SelectArroundId=i;
                    Data.IsSelectArround=true;
                    widgetarray[i].setTitle(Data.SelectArroundId+"");
                    widgetarray[i].invalidate();
                    layoutarray[i].smoothScrollBy(-500,-500,2000);
              /*      Routedesign myroute=new Routedesign(getActivity());
                    RouteSearch.FromAndTo fromAndTo=new RouteSearch.FromAndTo(Data.AroundpoiList.get(i).getMyLatLonPoint(),
                            new LatLonPoint(here.latitude,here.longitude));//出错了，都设置成经度
                    myroute.dodesign(fromAndTo,0);*/
                }
            }
        }
    }
    private class RefreshviewThread extends Thread{
        public void run(){
                while (true) {
                    try {
                        Thread.sleep(10000);
                        Control.candrawview = false;
                        Data.AroundpoiList.clear();
                        ArPoiSearch Arnear = new ArPoiSearch(getActivity(), "", "餐饮服务", "深圳市");
                        Arnear.setSearchtype(Servicetype.searchbound);
                        Arnear.doSearch();
                    } catch (InterruptedException e) {

                    }
                    Message msg = new Message();
                    msg.what = UPDATE_VIEW;
                    viewhandler.sendMessage(msg);
                }
        }
    }
    //UI线程用于处理view的移除。
    Handler viewhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VIEW:;
                    isee.removeAllViews();


                    break;
            }
            super.handleMessage(msg);
        }

    };

}
