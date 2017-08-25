package com.edward.navigation.UI.Fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapBaseIndoorMapInfo;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.IndoorRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorPlanNode;
import com.baidu.mapapi.search.route.IndoorRouteLine;
import com.baidu.mapapi.search.route.IndoorRoutePlanOption;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.bumptech.glide.Glide;
import com.edward.navigation.R;
import com.edward.navigation.UI.Activity.MainActivity;
import com.edward.navigation.UI.Activity.MyInfoActivity;
import com.edward.navigation.UI.Activity.SettingActivity;
import com.edward.navigation.UI.Base.BaseFragment;
import com.edward.navigation.UI.Presenter.MainFgPresenter;
import com.edward.navigation.UI.Presenter.MeFgPresenter;
import com.edward.navigation.UI.View.IMainFgView;
import com.edward.navigation.UI.View.IMeFgView;
import com.edward.navigation.View.Mytestview;
import com.edward.navigation.View.indoorview.BaseStripAdapter;
import com.edward.navigation.View.indoorview.StripListView;
import com.edward.navigation.app.AppConst;
import com.edward.navigation.manager.BroadcastManager;
import com.edward.navigation.use.Texture;
import com.edward.navigation.util.LogUtils;
import com.edward.navigation.util.UIUtils;
import com.edward.navigation.widget.CustomDialog;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.lqr.optionitemview.OptionItemView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.rong.imlib.model.UserInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @创建者 CSDN_LQR
 * @描述 我界面
 */
public class TestFragment extends BaseFragment<IMainFgView, MainFgPresenter> implements IMainFgView ,OnGetRoutePlanResultListener {

//    private CustomDialog mQrCardDialog;
    @BindView(R.id.texture)
    TextureView mytextureview;
    @OnClick(R.id.texture)
    public void click(){
        Log.i("TestFragment","edward");
    }
   /* @BindView(R.id.llMyInfo)
    LinearLayout mLlMyInfo;
    @BindView(R.id.ivHeader)
    ImageView mIvHeader;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvAccount)
    TextView mTvAccount;
    @BindView(R.id.ivQRCordCard)
    ImageView mIvQRCordCard;

    @BindView(R.id.oivAlbum)
    OptionItemView mOivAlbum;
    @BindView(R.id.oivCollection)
    OptionItemView mOivCollection;
    @BindView(R.id.oivWallet)
    OptionItemView mOivWallet;
    @BindView(R.id.oivCardPaket)
    OptionItemView mOivCardPaket;

    @BindView(R.id.oivSetting)
    OptionItemView mOivSetting;
*/

    //    以下定义是摄像头相关和摄像头会话相关
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private Bundle mysavedInstanceState;
    TextureView.SurfaceTextureListener textureListener;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final String TAG = "TestFragment";


    /**
     * MapView 是地图主控件
     * TextureView 是摄像头界面
     */
    FrameLayout.LayoutParams biglayout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    FrameLayout.LayoutParams smalllayout = new FrameLayout.LayoutParams(UIUtils.dip2Px(120),UIUtils.dip2Px(120), Gravity.BOTTOM|Gravity.RIGHT);
    Boolean bigcamera = false;
    @BindView(R.id.texture_transparent)
    TextureView mblanktexture;
    /*    @OnClick(R.id.texture)
        public void Textureonclick(){
            Log.i(TAG,"texture");
            if (!bigcamera) {//小摄像头说明地图大；那么地图变小
                mtextureView.setLayoutParams(biglayout);
                mTextureMapView.setLayoutParams(smalllayout);
                mTextureMapView.bringToFront();
                maincontent.invalidate();

            }
        }*/
    @BindView(R.id.map)
    TextureMapView mTextureMapView;

    @BindView(R.id.bmapView)
    TextureMapView mMapView;
    BaiduMap mBaiduMap;
    MapView mSmallMapView;
    @BindView(R.id.isIndoor)
    Button isIndoorBtn;
    @BindView(R.id.indoorRoutePlane)
    Button indoorRoutePlane;
    Boolean isIndoor = true;
    @BindView(R.id.Relayout_indoor)
    RelativeLayout layout;

    StripListView stripListView;
    BaseStripAdapter mFloorListAdapter;
    MapBaseIndoorMapInfo mMapBaseIndoorMapInfo = null;
    RoutePlanSearch mSearch;
    IndoorRouteLine mIndoorRouteline;
    IndoorRouteOverlay mIndoorRoutelineOverlay = null;
    int nodeIndex = -1;
    private TextView popupText = null; // 泡泡view

    @BindView(R.id.pre)
    Button mBtnPre = null; // 上一个节点
    @BindView(R.id.next)
    Button mBtnNext = null; // 下一个节点
    @BindView(R.id.maincontent)
    FrameLayout maincontent;

