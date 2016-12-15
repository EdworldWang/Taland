package com.dragon.navigation;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.IndoorData;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.RouteSearch;
import com.dragon.navigation.Adapter.SearchpoiAdapter;
import com.dragon.navigation.Adapter.TravelingAdapter;
import com.dragon.navigation.Control.Control;
import com.dragon.navigation.Control.Data;
import com.dragon.navigation.Control.Util;
import com.dragon.navigation.Function.ResGeoCoding;
import com.dragon.navigation.Function.Routedesign;
import com.dragon.navigation.Model.SearchpoiEntity;
import com.dragon.navigation.Model.TravelingEntity;
import com.dragon.navigation.util.NewWidget;
import com.dragon.navigation.util.Servicetype;
import com.dragon.navigation.util.ToastUtil;
import com.dragon.navigation.util.scrollerlayout;

import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;

/**
 * This file created by dragon on 2016/9/20 15:46,
 * belong to com.dragon.navigation .
 * 关于搜索的类
 */
public class ArPoiSearch implements PoiSearch.OnPoiSearchListener{
    private Activity mActivity;
    //输入搜索关键字
    private String mKeyWord;
    //搜索的风格：如酒店、美食、公交等
    private String mStylePoi;
    //城市名和区号：如果为空代表全国
    private String mCityCode;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiResult poiResult; // poi返回的结果
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private int currentPage = 0;// 当前页面，从0开始计数
    private List<PoiItem> poiItems;
    private List<SuggestionCity> suggestionCities;
    //  设置每页最多返回多少条poiitem
    private int mPoiItems=10;
    private int poiOrSuggestion=1;
    private PoiResult resultList;
    private LinearLayout lin;
    private LinearLayout.LayoutParams LP_FW;
    private float distance;
    public static LatLng here;

   // private List<SearchpoiEntity> SearchpoiList = new ArrayList<>();
    // ListView数据  用于存放搜索出来的结果,把数据用于Data里面
    private ListView listView;
    private SearchpoiAdapter mAdapter;
    public static int size;
    //在location处被赋值

    Servicetype searchtype;


    private scrollerlayout[] layoutarray=new scrollerlayout[10];
    private  NewWidget[]  widgetarray=new NewWidget[10];

    public void setActivity(Activity mActivity){
        this.mActivity = mActivity;
    }
    public void setKeyWord(String mKeyWord){
        this.mKeyWord = mKeyWord;
    }
    public void setStylePoi(String mStylePoi){
        this.mStylePoi = mStylePoi;
    }
    public void setCityCode(String mCityCode){
        this.mCityCode = mCityCode;
    }
    //    构造函数
    public ArPoiSearch(){

    }
    //    构造函数
    public ArPoiSearch(Activity activity){
        this.mActivity = activity;

    }
    //    构造函数
    public ArPoiSearch(Activity activity,String mKeyWord){
        this.mActivity = activity;
        this.mKeyWord = mKeyWord;
        this.mStylePoi = "";
        this.mCityCode = "";

    }

    public ArPoiSearch(Activity activity,String mKeyWord, String mStylePoi, String mCityCode,LinearLayout lin){
        this.mActivity= activity;
        this.mKeyWord = mKeyWord;
        this.mStylePoi = mStylePoi;
        this.mCityCode = mCityCode;
        this.lin = lin;
        this.LP_FW = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public ArPoiSearch(Activity activity,String mKeyWord, String mStylePoi, String mCityCode){
        this.mActivity= activity;
        this.mKeyWord = mKeyWord;
        this.mStylePoi = mStylePoi;
        this.mCityCode = mCityCode;
    }
    public void setSearchtype(Servicetype searchtype) {
        this.searchtype = searchtype;
    }

    public void doSearch(){
        showProgressDialog();// 耗时操作前，显示进度框
        currentPage = 0;
        ButterKnife.bind(this.mActivity);
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
     //query = new PoiSearch.Query(this.mKeyWord, "", this.mCityCode);
        //搜索类型不应该为“”
          query = new PoiSearch.Query(this.mKeyWord, mStylePoi, this.mCityCode);
        query.setPageSize(this.mPoiItems);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页);//设置查询页码
//        poiSearch.setOnPoiSearchListener(this);


        poiSearch = new PoiSearch(this.mActivity, query);//兴趣点搜索
        if(this.searchtype==Servicetype.searchbound) {
            poiSearch.setBound(new PoiSearch.SearchBound(
                    new LatLonPoint(here.latitude,here.longitude),1000));
        }
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }


    public class OnListItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            SearchpoiEntity SelectedEntity=mAdapter.getItem(position);
            Log.i("ArPoiSearch",SelectedEntity.getPoiName()+"   "+SelectedEntity.getDistance()+
                    "   "+SelectedEntity.getFirstbearing());

        }
    }



    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始

