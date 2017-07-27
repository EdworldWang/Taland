package com.dragon.navigation.UI.Activity;
// ------------------------ 主界面 ------------------------
/**
 * @author Edward
 * @date 2017/5/8
 */

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.dragon.Reback.ListviewCallBack;
import com.dragon.navigation.Adapter.SearchpoiAdapter;
import com.dragon.navigation.Control.Control;
import com.dragon.navigation.Control.Data;
import com.dragon.navigation.Function.ArPoiSearch;
import com.dragon.navigation.Function.MLocation;
import com.dragon.navigation.Function.Servicetype;
import com.dragon.navigation.Model.TravelingEntity;
import com.dragon.navigation.R;
import com.dragon.navigation.Renderer.MainRenderer;
import com.dragon.navigation.UI.Fragment.Moveviewfragment;
import com.dragon.navigation.UI.Fragment.fragmenttwo;
import com.dragon.navigation.View.Mytestview;
import com.dragon.navigation.View.NewWidget;
import com.dragon.navigation.View.scrollerlayout;
import com.dragon.navigation.use.DataSmoother;
import com.dragon.navigation.use.SampleApplicationGLView;
import com.dragon.navigation.use.Texture;
import com.dragon.navigation.util.AMapUtil;
import com.dragon.navigation.util.MyTextView;
import com.dragon.navigation.util.ToastUtil;
import com.dragon.orientationProvider.CalibratedGyroscopeProvider;
import com.dragon.orientationProvider.GravityCompassProvider;
import com.dragon.orientationProvider.ImprovedOrientationSensor2Provider;
import com.dragon.orientationProvider.OrientationProvider;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
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
import com.mikepenz.octicons_typeface_library.Octicons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Main extends AppCompatActivity implements SensorEventListener,
        TextWatcher, Inputtips.InputtipsListener, ListviewCallBack {
    @BindView(R.id.list_Lin)
    LinearLayout lin;
    private static final String TAG = "Main";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.texture)
    TextureView texture;
    @BindView(R.id.fragmentone)
    LinearLayout fragmentone;
    @BindView(R.id.searchText)
    AutoCompleteTextView searchText;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.search_bar_layout)
    RelativeLayout searchBarLayout;
    @BindView(R.id.merchant)
    TextView merchant;
    @BindView(R.id.My)
    Button My;

    @BindView(R.id.current_city)
    MyTextView currentCity;
    @BindView(R.id.Compass)
    Mytestview Compass;
    @BindView(R.id.part_camera2)
    RelativeLayout partCamera2;
    @BindView(R.id.fragmenttwo)
    LinearLayout fragmenttwo;
    @BindView(R.id.topview)
    FrameLayout topview;


    @OnClick(R.id.My)
    public void my(View v) {
        Intent intent = new Intent(Main.this, MySetting.class);
        startActivity(intent);
//            Toast.makeText(Main.this, "我的", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_search)
//            ToastUtil.show(Main.this,"搜索");
            public void search() {
                     Control.choosedestination = false;
                searchButton();
            //            更新UI
                     new Thread(new Runnable() {
                    @Override
                    public void run () { //　新建一个线程，并新建一个Message的对象，是用Handler的对象发送这个Message
                        Message msg = new Message();
                        msg.what = UPDATE_TEXT; // 用户自定义的一个值，用于标识不同类型的消息
                        mUiHandler.sendMessage(msg); // 发送消息
                    }
                }).
                start();
            }



    // final LinearLayout lin = (LinearLayout) findViewById(R.id.list_Lin)
    private ArPoiSearch mArPoiSearch;
    private MLocation mLocation;
    private Vector<Texture> mTextures;
    private SampleApplicationGLView mGlView;
    private MainRenderer mRenderer;
    //    private EditText editCity;
    private TextureView textureView;
    //用SparseIntArray来代替hashMap，进行性能优化。
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    private int Alivefrag;
    private FragmentManager fragmentManager;

    private static final int NUM_SMOOTH_SAMPLES = 4;
    private static final DataSmoother.Smoothing SMOOTHING = DataSmoother.Smoothing.AVERAGE;
    private final DataSmoother gravitySmoother = new DataSmoother(
            NUM_SMOOTH_SAMPLES, 3);
    private final DataSmoother magneticFieldSmoother = new DataSmoother(
            NUM_SMOOTH_SAMPLES, 3);

    //    静态初始化块
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    //    以下定义是摄像头相关和摄像头会话相关
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    //    *****************************************************************
//    指南针
    private Mytestview viewarround;
    //    搜素关键字
    private AutoCompleteTextView mSearchText;
    //    当前城市，自定义控件，为了获取焦点
    // 搜索
    //    商家
    private TextView mCurrentCity;
    private TextView mMerchant;
    private Button btnMy;
    // 要输入的poi搜索关键字
    private String keyWord = "";

    //****************************************************************
    float currentDegree = 0f;
    SensorManager mSensorManager;
    private Sensor accelerometer;//加速度传感器
    private Sensor magnetic;//地磁传感器
    scrollerlayout paper;

    NewWidget wen1;
    private float[] accelerometerValues = new float[3];
    private float[] gravityValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private float[] motion = new float[3];
    //***********新建子线程更新UI**********************
    private static final int UPDATE_TEXT = 1;
    private Handler mUiHandler = new MyUiHandler();
    private scrollerlayout[] layoutarray = new scrollerlayout[10];
    private NewWidget[] widgetarray = new NewWidget[10];

    private float currentAzimuth = UNKNOWN_AZIMUTH;
    public final static float UNKNOWN_AZIMUTH = Float.NaN;


    private OrientationProvider currentOrientationProvider;
    private OrientationProvider currentOrientationProvider2;
    private OrientationProvider currentOrientationProvider3;
    private ImprovedOrientationSensor2Provider currentImproved2;
    private GravityCompassProvider currentGravityCompassProvider;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private TextView mydegree;
    private scrollerlayout layout_sub_Lin;


    private static final int PROFILE_SETTING = 100000;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    private RelativeLayout buttonview;
    private List<TravelingEntity> travelingList = new ArrayList<>(); // ListView数据
    private SearchpoiAdapter mAdapter; // 主页数据

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.background);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // setContentView(R.layout.main_camera2);
        topview = (FrameLayout) findViewById(R.id.topview);


        mAdapter = new SearchpoiAdapter(this, Data.SearchpoiList);
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Data.screenWidth = dm.widthPixels;
        Data.screenHeigh = dm.heightPixels;

        fragmentManager = getFragmentManager();

        textureView = (TextureView) findViewById(R.id.texture);
        //  buttonview=(RelativeLayout)View.inflate(this,R.layout.main_camera2,null);
        // addContentView(buttonview,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        //  ViewGroup.LayoutParams.MATCH_PARENT));
        //    buttonview.bringToFront();
        // buttonview.setVisibility(View.INVISIBLE);
        //  ViewStub myviewstub =(ViewStub)findViewById(R.id.lanshouqian);
        viewarround = (Mytestview) findViewById(R.id.Compass);
        //    viewarround.setVisibility(View.INVISIBLE);
        // travelingList = ModelUtil.getTravelingData();
        // mAdapter = new TravelingAdapter(this, travelingList);


        Data.locationdes = new Location("des");
        Data.locationdes.setLongitude(0);
        Data.locationdes.setLatitude(0);
        Data.locationhere = new Location("des");
        Data.locationhere.setLongitude(0);
        Data.locationhere.setLatitude(0);
        ArPoiSearch.here = new LatLng(0, 0);
        // initnewview();
        //    testview=new NavigatorView(this);
        //  addContentView(testview,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        //      ViewGroup.LayoutParams.WRAP_CONTENT));
        FrameLayout BlankLayout = (FrameLayout) View.inflate(this, R.layout.blanklayout,
                null);

