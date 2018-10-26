package com.jueggs.customview.availableindicator

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import com.jueggs.andutils.extension.dpToPixel

class Attributes(context: Context, attrs: AttributeSet?) {
    var indicatorSize: Int = context.resources.getDimensionPixelSize(R.dimen.default_indicator_size)
    var indicatorSpace: Int = context.resources.getDimensionPixelSize(R.dimen.default_indicator_space)

    init {
        context.withStyledAttributes(attrs, R.styleable.AvailableIndicator) {
            val sizeDimension = getDimension(R.styleable.AvailableIndicator_indicatorSize, context.resources.getDimension(R.dimen.default_indicator_size))
            val spaceResId = when {
                sizeDimension < 24 -> R.dimen.indicator_space_small
                sizeDimension > 48 -> R.dimen.indicator_space_big
                else -> R.dimen.default_indicator_space
            }

            indicatorSize = context.dpToPixel(sizeDimension.toInt())
            indicatorSpace = context.resources.getDimensionPixelSize(spaceResId)
        }
    }
}