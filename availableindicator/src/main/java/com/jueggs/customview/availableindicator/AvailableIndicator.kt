package com.jueggs.customview.availableindicator

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jueggs.andutils.extension.*
import kotlinx.android.synthetic.main.indicator_layout.view.*

class AvailableIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    var attributes = Attributes(context, attrs)

    init {
        LayoutInflater.from(context).inflate(R.layout.indicator_layout, this, true)

        imgOnline.setWidthAndHeight(attributes.indicatorSize, attributes.indicatorSize)
        imgAvailable.setWidthAndHeight(attributes.indicatorSize, attributes.indicatorSize)
        imgAvailable.layoutParams.asMarginLayoutParams().leftMargin = attributes.indicatorSpace
    }

    fun setState(availableState: AvailableState) {
        var onlineColor = R.color.default_negative
        var availableColor = R.color.default_negative

        when (availableState) {
            AvailableState.OFFLINE -> {
                onlineColor = R.color.default_negative
                availableColor = R.color.default_negative
            }
            AvailableState.ONLINE -> {
                onlineColor = R.color.default_positive
                availableColor = R.color.default_negative
            }
            AvailableState.AVAILABLE -> {
                onlineColor = R.color.default_positive
                availableColor = R.color.default_positive
            }
        }

        imgOnline.drawable.asGradientDrawable().setColor(context.colorResToInt(onlineColor))
        imgAvailable.drawable.asGradientDrawable().setColor(context.colorResToInt(availableColor))
    }
}