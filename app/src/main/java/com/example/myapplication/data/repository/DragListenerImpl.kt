package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.DragIndexState
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBoundsMap
import com.example.myapplication.domain.model.TileDropZones
import com.example.myapplication.domain.model.getTileDropZones
import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.DragListener
import com.example.myapplication.presentation.utils.ListValues
import com.example.myapplication.shared.utils.AppLogger
import javax.inject.Inject

class DragListenerImpl @Inject constructor(
    val logger: AppLogger,
    val dragHelper: DragHelper
): DragListener {
    companion object {
        private val STATE_TAG = "DragListenerImpl"
    }

    private var listTilesBounds: TileBoundsMap? = null
        set(value) {
            if (field != value) {
                field = value
                logger.info(STATE_TAG, "List tiles bounds updated")
                _dropTileZones = value?.getTileDropZones()
            }
        }

    private var _dropTileZones: List<TileDropZones>? = null
    private val dropTileZones: List<TileDropZones>?
        get() = _dropTileZones


    private var currentDraggedTileIndex: Int? = null

    private var gridRowPerception : GridRowPerception? = null
    private var draggedTileTouchedHalfTop: Boolean? = null


    override fun onDragStart() {
        currentDraggedTileIndex = null
    }

    override fun onDrag(
        x: Float,
        y: Float,
        listBound: TileBoundsMap?,
        indexTileBeingDragged: DragIndexState,
        offsetFromCenter: Pair<Float, Float>
    ) {
       if (indexTileBeingDragged is DragIndexState.Dragging) {
           logger.info(STATE_TAG, "Dragging at x: $x, y: $y")

        if (currentDraggedTileIndex == null) {
            currentDraggedTileIndex = indexTileBeingDragged.index
        }
           listTilesBounds = listBound
           val centerX = x - offsetFromCenter.first
           val centerY = y - offsetFromCenter.second
              when (gridRowPerception?.getRowIndexForY(
                  centerY
               )) {
                   0 -> {

                       logger.info(STATE_TAG, "currentRow : 0")
                       handleDropZoneDetectionInRow(centerX, 0, currentDraggedTileIndex!!,
                           gridRowPerception!!
                       )


                   }

                   1 -> {
                       logger.info(STATE_TAG, "currentRow : 1")
                       handleDropZoneDetectionInRow(centerX, 1, currentDraggedTileIndex!!,
                           gridRowPerception!!
                       )
                   }
               }

       } else {
           logger.warning(STATE_TAG, "drag is not being initialized")
           return
       }
    }

    override fun onDragEnded() {
        currentDraggedTileIndex = null
    }

    override fun initializeListPerception(gridRowPerception: GridRowPerception?) {
       this.gridRowPerception  = gridRowPerception
    }

    private fun handleDropZoneDetectionInRow(
        centerX: Float,
        rowIndex: Int,
        currentDraggedTileIndex: Int,
        gridRowPerception: GridRowPerception

    ) {
        val zones = dropTileZones ?: return
        val bounds = listTilesBounds ?: return


        val tilesInRow = getTilesInRow(rowIndex, bounds, gridRowPerception)

        logger.info(STATE_TAG, "Tiles in row $rowIndex: $tilesInRow")

        val targetTile = tilesInRow.firstOrNull { tileIndex ->
            if (tileIndex == currentDraggedTileIndex) return@firstOrNull false

            val tileBounds = bounds[tileIndex] ?: return@firstOrNull false
            centerX in tileBounds.left..tileBounds.right
        } ?: return

        logger.info(STATE_TAG, "Hovering over tile $targetTile in row $rowIndex")


        val tileZones = zones.find { it.tileIndex == targetTile } ?: return

        val zoneType = when {
            centerX in tileZones.leftSwapZone -> DropZoneType.LEFT_SWAP
            centerX in tileZones.centerFolderZone -> DropZoneType.CENTER_FOLDER
            centerX in tileZones.rightSwapZone -> DropZoneType.RIGHT_SWAP
            else -> DropZoneType.NONE
        }

        logger.info(STATE_TAG, "Zone type: $zoneType on tile $targetTile")

        when (zoneType) {
            DropZoneType.LEFT_SWAP, DropZoneType.RIGHT_SWAP -> {
                if (targetTile != currentDraggedTileIndex) {
                    logger.info(STATE_TAG, "Swap tiles: $currentDraggedTileIndex ↔ $targetTile")
                    dragHelper.dragShadow("reorder", currentDraggedTileIndex, targetTile)
                    this.currentDraggedTileIndex = targetTile
                }
            }

            DropZoneType.CENTER_FOLDER -> {
                logger.info(STATE_TAG, "Would create folder with tiles $currentDraggedTileIndex + $targetTile")

            }

            DropZoneType.NONE -> {

            }
        }
}

    private fun getTilesInRow(rowIndex: Int, tileBoundsMap: TileBoundsMap, gridRowPerception: GridRowPerception?): List<Int> {
        val tilesInRow = tileBoundsMap.filter { (_, bounds) ->
            gridRowPerception?.getRowIndexForY(bounds.centerY) == rowIndex
        }.keys.toMutableList()

        tilesInRow.sortBy { tileIndex ->
            tileBoundsMap[tileIndex]?.centerX ?: 0f
        }

        return tilesInRow
    }


}

enum class DropZoneType {
    LEFT_SWAP,
    CENTER_FOLDER,
    RIGHT_SWAP,
    NONE
}



