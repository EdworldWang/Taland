package com.dragon.navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
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
import com.dragon.navigation.Control.Data;
import com.dragon.navigation.Function.Routedesign;
import com.dragon.navigation.util.NewWidget;
import com.dragon.navigation.util.scrollerlayout;

/**
 * Created by EdwardPC on 2016/12/9.
 */
public class fragmentone extends Fragment {
    private scrollerlayout[] layoutarray=new scrollerlayout[10];
    private  NewWidget[]  widgetarray=new NewWidget[10];

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.moveview, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        initnewview(10);
    }
    public void initnewview(int size){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        TextView ucan= (TextView) fm.findFragmentByTag("yourname").
                getView().findViewById(R.id.www);
        ucan.setText("woailuo");
        ucan.invalidate();
        FrameLayout isee= (FrameLayout) fm.findFragmentByTag("yourname").
                getView().findViewById(R.id.contentwidget);
        FrameLayout ican=(FrameLayout)View.inflate(getActivity(), R.layout.blanklayout,
                null);
        ican.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
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
        TextureView textureView=new TextureView(getActivity());
        ican.addView(textureView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
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
                    widgetarray[i].setTitle(String.valueOf(Data.AroundpoiList.get(i).getFirstbearing()));
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

}
