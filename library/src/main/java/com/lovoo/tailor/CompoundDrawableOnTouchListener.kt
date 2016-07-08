package com.lovoo.tailor

import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

/**
 * A touch listener which filters touches on the compound drawables of [TextView]s.
 */
abstract class CompoundDrawableOnTouchListener : View.OnTouchListener {

    enum class Direction {
        DIRECTION_LEFT, DIRECTION_TOP, DIRECTION_RIGHT, DIRECTION_BOTTOM, NO_DIRECTION
    }

    private var lastTouchedCompoundDrawableDirection = Direction.NO_DIRECTION

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (v !is TextView) {
            throw IllegalArgumentException("This click listener may only be applied on Views which support compound drawables, i.e. TextViews.")
        }

        val drawables = v.compoundDrawables
        if (event.action == MotionEvent.ACTION_DOWN && drawables.size == 4) {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()

            lastTouchedCompoundDrawableDirection = Direction.NO_DIRECTION

            // cache the root view so that we can retrieve it faster during the upcoming checks
            var rootView = v.rootView

            if (checkLeftTouch(v, drawables[Direction.DIRECTION_LEFT.ordinal], rootView, x)) {
                lastTouchedCompoundDrawableDirection = Direction.DIRECTION_LEFT
                return onDrawableTouch(event, Direction.DIRECTION_LEFT)
            }

            if (checkTopTouch(v, drawables[Direction.DIRECTION_TOP.ordinal], rootView, y)) {
                lastTouchedCompoundDrawableDirection = Direction.DIRECTION_TOP
                return onDrawableTouch(event, Direction.DIRECTION_TOP)
            }

            if (checkRightTouch(v, drawables[Direction.DIRECTION_RIGHT.ordinal], rootView, x)) {
                lastTouchedCompoundDrawableDirection = Direction.DIRECTION_RIGHT
                return onDrawableTouch(event, Direction.DIRECTION_RIGHT)
            }

            if (checkBottomTouch(v, drawables[Direction.DIRECTION_BOTTOM.ordinal], rootView, y)) {
                lastTouchedCompoundDrawableDirection = Direction.DIRECTION_BOTTOM
                return onDrawableTouch(event, Direction.DIRECTION_BOTTOM)
            }

        } else if (event.action == MotionEvent.ACTION_UP && lastTouchedCompoundDrawableDirection != Direction.NO_DIRECTION) {
            onDrawableTouch(event, lastTouchedCompoundDrawableDirection)
            lastTouchedCompoundDrawableDirection = Direction.NO_DIRECTION
            return true
        } else if (lastTouchedCompoundDrawableDirection != Direction.NO_DIRECTION) {
            return onDrawableTouch(event, lastTouchedCompoundDrawableDirection)
        }
        return false
    }

    /**
     * This method gets called when a touch is detected on one of the compound drawables.
     *
     * @param event the detected touch event
     * @param direction indicates which drawable has been clicked, one of
     *                   [Direction.DIRECTION_LEFT],
     *                   [Direction.DIRECTION_TOP],
     *                   [Direction.DIRECTION_RIGHT],
     *                   [Direction.DIRECTION_BOTTOM].
     * @return whether the event has been consumed
     */
    abstract fun onDrawableTouch(event: MotionEvent, direction: Direction): Boolean

    private fun checkLeftTouch(textView: View, leftDrawable: Drawable?, rootView: View, rawXClickPosition: Int): Boolean {
        return leftDrawable != null
                && rawXClickPosition <= textView.left - rootView.paddingLeft + textView.paddingLeft + leftDrawable.bounds.width()
                && rawXClickPosition >= textView.left - rootView.paddingLeft
    }

    private fun checkTopTouch(textView: View, topDrawable: Drawable?, rootView: View, rawYClickPosition: Int): Boolean {
        return topDrawable != null
                && rawYClickPosition <= textView.top - rootView.paddingTop + textView.paddingTop + topDrawable.bounds.height()
                && rawYClickPosition >= textView.top - rootView.paddingTop
    }

    private fun checkRightTouch(textView: View, rightDrawable: Drawable?, rootView: View, rawXClickPosition: Int): Boolean {
        return rightDrawable != null
                && rawXClickPosition >= textView.right + rootView.paddingRight - textView.paddingRight - rightDrawable.bounds.width()
                && rawXClickPosition <= textView.right + rootView.paddingRight
    }

    private fun checkBottomTouch(textView: View, bottomDrawable: Drawable?, rootView: View, rawYClickPosition: Int): Boolean {
        return bottomDrawable != null
                && rawYClickPosition >= textView.bottom + rootView.paddingBottom - textView.paddingBottom - bottomDrawable.bounds.height()
                && rawYClickPosition <= textView.bottom + rootView.paddingBottom
    }

}
