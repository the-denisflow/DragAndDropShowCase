package com.example.myapplication.data.repository

import com.example.myapplication.domain.model.DragIndexState
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileDropZones
import com.example.myapplication.domain.model.TileIndex
import com.example.myapplication.domain.model.getTileDropZones
import com.example.myapplication.domain.repository.DragListener
import com.example.myapplication.domain.repository.DropZoneDetectorHelper
import com.example.myapplication.shared.utils.AppLogger
import javax.inject.Inject

class DragListenerImpl @Inject constructor(
    val logger: AppLogger,
    val dragZoneDetectorHelper: DropZoneDetectorHelper
): DragListener {
    companion object {
        private val STATE_TAG = "DragListenerImpl"
    }

    private var listTilesBounds: Map<TileIndex, TileBounds>? = null
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

    override fun onDragStart(listBound: Map<TileIndex, TileBounds>,
                             gridRowPerception: GridRowPerception) {
        this.gridRowPerception = gridRowPerception
        listTilesBounds = listBound
        currentDraggedTileIndex = null
        dragZoneDetectorHelper.initialize(
            gridRowPerception
            ,dropTileZones!!,
            listTilesBounds!!)
    }

    override fun onDrag(
        x: Float,
        y: Float,
        indexTileBeingDragged: DragIndexState,
        offsetFromCenter: Pair<Float, Float>
    ) {
       if (indexTileBeingDragged is DragIndexState.Dragging) {
           logger.info(STATE_TAG, "Dragging at x: $x, y: $y")

        if (currentDraggedTileIndex == null) {
            currentDraggedTileIndex = indexTileBeingDragged.index
        }

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
               ).let { result ->
                   when(result){
                       is DropResult.Empty -> logger.info(STATE_TAG, "dragZoneDetectorHelper empty")
                       is DropResult.Failure -> logger.warning(STATE_TAG, "dragZoneDetectorHelper failure")
                       is DropResult.Success -> {
                           currentDraggedTileIndex = result.index
                           logger.info(STATE_TAG, "dragZoneDetectorHelper success")
                       }
                   }
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
}




