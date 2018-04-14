package com.jueggs.customview.rangebar

import android.annotation.SuppressLint
import android.content.Context
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.util.rangeCrop

@SuppressLint("ViewConstructor")
class RightThumb(context: Context, attrs: ThumbAttributes) : Thumb(context, attrs.diameter, attrs.color, startValue) {
    var positionLeftThumb: Int = 0
    var positionRightEdge: Int = 0
        set(value) {
            move(value)
        }

    override fun cropMove(unboundPosition: Int): Int = rangeCrop(positionLeftThumb, positionRightEdge, unboundPosition)
}