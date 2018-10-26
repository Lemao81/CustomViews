package com.jueggs.customview.rangebar.attribute

import android.content.Context
import android.content.res.TypedArray
import com.jueggs.andutils.extension.*
import com.jueggs.customview.rangebar.*
import com.jueggs.customview.rangebar.R

class BarAttributes(context: Context, a: TypedArray, thumbAttrs: ThumbAttributes) {

    init {
        val isTotalMinDefined = a.hasValue(R.styleable.RangeBar_totalMin)
        val isTotalMaxDefined = a.hasValue(R.styleable.RangeBar_totalMax)
        val isRangeMinDefined = a.hasValue(R.styleable.RangeBar_rangeMin)
        val isRangeMaxDefined = a.hasValue(R.styleable.RangeBar_rangeMax)

        assert(isTotalMinDefined == isTotalMaxDefined, { "Both totalMin and totalMax need to be defined or none of them" })
        assert(isRangeMinDefined == isRangeMaxDefined, { "Both rangeMin and rangeMax need to be defined or none of them" })
    }

    val height = Math.min(a.getDimensionPixelSize(R.styleable.RangeBar_barHeight, context.dpToPixel(DEFAULT_BAR_HEIGHT)), thumbAttrs.diameter)
    val heightF = height.toFloat()
    val radius = a.getInteger(R.styleable.RangeBar_barRadius, DEFAULT_BAR_RADIUS).toFloat()
    val margin = thumbAttrs.radius + thumbAttrs.margin
    val baseColor = a.getColor(R.styleable.RangeBar_barBaseColor, context.colorResToInt(R.color.default_bar_base_color))
    val rangeColor = a.getColor(R.styleable.RangeBar_barRangeColor, context.colorResToInt(R.color.default_bar_range_color))
    val totalMin = a.getInteger(R.styleable.RangeBar_totalMin, DEFAULT_TOTAL_MIN)
    val totalMax = Math.max(a.getInteger(R.styleable.RangeBar_totalMax, DEFAULT_TOTAL_MAX), totalMin)
    val rangeMin = Math.max(a.getInteger(R.styleable.RangeBar_rangeMin, totalMin), totalMin)
    val rangeMax = Math.min(a.getInteger(R.styleable.RangeBar_rangeMax, totalMax), totalMax)
}