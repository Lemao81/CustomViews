package com.jueggs.customview.rangebar.view

import android.content.Context
import com.jueggs.andutils.callback.TapUpListener
import com.jueggs.customview.rangebar.attribute.ThumbAttributes

class SmoothThumb(context: Context, attrs: ThumbAttributes, leftEdge: () -> Int, rightEdge: () -> Int) : Thumb(context, attrs, leftEdge, rightEdge) {

    init {
        addTouchListener(TapUpListener {
            position = (position + translationX).toInt()
            translationX = 0f
        })
    }

    override fun move(dx: Float) {
        translationX = translationRangeCrop(dx)
        val newPosition = position + translationX
        positionChangingPublisher.onNext(newPosition.toInt())

        if (!valuePoint.contains(newPosition))
            valuePointFinder.findValuePoint(valuePoint.iterator(), valuePoint.iterator(), newPosition)?.let { valuePoint = it }
    }
}