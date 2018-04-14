package com.jueggs.customview.rangebar.helper

import com.jueggs.customview.rangebar.attribute.BarAttributes

class ValueTransformer(width: Int, private val barAttrs: BarAttributes) {
    private val pixelPerValue: Float = (width / (barAttrs.totalMax - barAttrs.totalMin)).toFloat()

    fun positionToValue(position: Int) = (position / pixelPerValue + barAttrs.totalMin).toInt()
    fun valueToPosition(value: Int) = ((value - barAttrs.totalMin) * pixelPerValue).toInt()
}