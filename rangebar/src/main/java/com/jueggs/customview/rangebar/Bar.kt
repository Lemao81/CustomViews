package com.jueggs.customview.rangebar

import android.content.Context
import android.graphics.*
import android.view.View
import com.jueggs.customview.rangebar.attribute.BarAttributes
import com.jueggs.customview.util.measureView

class Bar(context: Context, private val attrs: BarAttributes) : View(context) {
    private val baseBounds = RectF(0f, 0f, 0f, attrs.heightF)
    private val rangeBounds = Rect(attrs.rangeMin, 0, attrs.rangeMax, attrs.height)

    private val basePaint = Paint().apply {
        color = attrs.baseColor
        style = Paint.Style.FILL
    }
    private val rangePaint = Paint().apply {
        color = attrs.rangeColor
        style = Paint.Style.FILL
    }

    fun setLeftRange(position: Int) {
        rangeBounds.left = position
    }

    fun setRightRange(position: Int) {
        rangeBounds.right = position
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        baseBounds.right = width.toFloat()
        baseBounds.bottom = height.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = measureView(widthMeasureSpec, heightMeasureSpec, Int.MAX_VALUE, attrs.height, this::setMeasuredDimension)

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(baseBounds, attrs.radius, attrs.radius, basePaint)
        canvas.drawRect(rangeBounds, rangePaint)
    }
}