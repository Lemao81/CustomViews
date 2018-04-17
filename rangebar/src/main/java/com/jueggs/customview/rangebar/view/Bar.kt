package com.jueggs.customview.rangebar.view

import android.content.Context
import android.graphics.*
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.jueggs.customview.rangebar.attribute.BarAttributes
import com.jueggs.customview.rangebar.util.measureView

class Bar(context: Context, private val attrs: BarAttributes) : View(context) {
    private val baseBounds = RectF(0f, 0f, 0f, attrs.heightF)
    private val rangeBounds = Rect(0, 0, 0, attrs.height)

    private val basePaint = Paint().apply { color = attrs.baseColor; style = Paint.Style.FILL }
    private val rangePaint = Paint().apply { color = attrs.rangeColor;style = Paint.Style.FILL }

    init {
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            leftMargin = attrs.margin
            rightMargin = attrs.margin
            gravity = Gravity.CENTER_VERTICAL
        }
    }

    fun initWidthAndRange(startMinRange: Int, startMaxRange: Int) {
        baseBounds.right = width.toFloat()
        rangeBounds.left = startMinRange
        rangeBounds.right = startMaxRange
    }

    fun setLeftRange(position: Int) {
        rangeBounds.left = position
        invalidate()
    }

    fun setRightRange(position: Int) {
        rangeBounds.right = position
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = measureView(widthMeasureSpec, heightMeasureSpec, baseBounds.right.toInt(), attrs.height, this::setMeasuredDimension)

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(baseBounds, attrs.radius, attrs.radius, basePaint)
        canvas.drawRect(rangeBounds, rangePaint)
    }
}