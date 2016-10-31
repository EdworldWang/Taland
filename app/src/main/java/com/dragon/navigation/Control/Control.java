package com.dragon.navigation.Control;

/**
 * Created by EdwardPC on 2016/10/13.
 */
public class Control {
    public static boolean displayPoi=false;
    public static boolean moveanimation=true;
    public static boolean movefastest=true;


    public static int CalMovespeed(){
        int distance=Math.abs(Data.movedistance-Data.alreadymove);
        if(distance>=500){
           return 50;
        }else{
            if(distance>=250){
                return 25;
            }else{
                return 5;
            }
        }
    }

}
