package com.jueggs.customview.rangebar.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.jueggs.customview.rangebar.*
import com.jueggs.customview.rangebar.attribute.*
import com.jueggs.customview.rangebar.helper.ValueTransformer
import io.reactivex.Observable
import io.reactivex.disposables.*

class RangeBar(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private lateinit var thumbAttrs: ThumbAttributes
    private lateinit var barAttrs: BarAttributes
    private lateinit var bar: Bar
    private lateinit var leftThumb: LeftThumb
    private lateinit var rightThumb: RightThumb
    private lateinit var valueTransformer: ValueTransformer
    private var disposables: CompositeDisposable? = null
    private var initialized = false

    init {
        obtainAttributes(context, attrs)
        createViews(context)

        addView(bar)
        addView(leftThumb)
        addView(rightThumb)

        viewTreeObserver.addOnGlobalLayoutListener { if (!initialized) initialized = init() }
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RangeBar)
        thumbAttrs = ThumbAttributes(context, a)
        barAttrs = BarAttributes(context, a, thumbAttrs.diameter)
        a.recycle()
    }

    private fun createViews(context: Context) {
        bar = Bar(context, barAttrs).apply {
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                leftMargin = thumbAttrs.radius
                rightMargin = thumbAttrs.radius
                gravity = Gravity.CENTER_VERTICAL
            }
        }
        leftThumb = LeftThumb(context, thumbAttrs).apply { layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) }
        rightThumb = RightThumb(context, thumbAttrs).apply { layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) }
    }

    private fun init(): Boolean {
        valueTransformer = ValueTransformer(width, barAttrs)
        val rangeMinPosition = valueTransformer.valueToPosition(barAttrs.rangeMin)
        val rangeMaxPosition = valueTransformer.valueToPosition(barAttrs.rangeMax)

        leftThumb.init(rangeMinPosition)
        rightThumb.init(rangeMaxPosition, rightEdge = width - thumbAttrs.diameter)
        bar.init(width, rangeMinPosition, rangeMaxPosition)

        disposables = CompositeDisposable()
        disposables?.add(leftThumb.observe().subscribe { position ->
            rightThumb.positionLeftThumb = position
            bar.setLeftRange(position)
            invalidate()
        })
        disposables?.add(rightThumb.observe().subscribe { position ->
            leftThumb.positionRightThumb = position
            bar.setRightRange(position)
            invalidate()
        })

        return true
    }

    fun observeMin(): Observable<Int> = leftThumb.observe()
            .map(valueTransformer::positionToValue)
            .distinct()

    fun observeMax(): Observable<Int> = rightThumb.observe()
            .map(valueTransformer::positionToValue)
            .distinct()

    fun observeRange(): Observable<Pair<Int, Int>> = Observable.merge(observeMin(), observeMax())
            .map {
                val rangeMinValue = valueTransformer.positionToValue(leftThumb.position)
                val rangeMaxValue = valueTransformer.positionToValue(rightThumb.position)
                Pair(rangeMinValue, rangeMaxValue)
            }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables?.dispose()
    }
}