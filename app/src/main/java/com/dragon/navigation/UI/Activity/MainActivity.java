package com.dragon.navigation.UI.Activity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.dragon.navigation.Adapter.CommonFragmentPagerAdapter;
import com.dragon.navigation.R;
import com.dragon.navigation.UI.Base.BaseActivity;
import com.dragon.navigation.UI.Base.BaseFragment;
import com.dragon.navigation.UI.Fragment.FragmentFactory;
import com.dragon.navigation.UI.Presenter.MainAtPresenter;
import com.dragon.navigation.UI.View.IMainAtView;
import com.dragon.navigation.View.Mytestview;
import com.dragon.navigation.app.AppConst;
import com.dragon.navigation.manager.BroadcastManager;
import com.dragon.navigation.util.CrossfadeWrapper;
import com.dragon.navigation.util.PopupWindowUtils;
import com.dragon.navigation.util.UIUtils;
import com.dragon.navigation.widget.EdwardToolbar;
import com.dragon.navigation.widget.MyViewPager;
import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryToggleDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.octicons_typeface_library.Octicons;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.dragon.navigation.util.UIUtils.getContext;


public class MainActivity extends BaseActivity<IMainAtView, MainAtPresenter> implements ViewPager.OnPageChangeListener, IMainAtView {

    private List<BaseFragment> mFragmentList = new ArrayList<>(4);

    @BindView(R.id.ibAddMenu)
    ImageButton mIbAddMenu;
    @BindView(R.id.vpContent)
    MyViewPager mVpContent;

    @BindView(R.id.llButtom)
    LinearLayout mllButtom;

    //底部
    @BindView(R.id.llMessage)
    LinearLayout mLlMessage;
    @BindView(R.id.tvMessageNormal)
    TextView mTvMessageNormal;
    @BindView(R.id.tvMessagePress)
    TextView mTvMessagePress;
    @BindView(R.id.tvMessageTextNormal)
    TextView mTvMessageTextNormal;
    @BindView(R.id.tvMessageTextPress)
    TextView mTvMessageTextPress;
    @BindView(R.id.tvMessageCount)
    public TextView mTvMessageCount;

    @BindView(R.id.llContacts)
    LinearLayout mLlContacts;
    @BindView(R.id.tvContactsNormal)
    TextView mTvContactsNormal;
    @BindView(R.id.tvContactsPress)
    TextView mTvContactsPress;
    @BindView(R.id.tvContactsTextNormal)
    TextView mTvContactsTextNormal;
    @BindView(R.id.tvContactsTextPress)
    TextView mTvContactsTextPress;
    @BindView(R.id.tvContactCount)
    public TextView mTvContactCount;
    @BindView(R.id.tvContactRedDot)
    public TextView mTvContactRedDot;

    @BindView(R.id.llDiscovery)
    LinearLayout mLlDiscovery;
    @BindView(R.id.tvDiscoveryNormal)
    TextView mTvDiscoveryNormal;
    @BindView(R.id.tvDiscoveryPress)
    TextView mTvDiscoveryPress;
    @BindView(R.id.tvDiscoveryTextNormal)
    TextView mTvDiscoveryTextNormal;
    @BindView(R.id.tvDiscoveryTextPress)
    TextView mTvDiscoveryTextPress;
    @BindView(R.id.tvDiscoveryCount)
   TextView mTvDiscoveryCount;

