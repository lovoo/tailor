package com.lovoo.tailor

import android.annotation.TargetApi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.EditText

/**
 * EditText which can recognize clicks on it's compound drawables.
 */
open class ButtonEditText : EditText {

    /**
     * Listener which is triggered as soon as the compound drawable has been clicked.
     */
    var onCompoundDrawableClickListener: OnCompoundDrawableClickListener? = null

    private var compoundDrawableOnTouchListener: CompoundDrawableOnTouchListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun setCompoundDrawables(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        super.setCompoundDrawables(left, top, right, bottom)
        isFocusableInTouchMode = true
    }

    override fun setCompoundDrawablesRelative(start: Drawable?, top: Drawable?, end: Drawable?, bottom: Drawable?) {
        super.setCompoundDrawablesRelative(start, top, end, bottom)
        ensureFocusability()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (compoundDrawableOnTouchListener == null && compoundDrawables.size > 0) {
            compoundDrawableOnTouchListener = object : CompoundDrawableOnTouchListener() {

                private var mGestureDetector: GestureDetector? = null

                override fun onDrawableTouch(event: MotionEvent, direction: CompoundDrawableOnTouchListener.Direction): Boolean {
                    if (mGestureDetector == null) {
                        mGestureDetector = GestureDetector(context, SingleTapConfirm(object : com.lovoo.tailor.OnClickListener {
                            override fun onClick() {
                                onCompoundDrawableClickListener?.onCompoundDrawableClicked(direction)
                            }
                        }))
                    }

                    mGestureDetector!!.onTouchEvent(event)

                    return true
                }
            }
        }

        return !(compoundDrawableOnTouchListener == null || !compoundDrawableOnTouchListener!!.onTouch(this, event)) || super.onTouchEvent(event)
    }

    private fun ensureFocusability() {
        if (!isFocusableInTouchMode) {
            isFocusableInTouchMode = true
        }
    }

    /**
     * Click listener to be triggered when a compound drawable has been clicked.
     */
    interface OnCompoundDrawableClickListener {

        /**
         * This method gets called when a click is detected on one of the compound drawables.
         *
         * @param direction indicates which drawable has been clicked, one of
         *                  [CompoundDrawableOnTouchListener.Direction.DIRECTION_LEFT],
         *                  [CompoundDrawableOnTouchListener.Direction.DIRECTION_TOP],
         *                  [CompoundDrawableOnTouchListener.Direction.DIRECTION_RIGHT],
         *                  [CompoundDrawableOnTouchListener.Direction.DIRECTION_BOTTOM].
         */
        fun onCompoundDrawableClicked(direction: CompoundDrawableOnTouchListener.Direction)
    }
}
