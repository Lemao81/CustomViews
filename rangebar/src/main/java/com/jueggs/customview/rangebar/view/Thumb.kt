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
import com.jueggs.customview.rangebar.helper.ValueTransformer

abstract class Thumb(context: Context, private val attrs: ThumbAttributes, private val valueTransformer: ValueTransformer) : View(context) {
    private var positionChangingPublisher: Subject<Int> = PublishSubject.create()
    private var valueChangingPublisher: Subject<Int> = PublishSubject.create()
    internal val valueChangedPublisher: Subject<Int> = PublishSubject.create()
    private var paint: Paint = Paint().apply { color = attrs.color;style = Paint.Style.FILL }

    var position: Int
        get() = (layoutParams as FrameLayout.LayoutParams).leftMargin
        set(value) {
            (layoutParams as FrameLayout.LayoutParams).leftMargin = value
            this.value = valueTransformer.positionToValue(value)
        }

    var value: Int = 0
        set(value) {
            field = value
            valueChangingPublisher.onNext(value)
        }

    init {
        layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        CompoundTouchListener().apply {
            add(TapDownListener { animateScaleBounceOut() })
            add(VerticalMoveListener { move(it.toInt()) })
            add(TapUpListener { animateScaleBounceIn() })
            add(TapUpListener { valueChangedPublisher.onNext(value) })
            setOnTouchListener(this)
        }
    }

    private fun move(dx: Int) {
        position = rangeCrop(bottomLimit(), topLimit(), position + dx)
        positionChangingPublisher.onNext(position)
    }

    abstract fun bottomLimit(): Int
    abstract fun topLimit(): Int

    fun observePositionChanging(): Observable<Int> = positionChangingPublisher

    fun observeValueChanging(): Observable<Int> = valueChangingPublisher

    fun observeValueChanged(): Observable<Int> = valueChangedPublisher

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = when {
            isInEditMode -> context.dpToPixel(DEFAULT_THUMB_DIAMETER)
            else -> attrs.diameter + 2 * attrs.margin
        }
        measureView(widthMeasureSpec, heightMeasureSpec, size, size, this::setMeasuredDimension)
    }

    override fun onDraw(canvas: Canvas) {
        if (isInEditMode) {
            val centerXY = context.dpToPixel(DEFAULT_THUMB_DIAMETER).toFloat()
            canvas.drawCircle(centerXY, centerXY, centerXY, paint)
        } else {
            val centerXY = attrs.margin + attrs.radiusF
            canvas.drawCircle(centerXY, centerXY, attrs.radiusF, paint)
        }
    }
}