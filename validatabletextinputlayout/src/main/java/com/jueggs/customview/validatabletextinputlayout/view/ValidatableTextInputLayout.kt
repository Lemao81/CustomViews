package com.jueggs.customview.validatabletextinputlayout.view

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet

class ValidatableTextInputLayout : TextInputLayout {
    //region constructor
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }
    //endregion

    private fun init(context: Context, attrs: AttributeSet?) {
        TODO("not implemented")
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        return super.onCreateDrawableState(extraSpace)
    }
}