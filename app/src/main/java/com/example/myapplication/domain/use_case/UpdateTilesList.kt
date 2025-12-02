package com.example.myapplication.domain.use_case

import com.example.myapplication.data.dto.TileData
import com.example.myapplication.domain.repository.TilesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateTilesList @Inject constructor(
    private val tilesRepository: TilesRepository
) {
    operator fun invoke(): Flow<List<TileData>> = tilesRepository.fetchTiles()
}