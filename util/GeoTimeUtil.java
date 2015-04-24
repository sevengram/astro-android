package com.mydeepsky.android.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GeoTimeUtil {
    private static final String CHINA_STANDARD_TIME = "China Standard Time";

    public enum GeoZone {
        WORLD, CHINA, EUROPE, NAMERICA, OCEANIA, SAFRICA, SAMERICA;

        @Override
        public String toString() {
            switch (this) {
            case CHINA:
                return "china";
            case EUROPE:
                return "europe";
            case NAMERICA:
                return "namerica";
            case OCEANIA:
                return "oceania";
            case SAFRICA:
                return "safrica";
            case SAMERICA:
                return "samerica";
            default:
                return "world";
            }
        }
    };

    public static GeoZone getZone(double lon, double lat) {
        if (lon < 150 && lon > 70 && lat < 55 && lat > 10) {
            return GeoZone.CHINA;
        } else if (lon < 55 && lon > -15 && lat < 70 && lat > 15) {
            return GeoZone.EUROPE;
        } else if (lon < -60 && lon > -140 && lat > 10 && lat < 60) {
            return GeoZone.NAMERICA;
        } else if (lon < 180 && lon > 100 && lat < -5 && lat > -50) {
            return GeoZone.OCEANIA;
        } else if (lon < 70 && lon > -30 && lat < 30 && lat > -40) {
            return GeoZone.SAFRICA;
        } else if (lon < -20 && lon > -100 && lat < 15 && lat > -55) {
            return GeoZone.SAMERICA;
        } else {
            return GeoZone.WORLD;
        }
    }

    public static String getTimeZoneName() {
        return TimeZone.getDefault().getDisplayName(Locale.US);
    }

    public static boolean isInChina() {
        return getTimeZoneName().equals(CHINA_STANDARD_TIME);
    }

    public static String getWeekday(Date date, TimeZone timeZone) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("E", Locale.getDefault());
        format.setTimeZone(timeZone);
        return format.format(date);
    }

    public static float cen2fah(float cen) {
        return cen * 9.0f / 5.0f + 32.0f;
    }

    public static float fah2cen(float fah) {
        return (fah - 32.0f) * 5.0f / 9.0f;
    }
}
