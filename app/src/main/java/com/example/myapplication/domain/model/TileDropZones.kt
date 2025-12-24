package com.example.myapplication.domain.model

data class TileDropZones(
    val tileIndex: Int,
    val leftSwapZone: ClosedFloatingPointRange<Float>,
    val centerFolderZone: ClosedFloatingPointRange<Float>,
    val rightSwapZone: ClosedFloatingPointRange<Float>
)