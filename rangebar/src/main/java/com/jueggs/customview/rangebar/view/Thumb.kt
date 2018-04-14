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
        val onTouchListener = CompoundTouchListener()
        onTouchListener += VerticalMoveListener { move(it.toInt()) }
        setOnTouchListener(onTouchListener)

        positionPublisher = BehaviorSubject.createDefault(startPosition)
    }

    fun move(dx: Int) {
        val unbounded = position + dx
        val bottomLimit = bottomLimit()
        val topLimit = topLimit()

        val toMove = when {
            unbounded < bottomLimit -> dx + (bottomLimit - unbounded)
            unbounded > topLimit -> dx - (unbounded - topLimit)
            else -> dx
        }

        if (toMove != 0) {
            position += toMove
            positionPublisher.onNext(position)
        }
    }

    abstract fun bottomLimit(): Int
    abstract fun topLimit(): Int

    fun observe(): Observable<Int> = positionPublisher

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = measureView(widthMeasureSpec, heightMeasureSpec, attrs.diameter, attrs.diameter, this::setMeasuredDimension)

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(attrs.radiusF, attrs.radiusF, attrs.radiusF, paint)
    }
}