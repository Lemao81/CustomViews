package com.jueggs.customview.rangebar

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.FrameLayout
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.util.*
import io.reactivex.Observable
import io.reactivex.subjects.*

abstract class Thumb(context: Context, private val attrs: ThumbAttributes, startPosition: Int) : View(context) {
    private val positionSubject: Subject<Int> = BehaviorSubject.createDefault(startPosition)

    private var paint: Paint = Paint().apply {
        color = attrs.color
        style = Paint.Style.FILL
    }

    var position: Int
        get() = (layoutParams as FrameLayout.LayoutParams).leftMargin
        set(value) {
            (layoutParams as FrameLayout.LayoutParams).leftMargin = value
        }

    init {
        val onTouchListener = CompoundTouchListener()
        onTouchListener += VerticalMoveListener { move(it.toInt()) }
        setOnTouchListener(onTouchListener)
    }

    fun move(dx: Int) {
        position += cropMove(position + dx)
    }

    abstract fun cropMove(unboundPosition: Int): Int

    fun observePosition(): Observable<Int> = positionSubject

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = measureView(widthMeasureSpec, heightMeasureSpec, attrs.diameter, attrs.diameter, this::setMeasuredDimension)

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(attrs.radiusF, attrs.radiusF, attrs.radiusF, paint)
    }
}