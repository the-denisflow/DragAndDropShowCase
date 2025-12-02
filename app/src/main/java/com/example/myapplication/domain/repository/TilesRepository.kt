package com.example.myapplication.domain.repository

import com.example.myapplication.data.dto.TileData
import kotlinx.coroutines.flow.Flow

interface TilesRepository {
    fun fetchTiles(): Flow<List<TileData>>
    fun updateTiles()
}