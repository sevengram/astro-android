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

package com.mydeepsky.android.util.astro;

public class TriMath {
    public static double deg2rad(double x) {
        return ((double) (x) * (Math.PI)) / 180.0;
    }

    public static double rad2deg(double x) {
        return (180.0 * (double) (x)) / (Math.PI);
    }

    public static double hr2deg(double raHr) {
        return raHr * 15.0;
    }

    public static double deg2hr(double raDeg) {
        return raDeg / 15.0;
    }

    public static double hr2rad(double hr) {
        return deg2rad(hr2deg(hr));
    }

    public static double rad2hr(double rad) {
        return deg2hr(rad2deg(rad));
    }

    public static double range_degrees(double d) {
        d = d % 360.;
        if (d < 0.)
            d += 360.;
        return d;
    }

    public static double sinDeg(double d) {
        return Math.sin(deg2rad(d));
    }

    public static double cosDeg(double d) {
        return Math.cos(deg2rad(d));
    }

    public static double tanDeg(double d) {
        return Math.tan(deg2rad(d));
    }

    public static double asincos(double a, double b) // from 0 - 2pi
    {
        if (a >= 0 && b >= 0) {
            return Math.asin(a);
        } else if (a > 0 && b < 0) {
            return Math.acos(b);
        } else if (a <= 0 && b <= 0) {
            return Math.asin(-a) + Math.PI;
        } else { // (a < 0 && b > 0)
            return Math.asin(a) + 2 * Math.PI;
        }
    }
}
