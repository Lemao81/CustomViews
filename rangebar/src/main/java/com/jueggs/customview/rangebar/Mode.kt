package com.jueggs.customview.rangebar

enum class Mode(val id: Int) {
    SMOOTH(0), SNAP(1), RASTER(2);

    companion object {
        fun find(id: Int) = when (id) {
            1 -> SNAP
            2 -> RASTER
            else -> SMOOTH
        }
    }
}