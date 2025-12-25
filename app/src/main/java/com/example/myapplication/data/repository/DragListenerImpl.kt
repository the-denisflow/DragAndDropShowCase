package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.DragIndexState
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileBoundsMap
import com.example.myapplication.domain.model.TileDropZones
import com.example.myapplication.domain.model.getTileDropZones
import com.example.myapplication.domain.model.getTilesInRow
import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.DragListener
import com.example.myapplication.domain.repository.DropZoneDetectorHelper
import com.example.myapplication.presentation.utils.ListValues
import com.example.myapplication.shared.utils.AppLogger
import javax.inject.Inject

class DragListenerImpl @Inject constructor(
    val logger: AppLogger,
    val dragHelper: DragHelper,
    val dragZoneDetectorHelper: DropZoneDetectorHelper
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

           val perception = gridRowPerception
           val draggedIndex = currentDraggedTileIndex
           val currentRow = perception?.getRowIndexForY(centerY)

           if (currentRow != null && draggedIndex != null) {
               dragZoneDetectorHelper.handleDropZoneDetectionInRow(
                   centerX,
                   currentRow,
                   draggedIndex,
                   perception,
                   dropTileZones,
                   listTilesBounds,
                   dragHelper
               ).takeIf { result -> result is DropResult.Success }?.let { result ->
                   currentDraggedTileIndex = (result as DropResult.Success).index
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
}




