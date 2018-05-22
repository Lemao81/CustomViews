package com.jueggs.customview.rangebar.view

import android.content.Context
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.rangebar.helper.*
import com.jueggs.customview.rangebar.util.TapUpListener

class RasterThumb(context: Context, attrs: ThumbAttributes, leftEdge: () -> Int, rightEdge: () -> Int) : Thumb(context, attrs, leftEdge, rightEdge) {

    init {
        addTouchListener(TapUpListener {
            position = valuePoint.position.toInt()
            translationX = 0f
        })
    }

    override var valuePoint: ValuePoint = ValuePoint.EMPTY
        set(value) {
            field = value
            valueChangingPublisher.onNext(value.value)
            positionChangingPublisher.onNext(value.position.toInt())
        }

    override fun move(dx: Float) {
        val newPosition = position + translationRangeCrop(dx)

        if (!valuePoint.contains(newPosition)) {
            findAndSetValuePoint(valuePoint.iterator(), valuePoint.iterator(), newPosition)
            translationX = valuePoint.position - position
        }
    }
}