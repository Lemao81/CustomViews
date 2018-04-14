package com.jueggs.customview.rangebar.view

import android.annotation.SuppressLint
import android.content.Context
import com.jueggs.customview.rangebar.attribute.*

@SuppressLint("ViewConstructor")
class LeftThumb(context: Context, thumbAttrs: ThumbAttributes) : Thumb(context, thumbAttrs) {
    var positionRightThumb: Int = 0

    override fun bottomLimit() = 0
    override fun topLimit() = positionRightThumb
}