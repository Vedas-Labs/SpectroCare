package com.vedas.spectrocare;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SingleTapDetector extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }
}
