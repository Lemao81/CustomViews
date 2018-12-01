package com.jueggs.customview.rangebar.attributes

import android.content.Context
import android.content.res.TypedArray
import com.jueggs.andutils.extension.*
import com.jueggs.customview.rangebar.*
import com.jueggs.customview.rangebar.R

class ThumbAttributes(context: Context, a: TypedArray) {
    val mode = a.getInteger(R.styleable.RangeBar_mode, MODE_SMOOTH)
    val diameter = a.getDimensionPixelSize(R.styleable.RangeBar_thumbDiameter, context.dipToPixel(DEFAULT_THUMB_DIAMETER))
    val radius = diameter / 2
    val margin = diameter / 6
    val rightEdgeOffset = diameter + margin
    val color = a.getColor(R.styleable.RangeBar_thumbColor, context.colorResToInt(R.color.default_thumb_color))
}