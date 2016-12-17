package com.dragon.navigation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon.navigation.Model.SearchpoiEntity;

/**
 * Created by EdwardPC on 2016/12/17.
 */
public class fragmenttwo extends Fragment {
    SearchpoiEntity destination;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.guide_view, container, false);

    }

    public void onStart() {
        super.onStart();
    }
    public void onResume(){
        super.onResume();
    }
    public void onStop(){
        super.onStop();
    }
    public void onDestroy(){
        super.onDestroy();
    }

    public void setDestination(SearchpoiEntity destination){
        this.destination=destination;
    }

}
