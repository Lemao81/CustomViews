package com.jueggs.customview.rangebar.view

import android.annotation.SuppressLint
import android.content.Context
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.util.rangeCrop

@SuppressLint("ViewConstructor")
class RightThumb(context: Context, attrs: ThumbAttributes) : Thumb(context, attrs) {
    var positionLeftThumb: Int = 0
    private var positionRightEdge: Int = 0

    override fun bottomLimit() = positionLeftThumb
    override fun topLimit() = positionRightEdge

    fun init(startPosition: Int, positionRightEdge: Int) {
        init(startPosition)
        this.positionRightEdge = positionRightEdge
        position = positionRightEdge
    }
}