//********************Location***************************
        mCurrentCity = (TextView) findViewById(R.id.current_city);
        mLocation = new MLocation(Main.this, savedInstanceState, mCurrentCity);
        mLocation.setHandler(handler);
        mLocation.initLoction();


//        ToastUtil.show(Main.this,mLocation.getLp());
//********************POI********************************

        mTextures = new Vector<Texture>();
        loadTextures();

        //ToastUtil.show(Main.this,mLocation.getLocationResult().getAddress());
        //imgZnz.setAlpha(0.2f);

//        ****************************************************
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//        实例化加速度传感器
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        实例化地磁传感器
        magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        calculateOrientation();
//        *******************************************************
        mSearchText = (AutoCompleteTextView) findViewById(R.id.searchText);
        mSearchText.addTextChangedListener(this);// 添加文本输入框监听事件
        mMerchant = (TextView) findViewById(R.id.merchant);
//以下动态加载
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        Thread mThread = new Thread(myRunnable);
        mThread.start();




        LinearLayout happy = (LinearLayout) View.inflate(this, R.layout.succees,
                null);


        //   setDefaultFragment();
        BlankLayout.setVisibility(View.VISIBLE);
        BlankLayout.addView(happy, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        happy.setPadding(200, 100, 0, 0);
       addContentView(BlankLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
      //  BlankLayout.bringToFront();
        assert textureView != null;
       textureView.setSurfaceTextureListener(textureListener);
        mydegree = (TextView) happy.findViewById(R.id.degree);
        mydegree.setText("");
        mydegree.setTextColor(Color.RED);
        initAR();
        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(100);
        final IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460")).withIdentifier(101);
        final IProfile profile3 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2).withIdentifier(102);
        final IProfile profile4 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3).withIdentifier(103);
        final IProfile profile5 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4).withIdentifier(104);
        final IProfile profile6 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5).withIdentifier(105);
        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,
                        profile2,
                        profile3,
                        profile4,
                        profile5,
                        profile6,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_plus).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        //例子为addcount
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile5).withIdentifier(count);
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

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                // .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_compact_header).withDescription(R.string.drawer_item_compact_header_desc).withIcon(GoogleMaterial.Icon.gmd_sun).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_action_bar_drawer).withDescription(R.string.drawer_item_action_bar_drawer_desc).withIcon(FontAwesome.Icon.faw_home).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_multi_drawer).withDescription(R.string.drawer_item_multi_drawer_desc).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_non_translucent_status_drawer).withDescription(R.string.drawer_item_non_translucent_status_drawer_desc).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_advanced_drawer).withDescription(R.string.drawer_item_advanced_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_embedded_drawer).withDescription(R.string.drawer_item_embedded_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_battery).withIdentifier(7).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_fullscreen_drawer).withDescription(R.string.drawer_item_fullscreen_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_labels).withIdentifier(8).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_custom_container_drawer).withDescription(R.string.drawer_item_custom_container_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_my_location).withIdentifier(9).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_menu_drawer).withDescription(R.string.drawer_item_menu_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_filter_list).withIdentifier(10).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_mini_drawer).withDescription(R.string.drawer_item_mini_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_battery_charging).withIdentifier(11).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_fragment_drawer).withDescription(R.string.drawer_item_fragment_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_disc_full).withIdentifier(12).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_collapsing_toolbar_drawer).withDescription(R.string.drawer_item_collapsing_toolbar_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_camera_rear).withIdentifier(13).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_persistent_compact_header).withDescription(R.string.drawer_item_persistent_compact_header_desc).withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(14).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_crossfade_drawer_layout_drawer).withDescription(R.string.drawer_item_crossfade_drawer_layout_drawer_desc).withIcon(GoogleMaterial.Icon.gmd_format_bold).withIdentifier(15).withSelectable(false),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new ExpandableBadgeDrawerItem().withName("Collapsable Badge").withIcon(GoogleMaterial.Icon.gmd_collection_case_play).withIdentifier(18).withSelectable(false).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700)).withBadge("100").withSubItems(
                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2000),
                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2001)
                        ),
                        new ExpandableDrawerItem().withName("Collapsable").withIcon(GoogleMaterial.Icon.gmd_collection_case_play).withIdentifier(19).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName("CollapsableItem").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2002),
                                new SecondaryDrawerItem().withName("CollapsableItem 2").withLevel(2).withIcon(GoogleMaterial.Icon.gmd_8tracks).withIdentifier(2003)
                        ),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(20).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withIdentifier(21).withTag("Bullhorn"),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withName("Switch").withIcon(Octicons.Icon.oct_tools).withChecked(true),
                        new SwitchDrawerItem().withName("Switch2").withIcon(Octicons.Icon.oct_tools).withChecked(true).withSelectable(false),
                        new ToggleDrawerItem().withName("Toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true),
                        new DividerDrawerItem(),
                        new SecondarySwitchDrawerItem().withName("Secondary switch").withIcon(Octicons.Icon.oct_tools).withChecked(true),
                        new SecondarySwitchDrawerItem().withName("Secondary Switch2").withIcon(Octicons.Icon.oct_tools).withChecked(true).withSelectable(false),
                        new SecondaryToggleDrawerItem().withName("Secondary toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true)
                ) // add the items we want to use with our Drawer
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
//                .withShowDrawerUntilDraggedOpened(true)
                .build();



       /* Mytestview nine=new Mytestview(this);
        addContentView(nine, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
             ViewGroup.LayoutParams.WRAP_CONTENT));*/


    }


    public void initAR() {
        currentOrientationProvider = new GravityCompassProvider((SensorManager) this.getSystemService(
                this.SENSOR_SERVICE));
        currentOrientationProvider2 = new CalibratedGyroscopeProvider((SensorManager) this.getSystemService(
                this.SENSOR_SERVICE));

        // Create OpenGL ES view:
        int depthSize = 16;
        int stencilSize = 0;
        boolean translucent = true;

        mGlView = new SampleApplicationGLView(this);
        mGlView.init(translucent, depthSize, stencilSize);
        mRenderer = new MainRenderer(this);
        mRenderer.setOrientationProvider(currentOrientationProvider);
        mRenderer.setArrowProvider(currentOrientationProvider2);
        mRenderer.setTextures(mTextures);
        mGlView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mGlView.setZOrderOnTop(true);
        mGlView.setRenderer(mRenderer);
        mGlView.bringToFront();
        addContentView(mGlView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.dragon.navigation/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);

    }


    // 定义一个内部类继承自Handler，并且覆盖handleMessage方法用于处理子线程传过来的消息
    class MyUiHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TEXT: // 接受到消息之后，对UI控件进行修改
                    mMerchant.setText("商家：11");
                    break;

                default:
                    break;
            }
        }
    }


    //    定义了一个独立的监听类
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.i("texture", "destroy");
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, "");
            Inputtips inputTips = new Inputtips(Main.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            mSearchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
//        摄像头打开激发该方法
        public void onOpened(CameraDevice camera) {
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
//            开始预览
            try {
                createCameraPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //        摄像头断开连接时的方法
        @Override
        public void onDisconnected(CameraDevice camera) {
            Log.e(TAG, "onDisconnected");
            cameraDevice.close();
            Main.this.cameraDevice = null;
        }

        //        打开摄像头出现错误时激发方法
        @Override
        public void onError(CameraDevice camera, int error) {
            Log.e(TAG, "onClose");
            cameraDevice.close();
            Main.this.cameraDevice = null;
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void initCamera2() {
        if (null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
//            如果没打开则返回
            return;
        }
//        摄像头管理器，专门用于检测、打开系统摄像头，并连接CameraDevices.
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            Log.e(TAG, "cameraDevice.getId " + cameraDevice.getId());//查看选中的摄像头ID
//            获取指定摄像头的相关特性（此处摄像头已经打开）
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());

//            定义图像尺寸
            Size[] jpegSizes;
//                获取摄像头支持的最大尺寸
            jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
////            初始化一个尺寸
//            int width = 640;
//            int height = 480;

            int width = jpegSizes[0].getWidth();
            int height = jpegSizes[0].getHeight();

//            创建一个ImageReader对象，用于获得摄像头的图像数据
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
//动态数组
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);

            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
//生成请求对象（TEMPLATE_STILL_CAPTURE此处请求是拍照）
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
//            将ImageReader的surface作为captureBuilder的输出目标
            captureBuilder.addTarget(reader.getSurface());
////设置自动对焦模式
//            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
////            设置自动曝光模式
//            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);//推荐采用这种最简单的设置请求模式
            // 获取设备方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
//            根据设置方向设置照片显示的方向
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

//            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            //拍照开始或是完成时调用，用来监听CameraCaptureSession的创建过程
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
//                    Toast.makeText(Main.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
//            设置默认的预览大小
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
//            请求预览
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            captureRequestBuilder.addTarget(surface);
//            创建cameraCaptureSession,第一个参数是图片集合，封装了所有图片surface,第二个参数用来监听这处创建过程
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(Main.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
//        实例化摄像头
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
//            指定要打开的摄像头
            cameraId = manager.getCameraIdList()[0];
//            获取打开摄像头的属性
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
//The available stream configurations that this camera device supports; also includes the minimum frame durations and the stall durations for each format/size combination.
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
//            打开摄像头，第一个参数代表要打开的摄像头，第二个参数用于监测打开摄像头的当前状态，第三个参数表示执行callback的Handler,
//            如果程序希望在当前线程中执行callback，像下面的设置为null即可。
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                manager.openCamera(cameraId, stateCallback, null);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera 1");
    }

    protected void updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
//        设置模式为自动
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        注册监听事件
        mSensorManager.registerListener(Main.this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(Main.this, magnetic, SensorManager.SENSOR_DELAY_UI);
        mLocation.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            Log.e(TAG, "textureViewAvailable");
//            openCamera();
            initCamera2();
        } else {
           textureView.setSurfaceTextureListener(textureListener);
        }

        if (mGlView != null) {


            mGlView.setVisibility(View.VISIBLE);//可見

            mGlView.setVisibility(View.VISIBLE);

            mGlView.onResume();
        }
        currentOrientationProvider.start();
        currentOrientationProvider2.start();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        mSensorManager.unregisterListener(this);
        closeCamera();
        stopBackgroundThread();
        super.onPause();

        gravitySmoother.clear();
        magneticFieldSmoother.clear();
        mLocation.onPause();
        mLocation.deactivate();
        currentOrientationProvider.stop();
        currentOrientationProvider2.stop();
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.dragon.navigation/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //    mLocation.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //     mLocation.onDestroy();
        mTextures.clear();
        mTextures = null;
    }


    private float calculateOrientation() {
//        SensorManager mSensorMgr = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
//        HandlerThread mHandlerThread = new HandlerThread("sensorThread");
//        mHandlerThread.start();
//        Handler handler = new Handler(mHandlerThread.getLooper());
//        mSensorMgr.registerListener(this, mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_FASTEST, handler);
        float[] values = new float[3];
        float[] R = new float[9];

        final float inclinationMat[] = new float[9];
      /*  SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);*/
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
    /*    for(int i=0;i<9;i+=3){
            Log.i("R",R[i]+"    "+R[i+1]+"    "+R[i+2]);
        }
            Log.i("values",values[0]+"    "+values[1]+"    "+values[2]);*/
        Data.currentAzimuth = RAD_TO_DEGREE * values[0];
        values[0] = (float) Math.toDegrees(values[0]);
        Data.yangle = (float) Math.toDegrees(values[1]);
        Data.toyangle = (float) Math.toDegrees(values[1]);
        Data.xangle = (float) Math.toDegrees(values[2]);
      /*  MainRenderer.setbearing(-Data.currentAzimuth);*/
        return values[0];

    }

    private static final float RAD_TO_DEGREE = (float) (360 / (2 * Math.PI));

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
          /*  gravitySmoother.put(event.values);
            gravitySmoother.getSmoothed(accelerometerValues, SMOOTHING);
*/
            accelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
          /*  gravitySmoother.put(event.values);
            gravitySmoother.getSmoothed(accelerometerValues, SMOOTHING);
*/
            gravityValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
           /* magneticFieldSmoother.put(event.values);
            magneticFieldSmoother.getSmoothed(magneticFieldValues, SMOOTHING);*/
            magneticFieldValues = event.values;

        }
        int degree = (int) calculateOrientation();

        //   RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f,
        //          Animation.RELATIVE_TO_SELF, 0.5f);

        // mNewWidget.scrollTo(Data.predegree*15,0);
//        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        //      imgZnz.setRotate(ORIENTATIONS.get(rotation),50,50);
        //   ra.setDuration(200);
        //  layout_sub_Lin.startAnimation(ta);

        // viewaround.startAnimation(ra);
        //  currentDegree = -degree;

       /* if(Control.candrawview==true) {*/

        //自定义动画类animator来达到部分控件的旋转和
        //指向那部分不变
        currentAzimuth = currentDegree;
        mydegree.setText(Data.provider[2] + "\n" +
                Data.q[1] + "\n" +
                "realbearing与指北针距离  " + Data.realbearing + "\n" +
                "databearing    " + Data.bearing + "\n");
        //  Data.currentAzimuth=currentDegree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //    检测搜索关键字是否为空，不为空就开始搜索
    public void searchButton() {
        keyWord = AMapUtil.checkEditText(mSearchText);
        if ("".equals(keyWord)) {
            ToastUtil.show(this, "请输入搜索关键字");

            return;
        } else {
//            默认搜索范围是深圳市，为空是全国
            //  final LinearLayout lin = (LinearLayout) findViewById(R.id.list_Lin);
            Data.SearchpoiList.clear();
            mArPoiSearch = new ArPoiSearch(this, keyWord, "", "深圳市", lin, handler);
            mArPoiSearch.setSearchtype(Servicetype.searchnear_view);
            mArPoiSearch.doSearch();
            Fragment contain = fragmentManager.findFragmentByTag("yourname");
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (contain != null) {
                ft.hide(contain);
                ft.commit();
                // mGlView.setVisibility(View.INVISIBLE);
            }

        }
    }


    /**
     * 初始化视图监听器
     */
    private void registerViewListener() {

    }


    //用于请求searcharround服务和刷新服务右上角的界面
    private Runnable myRunnable = new Runnable() {
        public void run() {
            while (true) {
                /*float degree = -calculateOrientation();
                Data.predegree = degree;*/
               /* layout_sub_Lin.offsetLeftAndRight(Data.predegree*15);*/
                //layout_sub_Lin.scrollTo(Data.predegree*15,0);
                try {
                    Thread.sleep(200);
                    Message circle = new Message();
                    circle.what = 11;
                    handler.sendMessage(circle);

                } catch (InterruptedException e) {

                }
            }


        }

    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
                case 88:
                    Log.i("cc", "有消息了!");
                    //在同一个线程中也可以通过handle进行消息的传递
                    break;
                case 2:

                    mydegree.setText("  handler.getLooper().getThread()" +
                            handler.getLooper().getThread() + "\n" +
                            "selectid" + Data.SelectArroundId + "\n" +
                            "Data.currentdegree=" + Data.currentAzimuth + "\n" +
                            "yangle=" + Data.yangle + "\n" +
                            "xangle=" + Data.xangle + "\n" +
                            "q" + Data.q[0] + " " + Data.q[1] + "   " + Data.q[2] + "\n" +
                            "vector=" + Data.vector[0] + "  " + Data.vector[1] + "   " + Data.vector[2] + "\n" +
                            "data.bearing" + Data.bearing + "\n" +
                            "real=" + Data.realbearing);
                    break;
                case 4:
                    Moveviewfragment fragNear = new Moveviewfragment();
                    //下面的参数可以缺省
                    FragmentTransaction ftnear = fragmentManager.beginTransaction();
                    ftnear.add(R.id.fragmentone, fragNear, "yourname");
                    ftnear.commit();

                    break;
                case 5:
                    fragmenttwo fragGuide = new fragmenttwo();
                    fragGuide.remove();
                    //  fragGuide.setDestination(msg.obj);
                    //下面的参数可以缺省
                    FragmentTransaction ftguide = fragmentManager.beginTransaction();
                    ftguide.add(R.id.fragmenttwo, fragGuide, "guide");
                    ftguide.commit();
                    //((ViewGroup)buttonview.getParent()).removeView(buttonview);
                    //采用移除的效率不高，在添加的时候会卡顿，耗费资源对时间监听由延迟
                    topview.bringToFront();
                    Alivefrag = 2;
                    // setContentView(R.layout.guide_view);
                    break;
                case 9:
                    if (Control.finishLocation == false) {

                        ArPoiSearch Arnear = new ArPoiSearch(Main.this, "", "餐饮服务", "深圳市");
                        Arnear.setHandler(this);
                        Arnear.setSearchtype(Servicetype.searchbound);
                        Arnear.doSearch();
                        Log.i("location", "i am here");

                        Control.finishLocation = true;
                    }
                    break;
                case 11:
//                    viewarround.doRotatetaAnim(Data.todegree,Data.currentAzimuth);
                    Data.todegree = Data.currentAzimuth;
                    break;
            }
            super.handleMessage(msg);
        }

    };



