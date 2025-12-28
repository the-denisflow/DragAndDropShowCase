package com.example.myapplication.data.dto

import com.example.myapplication.domain.model.TileStateData

data class TileData (
    val id: String,
    val name: String,
    val packageName: String,
    val resourceId: Int,
    val position: Int
)

fun TileData.toTileStateData(): TileStateData = TileStateData(
    id = id,
    label = name,
    resourceId = resourceId
)