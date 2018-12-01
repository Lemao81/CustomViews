package com.jueggs.customview.rangebar.helper

import android.content.Context
import com.jueggs.customview.rangebar.Mode
import com.jueggs.customview.rangebar.attributes.Attributes
import com.jueggs.customview.rangebar.view.*

internal class ThumbFactory(private val context: Context, private val attributes: Attributes) {
    fun create(leftEdge: () -> Int, rightEdge: () -> Int): Thumb = when (attributes.mode) {
        Mode.SMOOTH -> SmoothThumb(context, attributes, leftEdge, rightEdge)
        Mode.SNAP -> SnapThumb(context, attributes, leftEdge, rightEdge)
        Mode.RASTER -> RasterThumb(context, attributes, leftEdge, rightEdge)
    }
}