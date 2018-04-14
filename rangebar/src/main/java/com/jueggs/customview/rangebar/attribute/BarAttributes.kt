package com.jueggs.customview.rangebar.attribute

import android.content.Context
import android.content.res.TypedArray
import com.jueggs.customview.rangebar.*
import com.jueggs.customview.rangebar.R
import com.jueggs.customview.rangebar.util.*

class BarAttributes(context: Context, a: TypedArray, thumbAttrs: ThumbAttributes) {
    val height: Int = Math.min(a.getDimensionPixelSize(R.styleable.RangeBar_barHeight, context.dpToPixel(DEFAULT_BAR_HEIGHT)), thumbAttrs.diameter)
    val heightF: Float = height.toFloat()
    val radius: Float = a.getInteger(R.styleable.RangeBar_barRadius, DEFAULT_BAR_RADIUS).toFloat()
    val margin: Int = thumbAttrs.radius + thumbAttrs.margin
    val baseColor: Int = a.getColor(R.styleable.RangeBar_barBaseColor, context.getColorCompat(R.color.default_bar_base_color))
    val rangeColor: Int = a.getColor(R.styleable.RangeBar_barRangeColor, context.getColorCompat(R.color.default_bar_range_color))
    val totalMin: Int = a.getInteger(R.styleable.RangeBar_totalMin, DEFAULT_TOTAL_MIN)
    val totalMax: Int = a.getInteger(R.styleable.RangeBar_totalMax, DEFAULT_TOTAL_MAX)
    val rangeMin: Int = a.getInteger(R.styleable.RangeBar_rangeMin, DEFAULT_TOTAL_MIN)
    val rangeMax: Int = a.getInteger(R.styleable.RangeBar_rangeMax, DEFAULT_TOTAL_MAX)
}