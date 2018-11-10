package com.jueggs.customview.availableindicator

import android.content.Context

class Attributes(context: Context) {
    var indicatorSize: Int = context.resources.getDimensionPixelSize(R.dimen.default_indicator_size)
    var indicatorSpace: Int = context.resources.getDimensionPixelSize(R.dimen.default_indicator_space)
}