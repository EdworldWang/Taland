//package com.dragon.navigation;
//
//import android.app.ProgressDialog;
//import android.util.Log;
//
//import com.amap.api.services.core.PoiItem;
//import com.amap.api.services.core.SuggestionCity;
//import com.amap.api.services.poisearch.PoiResult;
//import com.amap.api.services.poisearch.PoiSearch;
//import com.dragon.navigation.util.ToastUtil;
//
//import java.util.List;
//
///**
// * This file created by dragon on 2016/9/20 15:46,
// * belong to com.dragon.navigation .
// */
//public class ArPoiSearch implements PoiSearch.OnPoiSearchListener {
//    //输入搜索关键字
//    private String mKeyWord;
//    //
//    private String mStylePoi;
//    //城市名和区号
//    private String mCityCode;
//    private ProgressDialog progDialog = null;// 搜索时进度条
//    private PoiResult poiResult; // poi返回的结果
//    private PoiSearch.Query query;// Poi查询条件类
//    private PoiSearch poiSearch;// POI搜索
//    private int currentPage = 0;// 当前页面，从0开始计数
//
//    public ArPoiSearch(){
//
//    }
//    public ArPoiSearch(String mKeyWord, String mStylePoi, String mCityCode){
//        this.mKeyWord = mKeyWord;
//        this.mStylePoi = mStylePoi;
//        this.mCityCode = mCityCode;
//    }
//
//    public void doSearchSearch(){
//        showProgressDialog();// 耗时操作前，显示进度框
//        currentPage = 0;
//        query = new PoiSearch.Query(this.mKeyWord, "", "深圳大学");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
//        query.setPageSize(10);// 设置每页最多返回多少条poiitem
//        query.setPageNum(currentPage);// 设置查第一页
//
//        poiSearch = new PoiSearch(this, query);//兴趣点搜索
//        poiSearch.setOnPoiSearchListener(this);
//        poiSearch.searchPOIAsyn();
//    }
//
//    /**
//     * POI信息查询回调方法
//     */
//    @Override
//    public void onPoiSearched(PoiResult result, int rCode) {
//        dissmissProgressDialog();// 隐藏对话框
//        if (rCode == 1000) {
//            if (result != null && result.getQuery() != null) {// 搜索poi的结果
//                if (result.getQuery().equals(query)) {// 是否是同一条
//                    poiResult = result;
//
//                    // 取得搜索到的poiitems有多少页
//                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//                    Log.e("dragon test1",poiItems+"");
//                    Log.e("dragon test2",poiItems.get(0)+"");
//                    Log.e("dragon test3",poiItems.get(0).getTitle()+""+poiItems.get(0).getSnippet());
//                    List<SuggestionCity> suggestionCities = poiResult
//                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
//                    if (poiItems != null && poiItems.size() > 0) {
////                        aMap.clear();// 清理之前的图标
////                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
////                        poiOverlay.removeFromMap();
////                        poiOverlay.addToMap();
////                        poiOverlay.zoomToSpan();
//                    } else if (suggestionCities != null
//                            && suggestionCities.size() > 0) {
//                        showSuggestCity(suggestionCities);
//
//                    } else {
//                        ToastUtil.show(ArPoiSearch.this,
//                                R.string.no_result);
//                    }
//                }
//            } else {
//                ToastUtil.show(ArPoiSearch.this,
//                        R.string.no_result);
//            }
//        } else {
//            ToastUtil.showerror(this, rCode);
//        }
//
//    }
//
//    /**
//     * 显示进度框
//     */
//    private void showProgressDialog() {
//        if (progDialog == null)
//            progDialog = new ProgressDialog(this);
//        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progDialog.setIndeterminate(false);
//        progDialog.setCancelable(false);
//        progDialog.setMessage("正在搜索:\n" + this.mKeyWord);
//        progDialog.show();
//    }
//
//    /**
//     * 隐藏进度框
//     */
//    private void dissmissProgressDialog() {
//        if (progDialog != null) {
//            progDialog.dismiss();
//        }
//    }
//
//    @Override
//    public void onPoiItemSearched(PoiItem item, int rCode) {
//        // TODO Auto-generated method stub
//
//    }
//    /**
//     * poi没有搜索到数据，返回一些推荐城市的信息
//     */
//    private void showSuggestCity(List<SuggestionCity> cities) {
//        String infomation = "推荐城市\n";
//        for (int i = 0; i < cities.size(); i++) {
//            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
//                    + cities.get(i).getCityCode() + "城市编码:"
//                    + cities.get(i).getAdCode() + "\n";
//        }
//
//        ToastUtil.show(ArPoiSearch.this, infomation);
//
//    }
//
//
//}
