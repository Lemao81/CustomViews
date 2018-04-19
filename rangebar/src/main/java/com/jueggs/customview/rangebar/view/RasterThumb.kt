package com.jueggs.customview.rangebar.view

import android.content.Context
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.rangebar.helper.*

class RasterThumb(context: Context, attrs: ThumbAttributes, leftEdge: () -> Int, rightEdge: () -> Int) : Thumb(context, attrs, leftEdge, rightEdge) {

    override var valuePoint: ValuePoint
        get() = super.valuePoint
        set(value) {
            super.valuePoint
            position = value.position.toInt()
            positionChangingPublisher.onNext(position)
        }

    override fun move(dx: Float) {
        val newPosition = position + translationRangeCrop(dx)
        if (!valuePoint.contains(newPosition)) findValuePoint(valuePoint.prev, valuePoint.next, newPosition)
    }
}