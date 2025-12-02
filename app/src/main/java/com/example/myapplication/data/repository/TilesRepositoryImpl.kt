package com.example.myapplication.data.repository

import com.example.myapplication.data.dto.TileData
import com.example.myapplication.domain.repository.TilesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TilesRepositoryImpl: TilesRepository {
    override fun fetchTiles(): Flow<List<TileData>> {
     return flow {
         listOf(
             TileData("1", "Item 1", 0),
             TileData("2", "Item 2", 1),
             TileData("3", "Item 3", 2),
             TileData("4", "Item 4", 3),
             TileData("5", "Item 5", 4),
         )
     }
    }

    override fun updateTiles() {
    }
}