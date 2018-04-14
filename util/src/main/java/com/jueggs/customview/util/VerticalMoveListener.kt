package com.jueggs.customview.util

import android.annotation.SuppressLint
import android.view.*

class VerticalMoveListener(private val onMove: (Float) -> Unit) : View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent) =
            when (event.action) {
                MotionEvent.ACTION_DOWN -> true
                MotionEvent.ACTION_MOVE -> {
                    onMove(event.x)
                    true
                }
                MotionEvent.ACTION_UP -> true
                else -> false
            }
}