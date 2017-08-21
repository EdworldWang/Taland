package com.edward.navigation.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by EdwardPC on 2016/12/1.
 */
public class Compassview extends Mytestview {
    private Paint mPaint;
    private float mR, mCx, mCy;//mR为半径,mCx,mCy为圆心
    private static final int mN = 6;
    private static final float DEGREES_UNIT = 360 / mN; //正N边形每个角  360/mN能整除
    private int Reccolor;
    private int Cirlecolor;
    public Compassview(Context context) {
        this(context, null);
    }

    public Compassview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Compassview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Canvas canvas=new Canvas();
        mPaint = new Paint();
        mR=200;
        //红色不动的那部分
        mPaint.setColor(0x8cdc143c);
        float Reclinewidth=(float) 2;
        mPaint.setStrokeWidth(Reclinewidth);//设置线宽
        mPaint.setStyle(Paint.Style.FILL);
        RectF rect = new RectF(mCx-mR,mCy-mR,mCx+mR,mCy+mR);
        canvas.drawArc(rect,-120,60,true,mPaint);
    }
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float mW = getMeasuredWidth();
        float mH = getMeasuredHeight();

        mCx = mW / 2;
        mCy = mH / 2;
        mR = Math.min(mCx, mCy) / 4 * 3;
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
