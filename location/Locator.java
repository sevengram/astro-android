package com.mydeepsky.android.location;

import android.content.Context;

public abstract class Locator {
    public final static int LOCATION_SETTINGS_REQUEST_CODE = 1003;
    
    public final static int ERR_NO_NETWORK = 2001;

    public final static int ERR_NO_LOCATION_SERVICE = 2002;

    public enum Provider {
        GPS, WIFI, MOBILE, NONE
    }

    protected int gpsScanSpan = 20000;

    protected int networkScanSpan = 5000;

    protected OnLocationUpdateListener listener;

    protected Provider provider = Provider.NONE;

    public abstract boolean isAvailable();

    public abstract void setProvider();

    public abstract void start(Context context);

    public abstract void stop();

    public void setOnLocationUpdateListener(OnLocationUpdateListener listener) {
        this.listener = listener;
    }

    public interface OnLocationUpdateListener {
        void onWithoutService(int error);

        void onLocationUpdate(LocationInfo location, long costTime, int locator);

        void onLocationError(boolean showMessage);
    }

    public class LocationInfo {
        public final static int VALID = 90;

        public final static int INVALID = 91;

        private double longitude;

        private double latitude;

        private String country;

        private String province;

        private String city;

        private String district;

        private String detail;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        @Override
        public String toString() {
            String result = province + ',' + city + ',' + district;
            return result.equals(",,") ? "Unknown" : result;
        }

    }
}
