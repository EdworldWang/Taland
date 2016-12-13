package com.dragon.navigation;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
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
    private ScrollThread scrollThread;

    private  static final int UPDATE_VIEW = 1;
    private  static final int SCROLL_VIEW = 2;
    public boolean ThreadIsCreated=false;
    private int viewnum=10;
    public static int SelectID=-1;
    public static boolean IsSelected=false;

    private float movex=0;
    private float movey=0;
    private float prex=0;
    private float prey=0;

    public static boolean IsSelectView;

    private int NoSelectTiTleColor=0x7f0a00ca;//蔚蓝色
    private int NoSelectContentColor=0x7f0a0077;//透明深蓝
    private int SelectedTitleColor=0x7fff4500;//橘红色
    private int SelectedContentColor;
    //-1表示没有人被选择
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.moveview, container, false);

    }

    @Override
    public void onStart(){
        super.onStart();
        initnewview(viewnum);

        if(ThreadIsCreated==false) {
          //  refreshviewthread = new RefreshviewThread();
         //   refreshviewthread.start();
           scrollThread =new ScrollThread();
           scrollThread.start();
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
        isee.setOnClickListener(new OnScrollerClick());
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
             widgetarray[i].setContent(String.valueOf(Data.AroundpoiList.get(i).getDistance()));
         //   widgetarray[i].setContent(size+"");
            widgetarray[i].setContentBackgroundColor(NoSelectContentColor);
            //0x7ffff4500
            widgetarray[i].setTitle(Data.AroundpoiList.get(i).getPoiName());
            widgetarray[i].setTitleBackgroundColor(NoSelectTiTleColor);
            widgetarray[i].setTextSize(40);
            widgetarray[i].setTextColor(Color.WHITE);
            widgetarray[i].setLayoutParams(blue);
            widgetarray[i].setId(i);
            widgetarray[i].setmType(Data.AroundpoiList.get(i).getPoiDes());
          layoutarray[i].addView(widgetarray[i]);
            //   Log.i("dfds",i+"    "+layoutarray[i].getId()+"   "+widgetarray[i].getId());
            isee.addView(layoutarray[i],new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            layoutarray[i].smoothScrollBy((int)-Data.screenWidth/2+widgetarray[i].getLayoutParams().width/2,
                    -100-i*90,1000);
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
            if(id==R.id.contentwidget){
                // Log.i("hello","good");给外面的relativelayout加事件后，
                //点击空白处，此处可行，说明接收到了事件
                    if(SelectID!=-1) {
                        //说明有view被选择了
                        widgetarray[SelectID].SelectedDraw(0x7f0a00ca);
                        SelectID=-1;
                        //没有view被选择
                    }
            }
            for (int i=0;i<widgetarray.length;i++){
                if(widgetarray[i].getId()==id){
                      /*  widgetarray[i].setTitle(widgetarray[i].getmType());
                        widgetarray[i].invalidate();*/
                    //widget做的改变需要用incalidate()才会改变
                    //解释了之前为什么settitle没有改变的原因
                    if(SelectID!=-1) {
                        //说明之前已经点击了一个地方
                        //先将之前的view设置回正常形态
                        //再重新绘制点击的view
                        //设置被选择的id
                        widgetarray[SelectID].SelectedDraw(NoSelectTiTleColor);
                    }
                    widgetarray[i].SelectedDraw(SelectedTitleColor);
                    SelectID=i;
                    layoutarray[i].smoothScrollBy(-500, -500, 2000);
              /*      Routedesign myroute=new Routedesign(getActivity());
                    RouteSearch.FromAndTo fromAndTo=new RouteSearch.FromAndTo(Data.AroundpoiList.get(i).getMyLatLonPoint(),
                            new LatLonPoint(here.latitude,here.longitude));//出错了，都设置成经度
                    myroute.dodesign(fromAndTo,0);*/

                }
            }


        }
    }
    public void textdrawview(){
                NewWidget mytext=new NewWidget(getActivity());
                mytext.setTitle("liangyu");

    }
    public void getscrollviewxy(float currentdegree,float currenty){

            ValueAnimator animatordegree = ValueAnimator.ofFloat(prex,currentdegree);
            animatordegree.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    movex = (float)animation.getAnimatedValue()*18;
                    doscroll();
                }
            });
            prex=currentdegree;
            animatordegree.setDuration(450);
            animatordegree.setInterpolator(new LinearInterpolator());
            animatordegree.start();

        ValueAnimator animatory = ValueAnimator.ofFloat(prey,currenty);
        animatory.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                movey = (float)animation.getAnimatedValue()*36;
                doscroll();
            }
        });
        prey=currenty;
        animatory.setDuration(350);
        animatory.setInterpolator(new AccelerateDecelerateInterpolator());
        animatory.start();
    }

    private void doscroll(){
            layoutarray[0].smoothScrollTo(-500,(int)movey);
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

    private class ScrollThread extends Thread{
        public void run(){
            while (true) {
                try{
                    sleep(350);
                    Message msg = new Message();
                    msg.what = SCROLL_VIEW;
                    viewhandler.sendMessage(msg);
                }catch (InterruptedException e){

                }

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
                case SCROLL_VIEW:;
                    getscrollviewxy(Data.q[0],Data.yangle);


                    break;
            }
            super.handleMessage(msg);
        }

    };

}
