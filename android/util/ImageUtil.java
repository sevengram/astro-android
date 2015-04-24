package com.mydeepsky.android.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtil {

    public static int getResId(Class<?> drawable, String name, int defaultValue) {
        try {
            return (Integer) drawable.getField(name).get(null);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static byte[] bitmap2bytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static void bitmap2File(Bitmap bitmap, String filename) {
        try {
            File f = new File(filename);
            FileOutputStream fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromRes(Context context, int resid) {
        Drawable src = context.getApplicationContext().getResources().getDrawable(resid);
        if (src instanceof BitmapDrawable) {
            return ((BitmapDrawable) src).getBitmap();
        } else {
            throw new UnsupportedException("Unsupported");
        }
    }

    public static Bitmap loadImageFromUrl(String url) throws IOException {
        InputStream inputStream = (InputStream) new URL(url).getContent();
        BitmapFactory.Options bpo = new BitmapFactory.Options();
        return BitmapFactory.decodeStream(inputStream, null, bpo);
    }

    static class UnsupportedException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public UnsupportedException(String str) {
            super(str);
        }
    }
}
