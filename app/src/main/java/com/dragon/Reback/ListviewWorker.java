package com.dragon.Reback;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dragon.navigation.Adapter.SearchpoiAdapter;
import com.dragon.navigation.Control.Data;
import com.dragon.navigation.Model.SearchpoiEntity;
import com.dragon.navigation.R;

/**
 * Created by EdwardPC on 2016/12/17.
 */
public class ListviewWorker implements ListviewCallBack {
    private Activity mActivity;
    private SearchpoiAdapter mAdapter;
    private LinearLayout lin;
    private ListView listView;
    private Handler handler;
    public ListviewWorker(Activity activity,LinearLayout lin,Handler handler){
        mActivity=activity;
        this.lin=lin;
        this.handler=handler;
    }
    @Override
    public void makeList() {
        Log.i("Worker","listview is searched=.="+this.toString());
        lin.removeAllViews();
        mAdapter=new SearchpoiAdapter(mActivity, Data.SearchpoiList);
        LinearLayout father = (LinearLayout) View.inflate(mActivity, R.layout.searchlistview,
                null);
       listView = (ListView) father.findViewById(R.id.listView);
        father.removeView(listView);
        listView.setAdapter(mAdapter);
        listView.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new OnListItemClick());
        lin.addView(listView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                  LinearLayout.LayoutParams.WRAP_CONTENT));
        Log.i("Worker","hear");
    }

    public class OnListItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            SearchpoiEntity SelectedEntity=mAdapter.getItem(position);
            Log.i("ArPoiSearch",SelectedEntity.getPoiName()+"   "+SelectedEntity.getDistance()+
                    "   "+SelectedEntity.getFirstbearing());
           //采用handler将信息在Activity中处理
            //
            Data.SelectedEntity=SelectedEntity;
            /*fragmenttwo fragGuide = new fragmenttwo();
            //  fragGuide.setDestination(msg.obj);
            //下面的参数可以缺省
            FragmentTransaction ftguide=mActivity.getFragmentManager().beginTransaction();
            ftguide.add(R.id.fragmenttwo, fragGuide,"guide");
            ftguide.commit();*/
            Message msg=new Message();
            msg.what=5;
            handler.sendMessage(msg);


        }
    }
}
