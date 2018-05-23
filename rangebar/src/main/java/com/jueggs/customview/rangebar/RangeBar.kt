package com.jueggs.customview.rangebar

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.jueggs.andutils.extension.use
import com.jueggs.customview.rangebar.attribute.*
import com.jueggs.customview.rangebar.helper.*
import com.jueggs.customview.rangebar.view.*
import io.reactivex.Observable
import io.reactivex.disposables.*

class RangeBar(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private lateinit var thumbAttrs: ThumbAttributes
    private lateinit var barAttrs: BarAttributes
    private lateinit var bar: Bar
    private lateinit var leftThumb: Thumb
    private lateinit var rightThumb: Thumb
    private var rangeLeftEdge: Int = 0
    private var rangeRightEdge: Int = 0
    private var disposables = CompositeDisposable()

    init {
        obtainAttributes(context, attrs)
        createAndAddViews(context)
        initialize()
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        context.withStyledAttributes(attrs, R.styleable.RangeBar) {
            thumbAttrs = ThumbAttributes(context, this)
            barAttrs = BarAttributes(context, this, thumbAttrs)
        }
    }

    private fun createAndAddViews(context: Context) {
        ThumbFactory(context, thumbAttrs).use { f ->
            leftThumb = f.create({ rangeLeftEdge }, { rightThumb.position }).apply { addView(this) }
            rightThumb = f.create({ leftThumb.position }, { rangeRightEdge }).apply { addView(this) }
        }

        bar = Bar(context, barAttrs, thumbAttrs, leftThumb, rightThumb).apply { addView(this, 0) }
    }

    private fun initialize() {
        rangeLeftEdge = thumbAttrs.margin

        disposables.add(leftThumb.observePositionChanging().subscribe { position ->
            bar.setLeftRange(position)
            requestLayout()
        })
        disposables.add(rightThumb.observePositionChanging().subscribe { position ->
            bar.setRightRange(position)
            requestLayout()
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rangeRightEdge = width - thumbAttrs.rightEdgeOffset
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables.dispose()
    }

    //region API
    fun getRangeMin() = leftThumb.valuePoint.value

    fun getRangeMax() = rightThumb.valuePoint.value

    fun getTotalMin() = barAttrs.totalMin

    fun getTotalMax() = barAttrs.totalMax

    fun setRangeMin(value: Int) {
        if (value <= getRangeMax())
            bar.valuePoints.singleOrNull { it.value == value }?.let { leftThumb.changeValuePoint(it) }
    }

    fun setRangeMax(value: Int) {
        if (value >= getRangeMin())
            bar.valuePoints.singleOrNull { it.value == value }?.let { rightThumb.changeValuePoint(it) }
    }

    fun observeMinChanging(): Observable<Int> = leftThumb.observeValueChanging().distinctUntilChanged()

    fun observeMaxChanging(): Observable<Int> = rightThumb.observeValueChanging().distinctUntilChanged()

    fun observeRangeChanging(): Observable<Pair<Int, Int>> = Observable.merge(observeMinChanging(), observeMaxChanging()).map { getRangeValues() }

    fun observeMinChanged(): Observable<Int> = leftThumb.observeValueChanged().distinctUntilChanged()

    fun observeMaxChanged(): Observable<Int> = rightThumb.observeValueChanged().distinctUntilChanged()

    fun observeRangeChanged(): Observable<Pair<Int, Int>> = Observable.merge(observeMinChanged(), observeMaxChanged()).map { getRangeValues() }
    //endregion

    private fun getRangeValues(): Pair<Int, Int> = Pair(getRangeMin(), getRangeMax())
}