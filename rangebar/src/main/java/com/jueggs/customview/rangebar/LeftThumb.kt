package com.jueggs.customview.rangebar

import android.annotation.SuppressLint
import android.content.Context
import com.jueggs.customview.rangebar.attribute.*
import com.jueggs.customview.util.rangeCrop

@SuppressLint("ViewConstructor")
class LeftThumb(context: Context, thumbAttrs: ThumbAttributes) : Thumb(context, thumbAttrs, startValue) {
    var positionRightThumb: Int = 0

    override fun cropMove(unboundPosition: Int): Int = rangeCrop(0, positionRightThumb, unboundPosition)
}