package com.jueggs.customview.rangebar

class ValueTransformer(width: Int, val totalMin: Int, totalMax: Int) {
    private val pixelPerValue: Float = (width / (totalMax - totalMin)).toFloat()

    fun positionToValue(position: Int) = (position / pixelPerValue + totalMin).toInt()
}