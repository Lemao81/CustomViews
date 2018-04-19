package com.jueggs.customview.rangebar.helper

class ValuePoint(val value: Int, val position: Float, interval: Float, var prev: ValuePoint? = null, var next: ValuePoint? = null) {
    var range: Pair<Float, Float>

    init {
        val halfInterval = interval / 2
        range = Pair(position - halfInterval, position + halfInterval)
    }

    fun contains(x: Float) = x >= range.first && x <= range.second

    companion object {
        val EMPTY = ValuePoint(0, 0f, 0f, null, null)
    }
}