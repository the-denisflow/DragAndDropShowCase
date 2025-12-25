package com.example.myapplication.domain.repository

import com.example.myapplication.data.repository.DropResult
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBoundsMap
import com.example.myapplication.domain.model.TileDropZones


interface DropZoneDetectorHelper {
    fun handleDropZoneDetectionInRow(
        centerX: Float,
        rowIndex: Int,
        currentDraggedTileIndex: Int,
    ): DropResult

    fun initialize(
        gridRowPerception: GridRowPerception,
        zones: List<TileDropZones>,
        bounds: TileBoundsMap
    )
}