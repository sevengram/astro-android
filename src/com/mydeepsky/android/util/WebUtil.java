/*
 * Deep Sky Assistant for Android
 * Author 2012 Jianxiang FAN <sevengram1991@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 */

package com.mydeepsky.android.util;


public class WebUtil {
    public static final String WIKI_URL = "http://en.m.wikipedia.org/wiki/";
    public static final String WIKI_UPLOAD_URL = "http://upload.wikimedia.org/";
    public static final String DATABASE_UPDATE_URL = "http://sourceforge.net/projects/deepsky/files/org.deepsky.database.zip/download";
    public static final String DATABASE_UPDATE_NAME = "org.deepsky.database.zip";
    public static final String LOCAL_URI_PREFIX = "file://";

    public static boolean isLocalUri(String uri) {
        return uri.startsWith(LOCAL_URI_PREFIX) || uri.startsWith("/");
    }

    public static boolean isWikiUrl(String url) {
        return url.startsWith(WIKI_URL) || url.startsWith(WIKI_UPLOAD_URL);
    }

    public static boolean isUpdate(String url) {
        return url.equals(DATABASE_UPDATE_URL);
    }

}
