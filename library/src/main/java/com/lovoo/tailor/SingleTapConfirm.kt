package com.lovoo.tailor

import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Gesture detector which triggers for single taps.
 *
 * @param onClickListener listener triggered when a single tap is detected
 */
internal class SingleTapConfirm(private var onClickListener: OnClickListener?) : GestureDetector.SimpleOnGestureListener() {

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        if (onClickListener != null) {
            onClickListener!!.onClick()
        }
        return true
    }
}
