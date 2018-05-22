package com.jueggs.customview.rangebar

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.jueggs.customview.rangebar.attribute.*
import com.jueggs.customview.rangebar.helper.*
import com.jueggs.customview.rangebar.util.doOnGlobalLayout
import com.jueggs.customview.rangebar.view.*
import io.reactivex.Observable
import io.reactivex.disposables.*
import java.util.*

class RangeBar(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private lateinit var thumbAttrs: ThumbAttributes
    private lateinit var barAttrs: BarAttributes
    private lateinit var bar: Bar
    private lateinit var leftThumb: Thumb
    private lateinit var rightThumb: Thumb
    private var rangeLeftEdge: Int = 0
    private var rangeRightEdge: Int = 0
    private var disposables: CompositeDisposable? = null
    private var valuePoints: LinkedList<ValuePoint> = LinkedList()

    init {
        obtainAttributes(context, attrs)
        createAndAddViews(context)
        doOnGlobalLayout(::initialize)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        context.withStyledAttributes(attrs, R.styleable.RangeBar) {
            thumbAttrs = ThumbAttributes(context, this)
            barAttrs = BarAttributes(context, this, thumbAttrs)
        }
    }

    private fun createAndAddViews(context: Context) {
        bar = Bar(context, barAttrs).apply { addView(this) }
        ThumbFactory(context, thumbAttrs).use { f ->
            leftThumb = f.create({ rangeLeftEdge }, { rightThumb.position }).apply { addView(this) }
            rightThumb = f.create({ leftThumb.position }, { rangeRightEdge }).apply { addView(this) }
        }
    }

    private fun initialize() {
        createValuePoints()

        rangeLeftEdge = thumbAttrs.margin
        rangeRightEdge = width - thumbAttrs.rightEdgeOffset

        disposables = CompositeDisposable()
        disposables?.add(leftThumb.observePositionChanging().subscribe { position ->
            bar.setLeftRange(position)
            requestLayout()
        })
        disposables?.add(rightThumb.observePositionChanging().subscribe { position ->
            bar.setRightRange(position)
            requestLayout()
        })

        bar.initialize()
        leftThumb.initialize(valuePoints.single { it.value == barAttrs.rangeMin })
        rightThumb.initialize(valuePoints.single { it.value == barAttrs.rangeMax })
    }

    private fun createValuePoints() {
        val width = bar.width.toFloat()
        val offset = thumbAttrs.margin.toFloat()
        val steps = barAttrs.totalMax - barAttrs.totalMin
        val interval = width / steps

        for (i in 0 until steps) {
            val value = i + barAttrs.totalMin
            val position = offset + i * interval
            valuePoints.add(ValuePoint(value, position, interval, valuePoints))
        }

        val lastPosition = offset + width
        val lastInterval = (lastPosition - valuePoints.last.range.second) * 2
        valuePoints.addLast(ValuePoint(barAttrs.totalMax, lastPosition, lastInterval, valuePoints))
    }

    fun getMin() = leftThumb.valuePoint.value

    fun getMax() = rightThumb.valuePoint.value

    fun setMin(value: Int) {
        if (getMax() < value) return
        val valuePoint = valuePoints.singleOrNull { it.value == value }
        if (valuePoint != null) {
            leftThumb.changeValuePoint(valuePoint)
            leftThumb.valueChangedPublisher.onNext(valuePoint.value)
        }
    }

    fun setMax(value: Int) {
        if (getMin() > value) return
        val valuePoint = valuePoints.singleOrNull { it.value == value }
        if (valuePoint != null) {
            rightThumb.changeValuePoint(valuePoint)
            rightThumb.valueChangedPublisher.onNext(valuePoint.value)
        }
    }

    fun observeMinChanging(): Observable<Int> = leftThumb.observeValueChanging().distinctUntilChanged()

    fun observeMaxChanging(): Observable<Int> = rightThumb.observeValueChanging().distinctUntilChanged()

    fun observeRangeChanging(): Observable<Pair<Int, Int>> = Observable.merge(observeMinChanging(), observeMaxChanging()).map { getRangeValues() }

    fun observeMinChanged(): Observable<Int> = leftThumb.observeValueChanged().distinctUntilChanged()

    fun observeMaxChanged(): Observable<Int> = rightThumb.observeValueChanged().distinctUntilChanged()

    fun observeRangeChanged(): Observable<Pair<Int, Int>> = Observable.merge(observeMinChanged(), observeMaxChanged()).map { getRangeValues() }

    private fun getRangeValues(): Pair<Int, Int> = Pair(getMin(), getMax())

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables?.dispose()
    }
}