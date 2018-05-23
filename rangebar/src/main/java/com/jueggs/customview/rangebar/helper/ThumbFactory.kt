package com.jueggs.customview.rangebar.helper

import android.content.Context
import com.jueggs.andutils.helper.ContextDisposable
import com.jueggs.customview.rangebar.*
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.rangebar.view.*

class ThumbFactory(context: Context?, private val attrs: ThumbAttributes) : ContextDisposable(context) {

    fun create(leftEdge: () -> Int, rightEdge: () -> Int): Thumb {
        return when (attrs.mode) {
            MODE_SNAP -> SnapThumb(context!!, attrs, leftEdge, rightEdge)
            MODE_RASTER -> RasterThumb(context!!, attrs, leftEdge, rightEdge)
            else -> SmoothThumb(context!!, attrs, leftEdge, rightEdge)
        }
    }
}