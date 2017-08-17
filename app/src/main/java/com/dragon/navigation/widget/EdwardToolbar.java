package com.dragon.navigation.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dragon.navigation.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Edward on 2017/7/31.
 */
public class EdwardToolbar extends Toolbar {
    private CircleImageView mImageView;
    private View mBlankView;
    private SearchView mSearchView;
    private View view;
    private int height;
    private int width;
    boolean hideblankview;
    private int toolbarheight;
    private Context mContext;
    private ActionBar mActionBar;
    private Activity mActivity;
    private LinearLayout.LayoutParams layoutsquare;
    public EdwardToolbar(Context context) {
        this(context, null);
    }

    public EdwardToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EdwardToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        view = LayoutInflater.from(getContext()).inflate(R.layout.edward_toolbar, null);
        mImageView = (CircleImageView) view.findViewById(R.id.edward_headview);
        mSearchView = (SearchView) view.findViewById(R.id.edward_searchview);
        mBlankView = view.findViewById(R.id.edward_blankview);
        if(attrs != null) {
            if (mImageView != null) {
                final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                        R.styleable.EdwardToolbar, defStyleAttr, 0);
                if(a.getDrawable(R.styleable.EdwardToolbar_head)!=null)
                     mImageView.setImageDrawable(a.getDrawable(R.styleable.EdwardToolbar_head));
                a.recycle();
            }
            if (mSearchView != null) {
                final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                        R.styleable.EdwardToolbar, defStyleAttr, 0);
                boolean isVisible = a.getBoolean(R.styleable.EdwardToolbar_searchview_visible, true);
                mSearchView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                a.recycle();
            }
        }

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
        addView(view, lp);
        initListener();
        // initSearchView();

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = this.getMeasuredHeight();
        width =  this.getMeasuredWidth();
        System.out.println("----------onMeasure-------------");
        System.out.println("height = "+height + "width" + width);
        toolbarheight = (int)(height*0.7);
        layoutsquare = (LinearLayout.LayoutParams)mImageView.getLayoutParams();
        layoutsquare.height = toolbarheight;
        layoutsquare.width = toolbarheight;
        // layoutsquare.weight = 0.2f;
        mImageView.setLayoutParams(layoutsquare);
        //修复最低的显示位置，布局上一些按钮的位置不居中的修复
        this.setMinimumHeight(this.getLayoutParams().height);
    }
  /*  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed,left,top,right,bottom);
        initHeadView();
    }*/
    public void initSearchView(){
        hideblankview = false;
         mSearchView.setIconified(false);
        /**
         * 默认情况下, search widget是"iconified“的，只是用一个图标 来表示它(一个放大镜),
         * 当用户按下它的时候才显示search box . 你可以调用setIconifiedByDefault(false)让search
         * box默认都被显示。 你也可以调用setIconified()让它以iconified“的形式显示。
         */
      //  mSearchView.setIconifiedByDefault(true);
        /**
         * 默认情况下是没提交搜索的按钮，所以用户必须在键盘上按下"enter"键来提交搜索.你可以同过setSubmitButtonEnabled(
         * true)来添加一个提交按钮（"submit" button)
         * 设置true后，右边会出现一个箭头按钮。如果用户没有输入，就不会触发提交（submit）事件
         */
        mSearchView.setSubmitButtonEnabled(true);
        /**
         * 初始是否已经是展开的状态
         * 写上此句后searchView初始展开的，也就是是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能展开出现输入框
         */
        mSearchView.onActionViewExpanded();
        // 设置search view的背景色
      //  mSearchView.setBackgroundColor(0x22ff00ff);
        /**
         * 默认情况下, search widget是"iconified“的，只是用一个图标 来表示它(一个放大镜),
         * 当用户按下它的时候才显示search box . 你可以调用setIconifiedByDefault(false)让search
         * box默认都被显示。 你也可以调用setIconified()让它以iconified“的形式显示。
         */
        mSearchView.setIconifiedByDefault(true);
    }
    public void initListener(){
        this.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("myview"," "+item.getItemId() + " "+mImageView.getId());
                switch (item.getItemId()) {
                    case R.id.edward_searchview:
                        hideblankview = true;
                        mBlankView.setVisibility(View.GONE);
                        //DisplayHomeAsUpEnabled(true);
                            Log.i("myview","itemclick");
                        break;
                }

                return false;
            }

        });
        //搜索框展开时后面叉叉按钮的点击事件
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mBlankView.setVisibility(View.VISIBLE);
                mSearchView.setWeightSum(0.1f);
                Toast.makeText(getContext(), "Close", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
//搜索图标按钮(打开搜索框的按钮)的点击事件
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlankView.setVisibility(View.GONE);
                mSearchView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f));
                try {
                    ((AppCompatActivity)mActivity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }catch(Exception e){

                }

                //setDisplayHomeAsUpEnabled(true);
                Toast.makeText(getContext(), "Open", Toast.LENGTH_SHORT).show();
            }
        });
//搜索框文字变化监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.e("CSDN_LQR", "TextSubmit : " + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("CSDN_LQR", "TextChange --> " + s);
                return false;
            }
        });


    }
    public void setActivity(Activity mActivity){
        this.mActivity = mActivity;
        ((AppCompatActivity)mActivity).setSupportActionBar(this);
    }
   /* private class ToolbarBuilder{
        Activity mActivity;
        public void withActivity(Activity mActivity) {
            this.mActivity = mActivity;
        }
    }*/
}
