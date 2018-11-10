package com.jueggs.customview.availableindicator

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.jueggs.andutils.extension.dipToPixel
import com.jueggs.customviewutils.attribute.AbstractAttributeStore

class AttributeStore(context: Context, attrs: AttributeSet?, stylableResId: IntArray) : AbstractAttributeStore<Attributes>() {
    init {
        init(context, attrs, stylableResId)
    }

    override fun createAttributes(context: Context): Attributes = Attributes(context)

    override fun setAttributes(context: Context, attributes: Attributes): TypedArray.() -> Unit = {
        val sizeInDips = getDimension(R.styleable.AvailableIndicator_indicatorSize, context.resources.getDimension(R.dimen.default_indicator_size))
        val spaceResId = when {
            sizeInDips < 24 -> R.dimen.indicator_space_small
            sizeInDips > 48 -> R.dimen.indicator_space_big
            else -> R.dimen.default_indicator_space
        }

        attributes.indicatorSize = context.dipToPixel(sizeInDips.toInt())
        attributes.indicatorSpace = context.resources.getDimensionPixelSize(spaceResId)
    }
}