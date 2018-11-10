package com.jueggs.customview.availableindicator

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jueggs.andutils.extension.*
import kotlinx.android.synthetic.main.indicator_layout.view.*

class AvailableIndicator : LinearLayout {
    //region constructor
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }
    //endregion

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.indicator_layout, this, true)
        val attributes = AttributeStore(context, attrs, R.styleable.AvailableIndicator).attributes
        val indicatorSize = attributes.indicatorSize

        imgOnline.setWidthAndHeight(indicatorSize, indicatorSize)
        imgAvailable.setWidthAndHeight(indicatorSize, indicatorSize)
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