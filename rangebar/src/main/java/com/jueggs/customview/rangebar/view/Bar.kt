package com.jueggs.customview.rangebar.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.*
import android.view.Gravity.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.jueggs.customview.rangebar.attributes.*
import com.jueggs.customview.rangebar.helper.ValuePoint
import com.jueggs.customviewutils.CvUtil.measureView
import java.util.*

@SuppressLint("ViewConstructor")
internal class Bar(context: Context, private val attributes: Attributes, private val leftThumb: Thumb, private val rightThumb: Thumb) : View(context) {
    lateinit var valuePoints: LinkedList<ValuePoint>
    private val baseBounds = RectF(0f, 0f, 0f, attributes.height.toFloat())
    private val rangeBounds = Rect(0, 0, 0, attributes.height)
    private val basePaint = Paint()
    private val rangePaint = Paint()
    private val radius = attributes.barRadius.toFloat()
    private val margin = (leftThumb.radius + leftThumb.margin).toInt()

    init {
        with(basePaint) {
            color = attributes.barBaseColor
            style = Paint.Style.FILL
        }
        with(rangePaint) {
            color = attributes.barRangeColor
            style = Paint.Style.FILL
        }

        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            leftMargin = margin
            rightMargin = margin
            gravity = CENTER_VERTICAL
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
        leftThumb.initialize(valuePoints.single { it.value == attributes.rangeMin })
        rightThumb.initialize(valuePoints.single { it.value == attributes.rangeMax })
    }

    private fun createValuePoints(): LinkedList<ValuePoint> {
        return LinkedList<ValuePoint>().also { points ->
            val width = width.toFloat()
            val offset = leftThumb.margin.toFloat()
            val steps = attributes.totalMax - attributes.totalMin
            val interval = width / steps

            for (i in 0 until steps) {
                val value = i + attributes.totalMin
                val position = offset + i * interval
                points.add(ValuePoint(value, position, interval, points))
            }

            val lastPosition = offset + width
            val lastInterval = (lastPosition - points.last.range.second) * 2
            points.addLast(ValuePoint(attributes.totalMax, lastPosition, lastInterval, points))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = measureView(widthMeasureSpec, heightMeasureSpec, baseBounds.right.toInt(), attributes.height, this::setMeasuredDimension)

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(baseBounds, radius, radius, basePaint)
        canvas.drawRect(rangeBounds, rangePaint)
    }
}