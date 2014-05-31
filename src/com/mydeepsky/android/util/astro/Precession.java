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


public class Precession {

    public static Position fix(double raRad, double decRad, double defaultEclipObliq,
            double eclipObliq, double defaultJD, double JD) {

        double elong = equa2elong(raRad, decRad, defaultEclipObliq);
        double elat = equa2elat(raRad, decRad, defaultEclipObliq);

        double A = TimeMath.julian_centries_since_J2000(defaultJD);
        double B = TimeMath.julian_centries_since_J2000(JD);

        double p = (B - A) * (5028.796195 + 1.1054348 * (B + A));

        elong += TriMath.deg2rad(p / 3600.0);

        raRad = eclip2raRad(elong, elat, eclipObliq);
        decRad = eclip2decRad(elong, elat, eclipObliq);

        return new Position(TriMath.rad2hr(raRad), TriMath.rad2deg(decRad));
    }

    private static double equa2elong(double ra, double dec, double eclipObliq) {
        double sin_elat = Math.sin(dec) * Math.cos(eclipObliq) - Math.cos(dec)
                * Math.sin(eclipObliq) * Math.sin(ra);
        double cos_elat = Math.sqrt(1 - sin_elat * sin_elat);

        double sin_elong = (Math.cos(dec) * Math.sin(ra) * Math.cos(eclipObliq) + Math.sin(dec)
                * Math.sin(eclipObliq))
                / cos_elat;
        double cos_elong = (Math.cos(ra) * Math.cos(dec)) / cos_elat;

        return TriMath.asincos(sin_elong, cos_elong);
    }

    private static double equa2elat(double ra, double dec, double eclipObliq) {
        double sin_elat = Math.sin(dec) * Math.cos(eclipObliq) - Math.cos(dec)
                * Math.sin(eclipObliq) * Math.sin(ra);
        return Math.asin(sin_elat);
    }

    private static double eclip2raRad(double elong, double elat, double eclipObliq) {
        double sin_dec = Math.sin(elat) * Math.cos(eclipObliq) + Math.cos(elat)
                * Math.sin(eclipObliq) * Math.sin(elong);
        double cos_dec = Math.sqrt(1 - sin_dec * sin_dec);

        double sin_ra = (Math.sin(elong) * Math.cos(eclipObliq) * Math.cos(elat) - Math.sin(elat)
                * Math.sin(eclipObliq))
                / cos_dec;
        double cos_ra = (Math.cos(elong) * Math.cos(elat)) / cos_dec;
        return TriMath.asincos(sin_ra, cos_ra);
    }

    private static double eclip2decRad(double elong, double elat, double eclipObliq) {
        double sin_dec = Math.sin(elat) * Math.cos(eclipObliq) + Math.cos(elat)
                * Math.sin(eclipObliq) * Math.sin(elong);
        return Math.asin(sin_dec);
    }
}
