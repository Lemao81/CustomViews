package com.jueggs.customview.rangebar.view

import android.content.Context
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.rangebar.util.*

class SnapThumb(context: Context, attrs: ThumbAttributes, leftEdge: () -> Int, rightEdge: () -> Int) : Thumb(context, attrs, leftEdge, rightEdge) {

    override fun addTouchListener() = TapUpListener {
        position = valuePoint.position.toInt()
        positionChangingPublisher.onNext(position)
    }

    override fun move(dx: Float) {
        translationX = translationRangeCrop(dx)
        val newPosition = position + translationX
        positionChangingPublisher.onNext(newPosition.toInt())
        if (!valuePoint.contains(newPosition)) findValuePoint(valuePoint.prev, valuePoint.next, newPosition)
    }
}