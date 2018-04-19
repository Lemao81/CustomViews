package com.jueggs.customview.rangebar.helper

import android.content.Context
import com.jueggs.customview.rangebar.*
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.rangebar.view.*

class ThumbFactory private constructor(private val attrs: ThumbAttributes) {

    fun create(context: Context, leftEdge: () -> Int, rightEdge: () -> Int) = when (attrs.mode) {
        MODE_SNAP -> SnapThumb(context, attrs, leftEdge, rightEdge)
        MODE_RASTER -> RasterThumb(context, attrs, leftEdge, rightEdge)
        else -> SmoothThumb(context, attrs, leftEdge, rightEdge)
    }

    companion object {
        private var INSTANCE: ThumbFactory? = null

        fun getInstance(attrs: ThumbAttributes): ThumbFactory {
            if (INSTANCE == null)
                INSTANCE = ThumbFactory(attrs)
            return INSTANCE!!
        }
    }
}