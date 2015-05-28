package com.mydeepsky.android.location;

import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.mydeepsky.android.util.NetworkManager;
import com.mydeepsky.android.util.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

public class GoogleLocator extends Locator implements LocationListener {
    static final String TAG = "GoogleLocator";

    public static final int ID = 62;

    private static final int MSG_TIMEOUT = 9001;

    private static final int MIN_DISTANCE = 100;

    private static final int MAX_GEOCODER_RESULTS = 5;

    private static final int GPS_TIMEOUT = 300000;

    private static final int NETWORK_TIMEOUT = 30000;

    private LocationManager locationManager;

    private Geocoder geocoder;

    private Timer timer;

    private long startTime = -1;

    private int timeout;

    public GoogleLocator(Context context) {
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.geocoder = new Geocoder(context);
    }

    @Override
    public void setProvider() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            this.provider = Provider.GPS;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && NetworkManager
            .isWifiAvailable()) {
            this.provider = Provider.WIFI;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && NetworkManager
            .isNetworkAvailable()) {
            this.provider = Provider.MOBILE;
        } else {
            this.provider = Provider.NONE;
        }
    }

    @Override
    public void start(Context context) {
        Log.d(TAG, "start");
        setProvider();
        switch (this.provider) {
            case WIFI:
            case MOBILE:
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    networkScanSpan, MIN_DISTANCE, this);
                timeout = NETWORK_TIMEOUT;
                break;
            case GPS:
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsScanSpan,
                    MIN_DISTANCE, this);
                try {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        networkScanSpan, MIN_DISTANCE, this);
                } catch (IllegalArgumentException ignored) {
                }
                timeout = GPS_TIMEOUT;
                break;
            default:
                break;
        }
        if (this.provider == Provider.NONE) {
            if (listener != null) {
                listener.onWithoutService(ERR_NO_LOCATION_SERVICE);
            }
            stop();
        } else {
            startTime = System.currentTimeMillis();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(MSG_TIMEOUT);
                }
            }, timeout);
        }
    }

    @Override
    public void stop() {
        startTime = -1;
        locationManager.removeUpdates(this);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIMEOUT:
                    if (listener != null)
                        listener.onLocationError(true);
                    stop();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        if (location == null)
            return;
        Log.d(TAG, location.toString());
        LocationInfo result = new LocationInfo();
        result.setLatitude(location.getLatitude());
        result.setLongitude(location.getLongitude());
        try {
            Address address = geocoder.getFromLocation(location.getLatitude(),
                location.getLongitude(), MAX_GEOCODER_RESULTS).get(0);
            Log.d(TAG, address.toString());
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                stringBuffer.append(address.getAddressLine(i)).append(' ');
            }
            result.setDetail(stringBuffer.toString());
            result.setCountry(StringUtil.safeString(address.getCountryName()));
            result.setProvince(StringUtil.safeString(address.getAdminArea()));
            result.setCity(StringUtil.safeString(address.getLocality()));
            result.setDistrict(StringUtil.safeString(address.getSubLocality()));
        } catch (Exception e) {
            result.setCountry("");
            result.setProvince("");
            result.setCity("");
            result.setDistrict("");
        }
        long costTime = startTime > 0 ? System.currentTimeMillis() - startTime : -1;
        startTime = -1;
        if (listener != null) {
            listener.onLocationUpdate(result, costTime, ID);
        }
        stop();
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isAvailable() {
        return
            (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && NetworkManager.isNetworkAvailable())
                || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
