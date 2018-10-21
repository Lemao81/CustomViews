package com.jueggs.customview.stackoverflowtag

import android.content.Context
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.jueggs.andutils.extension.*
import org.jetbrains.anko.*

class StackoverflowTag(context: Context, tagName: String, size: Int = 2) : TextView(context) {

    var tagName: String = tagName
        set(value) {
            field = value
            text = value
        }

    var rightMargin: Int = 3 + size

    init {
        text = tagName
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        background = context.backgroundColor(R.color.tag_background)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 9f + size)
        leftPadding = context.dpToPixel(4 + size)
        topPadding = context.dpToPixel(1 + size / 2)
        rightPadding = context.dpToPixel(4 + size)
        bottomPadding = context.dpToPixel(2 + size / 2)
        textColor = context.getColorCompat(R.color.tag_text)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams<ViewGroup.MarginLayoutParams>()?.rightMargin = context.dpToPixel(rightMargin)
    }
}