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


/* Nutation is a period oscillation of the Earths rotational axis around it's mean position.*/

/*
 Contains Nutation in longitude, obliquity and ecliptic obliquity.
 Angles are expressed in degrees.
 */
public class Nutation {
    private double longitude; /* !< Nutation in longitude */
    private double obliquity; /* !< Nutation in obliquity */
    private double ecliptic; /* !< Obliquity of the ecliptic */

    /* arguments and coefficients taken from table 21A on page 133 */
    private static final double nutation_arguments[][] = { { 0.0, 0.0, 0.0, 0.0, 1.0 },
            { -2.0, 0.0, 0.0, 2.0, 2.0 }, { 0.0, 0.0, 0.0, 2.0, 2.0 }, { 0.0, 0.0, 0.0, 0.0, 2.0 },
            { 0.0, 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0, 0.0 }, { -2.0, 1.0, 0.0, 2.0, 2.0 },
            { 0.0, 0.0, 0.0, 2.0, 1.0 }, { 0.0, 0.0, 1.0, 2.0, 2.0 },
            { -2.0, -1.0, 0.0, 2.0, 2.0 }, { -2.0, 0.0, 1.0, 0.0, 0.0 },
            { -2.0, 0.0, 0.0, 2.0, 1.0 }, { 0.0, 0.0, -1.0, 2.0, 2.0 },
            { 2.0, 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0, 1.0 }, { 2.0, 0.0, -1.0, 2.0, 2.0 },
            { 0.0, 0.0, -1.0, 0.0, 1.0 }, { 0.0, 0.0, 1.0, 2.0, 1.0 },
            { -2.0, 0.0, 2.0, 0.0, 0.0 }, { 0.0, 0.0, -2.0, 2.0, 1.0 },
            { 2.0, 0.0, 0.0, 2.0, 2.0 }, { 0.0, 0.0, 2.0, 2.0, 2.0 }, { 0.0, 0.0, 2.0, 0.0, 0.0 },
            { -2.0, 0.0, 1.0, 2.0, 2.0 }, { 0.0, 0.0, 0.0, 2.0, 0.0 },
            { -2.0, 0.0, 0.0, 2.0, 0.0 }, { 0.0, 0.0, -1.0, 2.0, 1.0 },
            { 0.0, 2.0, 0.0, 0.0, 0.0 }, { 2.0, 0.0, -1.0, 0.0, 1.0 },
            { -2.0, 2.0, 0.0, 2.0, 2.0 }, { 0.0, 1.0, 0.0, 0.0, 1.0 },
            { -2.0, 0.0, 1.0, 0.0, 1.0 }, { 0.0, -1.0, 0.0, 0.0, 1.0 },
            { 0.0, 0.0, 2.0, -2.0, 0.0 }, { 2.0, 0.0, -1.0, 2.0, 1.0 },
            { 2.0, 0.0, 1.0, 2.0, 2.0 }, { 0.0, 1.0, 0.0, 2.0, 2.0 }, { -2.0, 1.0, 1.0, 0.0, 0.0 },
            { 0.0, -1.0, 0.0, 2.0, 2.0 }, { 2.0, 0.0, 0.0, 2.0, 1.0 }, { 2.0, 0.0, 1.0, 0.0, 0.0 },
            { -2.0, 0.0, 2.0, 2.0, 2.0 }, { -2.0, 0.0, 1.0, 2.0, 1.0 },
            { 2.0, 0.0, -2.0, 0.0, 1.0 }, { 2.0, 0.0, 0.0, 0.0, 1.0 },
            { 0.0, -1.0, 1.0, 0.0, 0.0 }, { -2.0, -1.0, 0.0, 2.0, 1.0 },
            { -2.0, 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 2.0, 2.0, 1.0 },
            { -2.0, 0.0, 2.0, 0.0, 1.0 }, { -2.0, 1.0, 0.0, 2.0, 1.0 },
            { 0.0, 0.0, 1.0, -2.0, 0.0 }, { -1.0, 0.0, 1.0, 0.0, 0.0 },
            { -2.0, 1.0, 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 2.0, 0.0 },
            { 0.0, 0.0, -2.0, 2.0, 2.0 }, { -1.0, -1.0, 1.0, 0.0, 0.0 },
            { 0.0, 1.0, 1.0, 0.0, 0.0 }, { 0.0, -1.0, 1.0, 2.0, 2.0 },
            { 2.0, -1.0, -1.0, 2.0, 2.0 }, { 0.0, 0.0, 3.0, 2.0, 2.0 },
            { 2.0, -1.0, 0.0, 2.0, 2.0 } };

