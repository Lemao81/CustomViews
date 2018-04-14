package com.jueggs.customview.rangebar.view

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.FrameLayout
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.rangebar.util.*
import io.reactivex.Observable
import io.reactivex.subjects.*

abstract class Thumb(context: Context, private val attrs: ThumbAttributes) : View(context) {
    private lateinit var positionPublisher: Subject<Int>

    private var paint: Paint = Paint().apply {
        color = attrs.color
        style = Paint.Style.FILL
    }

    var position: Int
        get() = (layoutParams as FrameLayout.LayoutParams).leftMargin
        set(value) {
            (layoutParams as FrameLayout.LayoutParams).leftMargin = value
        }

    open fun init(startPosition: Int) {
        CompoundTouchListener().apply {
            add(TapDownListener { animateScaleBounceOut() })
            add(VerticalMoveListener { move(it.toInt()) })
            add(TapUpListener { animateScaleBounceIn() })
            setOnTouchListener(this)
        }

        positionPublisher = BehaviorSubject.createDefault(startPosition)
    }

    private fun move(dx: Int) {
        val toMove = calcMovement(position + dx, dx, bottomLimit(), topLimit())

        if (toMove != 0) {
            position += toMove
            positionPublisher.onNext(position)
        }
    }

    private fun calcMovement(unbounded: Int, dx: Int, bottomLimit: Int, topLimit: Int): Int = when {
        unbounded < bottomLimit -> dx + (bottomLimit - unbounded)
        unbounded > topLimit -> dx - (unbounded - topLimit)
        else -> dx
    }

    abstract fun bottomLimit(): Int
    abstract fun topLimit(): Int

    fun observe(): Observable<Int> = positionPublisher

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = attrs.diameter + 2 * attrs.margin
        measureView(widthMeasureSpec, heightMeasureSpec, size, size, this::setMeasuredDimension)
    }

    override fun onDraw(canvas: Canvas) {
        val centerXY = attrs.margin + attrs.radiusF
        canvas.drawCircle(centerXY, centerXY, attrs.radiusF, paint)
    }
}