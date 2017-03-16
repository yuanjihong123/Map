package com.example.administrator.mapclient.activity.homepage;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.example.administrator.mapclient.R;
import com.example.administrator.mapclient.view.ArcMenu;
import com.example.administrator.mapclient.view.MediaManager;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 主页
 */
public class HomeActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {
    //地图放大级别
    private ArcMenu mArcMenu;
    private RelativeLayout traffic, butt_selector,tvLocation;
    private RelativeLayout navigate;
    private PopupWindow popupWindow;
    private int from = 0;
    private Button but_seekhelp;
    private MapView mMapView;
    private AMap aMap;
    private double lat=39.9088691069;
    private double lon=116.3973823161;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirstLoc = true;
    private Mylistener mMylistener;
    //地图放大级别
    private float zoomlevel = 17f;
    //声明mListener对象，定位监听器
    private OnLocationChangedListener mListener = null;
    private MapView map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        this.map = (MapView) findViewById(R.id.map);
        //创建地图
        initdate();
        mMapView.onCreate(savedInstanceState);
    }
    /**
     * 初始化数据
     */
    private void initdate() {
        findview();
        initEvent();//展开or撤回
        foundmap();
        //自定义的回到当前位置的事件
        mMylistener = new Mylistener();
        tvLocation.setOnClickListener(mMylistener);
        butt_selector.setOnClickListener(mMylistener);
        but_seekhelp.setOnClickListener(mMylistener);
        traffic.setOnClickListener(mMylistener);
        navigate.setOnClickListener(mMylistener);
    }
    /**
     * 查找控件
     */
    private void findview() {
        tvLocation = (RelativeLayout) findViewById(R.id.tvLocation);
        mMapView = (MapView) findViewById(R.id.map);
        mArcMenu = (ArcMenu) findViewById(R.id.id_menu);
        butt_selector = (RelativeLayout) findViewById(R.id.butt_selector);
        traffic = (RelativeLayout) findViewById(R.id.traffic);
        navigate = (RelativeLayout) findViewById(R.id.navigate);
        but_seekhelp = (Button) findViewById(R.id.but_seekhelp);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                aMapLocation.getLocationType();
                lat = aMapLocation.getLatitude();
                lon = aMapLocation.getLongitude();
                aMapLocation.getAccuracy();
                aMapLocation.getAddress();
                aMapLocation.getCountry();//国家信息
                //Log.e("定位信息：", aMapLocation.getCountry().toString());

                aMapLocation.getProvince();
                aMapLocation.getCity();
                aMapLocation.getDistrict();
                aMapLocation.getStreet();
                aMapLocation.getStreetNum();
                aMapLocation.getCityCode();
                aMapLocation.getAdCode();
                aMapLocation.getAoiName();
                //获取定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);
                if (isFirstLoc) {

                    //设置缩放级别（缩放级别为4-20级）
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomlevel));
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    startLocation();
                    mListener.onLocationChanged(aMapLocation);
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }

            } else {
                Log.e("地图错误", "定位失败, 错误码:" + aMapLocation.getErrorCode() + ", 错误信息:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 设置点击监听
     */
    class Mylistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvLocation:
                    startLocation();
                    break;
                case R.id.traffic:
                    dialog();
                    break;
                case R.id.navigate: //导航

                    break;
                case R.id.but_seekhelp: //立即求助
                    from = Location.BOTTOM.ordinal();
                    initPopupWindow();
                    break;
                case R.id.butt_selector: //查看区域密度

                    break;
            }
        }
    }
    private void foundmap() {

        if (aMap == null) {
            aMap = mMapView.getMap();
            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(HomeActivity.this);
            settings.setMyLocationButtonEnabled(true);
            //添加指南针
            settings.setCompassEnabled(true);
            aMap.getCameraPosition();
            //管理缩放控件
            settings.setZoomControlsEnabled(false);
            settings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
            settings.setScaleControlsEnabled(true);
            //float scale = aMap.getScalePerPixel();
            aMap.setMyLocationEnabled(true);
            aMap.clear();
        }
        //开始定位
        location();
    }
    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();

    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        MediaManager.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        MediaManager.pause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }
    @Override
    public void deactivate() {
        mListener = null;
    }

    private void startLocation() {

        if (mLocationClient != null) {
            //19f代表地图放大的级别
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), zoomlevel));
        }
    }
    private void initEvent() {//弹出与撤回按钮监听
        if (mArcMenu.isOpen()) {
            mArcMenu.toggleMenu(600);
        }
        mArcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                switch (pos) {
                    case 1:
                        startActivity(new Intent(HomeActivity.this, SetActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this, FriendActivity.class));
                        break;
                    case 3:
                        Intent intent1 = new Intent(HomeActivity.this, PersonalActivity.class);
                        intent1.putExtra("flag",1);
                        startActivity(intent1);

                        break;
                    case 4:
                        startActivity(new Intent(HomeActivity.this, DynamicActivity.class));
                        break;
                    case 5:
                        Intent intent = new Intent(HomeActivity.this, InterestActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
    /**
     * 弹出提示框
     */
    public void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("提示");
        builder.setMessage("设置为交通地图？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //显示实时路况图层，aMap是地图控制器对象。
                aMap.setTrafficEnabled(true);
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //关闭实时路况图层，aMap是地图控制器对象。
                aMap.setTrafficEnabled(false);
            }
        });
        builder.create().show();
    }

    //底部弹出菜单
    protected void initPopupWindow() {
        final View popupWindowView = getLayoutInflater().inflate(R.layout.bottom_pop, null);
        //内容，高度，宽度
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        } else {
            popupWindow = new PopupWindow(popupWindowView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.FILL_PARENT, true);
        }
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0x0ffffff);
        popupWindow.setBackgroundDrawable(dw);
        //显示位置
        if (Location.BOTTOM.ordinal() == from) {
            popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
        //设置背景半透明
        backgroundAlpha(0.2f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                popupWindow.dismiss();
                return false;
            }
        });
        Button close = (Button) popupWindowView.findViewById(R.id.close);
        Button paose = (Button) popupWindowView.findViewById(R.id.paose);
        Button save = (Button) popupWindowView.findViewById(R.id.save);
        Button save1 = (Button) popupWindowView.findViewById(R.id.save1);
        //求助附近人
        paose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CustodianActivity.class));
                popupWindow.dismiss();
            }
        });
        //拨打紧急电话
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:110"));
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        //取消
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //求助管理员
        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CustodianActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    /**
     * 菜单弹出方向
     */
    public enum Location {
        BOTTOM;
    }
    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }
}
