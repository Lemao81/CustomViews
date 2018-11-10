package com.jueggs.customview.rangebar.view

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.FrameLayout
import com.jueggs.customview.rangebar.attribute.ThumbAttributes
import io.reactivex.Observable
import io.reactivex.subjects.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.*
import com.jueggs.andutils.callback.*
import com.jueggs.andutils.extension.*
import com.jueggs.customview.rangebar.helper.*
import com.jueggs.customviewutils.measureView
import com.jueggs.jutils.cropToRange

abstract class Thumb(context: Context, private val attrs: ThumbAttributes, private var leftEdge: () -> Int, private var rightEdge: () -> Int) : View(context) {
    private var paint: Paint = Paint().apply { color = attrs.color; style = Paint.Style.FILL }
    private var compoundTouchListener: CompoundTouchListener
    protected var valuePointFinder = ValuePointFinder()
    protected var positionChangingPublisher: Subject<Int> = PublishSubject.create()
    protected var valueChangingPublisher: Subject<Int> = PublishSubject.create()
    private val valueChangedPublisher: Subject<Int> = PublishSubject.create()

    var position: Int
        get() = layoutParams<FrameLayout.LayoutParams>()?.leftMargin ?: 0
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
        valueChangedPublisher.onNext(valuePoint.value)
    }

    abstract fun move(dx: Float)

    protected fun translationRangeCrop(dx: Float) = cropToRange((leftEdge() - position).toFloat(), (rightEdge() - position).toFloat(), dx)

    fun observePositionChanging(): Observable<Int> = positionChangingPublisher

    fun observeValueChanging(): Observable<Int> = valueChangingPublisher

    fun observeValueChanged(): Observable<Int> = valueChangedPublisher

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = measureView(widthMeasureSpec, heightMeasureSpec, attrs.diameter, attrs.diameter, this::setMeasuredDimension)

    override fun onDraw(canvas: Canvas) = canvas.drawCircle(attrs.radiusF, attrs.radiusF, attrs.radiusF, paint)
}