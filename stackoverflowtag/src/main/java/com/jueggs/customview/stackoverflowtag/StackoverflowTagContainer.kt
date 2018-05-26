package com.jueggs.customview.stackoverflowtag

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes

class StackoverflowTagContainer(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    var tagNames: List<String> = emptyList()
    private lateinit var attributes: Attributes

    init {
        obtainAttributes(context, attrs)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        context.withStyledAttributes(attrs, R.styleable.StackoverflowTag) {
            attributes = Attributes(context, this)
        }
    }
}