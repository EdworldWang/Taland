package com.edward.navigation.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by EdwardPC on 2016/11/1.
 */
public class scrollerlayout extends RelativeLayout {
    private Scroller mScroller;

    public scrollerlayout(Context context, AttributeSet attrs){
        super(context,attrs);
        mScroller=new Scroller(context);
    }

    public scrollerlayout(Context context){
        super(context);
        mScroller=new Scroller(context);
    }

    public void computeScroll(){
        if(mScroller.computeScrollOffset()){
            //这里调用View的scrollto完成实际的滚动
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    public void smoothScrollBy(int dx,int dy){
        //设置mscroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(),mScroller.getFinalY(),dx,dy);
        invalidate();//这里必须调用invalidate()才能保证computescroll()会被调用，
        // 否则不一定会刷新界面，看不到滚动效果
    }

    public void smoothScrollBy(int dx,int dy,int duration){
        //设置mscroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(),mScroller.getFinalY(),dx,dy,duration);
        invalidate();//这里必须调用invalidate()才能保证computescroll()会被调用，
        // 否则不一定会刷新界面，看不到滚动效果
    }

    public void smoothScrollTo(int fx,int fy,int duration){
        int dx=fx-mScroller.getFinalX();
        int dy=fy-mScroller.getFinalY();
        smoothScrollBy(dx,dy,duration);
    }

    public void smoothScrollTo(int fx,int fy){
        int dx=fx-mScroller.getFinalX();
        int dy=fy-mScroller.getFinalY();
        smoothScrollBy(dx,dy);
    }
}