    @BindView(R.id.llMe)
    LinearLayout mLlMe;
    @BindView(R.id.tvMeNormal)
    TextView mTvMeNormal;
    @BindView(R.id.tvMePress)
    TextView mTvMePress;
    @BindView(R.id.tvMeTextNormal)
    TextView mTvMeTextNormal;
    @BindView(R.id.tvMeTextPress)
    TextView mTvMeTextPress;
    @BindView(R.id.tvMeCount)
    TextView mTvMeCount;
    @BindView(R.id.llAR)
    LinearLayout mLlAR;
    @BindView(R.id.tvARNormal)
    TextView mTvARNormal;
    @BindView(R.id.tvARPress)
    TextView mTvARPress;
    @BindView(R.id.tvARTextNormal)
    TextView mTvARTextNormal;
    @BindView(R.id.tvARTextPress)
    TextView mTvARTextPress;
    @BindView(R.id.tvARCount)
    TextView mTvARCount;
    @BindView(R.id.drawerview)
    DrawerLayout mDrawerview;
    @BindView(R.id.nav_myinfo)
    NavigationView mMyInfoview;
    @BindView(R.id.mainview)
    LinearLayout mMainview;
    @BindView(R.id.head_portrait)
    CircleImageView circleImageView;
    @OnClick(R.id.head_portrait)
    public void openleft(){
        mDrawerview.openDrawer(GravityCompat.START);
        Log.i("toolbar","open");
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private Crossfader crossFader;
    private static final int PROFILE_SETTING = 100000;
    private boolean isDrawer=false;
    @Override
    public void init() {
        registerBR();
    }


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460");
        final IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"));
        final IProfile profile3 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile2));
        final IProfile profile4 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile3));
        final IProfile profile5 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile4)).withIdentifier(4);
        final IProfile profile6 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withTranslucentStatusBar(false)
                .addProfiles(
                        profile,
                        profile2,
                        profile3,
                        profile4,
                        profile5,
                        profile6,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(GoogleMaterial.Icon.gmd_plus).withIdentifier(PROFILE_SETTING),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_compact_header).withIcon(GoogleMaterial.Icon.gmd_sun).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_action_bar_drawer).withIcon(FontAwesome.Icon.faw_home).withBadge("22").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4),
                        new PrimaryDrawerItem().withDescription("A more complex sample").withName(R.string.drawer_item_advanced_drawer).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withTag("Bullhorn"),
                        new DividerDrawerItem()
                      //  new SwitchDrawerItem().withName("Switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                      //  new ToggleDrawerItem().withName("Toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(MainActivity.this, ((Nameable) drawerItem).getName().getText(MainActivity.this), Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
                .buildView();
      mMyInfoview.addHeaderView(result.getSlider());
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }
    @Override
    public void initView() {
        mAppBar.setVisibility(View.GONE);
         setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        toolbar.setMinimumHeight(toolbar.getHeight());
        mMainview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isDrawer){
                    return mMyInfoview.dispatchTouchEvent(motionEvent);
                }else{
                    return false;
                }
            }
        });
        mDrawerview.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                isDrawer=true;
                //获取屏幕的宽高
                WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                //设置右面的布局位置  根据左面菜单的right作为右面布局的left   左面的right+屏幕的宽度（或者right的宽度这里是相等的）为右面布局的right
                mMainview.layout(mMyInfoview.getRight(), 0, mMyInfoview.getRight() + display.getWidth(), display.getHeight());
            }
            @Override
            public void onDrawerOpened(View drawerView) {}
            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawer=false;
            }
            @Override
            public void onDrawerStateChanged(int newState) {}
        });



        setToolbarTitle(UIUtils.getString(R.string.app_name));
        mIbAddMenu.setVisibility(View.VISIBLE);
       // this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //等待全局数据获取完毕
        showWaitingDialog(UIUtils.getString(R.string.please_wait));
        //默认选中第一个
        setTransparency();
        mTvARPress.getBackground().setAlpha(255);
        mTvARTextPress.setTextColor(Color.argb(255, 69, 192, 26));

        //设置ViewPager的最大缓存页面
        mVpContent.setOffscreenPageLimit(4);

        mFragmentList.add(FragmentFactory.getInstance().getMainFragment());
        mFragmentList.add(FragmentFactory.getInstance().getRecentMessageFragment());
        mFragmentList.add(FragmentFactory.getInstance().getContactsFragment());
        mFragmentList.add(FragmentFactory.getInstance().getDiscoveryFragment());
        mFragmentList.add(FragmentFactory.getInstance().getMeFragment());

        mVpContent.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));
        // Inflate the layout for this fragment


        mVpContent.setIsCanScroll(false);
    }

    @Override
    public void initListener() {
        mIbAddMenu.setOnClickListener(v -> {
            //显示或隐藏popupwindow
            View menuView = View.inflate(MainActivity.this, R.layout.menu_main, null);
            PopupWindow popupWindow = PopupWindowUtils.getPopupWindowAtLocation(menuView, getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, UIUtils.dip2Px(5), mAppBar.getHeight() + 30);
            menuView.findViewById(R.id.tvCreateGroup).setOnClickListener(v1 -> {
                jumpToActivity(CreateGroupActivity.class);
                popupWindow.dismiss();
            });
            menuView.findViewById(R.id.tvHelpFeedback).setOnClickListener(v1 -> {
                jumpToWebViewActivity(AppConst.WeChatUrl.HELP_FEED_BACK);
                popupWindow.dismiss();
            });
            menuView.findViewById(R.id.tvAddFriend).setOnClickListener(v1 -> {
                jumpToActivity(AddFriendActivity.class);
                popupWindow.dismiss();
            });
            menuView.findViewById(R.id.tvScan).setOnClickListener(v1 -> {
                jumpToActivity(ScanActivity.class);
                popupWindow.dismiss();
            });
        });



        mLlMessage.setOnClickListener(v -> bottomBtnClick(v));
        mLlContacts.setOnClickListener(v -> bottomBtnClick(v));
        mLlDiscovery.setOnClickListener(v -> bottomBtnClick(v));
        mLlMe.setOnClickListener(v -> bottomBtnClick(v));
        mLlAR.setOnClickListener(v -> bottomBtnClick(v));

        mVpContent.setOnPageChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBR();
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public void bottomBtnClick(View view) {
        setTransparency();
        switch (view.getId()) {
            case R.id.llAR:
                mVpContent.setCurrentItem(0, false);
                mTvARPress.getBackground().setAlpha(255);
                mTvARTextPress.setTextColor(Color.argb(255, 69, 192, 26));
                startActivity(new Intent(this,Main.class));
                break;
            case R.id.llMessage:
                mVpContent.setCurrentItem(1, false);
                mTvMessagePress.getBackground().setAlpha(255);
                mTvMessageTextPress.setTextColor(Color.argb(255, 69, 192, 26));
                break;
            case R.id.llContacts:
                mVpContent.setCurrentItem(2, false);
                mTvContactsPress.getBackground().setAlpha(255);
                mTvContactsTextPress.setTextColor(Color.argb(255, 69, 192, 26));
                break;
            case R.id.llDiscovery:
                mVpContent.setCurrentItem(3, false);
                mTvDiscoveryPress.getBackground().setAlpha(255);
                mTvDiscoveryTextPress.setTextColor(Color.argb(255, 69, 192, 26));
                break;
            case R.id.llMe:
                mVpContent.setCurrentItem(4, false);
                mTvMePress.getBackground().setAlpha(255);
                mTvMeTextPress.setTextColor(Color.argb(255, 69, 192, 26));
                break;

        }
    }

    /**
     * 把press图片、文字全部隐藏(设置透明度)
     */
    private void setTransparency() {
        mTvMessageNormal.getBackground().setAlpha(255);
        mTvContactsNormal.getBackground().setAlpha(255);
        mTvDiscoveryNormal.getBackground().setAlpha(255);
        mTvMeNormal.getBackground().setAlpha(255);
        mTvARNormal.getBackground().setAlpha(255);

        mTvMessagePress.getBackground().setAlpha(1);
        mTvContactsPress.getBackground().setAlpha(1);
        mTvDiscoveryPress.getBackground().setAlpha(1);
        mTvMePress.getBackground().setAlpha(1);
        mTvARPress.getBackground().setAlpha(1);

        mTvMessageTextNormal.setTextColor(Color.argb(255, 153, 153, 153));
        mTvContactsTextNormal.setTextColor(Color.argb(255, 153, 153, 153));
        mTvDiscoveryTextNormal.setTextColor(Color.argb(255, 153, 153, 153));
        mTvMeTextNormal.setTextColor(Color.argb(255, 153, 153, 153));
        mTvARTextNormal.setTextColor(Color.argb(255, 153, 153, 153));

        mTvMessageTextPress.setTextColor(Color.argb(0, 69, 192, 26));
        mTvContactsTextPress.setTextColor(Color.argb(0, 69, 192, 26));
        mTvDiscoveryTextPress.setTextColor(Color.argb(0, 69, 192, 26));
        mTvMeTextPress.setTextColor(Color.argb(0, 69, 192, 26));
        mTvARTextPress.setTextColor(Color.argb(0, 69, 192, 26));
    }

    @Override
    protected MainAtPresenter createPresenter() {
        return new MainAtPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main2;
    }

    @Override
    protected boolean isToolbarCanBack() {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //根据ViewPager滑动位置更改透明度
        //positionOffset是当前页面滑动比例
        int diaphaneity_one = (int) (255 * positionOffset);
        int diaphaneity_two = (int) (255 * (1 - positionOffset));
        //左滑，从第i页滑向第i+1页，目的页面是i+1。参数变化：position开始一直是i，
        // 只有最后一次调用为i+1，positionOffset从０开始递增(不包括0)，一直递增到１(不包括１)，最后再变为０，
        // 倒数第二次调用这个值应该是接近１的数，可能是０.9几，受滑动快慢影响。
        //右滑，从第i页滑向第i-1页，目的页面是i-1。参数变化：position一直为i-1，positionOffset从１开始递减(不包括１)，一直递减到０。
        //总结：不管是向左还是向右滑动，总是涉及到两个页面，i和i+1或者i和i-1，当positionOffset！=０时，position值一直是两者中的最小值，当positionOffset==0时，position值为目的页面的序号；positionOffset的值都是左边页面的偏移。
        switch (position) {
            case 0:
               // 第一个position，这个参数要特别注意一下。当用手指滑动时，如果手指按在页面上不动，position和当前页面index是一致的；
                mTvARNormal.getBackground().setAlpha(diaphaneity_one);
                mTvARPress.getBackground().setAlpha(diaphaneity_two);
                mTvMessageNormal.getBackground().setAlpha(diaphaneity_two);
                mTvMessagePress.getBackground().setAlpha(diaphaneity_one);
                mTvARTextNormal.setTextColor(Color.argb(diaphaneity_one, 153, 153, 153));
                mTvARTextPress.setTextColor(Color.argb(diaphaneity_two, 69, 192, 26));
                mTvMessageTextNormal.setTextColor(Color.argb(diaphaneity_two, 153, 153, 153));
                mTvMessageTextPress.setTextColor(Color.argb(diaphaneity_one, 69, 192, 26));
              //  mAppBar.setVisibility(View.GONE);
                if(positionOffset < 0.1)
                    mllButtom.setVisibility(View.GONE);
                else
                    mllButtom.setVisibility(View.VISIBLE);
                break;
            case 1:
                mTvMessageNormal.getBackground().setAlpha(diaphaneity_one);
                mTvMessagePress.getBackground().setAlpha(diaphaneity_two);
                mTvContactsNormal.getBackground().setAlpha(diaphaneity_two);
                mTvContactsPress.getBackground().setAlpha(diaphaneity_one);
                mTvMessageTextNormal.setTextColor(Color.argb(diaphaneity_one, 153, 153, 153));
                mTvMessageTextPress.setTextColor(Color.argb(diaphaneity_two, 69, 192, 26));
                mTvContactsTextNormal.setTextColor(Color.argb(diaphaneity_two, 153, 153, 153));
                mTvContactsTextPress.setTextColor(Color.argb(diaphaneity_one, 69, 192, 26));
               // mAppBar.setVisibility(View.VISIBLE);
                mllButtom.setVisibility(View.VISIBLE);
                break;
            case 2:
                mTvContactsNormal.getBackground().setAlpha(diaphaneity_one);
                mTvContactsPress.getBackground().setAlpha(diaphaneity_two);
                mTvDiscoveryNormal.getBackground().setAlpha(diaphaneity_two);
                mTvDiscoveryPress.getBackground().setAlpha(diaphaneity_one);
                mTvContactsTextNormal.setTextColor(Color.argb(diaphaneity_one, 153, 153, 153));
                mTvContactsTextPress.setTextColor(Color.argb(diaphaneity_two, 69, 192, 26));
                mTvDiscoveryTextNormal.setTextColor(Color.argb(diaphaneity_two, 153, 153, 153));
                mTvDiscoveryTextPress.setTextColor(Color.argb(diaphaneity_one, 69, 192, 26));
               // mAppBar.setVisibility(View.VISIBLE);
                break;
            case 3:
                mTvDiscoveryNormal.getBackground().setAlpha(diaphaneity_one);
                mTvDiscoveryPress.getBackground().setAlpha(diaphaneity_two);
                mTvMeNormal.getBackground().setAlpha(diaphaneity_two);
                mTvMePress.getBackground().setAlpha(diaphaneity_one);
                mTvDiscoveryTextNormal.setTextColor(Color.argb(diaphaneity_one, 153, 153, 153));
                mTvDiscoveryTextPress.setTextColor(Color.argb(diaphaneity_two, 69, 192, 26));
                mTvMeTextNormal.setTextColor(Color.argb(diaphaneity_two, 153, 153, 153));
                mTvMeTextPress.setTextColor(Color.argb(diaphaneity_one, 69, 192, 26));
               //hvkljk./////////////////////////////////////////////////////// mAppBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1) {
            //如果是“通讯录”页被选中，则显示快速导航条
            FragmentFactory.getInstance().getContactsFragment().showQuickIndexBar(true);
        } else {
            FragmentFactory.getInstance().getContactsFragment().showQuickIndexBar(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state != ViewPager.SCROLL_STATE_IDLE) {
            //滚动过程中隐藏快速导航条
            FragmentFactory.getInstance().getContactsFragment().showQuickIndexBar(false);
        } else {
            FragmentFactory.getInstance().getContactsFragment().showQuickIndexBar(true);
        }
    }

    private void registerBR() {
        BroadcastManager.getInstance(this).register(AppConst.FETCH_COMPLETE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                hideWaitingDialog();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
/*
        ViewTreeObserver vto = edwardToolbar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                removeOnGlobalLayoutListener(edwardToolbar,this);
                Log.i("myview","--->"+edwardToolbar.getWidth()+"*"+edwardToolbar.getHeight());
            }
        });
        Log.i("myview","params "+edwardToolbar.getLayoutParams().width+"  getwidth "+ edwardToolbar.getScaleX() + edwardToolbar.getX()
                +edwardToolbar.getMeasuredWidth());*/
    }
     public static void removeOnGlobalLayoutListener(View view,ViewTreeObserver.OnGlobalLayoutListener victim){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            view.getViewTreeObserver().removeOnGlobalLayoutListener(victim);
        }else{
            view.getViewTreeObserver().removeGlobalOnLayoutListener(victim);
        }
    }

    private void unRegisterBR() {
        BroadcastManager.getInstance(this).unregister(AppConst.FETCH_COMPLETE);
    }

    @Override
    public TextView getTvMessageCount() {
        return mTvMessageCount;
    }
}
