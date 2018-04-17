package com.jueggs.customview.rangebar

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.jueggs.customview.rangebar.attribute.*
import com.jueggs.customview.rangebar.helper.ValueTransformer
import com.jueggs.customview.rangebar.util.doOnGlobalLayout
import com.jueggs.customview.rangebar.view.*
import io.reactivex.Observable
import io.reactivex.disposables.*

class RangeBar(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private lateinit var thumbAttrs: ThumbAttributes
    private lateinit var barAttrs: BarAttributes
    private var valueTransformer: ValueTransformer
    private var initialized = false
    private lateinit var bar: Bar
    internal lateinit var leftThumb: LeftThumb
    internal lateinit var rightThumb: RightThumb
    internal var rangeLeftEdge: Int = 0
    internal var rangeRightEdge: Int = 0
    private var disposables: CompositeDisposable? = null

    init {
        obtainAttributes(context, attrs)
        valueTransformer = ValueTransformer(barAttrs)
        createAndAddViews(context)
        doOnGlobalLayout { if (!initialized) initialized = onGlobalLayout() }
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RangeBar)
        thumbAttrs = ThumbAttributes(context, a)
        barAttrs = BarAttributes(context, a, thumbAttrs)
        a.recycle()
    }

    private fun createAndAddViews(context: Context) {
        bar = Bar(context, barAttrs).apply { addView(this) }
        leftThumb = LeftThumb(context, this, thumbAttrs, valueTransformer).apply { addView(this) }
        rightThumb = RightThumb(context, this, thumbAttrs, valueTransformer).apply { addView(this) }
    }

    private fun onGlobalLayout(): Boolean {
        valueTransformer.width = width
        val rangeMinPosition = valueTransformer.valueToPosition(barAttrs.rangeMin)
        val rangeMaxPosition = valueTransformer.valueToPosition(barAttrs.rangeMax)

        rangeRightEdge = width - thumbAttrs.rightEdgeOffset
        leftThumb.position = rangeMinPosition
        rightThumb.position = rangeMaxPosition
        bar.initWidthAndRange(width, rangeMinPosition, rangeMaxPosition)

        disposables = CompositeDisposable()
        disposables?.add(leftThumb.observePositionChanging().subscribe { position ->
            bar.setLeftRange(position)
            requestLayout()
        })
        disposables?.add(rightThumb.observePositionChanging().subscribe { position ->
            bar.setRightRange(position)
            requestLayout()
        })

        return true
    }

    fun observeMinChanging(): Observable<Int> = leftThumb.observeValueChanging().distinctUntilChanged()

    fun observeMaxChanging(): Observable<Int> = rightThumb.observeValueChanging().distinctUntilChanged()

    fun observeRangeChanging(): Observable<Pair<Int, Int>> = Observable.merge(observeMinChanging(), observeMaxChanging()).map { getRangeValues() }

    fun observeMinChanged(): Observable<Int> = leftThumb.observeValueChanged().distinctUntilChanged()

    fun observeMaxChanged(): Observable<Int> = rightThumb.observeValueChanged().distinctUntilChanged()

    fun observeRangeChanged(): Observable<Pair<Int, Int>> = Observable.merge(observeMinChanged(), observeMaxChanged()).map { getRangeValues() }

    private fun getRangeValues(): Pair<Int, Int> = Pair(leftThumb.value, rightThumb.value)

    fun getMin() = leftThumb.value

    fun getMax() = rightThumb.value

    fun setMin(value: Int) {
        if (getMax() < value) return
        val position = valueTransformer.valueToPosition(value)
        leftThumb.position = position
        bar.setLeftRange(position)
        requestLayout()
        leftThumb.valueChangedPublisher.onNext(getMin())
    }

    fun setMax(value: Int) {
        if (getMin() > value) return
        val position = valueTransformer.valueToPosition(value)
        rightThumb.position = position
        bar.setRightRange(position)
        requestLayout()
        rightThumb.valueChangedPublisher.onNext(getMax())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables?.dispose()
    }
}