//                    ToastUtil.show(this.mActivity,poiItems.get(0)+","+poiItems.get(1));
                //    LatLng var0= convertToLatLng(poiItems.get(1).getLatLonPoint());
                //    LatLng var1= convertToLatLng(poiItems.get(2).getLatLonPoint());
                    /*lin.removeAllViews();*/
                    if(searchtype==Servicetype.searchbound) {
                        Log.i("fdd","我来了"+"数据为"+poiItems.size());
                        for (int i = 0; i < poiItems.size(); i++) {
                            LatLng var0 = convertToLatLng(poiItems.get(i).getLatLonPoint());
                            ResGeoCoding resserver = new ResGeoCoding(this.mActivity);
                            resserver.doSearch(poiItems.get(i).getLatLonPoint());
                             /*   SearchpoiList.add(new SearchpoiEntity(poiItems.get(i).toString(),
                                        poiItems.get(i).getTypeDes(),poiItems.get(i).getDistance()));*/
                            PoiItem MyResult = poiItems.get(i);
                            distance = AMapUtils.calculateLineDistance(var0, here);
                            float mybearing = Util.getBearing(convertToLocation(MyResult.toString(),MyResult.getLatLonPoint()));
                            Data.AroundpoiList.add(new SearchpoiEntity(MyResult.toString(),
                                    MyResult.getTypeDes(), (int) distance,mybearing,
                                    MyResult.getSnippet() != null ? MyResult.getAdName() + MyResult.getSnippet()
                                            : MyResult.getAdName() + MyResult.getBusinessArea() + MyResult.getDirection(), poiItems.get(i).getLatLonPoint()));
                            //snippet 片段，即poi点的详细位置数据，需要加上区地址好点
                            // 有时会返回为空，设置时，若为空则设置为其区地址
                            //之前才用poiItems.get(i).getdistance()，返回值会出现-1的情况，
                            //原因是距离过大。所以采用自己计算

                        }
                        Data.poinum=poiItems.size();
                        lin.removeAllViews();
                        FragmentManager fm = mActivity.getFragmentManager();
                        FragmentTransaction transaction = fm.beginTransaction();
                        fragmentone mWeixin = new fragmentone();
                        //下面的参数可以缺省
                        transaction.add(R.id.fragments, mWeixin,"yourname");
                        transaction.commit();
                       // initnewview(poiItems.size());
                    }else if(searchtype==Servicetype.searchnear_view) {
                        for (int i = 0; i < poiItems.size(); i++) {
                            LatLng var0 = convertToLatLng(poiItems.get(i).getLatLonPoint());
                            ResGeoCoding resserver = new ResGeoCoding(this.mActivity);
                            resserver.doSearch(poiItems.get(i).getLatLonPoint());
                            PoiItem MyResult = poiItems.get(i);
                            distance = AMapUtils.calculateLineDistance(var0, here);
                            float mybearing = Util.getBearing(convertToLocation(MyResult.toString(),MyResult.getLatLonPoint()));
                            Data.SearchpoiList.add(new SearchpoiEntity(MyResult.toString(),
                                    MyResult.getTypeDes(), (int) distance,mybearing,
                                    MyResult.getSnippet() != null ? MyResult.getAdName() + MyResult.getSnippet()
                                            : MyResult.getAdName() + MyResult.getBusinessArea() + MyResult.getDirection(), MyResult.getLatLonPoint()));
                            //snippet 片段，即poi点的详细位置数据，需要加上区地址好点
                            // 有时会返回为空，设置时，若为空则设置为其区地址
                            //之前才用poiItems.get(i).getdistance()，返回值会出现-1的情况，
                            //原因是距离过大。所以采用自己计算



                        }
                        mAdapter = new SearchpoiAdapter(this.mActivity, Data.SearchpoiList);
                        LinearLayout father = (LinearLayout) View.inflate(this.mActivity, R.layout.searchlistview,
                                null);
                        listView = (ListView) father.findViewById(R.id.listView);
                        father.removeView(listView);
                        listView.setAdapter(mAdapter);
                        listView.setVisibility(View.VISIBLE);
                        listView.setOnItemClickListener(new OnListItemClick());
                        lin.addView(listView, LP_FW);
                    }






//                    poiItems.get(1).getTitle()+","+poiItems.get(1).getSnippet()
                   // ToastUtil.show(this.mActivity,","+ AMapUtils.calculateLineDistance(var0,var1));
                    suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    if (poiItems != null && poiItems.size() > 0) {
                        poiOrSuggestion=1;
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                        poiOrSuggestion=2;
                    } else {
                        ToastUtil.show(this.mActivity,
                                R.string.no_result);
                        poiOrSuggestion=0;
                    }
                }
            } else {
                ToastUtil.show(this.mActivity,
                        R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.mActivity, rCode);
        }

    }








    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static Location convertToLocation(String name,LatLonPoint latLonPoint){
        Location arroundlocation = new Location(name);
        arroundlocation.setLatitude(latLonPoint.getLatitude());
        arroundlocation.setLongitude(latLonPoint.getLongitude());
        return arroundlocation;
    }
    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this.mActivity);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + this.mKeyWord);
        progDialog.show();
    }
    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private String showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(this.mActivity, infomation);
        return infomation;
    }

//    下面这个方法还有问题，待解决
//    给出解决思路：需要用到android的代理开发模式，本人能力有限，留给后面的开发者去完善（dragon）
    public PoiResult getPoiResult(){
//        返回poi搜索结果
        if(1==poiOrSuggestion){
            resultList=this.poiResult;
        }

        return resultList;
    }
}

////        如果没有合适的POI搜索结果返回建议结果
//if(2==poiOrSuggestion){
//        resultList=this.poiResult.getSearchSuggestionCitys();
//        }