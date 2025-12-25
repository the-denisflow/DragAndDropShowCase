package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBoundsMap
import com.example.myapplication.domain.model.TileDropZones
import com.example.myapplication.domain.model.getTilesInRow
import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.DropZoneDetectorHelper

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

class DropZoneDetectorHelperImpl: DropZoneDetectorHelper {
    override fun handleDropZoneDetectionInRow(
        centerX: Float,
        rowIndex: Int,
        currentDraggedTileIndex: Int,
        gridRowPerception: GridRowPerception,
        zones: List<TileDropZones>?,
        bounds: TileBoundsMap?,
        dragHelper: DragHelper
    ): DropResult {
        zones ?: return DropResult.Failure
        bounds ?: return DropResult.Failure

        val tilesInRow = bounds.getTilesInRow(rowIndex, gridRowPerception)
        val targetTile = tilesInRow.firstOrNull { tileIndex ->
            if (tileIndex == currentDraggedTileIndex) return@firstOrNull false

            val tileBounds = bounds[tileIndex] ?: return@firstOrNull false
            centerX in tileBounds.left..tileBounds.right
        } ?: return DropResult.Failure

        val tileZones = zones.find { it.tileIndex == targetTile } ?: return DropResult.Failure

        val zoneType = when {
            centerX in tileZones.leftSwapZone -> DropZoneType.LEFT_SWAP
            centerX in tileZones.rightSwapZone -> DropZoneType.RIGHT_SWAP
            else -> DropZoneType.NONE
        }

        when (zoneType) {
            DropZoneType.LEFT_SWAP, DropZoneType.RIGHT_SWAP -> {
                if (targetTile != currentDraggedTileIndex) {
                    dragHelper.dragShadow("reorder", currentDraggedTileIndex, targetTile)
                    return DropResult.Success(targetTile)
                }
            }

            DropZoneType.NONE -> {
                return DropResult.Empty
            }
        }
        return DropResult.Empty
    }
}