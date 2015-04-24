package com.mydeepsky.android.util.astro;

import java.util.Calendar;
import java.util.Date;

public class TimeMath {
    public static final int NEVER_VISIBLE = Integer.MIN_VALUE;
    public static final int ALWAYS_VISIBLE = Integer.MAX_VALUE;

    public static final int DAY_LENGTH_MIN = 1440;

    public static final int ONE_DAY_MS = DAY_LENGTH_MIN * 60 * 1000;

    public static final double UNIX_JD = 2440587.5;

    public static final double MJD_JD = 2400000.5;

    public static int computeSolarTime(int baseSolarTime, double deltaSiderealTime) {
        return baseSolarTime + (int) Math.round(sidereal2solar(deltaSiderealTime * 60.0));
    }

    public static double sidereal2solar(double siderealTime) {
        return siderealTime * 365.242190402 / 366.242190402;
    }

    public static double solar2sidereal(double solarTime) {
        return solarTime * 366.242190402 / 365.242190402;
    }

    public static boolean isInOrder(double r, double m, double s) {
        return (r < m) && (m < s);
    }

    public static boolean isInPeriod(double startTime, double endTime, double time) {
        return (startTime <= time) && (time <= endTime);
    }

    private static int formatTime(int time) {
        if (time == NEVER_VISIBLE)
            return time;
        if (time == ALWAYS_VISIBLE)
            return time;

        while (time < 0) {
            time += 1440;
        }
        while (time >= DAY_LENGTH_MIN) {
            time -= DAY_LENGTH_MIN;
        }
        return time;
    }

    public static int getTimeFromSring(String time) {
        if (time.equals("NEVER"))
            return NEVER_VISIBLE;
        if (time.equals("ALWAYS"))
            return ALWAYS_VISIBLE;

        String[] temp = time.split(":");
        return Integer.parseInt(temp[0]) * 60 + Integer.parseInt(temp[1]);
    }

    public static String hourMinFormat(int time) {
        if (time == NEVER_VISIBLE)
            return "NEVER";
        if (time == ALWAYS_VISIBLE)
            return "ALWAYS";

        time = formatTime(time);

        String result = String.format("%02d", time / 60) + ":" + String.format("%02d", time % 60);
        return result;
    }

    public static double get_julian_day(Date date) {
        return UNIX_JD + date.getTime() / 24.0 / 3600.0 / 1000.0;
    }

    public static double get_julian_day(int y, int m, int d, int min, double timezone) {
        min -= (int) (timezone * 60);

        if (y < 0)
            y++;

        if (m <= 2) {
            y = y - 1;
            m = m + 12;
        }

        // Correct for the lost days in Oct 1582 when the Gregorian calendar
        // replaced the Julian calendar.
        int B = -2;
        if (y > 1582 || (y == 1582 && (m > 10 || (m == 10 && d >= 15)))) {
            B = y / 400 - y / 100;
        }

        return (Math.floor(365.25 * y) + Math.floor(30.6001 * (m + 1)) + B + 1720996.5 + d + min / 1440.0);
    }

    /*
     * Calculate the mean sidereal time at the meridian of Greenwich of a given
     * date. returns apparent sidereal time (degree). Formula 11.1, 11.4 pg 83
     */
    private static double get_greenwich_mean_sidereal_time(double JD) {

        double T = julian_centries_since_J2000(JD);

        /* calc mean angle */
        double sidereal = 280.46061837 + (360.98564736629 * (JD - 2451545.0))
                + (0.000387933 * T * T) - (T * T * T / 38710000.0);

        /* add a convenient multiple of 360 degrees */
        sidereal = TriMath.range_degrees(sidereal);

        return sidereal;
    }

    public static double get_greenwich_apparent_sidereal_time(Nutation nutation, double JD) {
        /* get the mean sidereal time */
        double sidereal = get_greenwich_mean_sidereal_time(JD);

        /*
         * GZ: This was the only place where this was used. I added the
         * summation here.
         */
        double correction = nutation.getLongitude()
                * Math.cos(TriMath.deg2rad(nutation.getApparentEcliptic()));
        sidereal += correction;

        return TriMath.range_degrees(sidereal);
    }

    public static double julian_centries_since_J2000(double JD) {
        return (JD - 2451545.0) / 36525.0;
    }

    public static Date jd2date(double JD) {
        return new Date(((long) ((JD - UNIX_JD) * 24 * 3600)) * 1000);
    }

    public static Date mjd2date(double mjd) {
        return jd2date(mjd + MJD_JD);
    }

    public static double date2mjd(Date date) {
        return get_julian_day(date) - MJD_JD;
    }

    public static Date midDay(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    public static Date fixTimezone(Date localtime, double timezone, double lon) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(localtime);
        cal.add(Calendar.SECOND, (int) ((timezone * 15 - lon) * 4 * 60));
        return cal.getTime();
    }

    public static int countDay(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);
        int leap = 0;
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            leap = 1;
        } else {
            leap = 0;
        }
        int mm[] = { 0, 31, leap + 59, leap + 90, leap + 120, leap + 151, leap + 181, leap + 212,
                leap + 243, leap + 273, leap + 304, leap + 334 };
        return mm[month - 1] + date;
    }
}
