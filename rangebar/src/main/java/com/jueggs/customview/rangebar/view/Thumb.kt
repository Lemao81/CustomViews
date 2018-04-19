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
import com.jueggs.customview.rangebar.helper.*
import java.nio.file.Files.move

abstract class Thumb(context: Context, private val attrs: ThumbAttributes, private var leftEdge: () -> Int, private var rightEdge: () -> Int) : View(context) {
    private var paint: Paint = Paint().apply { color = attrs.color; style = Paint.Style.FILL }
    protected var positionChangingPublisher: Subject<Int> = PublishSubject.create()
    private var valueChangingPublisher: Subject<Int> = PublishSubject.create()
    internal val valueChangedPublisher: Subject<Int> = PublishSubject.create()

    var position: Int
        get() = (layoutParams as FrameLayout.LayoutParams).leftMargin
        set(value) {
            (layoutParams as FrameLayout.LayoutParams).leftMargin = value
        }

    open var valuePoint: ValuePoint = ValuePoint.EMPTY
        set(value) {
            field = value
            valueChangingPublisher.onNext(value.value)
        }

    init {
        layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { setMargins(attrs.margin, attrs.margin, attrs.margin, attrs.margin) }

        CompoundTouchListener().apply {
            add(TapDownListener { animateScaleBounceOut() })
            add(VerticalMoveListener { move(it) })
            add(TapUpListener {
                animateScaleBounceIn()
                valueChangedPublisher.onNext(valuePoint.value)
            })
            addTouchListener()?.let { add(this) }
            setOnTouchListener(this)
        }
    }

    open fun addTouchListener(): OnTouchListener? = null

    fun init(valuePoint: ValuePoint) = changeValuePoint(valuePoint)

    fun changeValuePoint(valuePoint: ValuePoint) {
        this.valuePoint = valuePoint
        position = valuePoint.position.toInt()
        positionChangingPublisher.onNext(position)
    }

    abstract fun move(dx: Float)

    protected fun translationRangeCrop(dx: Float) = rangeCrop((leftEdge() - position).toFloat(), (rightEdge() - position).toFloat(), dx)

    protected fun findValuePoint(prev: ValuePoint?, next: ValuePoint?, position: Float) {
        if (prev == null) return searchForward(next, position)
        if (next == null) return searchBackward(prev, position)
        searchBothDirections(prev, next, position)
    }

    private fun searchForward(next: ValuePoint?, position: Float) {
        if (next == null) return
        if (next.contains(position)) {
            valuePoint = next; return
        }
        searchForward(next.next, position)
    }

    private fun searchBackward(prev: ValuePoint?, position: Float) {
        if (prev == null) return
        if (prev.contains(position)) {
            valuePoint = prev; return
        }
        searchBackward(prev.prev, position)
    }

    private fun searchBothDirections(prev: ValuePoint, next: ValuePoint, position: Float) {
        if (prev.contains(position)) {
            valuePoint = prev; return
        }
        if (next.contains(position)) {
            valuePoint = next; return
        }
        findValuePoint(prev.prev, next.next, position)
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