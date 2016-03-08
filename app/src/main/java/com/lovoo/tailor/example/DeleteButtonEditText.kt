package com.lovoo.tailor.example

import android.R
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import com.lovoo.tailor.ButtonEditText
import com.lovoo.tailor.CompoundDrawableOnTouchListener

/**
 * EditText with a delete button on the right which is only shown when the view is focused.
 *
 * Serves as an example for how to use a [ButtonEditText].
 */
class DeleteButtonEditText : ButtonEditText {
    private var deleteButtonPadding = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        deleteButtonPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, context.resources.displayMetrics).toInt()

        onCompoundDrawableClickListener = object : OnCompoundDrawableClickListener {
            override fun onCompoundDrawableClicked(direction: CompoundDrawableOnTouchListener.Direction) {
                if (direction === CompoundDrawableOnTouchListener.Direction.DIRECTION_RIGHT) {
                    setText("")
                }
            }
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            compoundDrawablePadding = deleteButtonPadding
            val deleteDrawable = getDrawable(context, R.drawable.ic_input_delete)
            deleteDrawable?.setColorFilter(DELETE_BUTTON_COLOR, PorterDuff.Mode.SRC_IN)
            setCompoundDrawablesWithIntrinsicBounds(null, null, deleteDrawable, null)
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
    }

    companion object {

        val DELETE_BUTTON_COLOR = Color.parseColor("#3A3838")

        private fun getDrawable(context: Context, drawableId: Int): Drawable? {
            if (Build.VERSION.SDK_INT > 21) {
                return context.getDrawable(drawableId)
            } else {
                @Suppress("DEPRECATION")
                return context.resources.getDrawable(drawableId)
            }
        }
    }
}
