package com.example.myapplication.domain.repository

/**
 * Common interface for rectangular bounds in the grid system.
 */
interface Bounds {
    val top: Float
    val bottom: Float
    val left: Float
    val right: Float

    val centerY: Float get() = (top + bottom) / 2f
    val centerX: Float get() = (left + right) / 2f

    /**
     * Checks if a Y coordinate falls within the vertical bounds.
     */
    operator fun contains(y: Float): Boolean = y in top..bottom
}