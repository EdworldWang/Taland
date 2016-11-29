package com.dragon.navigation.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dragon.navigation.Model.TravelingEntity;
import com.dragon.navigation.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class TravelingAdapter extends BaseListAdapter<TravelingEntity> {

    private boolean isNoData;
    private int mHeight;
    public static final int ONE_SCREEN_COUNT = 7; // 一屏能显示的个数，这个根据屏幕高度和各自的需求定
    public static final int ONE_REQUEST_COUNT = 10; // 一次请求的个数

    public TravelingAdapter(Context context) {
        super(context);
    }

    public TravelingAdapter(Context context, List<TravelingEntity> list) {
        super(context, list);
    }

    // 设置数据
    public void setData(List<TravelingEntity> list) {
        clearAll();
        addALL(list);

        isNoData = false;
        if (list.size() == 1 && list.get(0).isNoData()) {
            // 暂无数据布局
            isNoData = list.get(0).isNoData();
            mHeight = list.get(0).getHeight();
        } else {
            // 添加空数据
            if (list.size() < ONE_SCREEN_COUNT) {
                addALL(createEmptyList(ONE_SCREEN_COUNT - list.size()));
            }
        }
        notifyDataSetChanged();
    }

    // 创建不满一屏的空数据
    public List<TravelingEntity> createEmptyList(int size) {
        List<TravelingEntity> emptyList = new ArrayList<>();
        if (size <= 0) return emptyList;
        for (int i=0; i<size; i++) {
            emptyList.add(new TravelingEntity());
        }
        return emptyList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 暂无数据
        if (isNoData) {
            convertView = mInflater.inflate(R.layout.item_no_data_layout, null);
            if(convertView==null){
                for (int i=0;i<5;i++) {
                    Log.e("fsfsf", "fdsfds");
                }
            }else{
                for (int i=0;i<5;i++) {
                    Log.e("fsfsf", "1111111111111111111");
                }
            }
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
            RelativeLayout rootView = ButterKnife.findById(convertView, R.id.rl_root_view);
            rootView.setLayoutParams(params);
            return convertView;
        }

        // 正常数据
        final ViewHolder holder;
        if (convertView != null && convertView instanceof LinearLayout) {
            holder = (ViewHolder) convertView.getTag();
        } else {

            convertView = mInflater.inflate(R.layout.item_travel, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            if(holder==null){
                for (int i=0;i<5;i++) {
                    Log.e("fsfsf", "33333333");
                }
            }else{
                for (int i=0;i<5;i++) {
                    Log.e("fsfsf", "2222222");
                }
            }
        }

        TravelingEntity entity = getItem(position);
        if(holder.llRootView==null){
            for (int i=0;i<5;i++) {
                Log.e("fsfsf", "please");
            }
        }else{
            for (int i=0;i<5;i++) {
                Log.e("fsfsf", "225555522222");
            }
        }
        holder.llRootView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(entity.getType())) {
            holder.llRootView.setVisibility(View.INVISIBLE);
            return convertView;
        }

        holder.tvTitle.setText(entity.getFrom() + entity.getTitle() + entity.getType());
        holder.tvRank.setText("排名：" + entity.getRank());
        mImageManager.loadUrlImage(entity.getImage_url(), holder.ivImage);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ll_root_view)
        LinearLayout llRootView;
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_rank)
        TextView tvRank;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
