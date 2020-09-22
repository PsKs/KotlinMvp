package com.hazz.kotlinmvp.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.hazz.kotlinmvp.R

/**
 * Created by xuhao on 2017/12/4.
 * desc: EditText with delete button
 */
class ClearEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                              defStyle: Int = android.R.attr.editTextStyle) : EditText(context, attrs, defStyle), View.OnFocusChangeListener, TextWatcher {
    // Delete button on the right side of EditText
    private var mClearDrawable: Drawable? = null
    private var hasFocus: Boolean = false

    init {
        init()
    }

    @Suppress("DEPRECATION")
    private fun init() {
        // Get EditText's DrawableRight. If it is not set, we will use the default picture.
        // The order of getting pictures is upper left and lower right (0,1,2,3,)
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            mClearDrawable = resources.getDrawable(
                    R.mipmap.ic_action_clear)
        }

        mClearDrawable!!.setBounds(0, 0, mClearDrawable!!.intrinsicWidth,
                mClearDrawable!!.intrinsicHeight)
        // Hide icon by default
        setClearIconVisible(false)
        // Set up monitoring for focus changes
        onFocusChangeListener = this
        // Set up monitoring for changes in the content of the input box
        addTextChangedListener(this)
    }

    /*
     * @Description：isInnerWidth, isInnerHeight为true，If the touch point is within the delete icon, it is deemed to have clicked the delete icon
     * event.getX() Get the X coordinate corresponding to the upper left corner of itself
     * event.getY() Get the Y coordinate corresponding to the upper left corner of itself
     * getWidth() Get the width of the control
     * getHeight() Get the height of the control
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth:
     * getWidth() - getTotalPaddingRight() Get the distance from the left edge of the delete icon to the right edge of the control
     * getWidth() - getPaddingRight() Calculate the distance from the right edge of the delete icon to the left edge of the control
     * isInnerHeight:
     * distance - Delete the distance from the top edge of the icon to the top edge of the control
     * distance + height - Delete the distance from the bottom edge of the icon to the top edge of the control
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                val x = event.x.toInt()
                val y = event.y.toInt()
                val rect = compoundDrawables[2].bounds
                val height = rect.height()
                val distance = (getHeight() - height) / 2
                val isInnerWidth = x > width - totalPaddingRight && x < width - paddingRight
                val isInnerHeight = y > distance && y < distance + height
                if (isInnerWidth && isInnerHeight) {
                    this.setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * When the focus of ClearEditText changes,
     * Enter a length of zero, hide the delete icon, otherwise, display the delete icon
     */
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        if (hasFocus) {
            setClearIconVisible(text.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }
    }

    private fun setClearIconVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(compoundDrawables[0],
                compoundDrawables[1], right, compoundDrawables[3])
    }

    override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        if (hasFocus) {
            setClearIconVisible(s.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                   after: Int) {

    }

    override fun afterTextChanged(s: Editable) {

    }
}