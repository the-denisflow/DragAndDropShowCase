package com.example.myapplication.domain.model

import androidx.compose.runtime.snapshots.SnapshotStateMap

/**
 * Represents the vertical bounds of a tile in the launcher grid.
 */
data class TileBounds(
    val top: Float,
    val bottom: Float,
    val right: Float,
    val left: Float
) {
    /**
     * Checks if a Y coordinate falls within this tile's bounds.
     */
    operator fun contains(y: Float): Boolean = y in top..bottom
}

typealias TileIndex = Int
typealias TileBoundsMap = SnapshotStateMap<TileIndex, TileBounds>