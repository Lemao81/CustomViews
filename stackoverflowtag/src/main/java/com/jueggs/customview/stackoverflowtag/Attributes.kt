package com.jueggs.customview.stackoverflowtag

import android.content.Context
import android.content.res.TypedArray
import java.security.InvalidParameterException

class Attributes(context: Context, a: TypedArray) {
    val size = when (a.getInteger(R.styleable.StackoverflowTag_size, 1)) {
        0 -> Size.SMALL
        1 -> Size.MEDIUM
        2 -> Size.BIG
        else -> throw InvalidParameterException("Allowed values for size attribute are 0 (small), 1 (medium), 2 (big)")
    }
}