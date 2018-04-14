package com.jueggs.customview.rangebar.util

import android.annotation.SuppressLint
import android.view.*

class CompoundTouchListener : View.OnTouchListener {
    private val listeners: MutableList<View.OnTouchListener> = mutableListOf()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        var consumed = false
        listeners.forEach {
            if (it.onTouch(v, event))
                consumed = true
        }
        return consumed
    }

    operator fun plusAssign(listener: View.OnTouchListener) {
        listeners.add(listener)
    }
}