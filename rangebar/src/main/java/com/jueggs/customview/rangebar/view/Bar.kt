package com.jueggs.customview.rangebar.view

import android.content.Context
import android.graphics.*
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.jueggs.andutils.measureView
import com.jueggs.customview.rangebar.attribute.*
import com.jueggs.customview.rangebar.helper.ValuePoint
import java.util.*

class Bar(
        context: Context,
        private val barAttrs: BarAttributes,
        private val thumbAttrs: ThumbAttributes,
        private val leftThumb: Thumb,
        private val rightThumb: Thumb) : View(context) {
    private val baseBounds = RectF(0f, 0f, 0f, barAttrs.heightF)
    private val rangeBounds = Rect(0, 0, 0, barAttrs.height)
    lateinit var valuePoints: LinkedList<ValuePoint>

    private val basePaint = Paint().apply { color = barAttrs.baseColor; style = Paint.Style.FILL }
    private val rangePaint = Paint().apply { color = barAttrs.rangeColor;style = Paint.Style.FILL }

    init {
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            leftMargin = barAttrs.margin
            rightMargin = barAttrs.margin
            gravity = Gravity.CENTER_VERTICAL
        }
    }

    fun setLeftRange(position: Int) {
        rangeBounds.left = position
        invalidate()
    }

    fun setRightRange(position: Int) {
        rangeBounds.right = position
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        baseBounds.right = width.toFloat()

        valuePoints = createValuePoints()
        leftThumb.initialize(valuePoints.single { it.value == barAttrs.rangeMin })
        rightThumb.initialize(valuePoints.single { it.value == barAttrs.rangeMax })
    }

    private fun createValuePoints(): LinkedList<ValuePoint> {
        return LinkedList<ValuePoint>().also { points ->
            val width = width.toFloat()
            val offset = thumbAttrs.margin.toFloat()
            val steps = barAttrs.totalMax - barAttrs.totalMin
            val interval = width / steps

            for (i in 0 until steps) {
                val value = i + barAttrs.totalMin
                val position = offset + i * interval
                points.add(ValuePoint(value, position, interval, points))
            }

            val lastPosition = offset + width
            val lastInterval = (lastPosition - points.last.range.second) * 2
            points.addLast(ValuePoint(barAttrs.totalMax, lastPosition, lastInterval, points))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = measureView(widthMeasureSpec, heightMeasureSpec, baseBounds.right.toInt(), barAttrs.height, this::setMeasuredDimension)

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(baseBounds, barAttrs.radius, barAttrs.radius, basePaint)
        canvas.drawRect(rangeBounds, rangePaint)
    }
}