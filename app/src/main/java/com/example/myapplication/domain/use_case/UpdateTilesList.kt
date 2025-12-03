package com.example.myapplication.domain.use_case

import com.example.myapplication.data.dto.toTileStateData
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.domain.repository.TilesRepository
import com.example.myapplication.shared.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateTilesList @Inject constructor(
    private val tilesRepository: TilesRepository,
    private val logger: AppLogger
) {
    operator fun invoke(): Flow<List<TileStateData>> {
        logger.info("UpdateTilesList", "invoke called")
        return tilesRepository.fetchTiles().map { tilesList ->
            tilesList.map { it.toTileStateData() }.also {
                logger.info("UpdateTilesList", "invoke mapped list + " +
                        "$it")
            }
        }
    }
}