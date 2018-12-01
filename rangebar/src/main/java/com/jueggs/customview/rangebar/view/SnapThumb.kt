package com.jueggs.customview.rangebar.view

import android.annotation.SuppressLint
import android.content.Context
import com.jueggs.andutils.callback.TapUpListener
import com.jueggs.customview.rangebar.attributes.Attributes

@SuppressLint("ViewConstructor")
internal class SnapThumb(context: Context, attributes: Attributes, leftEdge: () -> Int, rightEdge: () -> Int) : Thumb(context, attributes, leftEdge, rightEdge) {

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