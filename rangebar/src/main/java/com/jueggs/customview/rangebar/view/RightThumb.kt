package com.jueggs.customview.rangebar.view

import android.annotation.SuppressLint
import android.content.Context
import com.jueggs.customview.rangebar.RangeBar
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.rangebar.helper.ValueTransformer

@SuppressLint("ViewConstructor")
class RightThumb(context: Context, private val rangeBar: RangeBar, attrs: ThumbAttributes, valueTransformer: ValueTransformer) : Thumb(context, attrs, valueTransformer) {
    override fun bottomLimit() = rangeBar.leftThumb.position
    override fun topLimit() = rangeBar.rangeRightEdge
}