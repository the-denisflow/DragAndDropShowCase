package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileDropZones
import com.example.myapplication.domain.model.TileIndex
import com.example.myapplication.domain.model.getTilesInRow
import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.DropZoneDetectorHelper
import com.example.myapplication.shared.utils.AppLogger

enum class DropZoneType {
    LEFT_SWAP,
    RIGHT_SWAP,
    NONE
}

sealed class DropResult(){
    class Success(val index: Int): DropResult()
    object Empty: DropResult()
    object Failure: DropResult()
}

/**
 * Detects which drop zone a dragged tile is over and triggers the
 * appropriate action.
 *
 * Must be initialized with [initialize] before use.
 */
class DropZoneDetectorHelperImpl(
    val dragHelper: DragHelper,
    val logger: AppLogger,
): DropZoneDetectorHelper {
    private val STATE_TAG = "DropZoneDetectorHelperImpl"
    private var gridRowPerception: GridRowPerception? = null
    private var zones: List<TileDropZones>? = null
    private var bounds: Map<TileIndex, TileBounds>? = null

    /**
     * Initializes the detector with grid layout information.
     * Must be called before [handleDropZoneDetectionInRow].
     *
     * @param gridRowPerception Grid row bounds for determining which row contains a Y-coordinate
     * @param zones Pre-calculated drop zones for all tiles
     * @param bounds Pre-calculated bounds for all tiles
     */
    override fun initialize(
        gridRowPerception: GridRowPerception,
        zones: List<TileDropZones>,
        bounds: Map<TileIndex, TileBounds>
    ) {
        this.gridRowPerception = gridRowPerception
        this.zones = zones
        this.bounds = bounds
    }
/**
 * Detects drop zone and executes swap if needed.
 *
 * @param centerX X-coordinate of dragged tile's center
 * @param rowIndex Which row to check
 *
 * @return [DropResult.Success] with new index if swapped,
 *         [DropResult.Empty] if no action,
 *         [DropResult.Failure] if error
 */
override fun handleDropZoneDetectionInRow(
        centerX: Float,
        rowIndex: Int,
        currentDraggedTileIndex: Int,
    ): DropResult {
        val currentZones  = zones ?: run {
            return DropResult.Failure
        }
        val currentBounds = bounds ?: run {
           return DropResult.Failure
        }
        // get the index of the tiles that are present in the row
        val tilesInRow = currentBounds.getTilesInRow(rowIndex, gridRowPerception)
        // find the tile that the current X coordinate is hoovering on
        val targetTile = tilesInRow.firstOrNull { tileIndex ->
            // skip if it is the tiles being dragged himself
            if (tileIndex == currentDraggedTileIndex) return@firstOrNull false
           // make sure that the X coordinate is within the bounds of the tile
            val tileBounds = currentBounds[tileIndex] ?: return@firstOrNull false
            centerX in tileBounds.left..tileBounds.right
        } ?: return DropResult.Failure

        // once the index of the tile hoovered on is detected
        // we retrieve the zones
        val tileZones = currentZones.find { it.tileIndex == targetTile } ?: return DropResult.Failure

        // we see if the X coordinate is within which zone
        val zoneType = when (centerX) {
            in tileZones.leftSwapZone -> DropZoneType.LEFT_SWAP
            in tileZones.rightSwapZone -> DropZoneType.RIGHT_SWAP
            else -> DropZoneType.NONE
        }

        when (zoneType) {
            DropZoneType.LEFT_SWAP,
            DropZoneType.RIGHT_SWAP -> {
                if (targetTile != currentDraggedTileIndex) {
                    dragHelper.dragShadow("reorder", currentDraggedTileIndex, targetTile)
                    logger.info(STATE_TAG, "reorder called from $currentDraggedTileIndex to $targetTile")
                    return DropResult.Success(targetTile)
                }
            }

            DropZoneType.NONE -> {
                logger.info(STATE_TAG, "none called")
                return DropResult.Empty
            }
        }
            logger.info(STATE_TAG, "none called")
             return DropResult.Empty
    }
}