package com.jueggs.customview.stackoverflowtag

import android.content.Context
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.jueggs.andutils.extension.*
import org.jetbrains.anko.*

class StackoverflowTag(context: Context, tagName: String, private val size: Size = Size.MEDIUM) : TextView(context) {

    var tagName: String = tagName
        set(value) {
            field = value
            text = value
        }

    var rightMargin: Int = when (size) {
        Size.SMALL -> 4
        Size.MEDIUM -> 6
        Size.BIG -> 8
    }

    init {
        text = tagName
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        background = context.backgroundColor(R.color.tag_background)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, getSizeOfText())
        leftPadding = context.dpToPixel(getLateralPadding())
        topPadding = context.dpToPixel(getTopPadding())
        rightPadding = context.dpToPixel(getLateralPadding())
        bottomPadding = context.dpToPixel(getBottomPadding())
        textColor = context.getColorCompat(R.color.tag_text)
    }

    private fun getSizeOfText(): Float = when (size) {
        Size.SMALL -> 14f
        Size.MEDIUM -> 16f
        Size.BIG -> 17f
    }

    private fun getLateralPadding(): Int = when (size) {
        Size.SMALL -> 9
        Size.MEDIUM -> 11
        Size.BIG -> 12
    }

    private fun getTopPadding(): Int = when (size) {
        Size.SMALL -> 2
        Size.MEDIUM -> 4
        Size.BIG -> 5
    }

    private fun getBottomPadding(): Int = when (size) {
        Size.SMALL -> 3
        Size.MEDIUM -> 5
        Size.BIG -> 6
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams<ViewGroup.MarginLayoutParams>()?.rightMargin = context.dpToPixel(rightMargin)
    }
}