package com.jueggs.customview.rangebar

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.jueggs.andutils.extension.use
import com.jueggs.customview.rangebar.attributes.*
import com.jueggs.customview.rangebar.helper.*
import com.jueggs.customview.rangebar.view.*
import io.reactivex.Observable
import io.reactivex.disposables.*

class RangeBar : FrameLayout {
    private lateinit var attributes: Attributes
    private lateinit var bar: Bar
    private lateinit var leftThumb: Thumb
    private lateinit var rightThumb: Thumb
    private var rangeLeftEdge: Int = 0
    private var rangeRightEdge: Int = 0
    private var disposables = CompositeDisposable()

    //region constructor
    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }
    //endregion

    private fun init(context: Context, attrs: AttributeSet?) {
        attributes = AttributeStore(context, attrs, R.styleable.RangeBar).attributes
        createAndAddViews(context)

        rangeLeftEdge = leftThumb.margin

        disposables.add(leftThumb.observePositionChanging().subscribe { position ->
            bar.setLeftRange(position)
            requestLayout()
        })
        disposables.add(rightThumb.observePositionChanging().subscribe { position ->
            bar.setRightRange(position)
            requestLayout()
        })
    }

    private fun createAndAddViews(context: Context) {
        val thumbFactory = ThumbFactory(context, attributes)
        leftThumb = thumbFactory.create({ rangeLeftEdge }, { rightThumb.position }).apply { addView(this) }
        rightThumb = thumbFactory.create({ leftThumb.position }, { rangeRightEdge }).apply { addView(this) }
        bar = Bar(context, attributes, leftThumb, rightThumb).apply { addView(this, 0) }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        rangeRightEdge = width - (attributes.thumbDiameter + rightThumb.margin)
    }

    override fun onDetachedFromWindow() {
        disposables.dispose()
        super.onDetachedFromWindow()
    }

    //region API
    fun getRangeMin() = leftThumb.valuePoint.value

    fun getRangeMax() = rightThumb.valuePoint.value

    fun getTotalMin() = attributes.totalMin

    fun getTotalMax() = attributes.totalMax

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