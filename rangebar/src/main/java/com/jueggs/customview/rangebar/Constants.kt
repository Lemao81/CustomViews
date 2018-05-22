package com.jueggs.customview.rangebar

import android.content.Context

const val DEFAULT_THUMB_DIAMETER = 36
const val DEFAULT_BAR_HEIGHT = 8
const val DEFAULT_BAR_RADIUS = 10
const val DEFAULT_TOTAL_MIN = 0
const val DEFAULT_TOTAL_MAX = 100

const val MODE_SMOOTH = 0
const val MODE_SNAP = 1
const val MODE_RASTER = 2

abstract class ContextDisposable(protected var context: Context?) : Disposable {
    init {
        checkNotNull(context)
    }

    override fun dispose() {
        context = null
    }
}

interface Disposable {
    fun dispose()
}

inline fun <T : Disposable?, R> T.use(block: (T) -> R): R {
    var exception: Throwable? = null
    try {
        return block(this)
    } catch (e: Throwable) {
        exception = e
        throw e
    } finally {
        when {
            this == null -> {
            }
            exception == null -> dispose()
            else ->
                try {
                    dispose()
                } catch (disposeException: Throwable) {
                    // cause.addSuppressed(disposeException) // ignored here
                }
        }
    }
}