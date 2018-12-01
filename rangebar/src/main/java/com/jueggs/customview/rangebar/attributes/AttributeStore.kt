package com.jueggs.customview.rangebar.attributes

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.jueggs.customview.rangebar.Mode
import com.jueggs.customview.rangebar.R
import com.jueggs.customviewutils.attribute.AbstractAttributeStore
import java.lang.IllegalStateException

class AttributeStore(context: Context, attrs: AttributeSet?, stylableResId: IntArray) : AbstractAttributeStore<Attributes>(context, attrs, stylableResId) {
    override fun createAttributes(context: Context) = Attributes(context)

    override fun initializeAttributes(context: Context, attributes: Attributes): TypedArray.() -> Unit = {
        attributes.mode = Mode.find(getInteger(R.styleable.RangeBar_mode, Mode.SMOOTH.id))
        attributes.height = getDimensionPixelSize(R.styleable.RangeBar_height, attributes.height)
        attributes.barRadius = getDimensionPixelSize(R.styleable.RangeBar_barRadius, attributes.barRadius)
        attributes.barBaseColor = getColor(R.styleable.RangeBar_barBaseColor, attributes.barBaseColor)
        attributes.barRangeColor = getColor(R.styleable.RangeBar_barRangeColor, attributes.barRangeColor)
        attributes.totalMin = getInteger(R.styleable.RangeBar_totalMin, attributes.totalMin)
        attributes.totalMax = getInteger(R.styleable.RangeBar_totalMax, attributes.totalMax)
        attributes.rangeMin = getInteger(R.styleable.RangeBar_rangeMin, attributes.rangeMin)
        attributes.rangeMax = getInteger(R.styleable.RangeBar_rangeMax, attributes.rangeMax)
        attributes.thumbDiameter = getDimensionPixelSize(R.styleable.RangeBar_thumbDiameter, attributes.thumbDiameter)
        attributes.thumbColor = getColor(R.styleable.RangeBar_thumbColor, attributes.thumbColor)
    }

    override fun validateAttributes(): Attributes.() -> Unit = {
        if (totalMin > totalMax) throw IllegalStateException("Rangebar: total min ($totalMin) > total max ($totalMax)")
        if (rangeMin > rangeMax) throw IllegalStateException("Rangebar: range min ($rangeMin) > range max ($rangeMax)")
        if (rangeMin < totalMin) throw IllegalStateException("Rangebar: range min ($rangeMin) < total min ($totalMin)")
        if (rangeMax > totalMax) throw IllegalStateException("Rangebar: range max ($rangeMax) > total max ($totalMax)")
    }
}