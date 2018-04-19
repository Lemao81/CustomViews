package com.jueggs.customview.rangebar

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
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
    private var initialized = false
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
        doOnGlobalLayout { if (!initialized) initialized = init() }
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RangeBar)
        thumbAttrs = ThumbAttributes(context, a)
        barAttrs = BarAttributes(context, a, thumbAttrs)
        a.recycle()
    }

    private fun createAndAddViews(context: Context) {
        bar = Bar(context, barAttrs).apply { addView(this) }
        val thumbFactory = ThumbFactory.getInstance(thumbAttrs)
        leftThumb = thumbFactory.create(context, { rangeLeftEdge }, { rightThumb.position }).apply { addView(this) }
        rightThumb = thumbFactory.create(context, { leftThumb.position }, { rangeRightEdge }).apply { addView(this) }
    }

    private fun init(): Boolean {
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

        bar.init()
        leftThumb.init(valuePoints.single { it.value == barAttrs.rangeMin })
        rightThumb.init(valuePoints.single { it.value == barAttrs.rangeMax })

        return true
    }

    private fun createValuePoints() {
        val offset = thumbAttrs.margin.toFloat()
        val interval = (bar.width / (barAttrs.totalMax - barAttrs.totalMin)).toFloat()
        valuePoints.addFirst(ValuePoint(barAttrs.totalMin, offset, interval, null, null))

        val width = bar.width.toFloat()
        var prev = valuePoints.first
        for (i in barAttrs.totalMin + 1 until barAttrs.totalMax) {
            val position = (i - barAttrs.totalMin) * interval + offset
            valuePoints.add(ValuePoint(i, position, interval, prev, null))
            prev.next = valuePoints.last
            prev = prev.next
        }
        valuePoints.addLast(ValuePoint(barAttrs.totalMax, width, (width - prev.range.second) * 2, prev, null))
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