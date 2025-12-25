package com.example.myapplication.domain.model

import androidx.compose.runtime.snapshots.SnapshotStateMap

/**
 * Represents the vertical bounds of a tile in the launcher grid.
 */
data class TileBounds(
    val top: Float,
    val bottom: Float,
    val right: Float,
    val left: Float,
    val centerY: Float = (top + bottom) / 2f,
    val centerX: Float = (left + right) / 2f
) {
    /**
     * Checks if a Y coordinate falls within this tile's bounds.
     */
    operator fun contains(y: Float): Boolean = y in top..bottom

}

typealias TileIndex = Int
typealias TileBoundsMap = SnapshotStateMap<TileIndex, TileBounds>

fun TileBoundsMap.getTileDropZones(): List<TileDropZones> {
    return this.map { (tileIndex, bounds) ->
        val width = bounds.right - bounds.left
        val leftThreshold = bounds.left + (width * 0.2f)
        val rightThreshold = bounds.left + (width * 0.8f)

        TileDropZones(
            tileIndex = tileIndex,
            leftSwapZone = bounds.left..leftThreshold,
            centerFolderZone = leftThreshold..rightThreshold,
            rightSwapZone = rightThreshold..bounds.right
        )
    }
}

fun TileBoundsMap.getTilesInRow(rowIndex: Int, gridRowPerception: GridRowPerception?): List<Int> {
    val tilesInRow = this.filter { (_, bounds) ->
        gridRowPerception?.getRowIndexForY(bounds.centerY) == rowIndex
    }.keys.toMutableList()

    tilesInRow.sortBy { tileIndex ->
        this[tileIndex]?.centerX ?: 0f
    }
    return tilesInRow
}