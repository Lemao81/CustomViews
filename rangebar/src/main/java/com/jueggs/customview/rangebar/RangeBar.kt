package com.jueggs.customview.rangebar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import com.jueggs.customview.rangebar.R.attr.*
import com.jueggs.customview.rangebar.attribute.*
import io.reactivex.Observable

class RangeBar(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    private lateinit var thumbAttrs: ThumbAttributes
    private lateinit var barAttrs: BarAttributes
    private lateinit var bar: Bar
    private lateinit var leftThumb: LeftThumb
    private lateinit var rightThumb: RightThumb
    private lateinit var valueTransformer: ValueTransformer

    init {
        obtainAttributes(context, attrs)
        createViews(context)

        addView(bar)
        addView(leftThumb)
        addView(rightThumb)

        viewTreeObserver.addOnGlobalLayoutListener {
            rightThumb.positionRightEdge = width - thumbDiameter
            valueTransformer = ValueTransformer(width, barAttrs.totalMin, barAttrs.totalMax)
        }
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RangeBar)
        thumbAttrs = ThumbAttributes(context, a)
        barAttrs = BarAttributes(context, a, thumbAttrs.diameter)
        a.recycle()
    }

    private fun createViews(context: Context) {
        bar = Bar(context, barAttrs).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                leftMargin = thumbAttrs.radius
                rightMargin = thumbAttrs.radius
                gravity = Gravity.CENTER_VERTICAL
            }
        }
        leftThumb = LeftThumb(context, thumbAttrs).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.NO_GRAVITY
            }
        }
        rightThumb = RightThumb(context, thumbAttrs).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.NO_GRAVITY
            }
        }

        leftThumb.observePosition().subscribe { position ->
            rightThumb.positionLeftThumb = position
            bar.setLeftRange(position)
        }
        rightThumb.observePosition().subscribe { position ->
            leftThumb.positionRightThumb = position
            bar.setRightRange(position)
        }
    }

    fun observeMin(): Observable<Int> = leftThumb.observePosition()
            .map(valueTransformer::positionToValue)
            .distinct()

    fun observeMax(): Observable<Int> = rightThumb.observePosition()
            .map(valueTransformer::positionToValue)
            .distinct()

    fun observeRange(): Observable<Pair<Int, Int>> = Observable.merge(observeMin(), observeMax())
            .map { Pair(valueTransformer.positionToValue(leftThumb.position), valueTransformer.positionToValue(rightThumb.position)) }
}