package com.mydeepsky.android.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class NetworkManager {
    private static Context sAppContext;

    private static final int NETWORK_TYPE_WIFI = 50;

    public static void init(Context context) {
        sAppContext = context.getApplicationContext();
    }

    public static String getMacAddress() {
        WifiManager wifiManager = (WifiManager) sAppContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return TextUtils.isEmpty(wifiInfo.getMacAddress()) ? "000000000000" : wifiInfo
                .getMacAddress();
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) sAppContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) sAppContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isMobileConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) sAppContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager
            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) sAppContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public static boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) sAppContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static boolean isGpsEnable() {
        LocationManager locationManager = ((LocationManager) sAppContext
                .getSystemService(Context.LOCATION_SERVICE));
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static int getNetworkType() {
        if (isWifiConnected()) {
            return NETWORK_TYPE_WIFI;
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) sAppContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getNetworkType();
        }
    }

    public static String getSimpleNetworkString() {
        if (isWifiConnected()) {
            return "wifi";
        } else if (isFastMobileNetwork()) {
            return "3g";
        } else {
            return "2g";
        }
    }

    public static boolean isFastMobileNetwork() {
        TelephonyManager telephonyManager = (TelephonyManager) sAppContext
                .getSystemService(Context.TELEPHONY_SERVICE);

        int type = telephonyManager.getNetworkType();
        switch (type) {
        case TelephonyManager.NETWORK_TYPE_1xRTT:
            return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_CDMA:
            return false; // ~ 14-64 kbps
        case TelephonyManager.NETWORK_TYPE_EDGE:
            return false; // ~ 50-100 kbps
        case TelephonyManager.NETWORK_TYPE_EHRPD:
            return true; // ~ 1-2 Mbps
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
            return true; // ~ 400-1000 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
            return true; // ~ 600-1400 kbps
        case TelephonyManager.NETWORK_TYPE_EVDO_B:
            return true; // ~ 5 Mbps
        case TelephonyManager.NETWORK_TYPE_GPRS:
            return false; // ~ 100 kbps
        case TelephonyManager.NETWORK_TYPE_HSDPA:
            return true; // ~ 2-14 Mbps
        case TelephonyManager.NETWORK_TYPE_HSPA:
            return true; // ~ 700-1700 kbps
        case TelephonyManager.NETWORK_TYPE_HSPAP:
            return true; // ~ 10-20 Mbps
        case TelephonyManager.NETWORK_TYPE_HSUPA:
            return true; // ~ 1-23 Mbps
        case TelephonyManager.NETWORK_TYPE_IDEN:
            return false; // ~ 25 kbps
        case TelephonyManager.NETWORK_TYPE_UMTS:
            return true; // ~ 400-7000 kbps
        case TelephonyManager.NETWORK_TYPE_LTE:
            return true; // ~ 10+ Mbps
        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            return false;
        default:
            return false;
        }
    }
}
