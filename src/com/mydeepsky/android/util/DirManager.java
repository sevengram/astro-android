package com.mydeepsky.android.util;

import java.io.File;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

public class DirManager {
    private static Context sAppContext;

    public static void init(Context context) {
        sAppContext = context.getApplicationContext();
    }

    public final static String SD_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath();

    public static String getPrivateCachePath() {
        return sAppContext.getCacheDir().getAbsolutePath();
    }

    public static String getPrivateFilesPath() {
        return sAppContext.getFilesDir().getAbsolutePath();
    }

    public static String getCachePath() {
        if (hasExternalStorage()) {
            if (Build.VERSION.SDK_INT >= 8) {
                return sAppContext.getExternalCacheDir().getAbsolutePath();
            } else {
                return Environment.getExternalStorageDirectory() + "/Android/data/"
                        + sAppContext.getPackageName() + "/cache";
            }
        } else {
            return getPrivateCachePath();
        }
    }

    public static String getFilesPath() {
        if (hasExternalStorage()) {
            if (Build.VERSION.SDK_INT >= 8) {
                return sAppContext.getExternalFilesDir(null).getAbsolutePath();
            } else {
                return Environment.getExternalStorageDirectory() + "/Android/data/"
                        + sAppContext.getPackageName() + "/files";
            }
        } else {
            return getPrivateFilesPath();
        }
    }

    public static String getAppPath() {
        if (hasExternalStorage()) {
            StringBuffer buffer = new StringBuffer(SD_PATH);
            return buffer.append(File.separatorChar).append(AppUtil.getCorpName(sAppContext))
                    .append(File.separatorChar).append(AppUtil.getAppName(sAppContext)).toString();
        } else {
            return getPrivateFilesPath();
        }
    }

    public static String getImageCachePath() {
        return getCachePath() + "/image";
    }

    public static String getDownloadPath() {
        return getAppPath() + "/download";
    }

    public static String getListPath() {
        return getAppPath() + "/list";
    }

    public static boolean hasExternalStorage() {
        if (Build.VERSION.SDK_INT < 8) {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } else {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && sAppContext.getExternalCacheDir() != null
                    && sAppContext.getExternalFilesDir(null) != null;
        }
    }

}
