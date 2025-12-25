package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBoundsMap
import com.example.myapplication.domain.model.TileDropZones
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

class DropZoneDetectorHelperImpl(
    val dragHelper: DragHelper,
    val logger: AppLogger,
): DropZoneDetectorHelper {
    private val STATE_TAG = "DropZoneDetectorHelperImpl"
    private var gridRowPerception: GridRowPerception? = null
    private var zones: List<TileDropZones>? = null
    private var bounds: TileBoundsMap? = null

    override fun initialize(
        gridRowPerception: GridRowPerception,
        zones: List<TileDropZones>,
        bounds: TileBoundsMap
    ) {
        this.gridRowPerception = gridRowPerception
        this.zones = zones
        this.bounds = bounds
    }

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

        val tilesInRow = currentBounds.getTilesInRow(rowIndex, gridRowPerception)
        val targetTile = tilesInRow.firstOrNull { tileIndex ->
            if (tileIndex == currentDraggedTileIndex) return@firstOrNull false
            val tileBounds = currentBounds[tileIndex] ?: return@firstOrNull false
            centerX in tileBounds.left..tileBounds.right
        } ?: return DropResult.Failure

        val tileZones = currentZones.find { it.tileIndex == targetTile } ?: return DropResult.Failure

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