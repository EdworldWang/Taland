package com.dragon.navigation.View;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Scroller;

import com.dragon.navigation.R;

/**
 * This file created by dragon on 2016/10/5 16:22,
 * belong to com.example.dragon.dynamicappend .
 */
public class NewWidget extends View {
    private static final String TAG="Scroller";
    public Scroller mScroller;
    /**
     * 文本
     */
    private String mTitle;
    private String mContent;
    private String mType;
    /**
     * 文本的颜色
     */
    private int mTextColor;
    /**
     * 文本的大小
     */
    private int mTextSize;
    //背景颜色
    private int mTitleBackgroundColor;
    private int mContentBackgroundColor;
    /**
     * 绘制时控制文本绘制的范围
     */
//    private Rect mBoundTitle;
//    private Rect mBoundContent;
    private Paint mPaint;
    private int Nid;
    private float destination;
 //   private float nowdegree;

    public int getNid() {
        return Nid;
    }

    public void setNid(int nid) {
        Nid = nid;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public void setDestination(float Destination){
        this.destination=Destination;
    }
    public float getDestination(){
        return destination;
    }
    boolean IsSelected=false;
    public NewWidget(Context context, AttributeSet attrs)
    {

        this(context, attrs, 0);
//        init();
    }
    public NewWidget(Context context)
    {

        this(context, null);
//        init();

    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public NewWidget(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
//        mTextSize=22;
        mScroller = new Scroller(context);
        destination=-20.f;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NewWidget, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.NewWidget_title:
                    mTitle = a.getString(attr);
                    break;
//默认标题背景颜色绿
                case R.styleable.NewWidget_titleBackgroundColor:
                    mTitleBackgroundColor = a.getColor(attr, Color.GREEN);
                    break;
//默认内容背景颜色黄
                case R.styleable.NewWidget_contentBackgroundColor:
                    mContentBackgroundColor = a.getColor(attr,Color.YELLOW);
                    break;
                case R.styleable.NewWidget_content:
                    mContent = a.getString(attr);
                    break;
                case R.styleable.NewWidget_textColor:
                    // 默认颜色设置为黑色
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.NewWidget_textSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;

            }

        }
        a.recycle();
        init();
    }
    private void init(){
        /**
         * 获得我们所定义的自定义样式属性
         */


        /**
         * 获得绘制文本的宽和高，初始化画笔
         */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setTextAlign(Paint.Align.CENTER);
    }


    public void setTitle(String title){
        mTitle = title;
//        自定义控件的属性发生改变之后，invalidate()方法让系统去调用view的onDraw()重新绘制
//        invalidate();
//        控件属性的改变可能导致控件所占的大小和形状发生改变，所以我们需要调用requestLayout()来请求测量获取一个新的布局位置
//        requestLayout();
    }
    public void setContent(String content){
        mContent=content;
    }
    public void setTitleBackgroundColor(int color){
        mTitleBackgroundColor = color;
    }
    public void setContentBackgroundColor(int color){
        mContentBackgroundColor = color;
    }
    public void setTextSize(int size){
        mTextSize = size;
    }
    public void setTextColor(int color){
        mTextColor=color;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mContentBackgroundColor);
        RectF re1 = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRoundRect(re1,20,20,mPaint);
//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mTitleBackgroundColor);
        RectF re2 = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()/2);
        canvas.drawRoundRect(re2,20,20,mPaint);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight()/2, mPaint);
        mPaint.setColor(mTextColor);
        canvas.drawText(mTitle, getWidth() / 2 , getHeight() / 3 +10 , mPaint);

        mPaint.setColor(mTextColor);
        canvas.drawText(mContent, getWidth() / 2 , getHeight()-getHeight()/7 , mPaint);
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
    }

    public void SelectedDraw(int color){
        mTitleBackgroundColor=color;
        mContentBackgroundColor=color;
        invalidate();
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

    public void smoothScrollTo(int fx,int fy){
        int dx=fx-mScroller.getFinalX();
        int dy=fy-mScroller.getFinalY();
        smoothScrollBy(dx,dy);
    }
    public void smoothScrollTo(int fx,int fy,int duration){
        int dx=fx-mScroller.getFinalX();
        int dy=fy-mScroller.getFinalY();
        smoothScrollBy(dx,dy,duration);
    }



}
