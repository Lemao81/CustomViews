package com.jueggs.customview.rangebar.attribute

import android.content.Context
import android.content.res.TypedArray
import com.jueggs.customview.rangebar.*
import com.jueggs.customview.rangebar.R
import com.jueggs.customview.util.*

class ThumbAttributes(context: Context, a: TypedArray) {
    val diameter = a.getDimensionPixelSize(R.styleable.RangeBar_thumbDiameter, context.dpToPixel(DEFAULT_THUMB_DIAMETER))
    val radius = (diameter / 2)
    val radiusF = radius.toFloat()
    val color = a.getColor(R.styleable.RangeBar_thumbColor, context.getColorCompat(R.color.default_thumb_color))
}