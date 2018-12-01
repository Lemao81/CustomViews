package com.jueggs.customview.rangebar.attributes

import android.content.Context
import com.jueggs.andutils.extension.colorResToInt
import com.jueggs.customview.rangebar.Mode
import com.jueggs.customview.rangebar.R

class Attributes(context: Context) {
    var mode = Mode.SMOOTH
    var height = context.resources.getDimensionPixelSize(R.dimen.default_height)
    var barRadius = context.resources.getDimensionPixelSize(R.dimen.default_bar_radius)
    var barBaseColor = context.colorResToInt(R.color.default_bar_base_color)
    var barRangeColor = context.colorResToInt(R.color.default_bar_range_color)
    var totalMin = context.resources.getInteger(R.integer.default_total_min)
    var totalMax = context.resources.getInteger(R.integer.default_total_max)
    var rangeMin = context.resources.getInteger(R.integer.default_total_min)
    var rangeMax = context.resources.getInteger(R.integer.default_total_max)
    var thumbDiameter = context.resources.getDimensionPixelSize(R.dimen.default_thumb_diameter)
    var thumbColor = context.colorResToInt(R.color.default_thumb_color)
}