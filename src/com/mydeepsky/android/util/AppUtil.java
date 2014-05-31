package com.mydeepsky.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppUtil {
    public static String getChannel(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo.metaData.containsKey("UMENG_CHANNEL")) {
                return applicationInfo.metaData.get("UMENG_CHANNEL").toString();
            }
        } catch (NameNotFoundException e) {
        }
        return "unknown";
    }

    public static String getCorpName(Context context) {
        String packageName = context.getPackageName();
        String[] subs = packageName.split("\\.");
        if (subs.length > 1) {
            return subs[1];
        } else {
            return "";
        }
    }

    public static String getAppName(Context context) {
        String packageName = context.getPackageName();
        String[] subs = packageName.split("\\.");
        if (subs.length > 2) {
            return subs[2];
        } else {
            return "";
        }
    }

    public static String getVersionName(Context context) {
        String packageName = context.getPackageName();
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        String packageName = context.getPackageName();
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

}
