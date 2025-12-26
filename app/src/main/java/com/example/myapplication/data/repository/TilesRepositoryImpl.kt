package com.example.myapplication.data.repository

import com.example.myapplication.data.dto.TileData
import com.example.myapplication.domain.repository.TilesRepository
import com.example.myapplication.shared.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TilesRepositoryImpl @Inject constructor(
    private val logger: AppLogger
) : TilesRepository {
    private val SATE_TAG = "TilesRepositoryImpl"
    private val _tiles = MutableStateFlow<List<TileData>>(
        listOf(
            TileData("1", "Item 1", 0),
            TileData("2", "Item 2", 1),
            TileData("3", "Item 3", 2),
            TileData("4", "Item 4", 3),
            TileData("5", "Item 5", 4),
            TileData("6", "Item 6", 5),
            TileData("7", "Item 7", 6),
            TileData("8", "Item 8", 7),
            TileData("9", "Item 9", 8),
        )
    )

    override fun fetchTiles(): Flow<List<TileData>> {
        logger.info(SATE_TAG, "fetchTiles called")
        return _tiles.asStateFlow()
    }

    override fun reorderTiles(from: Int, to: Int) {
        logger.info(SATE_TAG, "reorderTiles called from $from to $to")
        reorder(from, to)
    }

    fun reorder(fromPosition: Int, toPosition: Int) {
        val currentTiles = _tiles.value.toMutableList()
        if (fromPosition < currentTiles.size && toPosition < currentTiles.size) {
            val tile = currentTiles.removeAt(fromPosition)
            currentTiles.add(toPosition, tile)
            currentTiles.forEachIndexed { index, tile ->
                currentTiles[index] = tile.copy(position = index)
            }

            _tiles.value = currentTiles
            logger.info(SATE_TAG, "Reordered from $fromPosition to $toPosition")
        }
    }
}