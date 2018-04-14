package com.jueggs.customview.util

import android.view.*

class VerticalMoveListener(private val onMove: (Float) -> Unit) : View.OnTouchListener {
    private var previousValue: Float = 0f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE) {
            val newValue = event.x
            onMove(newValue - previousValue)
            previousValue = newValue
            return true
        }
        return false
    }
}