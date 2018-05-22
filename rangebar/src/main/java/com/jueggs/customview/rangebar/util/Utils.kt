package com.jueggs.customview.rangebar.util

import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.*
import android.view.animation.AnticipateInterpolator

fun measureView(widthMeasureSpec: Int, heightMeasureSpec: Int, desiredWidth: Int, desiredHeight: Int, setMeasuredDimension: (Int, Int) -> Unit) {
    val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
    val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
    val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
    val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

    val width = when (widthMode) {
        View.MeasureSpec.EXACTLY -> widthSize
        View.MeasureSpec.AT_MOST -> Math.min(desiredWidth, widthSize)
        View.MeasureSpec.UNSPECIFIED -> desiredWidth
        else -> desiredWidth
    }
    val height = when (heightMode) {
        View.MeasureSpec.EXACTLY -> heightSize
        View.MeasureSpec.AT_MOST -> Math.min(desiredHeight, heightSize)
        View.MeasureSpec.UNSPECIFIED -> desiredHeight
        else -> desiredHeight
    }

    setMeasuredDimension(width, height)
}

fun Context.dpToPixel(dips: Int): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips.toFloat(), resources.displayMetrics).toInt()

fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun isLollipopOrAbove(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

fun rangeCrop(bottomLimit: Float, topLimit: Float, value: Float) = Math.min(Math.max(bottomLimit, value), topLimit)

fun View.animateScaleBounceOut(duration: Long = 150, scale: Float = 1.1f, tension: Float = 6f) {
    animate().apply {
        this.duration = duration
        scaleX(scale)
        scaleY(scale)
        interpolator = AnticipateInterpolator(tension)
        start()
    }
}

fun View.animateScaleBounceIn(duration: Long = 150, scale: Float = 0.91f, tension: Float = 6f) {
    animate().apply {
        this.duration = duration
        scaleX(scale)
        scaleY(scale)
        interpolator = AnticipateInterpolator(tension)
        start()
    }
}

inline fun View.doOnGlobalLayout(crossinline action: () -> Unit) {
    val vto = viewTreeObserver
    vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            action()
            when {
                vto.isAlive -> vto.removeOnGlobalLayoutListener(this)
                else -> viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(): T {
    assert(layoutParams is T)
    return layoutParams as T
}