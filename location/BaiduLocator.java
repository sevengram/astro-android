package com.mydeepsky.android.location;

import android.content.Context;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.mydeepsky.android.util.NetworkManager;

public class BaiduLocator extends Locator implements BDLocationListener {
    static final String TAG = "BaiduLocator";

    private static final String DEFAULT_COUNTRY_ZH = "中国";

    public static final int ID = 61;

    private static final int GPS_RETRY_TIMES = 10;

    private static final int NETWORK_RETRY_TIMES = 5;

    private int retryCount = 0;

    private int retryTimes = 5;

    private long startTime = -1;

    private LocationClient locationClient;

    @Override
    public void setProvider() {
        if (NetworkManager.isGpsEnable()) {
            this.provider = Provider.GPS;
        } else if (NetworkManager.isWifiAvailable()) {
            this.provider = Provider.WIFI;
        } else if (NetworkManager.isNetworkAvailable()) {
            this.provider = Provider.MOBILE;
        } else {
            this.provider = Provider.NONE;
        }
    }

    public BaiduLocator(Context context) {
        this.locationClient = new LocationClient(context.getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setScanSpan(networkScanSpan);
        retryTimes = NETWORK_RETRY_TIMES;
        this.locationClient.setLocOption(option);
        this.locationClient.registerLocationListener(this);
    }

    @Override
    public void start(Context context) {
        Log.d(TAG, "start");
        LocationClientOption option = locationClient.getLocOption();
        setProvider();
        switch (this.provider) {
            case WIFI:
            case MOBILE:
                option.setOpenGps(false);
                option.setScanSpan(networkScanSpan);
                retryTimes = NETWORK_RETRY_TIMES;
                break;
            case GPS:
                option.setOpenGps(true);
                option.setScanSpan(gpsScanSpan);
                retryTimes = GPS_RETRY_TIMES;
                break;
            default:
                break;
        }
        if (this.provider == Provider.NONE) {
            if (listener != null) {
                listener.onWithoutService(ERR_NO_NETWORK);
            }
            stop();
        } else {
            startTime = System.currentTimeMillis();
            locationClient.setLocOption(option);
            retryCount = 0;
            locationClient.start();
        }
    }

    public void stop() {
        if (locationClient.isStarted()) {
            startTime = -1;
            locationClient.stop();
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null)
            return;
        LocationInfo result = new LocationInfo();
        switch (location.getLocType()) {
            case BDLocation.TypeGpsLocation:
            case BDLocation.TypeCacheLocation:
            case BDLocation.TypeNetWorkLocation:
            case BDLocation.TypeOffLineLocation:
                result.setLongitude(location.getLongitude());
                result.setLatitude(location.getLatitude());
                result.setCountry(DEFAULT_COUNTRY_ZH);
                result.setProvince(location.getProvince());
                result.setCity(location.getCity());
                result.setDistrict(location.getDistrict());
                result.setDetail(location.getAddrStr());
                Log.d(TAG, "Addr: " + location.getAddrStr());
                long costTime = startTime > 0 ? System.currentTimeMillis() - startTime : -1;
                startTime = -1;
                if (listener != null)
                    listener.onLocationUpdate(result, costTime, ID);
                stop();
                break;
            default:
                retryCount++;
                if (retryCount > retryTimes) {
                    if (listener != null)
                        listener.onLocationError(true);
                    stop();
                }
                break;
        }
    }

    @Override
    public boolean isAvailable() {
        return NetworkManager.isNetworkAvailable() || NetworkManager.isGpsEnable();
    }
}
