package com.mydeepsky.android.ui.widget;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomerViewPager extends ViewPager {

    private boolean mScrollable;

    private Context context;

    public CustomerViewPager(Context context) {
        super(context);
        this.context = context;
        this.mScrollable = true;
    }

    public CustomerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.mScrollable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mScrollable == false) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScrollable == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }

    public boolean isScrollable() {
        return mScrollable;
    }

    public void setScrollable(boolean isScrollable) {
        this.mScrollable = isScrollable;
    }

    public void resetSwitchDuration() {
        setSwitchDuration(0);
    }

    public void setSwitchDuration(int duration) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            SpeedScroller scroller = new SpeedScroller(context);
            scroller.setDuration(duration);
            mScroller.set(this, scroller);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
