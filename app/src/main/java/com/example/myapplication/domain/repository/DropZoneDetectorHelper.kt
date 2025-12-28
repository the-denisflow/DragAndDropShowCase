package com.example.myapplication.domain.repository

import com.example.myapplication.data.repository.DropResult
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileDropZones
import com.example.myapplication.domain.model.TileIndex


interface DropZoneDetectorHelper {
    fun handleDropZoneDetectionInRow(
        centerX: Float,
        rowIndex: Int,
        currentDraggedTileIndex: Int,
    ): DropResult

    fun initialize(
        gridRowPerception: GridRowPerception,
        zones: List<TileDropZones>,
        bounds: Map<TileIndex, TileBounds>
    )
}