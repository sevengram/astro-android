package com.mydeepsky.android.util;

import android.content.Context;

public class ResourceManager {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static String getString(int resid) {
        if (mContext != null) {
            return mContext.getString(resid);
        } else {
            return "";
        }
    }
}
