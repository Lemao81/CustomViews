package com.jueggs.customview.rangebar.view

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.FrameLayout
import com.jueggs.customview.rangebar.*
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import com.jueggs.customview.rangebar.util.*
import io.reactivex.Observable
import io.reactivex.subjects.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.*
import com.jueggs.customview.rangebar.helper.*

abstract class Thumb(context: Context, private val attrs: ThumbAttributes, private var leftEdge: () -> Int, private var rightEdge: () -> Int) : View(context) {
    private var paint: Paint = Paint().apply { color = attrs.color; style = Paint.Style.FILL }
    private var compoundTouchListener: CompoundTouchListener
    protected var positionChangingPublisher: Subject<Int> = PublishSubject.create()
    protected var valueChangingPublisher: Subject<Int> = PublishSubject.create()
    internal val valueChangedPublisher: Subject<Int> = PublishSubject.create()

    var position: Int
        get() = layoutParams<FrameLayout.LayoutParams>().leftMargin
        set(value) {
            updateLayoutParams<FrameLayout.LayoutParams> { leftMargin = value }
        }

    open var valuePoint: ValuePoint = ValuePoint.EMPTY
        set(value) {
            field = value
            valueChangingPublisher.onNext(value.value)
        }

    init {
        layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { setMargins(attrs.margin, attrs.margin, attrs.margin, attrs.margin) }

        compoundTouchListener = CompoundTouchListener().apply {
            add(TapDownListener { animateScaleBounceOut() })
            add(VerticalMoveListener { move(it) })
            add(TapUpListener {
                animateScaleBounceIn()
                valueChangedPublisher.onNext(valuePoint.value)
            })
            setOnTouchListener(this)
        }
    }

    fun addTouchListener(listener: OnTouchListener) = compoundTouchListener.add(listener)

    fun initialize(valuePoint: ValuePoint) = changeValuePoint(valuePoint)

    fun changeValuePoint(valuePoint: ValuePoint) {
        this.valuePoint = valuePoint
        position = valuePoint.position.toInt()
        positionChangingPublisher.onNext(position)
    }

    abstract fun move(dx: Float)

    protected fun translationRangeCrop(dx: Float) = rangeCrop((leftEdge() - position).toFloat(), (rightEdge() - position).toFloat(), dx)

    protected fun findAndSetValuePoint(backwardIterator: ListIterator<ValuePoint>, forwardIterator: ListIterator<ValuePoint>, position: Float) {
        if (!backwardIterator.hasPrevious() && forwardIterator.hasNext())
            return searchForward(forwardIterator, position)
        if (!forwardIterator.hasNext() && backwardIterator.hasPrevious())
            return searchBackward(backwardIterator, position)
        if (backwardIterator.hasPrevious() && forwardIterator.hasNext())
            searchBothDirections(backwardIterator, forwardIterator, position)
    }

    private fun searchForward(iterator: ListIterator<ValuePoint>, position: Float) {
        val next = iterator.next()
        if (next.contains(position)) {
            valuePoint = next; return
        }
        if (iterator.hasNext())
            searchForward(iterator, position)
    }

    private fun searchBackward(iterator: ListIterator<ValuePoint>, position: Float) {
        val prev = iterator.previous()
        if (prev.contains(position)) {
            valuePoint = prev; return
        }
        if (iterator.hasPrevious())
            searchBackward(iterator, position)
    }

    private fun searchBothDirections(backwardIterator: ListIterator<ValuePoint>, forwardIterator: ListIterator<ValuePoint>, position: Float) {
        val prev = backwardIterator.previous()
        if (prev.contains(position)) {
            valuePoint = prev; return
        }
        val next = forwardIterator.next()
        if (next.contains(position)) {
            valuePoint = next; return
        }
        findAndSetValuePoint(backwardIterator, forwardIterator, position)
    }

    fun observePositionChanging(): Observable<Int> = positionChangingPublisher

    fun observeValueChanging(): Observable<Int> = valueChangingPublisher

    fun observeValueChanged(): Observable<Int> = valueChangedPublisher

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = when {
            isInEditMode -> context.dpToPixel(DEFAULT_THUMB_DIAMETER)
            else -> attrs.diameter
        }
        measureView(widthMeasureSpec, heightMeasureSpec, size, size, this::setMeasuredDimension)
    }

    override fun onDraw(canvas: Canvas) {
        if (isInEditMode) {
            val editRadius = context.dpToPixel(DEFAULT_THUMB_DIAMETER).toFloat() / 2
            canvas.drawCircle(editRadius, editRadius, editRadius, paint)
        } else {
            canvas.drawCircle(attrs.radiusF, attrs.radiusF, attrs.radiusF, paint)
        }
    }
}