package com.jueggs.customview.rangebar.view

import android.content.Context
import com.jueggs.andutils.helper.TapUpListener
import com.jueggs.customview.rangebar.attribute.ThumbAttributes

class SnapThumb(context: Context, attrs: ThumbAttributes, leftEdge: () -> Int, rightEdge: () -> Int) : Thumb(context, attrs, leftEdge, rightEdge) {

    init {
        addTouchListener(TapUpListener {
            position = valuePoint.position.toInt()
            translationX = 0f
            positionChangingPublisher.onNext(position)
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