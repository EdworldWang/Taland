package com.dragon.navigation.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.dragon.navigation.Control.Data;
import com.dragon.navigation.R;

/**
 * Created by EdwardPC on 2016/12/1.
 */
public class Mytestview extends View {

        private Paint mPaint;
        private float mR, mCx, mCy;//mR为半径,mCx,mCy为圆心
        private static final int mN = 6;
        private static final float DEGREES_UNIT = 360 / mN; //正N边形每个角  360/mN能整除
        private int Reccolor;
        private int Cirlecolor;
    private float Rotatedegree=0;
        public Mytestview(Context context) {
            this(context, null);
        }

        public Mytestview(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public Mytestview(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

            mPaint = new Paint();
        }

        @Override
        //这个方法会在view的大小改变后被调用
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            float mW = getMeasuredWidth();
            float mH = getMeasuredHeight();

            mCx = mW / 2;
            mCy = mH / 2;
            mR = Math.min(mCx, mCy) / 4 * 3;
        }
    public void setReccolor(int color){
        Reccolor = color;
    }
    public void setCirlecolor(int color){
        Cirlecolor = color;
    }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            mR=200;//单位为dip换成dp会好点
            //这是半径大小
            //跟后面的distance进行换算，500即500以上
            mPaint.setColor(0xffffffff);
            //如果只是简单的调用R.color里面的颜色的话会出现大多数颜色都是蓝色的情况，
            // 原因是R.color里面的颜色大多都是GBA,并没有A,故需要设置A值

            mPaint.setStyle(Paint.Style.STROKE);
            float Reclinewidth=(float) 2;
          mPaint.setStrokeWidth(Reclinewidth);//设置线宽
            //Paint.Style.FILL设置画笔为实心，STROKE为空心
            //该方法用于设置画笔的空心线宽。该方法在矩形、圆形等图形上有明显的效果。
            //对直线无效


            float d = (float) (2 * mR * Math.sin(Math.toRadians(DEGREES_UNIT / 2)));
            float c = mCy - mR;
            float y = (d * d + mCy * mCy - c * c - mR * mR) / (2 * (mCy - c));
            float x = (float) (mCx + Math.sqrt(-1 * c * c + 2 * c * y + d * d - y * y));
            //每次画两条线，一条连接圆心（mCx,mCy）和圆心正上方的角点（mCx,c)
            //一条直线为圆心正上方的角点（mCx,c)跟右边的角点（x,y)



            canvas.rotate(Rotatedegree,mCx,mCy);//add to test0


            canvas.rotate(DEGREES_UNIT/2,mCx,mCy);
            for (int i = 0; i < mN; i++) {
                canvas.save();
                canvas.rotate(DEGREES_UNIT*i , mCx, mCy);
                canvas.drawLine(mCx, mCy, mCx, c, mPaint);
                canvas.drawLine(mCx, c, x, y, mPaint);
                canvas.restore();
            }
            //画的是正六边形为了摆正，开始出现的错误在于旋转的角度为DEGREES_UNIT ，
            // 跟原来的形状无区别，修改为DEGREES_UNIT /2就好了
            //之后如果需要绘制除圆形外的其他矩形的话需要将角度旋转回去

            //rotate后面设置的是旋转中心，如果不填则相对屏幕左上角进行旋转
            //并且rotate不会对之前所画的东西进行旋转，比起画布的旋转来说这里更像是一个人位置的移动
            //而观众观察视图的视角并不会改变

            //下面加上Reclinewidth/2,是为了消除出来的一点边界
            canvas.drawCircle(mCx,mCy,mR/3,mPaint);
            canvas.drawCircle(mCx,mCy,mR/3*2,mPaint);
            canvas.drawCircle(mCx,mCy,mR+Reclinewidth/2,mPaint);

            canvas.rotate(-DEGREES_UNIT/2,mCx,mCy);



            canvas.rotate(-Rotatedegree,mCx,mCy);
            //以上为需要旋转的部分，下面为不旋转的部分
            RectF rect = new RectF(mCx-mR,mCy-mR,mCx+mR,mCy+mR);
            //drawArc(rect,0,90,true,paint)
            // 弧线所使用的矩形区域大小,开始角度,扫过的角度,是否使用中心
            //开始的角度为x轴正向，是否使用中心需要设置画笔的风格为实心
            mPaint.setColor(0x8c66cdaa);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawArc(rect,-120,60,true,mPaint);
           /* save：用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。 restore：用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
            对canvas中特定元素的旋转平移等操作实际上是对整个画布进行了操作，
            所以如果不对canvas进行save以及restore，
            那么每一次绘图都会在上一次的基础上进行操作，最后导致错位。
            比如说你相对于起始点每次30度递增旋转，30，60，90.如果不使用save
            以及 restore 就会变成30, 90, 150，
            每一次在前一次基础上进行了旋转。save是入栈，restore是出栈。*/
            //并不会保存截图，而是保存了canvas的状态，保存旋转0度的情况，这里若把旋转的*i去掉
            //相应的canvas.save()和restore()也可以去掉。
            mPaint.setColor(0x9b0b0101);
            canvas.drawCircle(mCx,mCy,mR,mPaint);

            canvas.rotate(Rotatedegree,mCx,mCy);
            if(Data.poinum!=0) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setStrokeWidth(2.5f);
                mPaint.setColor(0xffffffff);
                float drawDistance=0;
                for (int i = 0;i<Data.AroundpoiList.size();i++){
                    canvas.save();
                    //各个地方旋转的时候要注意旋转的中心设置
                    canvas.rotate(Data.AroundpoiList.get(i).getFirstbearing(),mCx,mCy);
                    float distance=Data.AroundpoiList.get(i).getDistance();
                    if(distance>=500){
                        drawDistance=198;
                    }else{
                        drawDistance=(Data.AroundpoiList.get(i).getDistance()/2.5f);
                    }
                    canvas.drawCircle(mCx,mCy-drawDistance,4,mPaint);

                }
                //单独画这个点,若在上面先画则会使得后面画的白点盖住了红点，达不到优先展示
              /*  if(Data.IsSelectArround) {
                    canvas.save();
                     canvas.rotate(Data.AroundpoiList.get(Data.SelectArroundId).getFirstbearing(), mCx, mCy);
                    float distance = Data.AroundpoiList.get(Data.SelectArroundId).getDistance();
                    if (distance >= 500) {
                        drawDistance = 198;
                    } else {
                        drawDistance = (Data.AroundpoiList.get(Data.SelectArroundId).getDistance() / 2.5f);
                    }
                    mPaint.setColor(0xffff0000);
                    canvas.drawCircle(mCx, mCy - drawDistance, 4, mPaint);
                    canvas.restore();
                }*/
            }
            canvas.rotate(-Rotatedegree,mCx,mCy);
        }
    public void doRotatetaAnim(float currentdegree,float todegree){
        ValueAnimator animator = ValueAnimator.ofFloat(currentdegree,todegree);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Rotatedegree = (float)animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(200);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
