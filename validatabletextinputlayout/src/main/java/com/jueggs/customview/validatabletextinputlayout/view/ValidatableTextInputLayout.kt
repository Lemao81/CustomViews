package com.jueggs.customview.validatabletextinputlayout.view

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet

class ValidatableTextInputLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextInputLayout(context, attrs, defStyleAttr) {
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        return super.onCreateDrawableState(extraSpace)
    }
}