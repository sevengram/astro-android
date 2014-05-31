package com.mydeepsky.android.util.astro;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class SunCalc {
    private Date twilightStartTime;
    private Date twilightEndTime;
    private Date setTime;
    private Date riseTime;

    private double longitude;
    private double latitude;

    public SunCalc(double lon, double lat) {
        super();
        this.longitude = lon;
        this.latitude = lat;
    }

    public void update(Date time) {
        double timezone = TimeZone.getDefault().getRawOffset() / 3600.0 / 1000.0;
        int riseSetLength = getDayLength(time, 0.0);
        int twilightLength = getDayLength(time, -18.0);

        Date midDay = TimeMath.midDay(time);
        Calendar cal = Calendar.getInstance();
        if (riseSetLength == TimeMath.DAY_LENGTH_MIN || riseSetLength == 0) {
            cal.clear();
            this.setTime = this.riseTime = cal.getTime();
        } else {
            cal.setTime(midDay);
            cal.add(Calendar.MINUTE, -riseSetLength / 2);
            Date localRiseTime = cal.getTime();
            cal.add(Calendar.MINUTE, riseSetLength);
            Date localSetTime = cal.getTime();
            this.riseTime = TimeMath.fixTimezone(localRiseTime, timezone, this.longitude);
            this.setTime = TimeMath.fixTimezone(localSetTime, timezone, this.longitude);
        }

        if (twilightLength == TimeMath.DAY_LENGTH_MIN || twilightLength == 0) {
            cal.clear();
            this.twilightStartTime = this.twilightEndTime = cal.getTime();
        } else {
            cal.setTime(midDay);
            cal.add(Calendar.MINUTE, -twilightLength / 2);
            Date localtwilightStartTime = cal.getTime();
            cal.add(Calendar.MINUTE, twilightLength);
            Date localtwilightEndTime = cal.getTime();
            this.twilightStartTime = TimeMath.fixTimezone(localtwilightStartTime, timezone,
                    this.longitude);
            this.twilightEndTime = TimeMath.fixTimezone(localtwilightEndTime, timezone,
                    this.longitude);
        }
    }

    private int getDayLength(Date time, double alt) {
        int N = TimeMath.countDay(time);
        double sunDec = TriMath.rad2deg(Math.asin(TriMath.sinDeg(-23.44)
                * TriMath.cosDeg(360 / 365.24 * (N + 10) + 360 / Math.PI * 0.0167
                        * TriMath.sinDeg(360 / 365.24 * (N - 2)))));
        // double sunDec = -23.43759189 * Math.cos(Math.PI / 182.625 *
        // TimeMath.countDay(time));
        double cosr = TriMath.tanDeg(this.latitude) * TriMath.tanDeg(sunDec);
        double angleFix = TriMath.sinDeg(alt)
                / (TriMath.cosDeg(this.latitude) * TriMath.cosDeg(sunDec));
        cosr -= angleFix;
        if (cosr >= 1.0) {
            return TimeMath.DAY_LENGTH_MIN;
        } else if (cosr <= -1.0) {
            return 0;
        } else {
            double rRad = Math.acos(cosr);
            return (int) ((1 - rRad / Math.PI) * TimeMath.DAY_LENGTH_MIN);
        }
    }

    public Date getTwilightStartTime() {
        return twilightStartTime;
    }

    public Date getTwilightEndTime() {
        return twilightEndTime;
    }

    public Date getSetTime() {
        return setTime;
    }

    public Date getRiseTime() {
        return riseTime;
    }
}