    private static final double nutation_coefficients[][] = { { -171996.0, -174.2, 92025.0, 8.9 },
            { -13187.0, -1.6, 5736.0, -3.1 }, { -2274.0, 0.2, 977.0, -0.5 },
            { 2062.0, 0.2, -895.0, 0.5 }, { 1426.0, -3.4, 54.0, -0.1 }, { 712.0, 0.1, -7.0, 0.0 },
            { -517.0, 1.2, 224.0, -0.6 }, { -386.0, -0.4, 200.0, 0.0 },
            { -301.0, 0.0, 129.0, -0.1 }, { 217.0, -0.5, -95.0, 0.3 }, { -158.0, 0.0, 0.0, 0.0 },
            { 129.0, 0.1, -70.0, 0.0 }, { 123.0, 0.0, -53.0, 0.0 }, { 63.0, 0.0, 0.0, 0.0 },
            { 63.0, 1.0, -33.0, 0.0 }, { -59.0, 0.0, 26.0, 0.0 }, { -58.0, -0.1, 32.0, 0.0 },
            { -51.0, 0.0, 27.0, 0.0 }, { 48.0, 0.0, 0.0, 0.0 }, { 46.0, 0.0, -24.0, 0.0 },
            { -38.0, 0.0, 16.0, 0.0 }, { -31.0, 0.0, 13.0, 0.0 }, { 29.0, 0.0, 0.0, 0.0 },
            { 29.0, 0.0, -12.0, 0.0 }, { 26.0, 0.0, 0.0, 0.0 }, { -22.0, 0.0, 0.0, 0.0 },
            { 21.0, 0.0, -10.0, 0.0 }, { 17.0, -0.1, 0.0, 0.0 }, { 16.0, 0.0, -8.0, 0.0 },
            { -16.0, 0.1, 7.0, 0.0 }, { -15.0, 0.0, 9.0, 0.0 }, { -13.0, 0.0, 7.0, 0.0 },
            { -12.0, 0.0, 6.0, 0.0 }, { 11.0, 0.0, 0.0, 0.0 }, { -10.0, 0.0, 5.0, 0.0 },
            { -8.0, 0.0, 3.0, 0.0 }, { 7.0, 0.0, -3.0, 0.0 }, { -7.0, 0.0, 0.0, 0.0 },
            { -7.0, 0.0, 3.0, 0.0 }, { -7.0, 0.0, 3.0, 0.0 }, { 6.0, 0.0, 0.0, 0.0 },
            { 6.0, 0.0, -3.0, 0.0 }, { 6.0, 0.0, -3.0, 0.0 }, { -6.0, 0.0, 3.0, 0.0 },
            { -6.0, 0.0, 3.0, 0.0 }, { 5.0, 0.0, 0.0, 0.0 }, { -5.0, 0.0, 3.0, 0.0 },
            { -5.0, 0.0, 3.0, 0.0 }, { -5.0, 0.0, 3.0, 0.0 }, { 4.0, 0.0, 0.0, 0.0 },
            { 4.0, 0.0, 0.0, 0.0 }, { 4.0, 0.0, 0.0, 0.0 }, { -4.0, 0.0, 0.0, 0.0 },
            { -4.0, 0.0, 0.0, 0.0 }, { -4.0, 0.0, 0.0, 0.0 }, { 3.0, 0.0, 0.0, 0.0 },
            { -3.0, 0.0, 0.0, 0.0 }, { -3.0, 0.0, 0.0, 0.0 }, { -3.0, 0.0, 0.0, 0.0 },
            { -3.0, 0.0, 0.0, 0.0 }, { -3.0, 0.0, 0.0, 0.0 }, { -3.0, 0.0, 0.0, 0.0 },
            { -3.0, 0.0, 0.0, 0.0 } };

    public Nutation(double JD) {
        double c_longitude = 0.0, c_obliquity = 0.0, c_ecliptic = 0.0;
        double coeff_sine, coeff_cos;

        double T = TimeMath.julian_centries_since_J2000(JD);

        /* GZotti: Laskar's formula, only valid for J2000+/-10000 years! */
        if (Math.abs(T) < 100.0) {
            double U = T / 100.0;
            c_ecliptic = ((((((((((2.45 * U + 5.79) * U + 27.87) * U + 7.12) * U - 39.05) * U - 249.67)
                    * U - 51.38)
                    * U + 1999.25)
                    * U - 1.55) * U) - 4680.93)
                    * U + 21.448;
        } else /*
                * Use IAU formula. This might be more stable in faraway times,
                * but is less accurate in any case for |U|<1.
                */
        {
            c_ecliptic = ((0.001813 * T - 0.00059) * T - 46.8150) * T + 21.448;
        }

        c_ecliptic /= 60.0;
        c_ecliptic += 26.0;
        c_ecliptic /= 60.0;
        c_ecliptic += 23.0;

        /* GZotti: I prefer Horner's Scheme and its better numerical accuracy. */
        double D = TriMath
                .deg2rad(((T / 189474.0 - 0.0019142) * T + 445267.111480) * T + 297.85036);
        double M = TriMath.deg2rad(((T / 300000.0 - 0.0001603) * T + 35999.050340) * T + 357.52772);
        double MM = TriMath
                .deg2rad(((T / 56250.0 + 0.0086972) * T + 477198.867398) * T + 134.96298);
        double F = TriMath.deg2rad(((T / 327270.0 - 0.0036825) * T + 483202.017538) * T + 93.27191);
        double O = TriMath.deg2rad(((T / 450000.0 + 0.0020708) * T - 1934.136261) * T + 125.04452);

        double[] C = { D, M, MM, F, O };

        /* calc sum of terms in table 21A */
        for (int i = 0; i < nutation_arguments.length; i++) {
            /* calc coefficients of sine and cosine */
            coeff_sine = nutation_coefficients[i][0] + nutation_coefficients[i][1] * T;
            coeff_cos = nutation_coefficients[i][2] + nutation_coefficients[i][3] * T;

            for (int j = 0; j < C.length; j++) {
                if (nutation_arguments[i][j] != 0) {
                    c_longitude += coeff_sine * (Math.sin(nutation_arguments[i][j] * C[j]));
                    c_obliquity += coeff_cos * (Math.cos(nutation_arguments[i][j] * C[j]));
                }
            }
        }

        /* change to degrees */
        c_longitude /= 36000000.0;
        c_obliquity /= 36000000.0;

        /* return results */
        longitude = c_longitude;
        obliquity = c_obliquity;
        ecliptic = c_ecliptic;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getObliquity() {
        return obliquity;
    }

    public double getMeanEcliptic() {
        return ecliptic;
    }

    public double getApparentEcliptic() {
        return ecliptic + obliquity;
    }
}
