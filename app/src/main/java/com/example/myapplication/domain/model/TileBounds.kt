package com.example.myapplication.domain.model

import com.example.myapplication.domain.repository.Bounds

private const val DROP_ZONE_LEFT_PERCENT = 0.2f
private const val DROP_ZONE_RIGHT_PERCENT = 0.8f

data class TileBounds(
    override val top: Float,
    override val bottom: Float,
    override val right: Float,
    override val left: Float
) : Bounds {
    /**
     * Checks if both X and Y coordinates fall within this tile's bounds.
     */
    fun contains(x: Float, y: Float): Boolean =
        x in left..right && y in top..bottom
}

/**
 * Represents the bounds of an entire row in the launcher grid.
 * Spans the full horizontal width of the grid.
 */
data class RowBounds(
    override val top: Float,
    override val bottom: Float,
    override val left: Float,
    override val right: Float
) : Bounds

typealias TileIndex = Int

/**
 * Calculates drop zones for all tiles in the grid.
 *
 * ## Calculation Formula:
 * For a tile with bounds `[left, right]`
 * ```
 * width = right - left
 * leftThreshold = left + (width * 0.2)
 * rightThreshold = left + (width * 0.8)
 *
 * Left zone: [left, leftThreshold]
 * Center zone: [leftThreshold, rightThreshold]
 * Right zone: [rightThreshold, right]
 * ```
 */
fun Map<TileIndex, TileBounds>.getTileDropZones(): List<TileDropZones> {
    return this.map { (tileIndex, bounds) ->
        val width = bounds.right - bounds.left
        val leftThreshold = bounds.left + (width * DROP_ZONE_LEFT_PERCENT)
        val rightThreshold = bounds.left + (width * DROP_ZONE_RIGHT_PERCENT)

        TileDropZones(
            tileIndex = tileIndex,
            leftSwapZone = bounds.left..leftThreshold,
            centerFolderZone = leftThreshold..rightThreshold,
            rightSwapZone = rightThreshold..bounds.right
        )
    }
}

/**
 * Retrieves all tile indices in a specific row, sorted left to right.
 *
 * ## Example:
 * ```
 * Grid Layout (3 columns):
 * Row 0: [Tile 0] [Tile 1] [Tile 2]
 * Row 1: [Tile 3] [Tile 4] [Tile 5]
 *
 * tileBounds.getTilesInRow(0, perception) // Returns: [0, 1, 2]
 * tileBounds.getTilesInRow(1, perception) // Returns: [3, 4, 5]
 * ``
 **/
fun Map<TileIndex, TileBounds>.getTilesInRow(rowIndex: Int, gridRowPerception: GridRowPerception?): List<Int> {
    // make sure the center is contained in the given row
    val tilesInRow = this.filter { (_, bounds) ->
        gridRowPerception?.getRowIndexForY(bounds.centerY) == rowIndex
    }.keys.toMutableList()

    tilesInRow.sortBy { tileIndex ->
        this[tileIndex]?.centerX ?: 0f
    }
    return tilesInRow
}