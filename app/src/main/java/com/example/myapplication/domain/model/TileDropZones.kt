package com.example.myapplication.domain.model

/**
 * Represents the three horizontal drop zones within a single
 * tile for drag and drop interactions.
 *
 * @param tileIndex The index of the tile within the grid.
 * @param leftSwapZone The range of X coordinates representing the left swap zone.
 * @param centerFolderZone The range of X coordinates representing the center folder zone.
 * @param rightSwapZone The range of X coordinates representing the right swap zone.
 */
data class TileDropZones(
    val tileIndex: Int,
    val leftSwapZone: ClosedFloatingPointRange<Float>,
    val centerFolderZone: ClosedFloatingPointRange<Float>,
    val rightSwapZone: ClosedFloatingPointRange<Float>
)