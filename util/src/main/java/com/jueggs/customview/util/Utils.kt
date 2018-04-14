package com.jueggs.customview.util

import android.content.Context
import android.os.Build
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View

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

fun rangeCrop(bottomLimit: Int, topLimit: Int, value: Int) = Math.min(Math.max(bottomLimit, value), topLimit)