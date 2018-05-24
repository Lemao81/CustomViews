package com.jueggs.customview.stackoverflowtag

import android.content.Context
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.jueggs.andutils.extension.*
import org.jetbrains.anko.*

class StackoverflowTag(context: Context, tagName: String, var rightMargin: Int = 8) : TextView(context) {

    var tagName: String = ""
        set(value) {
            field = value
            text = value
        }

    init {
        this.tagName = tagName

        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        background = context.backgroundColor(R.color.tag_background)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        leftPadding = context.dpToPixel(11)
        topPadding = context.dpToPixel(4)
        rightPadding = context.dpToPixel(11)
        bottomPadding = context.dpToPixel(5)
        textColor = context.getColorCompat(R.color.tag_text)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams<ViewGroup.MarginLayoutParams>()?.rightMargin = context.dpToPixel(rightMargin)
    }
}