    //当前地点
    LatLng currentPt;
    @BindView(R.id.layout_control)
    RelativeLayout mRelayout_control;
    @BindView(R.id.topview)
    DrawerLayout rootview;
    @BindView(R.id.floatingToolbar)
    FloatingToolbar mFloatingToolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @OnClick(R.id.fab)
    public void clickfab(){
        if(mFloatingToolbar != null)
            Log.i("MainFragment", "fab");
        //  mFloatingToolbar.show();
    }
    @BindView(R.id.Compass)
    Mytestview Compass;
    @OnClick(R.id.Compass)
    public void setViewarround(){
        // rootview.openDrawer(GravityCompat.START);
        Log.i("MainFragment","setViewarround");
    }

    /*@SuppressWarnings("unused")
    @Override
    public void init() {
        registerBR();
    }
*/
 /*   @Override
    public void initData() {
        mPresenter.loadUserInfo();
    }
*/

    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textureListener = new TextureView.SurfaceTextureListener() {
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


    }
    @Override
    public void initView(View rootView) {
     /*   mIvQRCordCard.setOnClickListener(v -> showQRCard());
        mOivAlbum.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_JIAN_SHU));
        mOivCollection.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_CSDN));
        mOivWallet.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_OSCHINA));
        mOivCardPaket.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToWebViewActivity(AppConst.WeChatUrl.MY_GITHUB));
*/
        mBaiduMap = mMapView.getMap();
        if(mBaiduMap == null){
            Log.e(TAG,"null");
        }
        mBaiduMap.setViewPadding(0,0,0,-200);

        mFloatingToolbar.attachFab(fab);
        //mTextureMapView.setVisibility(View.INVISIBLE);
        mMapView.setVisibility(View.INVISIBLE);
        // mtextureView.setVisibility(View.INVISIBLE);
        layout.setVisibility(View.INVISIBLE);
        mRelayout_control.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initListener() {
       /* mLlMyInfo.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToActivityAndClearTop(MyInfoActivity.class));
        mOivSetting.setOnClickListener(v -> ((MainActivity) getActivity()).jumpToActivityAndClearTop(SettingActivity.class));*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBR();
    }

   /* private void showQRCard() {
        if (mQrCardDialog == null) {
            View qrCardView = View.inflate(getActivity(), R.layout.include_qrcode_card, null);
            ImageView ivHeader = (ImageView) qrCardView.findViewById(R.id.ivHeader);
            TextView tvName = (TextView) qrCardView.findViewById(R.id.tvName);
            ImageView ivCard = (ImageView) qrCardView.findViewById(R.id.ivCard);
            TextView tvTip = (TextView) qrCardView.findViewById(R.id.tvTip);
            tvTip.setText(UIUtils.getString(R.string.qr_code_card_tip));

            UserInfo userInfo = mPresenter.getUserInfo();
            if (userInfo != null) {
                Glide.with(getActivity()).load(userInfo.getPortraitUri()).centerCrop().into(ivHeader);
                tvName.setText(userInfo.getName());
                Observable.just(QRCodeEncoder.syncEncodeQRCode(AppConst.QrCodeCommon.ADD + userInfo.getUserId(), UIUtils.dip2Px(100)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> ivCard.setImageBitmap(bitmap), this::loadQRCardError);
            }

            mQrCardDialog = new CustomDialog(getActivity(), 300, 400, qrCardView, R.style.MyDialog);
        }
        mQrCardDialog.show();
    }

    private void loadQRCardError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
    }

    private void registerBR() {
        BroadcastManager.getInstance(getActivity()).register(AppConst.CHANGE_INFO_FOR_ME, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.loadUserInfo();
            }
        });
    }*/
    @Override
    public void onResume() {

        Log.i("father", "textureViewAvailable");
        maincontent.invalidate();
        startBackgroundThread();
        if (mytextureview.isAvailable()) {
            Log.e(TAG, "textureViewAvailable");
//            openCamera();
            initCamera2();
        } else {
             mytextureview.setSurfaceTextureListener(textureListener);
        }
        //加了以后，在进行右滑时有问题出现背景自动更新位置变化
       // mMapView.onResume();
       // mTextureMapView.onResume();
        super.onResume();
    }
    private void unregisterBR() {
        BroadcastManager.getInstance(getActivity()).unregister(AppConst.CHANGE_INFO_FOR_ME);
    }

    @Override
    protected MainFgPresenter createPresenter() {
        return new MainFgPresenter((MainActivity) getActivity());
    }
    @Override
    protected int provideContentViewId() {
        return R.layout.cameradrawer;
    }

 /*   @Override
    public ImageView getIvHeader() {
        return mIvHeader;
    }

    @Override
    public TextView getTvName() {
        return mTvName;
    }

    @Override
    public TextView getTvAccount() {
        return mTvAccount;
    }*/

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
    @Override
    public void onStart(){
        super.onStart();
        deletelogoview(mTextureMapView);
        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(39.915071, 116.403907); // 默认 天安门
        float zoom = 11.0f; // 默认 11级
        Intent intent = getActivity().getIntent();
        if (null != intent) {
            center = new LatLng(intent.getDoubleExtra("y", 39.915071),
                    intent.getDoubleExtra("x", 116.403907));
            zoom = intent.getFloatExtra("level", 11.0f);
        }
        builder.target(center).zoom(zoom);
        mBaiduMap = mMapView.getMap();
        deletelogoview(mMapView);
        LatLng centerpos = new LatLng(39.916958, 116.379278); // 西单大悦城
        builder = new MapStatus.Builder();
        builder.target(centerpos).zoom(19.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mBaiduMap.setIndoorEnable(true);

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        indoorRoutePlane.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发起室内路线规划检索
                IndoorPlanNode startNode = new IndoorPlanNode(new LatLng(39.917380, 116.37978), "F1");
                IndoorPlanNode endNode = new IndoorPlanNode(new LatLng(39.917239, 116.37955), "F6");
                IndoorRoutePlanOption irpo = new IndoorRoutePlanOption().from(startNode).to(endNode);
                mSearch.walkingIndoorSearch(irpo);
            }
        });

        isIndoorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isIndoor) {
                    EnableIndoorMap();
                } else {
                    DisableIndoorMap();
                }
                isIndoor = !isIndoor;
            }
        });

        stripListView = new StripListView(getContext());
        layout.addView( stripListView );
        //  setContentView(layout);
        mFloorListAdapter = new BaseStripAdapter(getContext());


        mBaiduMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
            @Override
            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
                if (b == false || mapBaseIndoorMapInfo == null) {
                    stripListView.setVisibility(View.INVISIBLE);
                    return;
                }
                mFloorListAdapter.setmFloorList( mapBaseIndoorMapInfo.getFloors());
                stripListView.setVisibility(View.VISIBLE);
                stripListView.setStripAdapter(mFloorListAdapter);
                mMapBaseIndoorMapInfo = mapBaseIndoorMapInfo;
            }
        });
        stripListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mMapBaseIndoorMapInfo == null) {
                    return;
                }
                String floor = (String) mFloorListAdapter.getItem(position);
                mBaiduMap.switchBaseIndoorMapFloor(floor, mMapBaseIndoorMapInfo.getID());
                mFloorListAdapter.setSelectedPostion(position);
                mFloorListAdapter.notifyDataSetInvalidated();
            }
        });
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
    }

    private void EnableIndoorMap() {

        indoorRoutePlane.setEnabled(true);
        mBaiduMap.setIndoorEnable(true);
        isIndoorBtn.setText("关闭室内图");

        Toast.makeText(getActivity(), "室内图已打开", Toast.LENGTH_SHORT).show();
    }

    private void DisableIndoorMap() {
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);

        indoorRoutePlane.setEnabled(false);
        if (null != mIndoorRoutelineOverlay) {
            mIndoorRoutelineOverlay.removeFromMap();
            mIndoorRoutelineOverlay = null;
        }

        mBaiduMap.clear();
        mBaiduMap.setIndoorEnable(false);
        isIndoorBtn.setText("打开室内图");

        Toast.makeText(getActivity(), "室内图已关闭", Toast.LENGTH_SHORT).show();
    }

    protected void initCamera2() {
        if (null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
//            如果没打开则返回
            return;
        }
//        摄像头管理器，专门用于检测、打开系统摄像头，并连接CameraDevices.
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
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
            outputSurfaces.add(new Surface(mytextureview.getSurfaceTexture()));
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
            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
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
            SurfaceTexture texture = mytextureview.getSurfaceTexture();
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
                    Toast.makeText(getActivity(), "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
//        实例化摄像头
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
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
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                manager.openCamera(cameraId, stateCallback, null);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera 1");
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
            cameraDevice = null;
        }

        //        打开摄像头出现错误时激发方法
        @Override
        public void onError(CameraDevice camera, int error) {
            Log.e(TAG, "onClose");
            cameraDevice.close();
            cameraDevice = null;
        }
    };
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
    private void deletelogoview(TextureMapView mMapView){
        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }

        //地图上比例尺
        mMapView.showScaleControl(false);
        // 隐藏缩放控件
        mMapView.showZoomControls(false);
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

    //以下为不同的路线规划的返回

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        if (indoorRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            IndoorRouteOverlay overlay = new IndoorRouteOverlay(mBaiduMap);
            mIndoorRouteline = indoorRouteResult.getRouteLines().get(0);
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);
            overlay.setData(indoorRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }


}
