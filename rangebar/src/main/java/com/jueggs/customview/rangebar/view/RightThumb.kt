package com.jueggs.customview.rangebar.view

import android.annotation.SuppressLint
import android.content.Context
import com.jueggs.customview.rangebar.attribute.ThumbAttributes

@SuppressLint("ViewConstructor")
class RightThumb(context: Context, attrs: ThumbAttributes) : Thumb(context, attrs) {
    var positionLeftThumb: Int = 0
    private var rightEdge: Int = 0

    override fun bottomLimit() = positionLeftThumb
    override fun topLimit() = rightEdge

    fun init(startPosition: Int, rightEdge: Int) {
        init(startPosition)
        this.rightEdge = rightEdge
        position = rightEdge
    }
}