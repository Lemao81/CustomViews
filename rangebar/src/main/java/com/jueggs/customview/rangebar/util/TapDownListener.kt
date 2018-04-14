package com.jueggs.customview.rangebar.util

import android.annotation.SuppressLint
import android.view.*

class TapDownListener(private val onTabDown: () -> Unit) : View.OnTouchListener {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent) =
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    onTabDown()
                    true
                }
                MotionEvent.ACTION_UP -> true
                else -> false
            }
}