/*    private void fillAdapter(List<TravelingEntity> list) {
            mAdapter.setData(list);
        }*/

    private void loadTextures() {
        mTextures.add(Texture.loadTextureFromApk("Collect/icebird.jpg", getAssets(),
                "icebird"));
        mTextures.add(Texture.loadTextureFromApk("TextureTeapotRed.png", getAssets(),
                "Red"));
        mTextures.add(Texture.loadTextureFromApk("znz.png", getAssets(),
                "znz"));
        mTextures.add(Texture.loadTextureFromApk("001.png", getAssets(),
                "1"));
    }

    public void makeList() {
        Log.i("Main", "listview is searched" + this.toString());

        mAdapter = new SearchpoiAdapter(Main.this, Data.SearchpoiList);
    }

    /*-----返回键功能重写-----*/
    @Override
    public void onBackPressed() {
        if (Alivefrag == 2) {
            Fragment fragment = fragmentManager.findFragmentByTag("guide");

            FragmentTransaction removeft = fragmentManager.beginTransaction();
            removeft.remove(fragment);
            removeft.commit();
            buttonview.bringToFront();
        }else{
            finish();
        }
            //buttonview会盖住之前的摄像头背景，故要将背景的layout提取出来
            //置于最顶部
           /* addContentView(buttonview,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));*/

         /*   closeCamera();
            stopBackgroundThread();
            startBackgroundThread();*/


        // 完全由自己控制返回键逻辑，系统不在控制，但是有个前提是不要在Activity的onKeyDown或者OnKeyUp中拦截掉返回键

        // 拦截：就是在OnKeyDown或者OnKeyUp中自己处理了返回键（这里处理之后return true.或者return false都会导致onBackPressed不会执行）

        // 不拦截：在OnKeyDown和OnKeyUp中返回super对应的方法（如果两个方法都被覆写就分别都要返回super.onKeyDown,super.onKeyUp）
    }
   /* public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            Toast.makeText(this,"魔力去吧Back键测试",1).show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }*/


}
