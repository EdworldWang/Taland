package com.dragon.navigation.Control;

/**
 * Created by EdwardPC on 2016/10/13.
 */
public class Data {
    public static float[] ProjectionMatrix={
            0.0f , -1.69032f ,  0.0f  , 0.0f,
           -3.0050135f ,  0.0f ,  0.0f  , 0.0f,
            0.0f ,  -0.0015625f  , 1.004008f ,  1.0f,
           0.0f ,  0.0f  , -20.040081f ,  0.0f
        };
   public static float[] modelViewMatrix=  {
           0, -1, 0, 0,
            -1, 0, 0, 0,
            0, 0, -1.0f, 0,
            0, 0, 4000, 1};
    public static float degree;
    public static boolean getfirstdegree=false;
    public static float firstdegree;
    public static int predegree;
    public static boolean modelDrawed=false;
    public static float getMoveX(float degree){

        float disdegee=degree-firstdegree;
        if(disdegee>180){
            disdegee=360-disdegee;
        }
        if(disdegee<-180){
            disdegee=360+disdegee;
        }
        float X=(float) (2000/90*disdegee);
        return X;
    }

}
