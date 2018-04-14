package com.jueggs.customview.rangebar.util

import android.annotation.SuppressLint
import android.view.*

class VerticalMoveListener(private val onMove: (Float) -> Unit) : View.OnTouchListener {
    private var startX: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent) =
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    onMove(event.x - startX)
                    true
                }
                MotionEvent.ACTION_UP -> true
                else -> false
            }
}