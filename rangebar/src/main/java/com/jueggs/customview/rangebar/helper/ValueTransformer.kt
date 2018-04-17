package com.jueggs.customview.rangebar.helper

import com.jueggs.customview.rangebar.attribute.BarAttributes

class ValueTransformer(private val barAttrs: BarAttributes) {
    var width: Int = 0
        set(value) {
            pixelPerValue = (value / (barAttrs.totalMax - barAttrs.totalMin)).toFloat()
        }
    private var pixelPerValue: Float = 0f

    fun positionToValue(position: Int) = (position / pixelPerValue + barAttrs.totalMin).toInt()
    fun valueToPosition(value: Int) = ((value - barAttrs.totalMin) * pixelPerValue).toInt()
}