package com.jueggs.customview.rangebar.view

import android.annotation.SuppressLint
import android.content.Context
import com.jueggs.customview.rangebar.RangeBar
import com.jueggs.customview.rangebar.attribute.*
import com.jueggs.customview.rangebar.helper.ValueTransformer

@SuppressLint("ViewConstructor")
class LeftThumb(context: Context, private val rangeBar: RangeBar, thumbAttrs: ThumbAttributes, valueTransformer: ValueTransformer) : Thumb(context, thumbAttrs, valueTransformer) {
    override fun bottomLimit() = rangeBar.rangeLeftEdge
    override fun topLimit() = rangeBar.rightThumb.position
}