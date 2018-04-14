package com.jueggs.customview.util

import android.view.*

class TapUpListener(private val onTabUp: () -> Unit) : View.OnTouchListener {
    override fun onTouch(v: View, event: MotionEvent) =
            when (event.action) {
                MotionEvent.ACTION_DOWN -> true
                MotionEvent.ACTION_UP -> {
                    onTabUp()
                    true
                }
                else -> false
            }
}