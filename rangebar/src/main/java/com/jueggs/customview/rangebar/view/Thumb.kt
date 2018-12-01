package com.jueggs.customview.rangebar.view

import android.content.Context
import android.graphics.*
import android.view.View
import android.widget.FrameLayout
import io.reactivex.Observable
import io.reactivex.subjects.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.jueggs.andutils.callback.*
import com.jueggs.andutils.extension.*
import com.jueggs.customview.rangebar.RangebarConstants.THUMB_BOUNCE_SCALE
import com.jueggs.customview.rangebar.attributes.Attributes
import com.jueggs.customview.rangebar.helper.*
import com.jueggs.customviewutils.CvUtil.measureView
import com.jueggs.jutils.Util.cropToRange

internal abstract class Thumb(context: Context, private val attributes: Attributes, private var leftEdge: () -> Int, private var rightEdge: () -> Int) : View(context) {
    private var paint: Paint = Paint()
    private var compoundTouchListener: CompoundTouchListener
    private val valueChangedPublisher: Subject<Int> = PublishSubject.create()
    protected var valuePointFinder = ValuePointFinder()
    protected var positionChangingPublisher: Subject<Int> = PublishSubject.create()
    protected var valueChangingPublisher: Subject<Int> = PublishSubject.create()
    val radius = attributes.thumbDiameter / 2f
    val margin = ((attributes.thumbDiameter * THUMB_BOUNCE_SCALE - attributes.thumbDiameter) / 2).toInt()

    var position: Int
        get() = layoutParams.asMarginLayoutParams().leftMargin
        set(value) {
            layoutParams.asMarginLayoutParams().leftMargin = value
            layoutParams = layoutParams
        }

    open var valuePoint: ValuePoint = ValuePoint.EMPTY
        set(value) {
            field = value
            valueChangingPublisher.onNext(value.value)
        }

    init {
        with(paint) {
            color = attributes.thumbColor
            style = Paint.Style.FILL
        }

        layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            setMargins(margin, margin, margin, margin)
        }

        compoundTouchListener = CompoundTouchListener().apply {
            add(TapDownListener { animateScaleBounceOut(scale = THUMB_BOUNCE_SCALE) })
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = measureView(widthMeasureSpec, heightMeasureSpec, attributes.thumbDiameter, attributes.thumbDiameter, this::setMeasuredDimension)

    override fun onDraw(canvas: Canvas) = canvas.drawCircle(radius, radius, radius, paint)
}