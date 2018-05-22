package com.jueggs.customview.rangebar.helper

import java.util.*

class ValuePoint(
        val value: Int,
        val position: Float,
        interval: Float,
        private var allPoints: LinkedList<ValuePoint>) {
    var range: Pair<Float, Float>

    init {
        val halfInterval = interval / 2
        range = Pair(position - halfInterval, position + halfInterval)
    }

    fun contains(x: Float) = x >= range.first && x <= range.second

    fun iterator(): ListIterator<ValuePoint> = allPoints.listIterator(allPoints.indexOf(this))

    companion object {
        val EMPTY = ValuePoint(0, 0f, 0f, LinkedList())
    }
}