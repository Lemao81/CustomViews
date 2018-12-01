package com.jueggs.customview.rangebar.view

import android.content.Context
import com.jueggs.andutils.callback.TapUpListener
import com.jueggs.customview.rangebar.attributes.ThumbAttributes
import com.jueggs.customview.rangebar.helper.*

class RasterThumb(context: Context, attrs: ThumbAttributes, leftEdge: () -> Int, rightEdge: () -> Int) : Thumb(context, attrs, leftEdge, rightEdge) {

    override var valuePoint: ValuePoint = ValuePoint.EMPTY
        set(value) {
            field = value
            valueChangingPublisher.onNext(value.value)
            positionChangingPublisher.onNext(value.position.toInt())
        }

    init {
        addTouchListener(TapUpListener {
            position = valuePoint.position.toInt()
            translationX = 0f
        })
    }

    override fun move(dx: Float) {
        val newPosition = position + translationRangeCrop(dx)

        if (!valuePoint.contains(newPosition))
            valuePointFinder.findValuePoint(valuePoint.iterator(), valuePoint.iterator(), newPosition)?.let {
                valuePoint = it
                translationX = it.position - position
            }
    }
}