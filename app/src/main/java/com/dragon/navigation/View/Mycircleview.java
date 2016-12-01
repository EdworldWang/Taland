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
 * Created by EdwardPC on 2016/12/1.
 */
public class Mycircleview extends View{
    private int center;
    private static final String TAG="Scroller";
    public Scroller mScroller;
    /**
     * 文本
     */
    private String mTitle;
    private String mContent;
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
    public Mycircleview(Context context)
    {
        this(context,null);
//        init();
    }
    public Mycircleview(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    public Mycircleview(Context context, AttributeSet attrs, int defStyle)
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
        mPaint.setColor(mTitleBackgroundColor);
        RectF re1 = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRoundRect(re1,20,20,mPaint);
//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mContentBackgroundColor);
        RectF re2 = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()/2);
        canvas.drawRoundRect(re2,20,20,mPaint);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight()/2, mPaint);
        mPaint.setColor(mTextColor);
        canvas.drawText(mTitle, getWidth() / 2 , getHeight() / 3 +10 , mPaint);

        mPaint.setColor(mTextColor);
        canvas.drawText(mContent, getWidth() / 2 , getHeight()-getHeight()/7 , mPaint);
    }
}
