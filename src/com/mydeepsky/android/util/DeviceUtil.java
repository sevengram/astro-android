package com.mydeepsky.android.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class DeviceUtil {
    public static String getID(Context context) {
        StringBuffer sb = new StringBuffer();
        return sb.append(getImei(context)).append(NetworkManager.getMacAddress().replace(":", ""))
                .toString();
    }

    public static String getImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return TextUtils.isEmpty(tm.getDeviceId()) ? "000000000000000" : tm.getDeviceId();
    }

    public static String getImsi(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return StringUtil.safeString(tm.getSubscriberId());
    }

    public static int getAndroidSdkVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    public static String getDeviceModel() {
        StringBuffer sb = new StringBuffer();
        sb.append(Build.MANUFACTURER).append(' ').append(Build.BRAND).append(' ')
                .append(Build.MODEL);
        return sb.toString();
    }

    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}
