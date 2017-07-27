package com.dragon.navigation.UI.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
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
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.navigation.R;
import com.dragon.navigation.UI.Activity.Main;
import com.dragon.navigation.UI.Activity.MainActivity;
import com.dragon.navigation.UI.Base.BaseFragment;
import com.dragon.navigation.UI.Presenter.MainFgPresenter;
import com.dragon.navigation.UI.View.IMainFgView;
import com.dragon.navigation.View.Mytestview;
import com.dragon.navigation.use.Texture;
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

import butterknife.BindView;

/**
 * Created by Administrator on 2017/6/22.
 */

public class MainFragment extends BaseFragment<IMainFgView, MainFgPresenter> implements IMainFgView {
    @BindView(R.id.texture)
    TextureView mtextureView;
    @BindView(R.id.topview)
    FrameLayout rootview;
    @BindView(R.id.part_camera2)
    RelativeLayout partCamera2;
    @BindView(R.id.Compass)
    Mytestview Compass;
    private static final String TAG = "MainFragment";
    //    以下定义是摄像头相关和摄像头会话相关
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    //用SparseIntArray来代替hashMap，进行性能优化。
    private Mytestview viewarround;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();




    private AccountHeader headerResult = null;
    private Drawer result = null;
    private static final int PROFILE_SETTING = 100000;

    private Bundle mysavedInstanceState;
    TextureView.SurfaceTextureListener textureListener;
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


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
        @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected MainFgPresenter createPresenter() {
        return new MainFgPresenter((MainActivity) getActivity());
    }
    public void init(){

    }
    @Override
    protected int provideContentViewId() {

        return R.layout.background;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        Log.e(TAG, "onCreateOptionsMenu()");
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.navigation,menu);
    }
    @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // don't look at this layout it's just a listView to show how to handle the keyboard
      View father = inflater.inflate(R.layout.blanklayout2, container, false);
      View view = inflater.inflate(R.layout.fg_background, container, false);
     Toolbar toolbar = (Toolbar) view.findViewById(R.id.mytoolbar);
     ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //不显示默认的标题
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
       //给予fragment menu的句柄
        setHasOptionsMenu(true);
        //设置沉浸式半透明的toolbar
      toolbar.setBackgroundColor(Color.BLACK);
      toolbar.getBackground().setAlpha(90);
      //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.drawer_item_fullscreen_drawer);
      viewarround = (Mytestview) view.findViewById(R.id.Compass);
      mtextureView =(TextureView)view.findViewById(R.id.texture);
      mtextureView.setSurfaceTextureListener(textureListener);
      //textureview盖住了其他的view

      final IProfile profile = new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(100);
      final IProfile profile2 = new ProfileDrawerItem().withName("Bernat Borras").withEmail("alorma@github.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460")).withIdentifier(101);
      final IProfile profile3 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2).withIdentifier(102);
      final IProfile profile4 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3).withIdentifier(103);
      final IProfile profile5 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4).withIdentifier(104);
      final IProfile profile6 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5).withIdentifier(105);
      // Create the AccountHeader
      headerResult = new AccountHeaderBuilder()
              .withActivity(getActivity())
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
                      new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_plus).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING),
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
              .withActivity(getActivity())
             .withToolbar(toolbar)
              .withFullscreen(true)
              .withHasStableIds(true)
            //  .withDisplayBelowStatusBar(false)
              .withRootView((ViewGroup)view.findViewById(R.id.rootviewfather))
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
                .withDrawerGravity(Gravity.LEFT)
//                .withShowDrawerUntilDraggedOpened(true)
              .buildForFragment();
        // result.getDrawerLayout().setFitsSystemWindows(false);
      result.getSlider().setFitsSystemWindows(false);
      ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
          if (Build.VERSION.SDK_INT >= 19) {
              result.getDrawerLayout().setFitsSystemWindows(false);
          }
      return view;
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

    @Override
    public void onResume() {
        super.onResume();
        //        注册监听事件
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (mtextureView.isAvailable()) {
            Log.e(TAG, "textureViewAvailable");
//            openCamera();
            initCamera2();
        } else {
             mtextureView.setSurfaceTextureListener(textureListener);
        }
    }
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
            outputSurfaces.add(new Surface(mtextureView.getSurfaceTexture()));
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
            SurfaceTexture texture = mtextureView.getSurfaceTexture();
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

}
