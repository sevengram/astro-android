package com.mydeepsky.android.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mydeepsky.android.location.Locator.OnLocationUpdateListener;
import com.mydeepsky.android.location.LocatorFactory.LocatorType;
import com.mydeepsky.android.util.GeoTimeUtil;

public abstract class LocatorActivity extends FragmentActivity implements OnLocationUpdateListener {
    static final String TAG = "_LocatorActivity";

    protected Locator locator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.locator = LocatorFactory.createLocator(this,
                    GeoTimeUtil.isInChina() ? LocatorType.BAIDU : LocatorType.GOOGLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Locator.LOCATION_SETTINGS_REQUEST_CODE) {
            if (locator.isAvailable()) {
                locator.start(this);
            } else {
                onLocationError(false);
            }
        }
    }

    @Override
    protected void onResume() {
        locator.setOnLocationUpdateListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        locator.setOnLocationUpdateListener(null);
        super.onPause();
    }

    protected void startLocator() {
        locator.stop();
        locator.start(this);
    }

    @Override
    protected void onStop() {
        locator.stop();
        super.onStop();
    }
}
