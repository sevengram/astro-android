package com.mydeepsky.android.location;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.mydeepsky.android.util.NetworkManager;
import com.mydeepsky.android.util.StringUtil;

public class GoogleLocator extends Locator implements LocationListener {
    static final String TAG = "GoogleLocator";

    public static final int ID = 62;

    private static final int MSG_TIMEOUT = 9001;

    private static final int MIN_DISTANCE = 100;

    private static final int MAX_GEOCODER_RESULTS = 5;

    private LocationManager locationManager;

    private Geocoder geocoder;

    private Timer timer;

    private int gpsTimeout = 300000;

    private int networkTimeout = 30000;

    private long startTime = -1;

    private int timeout;

    public GoogleLocator(Context context) {
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.geocoder = new Geocoder(context);
    }

    @Override
    public boolean setProvider() {
        Provider newProvider;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            newProvider = Provider.GPS;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && NetworkManager.isWifiAvailable()) {
            newProvider = Provider.WIFI;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                && NetworkManager.isNetworkAvailable()) {
            newProvider = Provider.MOBILE;
        } else {
            newProvider = Provider.NONE;
        }
        boolean result = (!newProvider.equals(this.provider));
        this.provider = newProvider;
        return result;
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
            timeout = networkTimeout;
            startTime = System.currentTimeMillis();
            break;
        case GPS:
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, gpsScanSpan,
                    MIN_DISTANCE, this);
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        networkScanSpan, MIN_DISTANCE, this);
            } catch (IllegalArgumentException e) {
            }
            timeout = gpsTimeout;
            startTime = System.currentTimeMillis();
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
            StringBuffer stringBuffer = new StringBuffer();
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
        return (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && NetworkManager
                .isNetworkAvailable())
                || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
