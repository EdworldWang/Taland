package com.dragon.navigation.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dragon.navigation.Model.SearchpoiEntity;
import com.dragon.navigation.Model.TravelingEntity;
import com.dragon.navigation.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class SearchpoiAdapter extends BaseListAdapter<SearchpoiEntity> {

    private boolean isNoData;
    private int mHeight;
    public static final int ONE_SCREEN_COUNT = 7; // 一屏能显示的个数，这个根据屏幕高度和各自的需求定
    public static final int ONE_REQUEST_COUNT = 10; // 一次请求的个数

    public SearchpoiAdapter(Context context) {
        super(context);
    }

    public SearchpoiAdapter(Context context, List<SearchpoiEntity> list) {
        super(context, list);
    }

    // 设置数据
    public void setData(List<SearchpoiEntity> list) {
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
    public List<SearchpoiEntity> createEmptyList(int size) {
        List<SearchpoiEntity> emptyList = new ArrayList<>();
        if (size <= 0) return emptyList;
        for (int i=0; i<size; i++) {
            emptyList.add(new SearchpoiEntity());
        }
        return emptyList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 暂无数据
        if (isNoData) {
            convertView = mInflater.inflate(R.layout.item_no_data_layout, null);
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

            convertView = mInflater.inflate(R.layout.item_searchpoi, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        SearchpoiEntity entity = getItem(position);
        holder.searchpoi_root_view.setVisibility(View.VISIBLE);
        holder.poiname.setText(entity.getPoiName());
        holder.poides.setText(entity.getPoiDes());
        holder.poidis.setText(entity.getDistance());
        //mImageManager.loadUrlImage(entity.getImage_url(), holder.poiImage);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.searchpoi_root_view)
        LinearLayout searchpoi_root_view;
        @BindView(R.id.poi_image)
        ImageView poiImage;
        @BindView(R.id.poi_name)
        TextView poiname;
        @BindView(R.id.poi_description)
        TextView poides;
        @BindView(R.id.poi_dis)
        TextView poidis;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
