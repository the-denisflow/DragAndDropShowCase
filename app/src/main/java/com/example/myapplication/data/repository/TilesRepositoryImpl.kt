package com.example.myapplication.data.repository

import com.example.myapplication.data.dto.TileData
import com.example.myapplication.domain.repository.TilesRepository
import com.example.myapplication.shared.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import com.example.myapplication.R


@Singleton
class TilesRepositoryImpl @Inject constructor(
    private val logger: AppLogger
) : TilesRepository {
    private val SATE_TAG = "TilesRepositoryImpl"
    private val _tiles = MutableStateFlow<List<TileData>>(
        listOf(
            TileData(
                id = "1",
                name = "Chrome",
                packageName = "",
                resourceId = R.drawable.img,
                position = 0),
            TileData(
                id = "2",
                name = "NixOs",
                packageName = "",
                resourceId = R.drawable.nixos,
                position = 1
            ),
            TileData(
                id = "3",
                name = "BG 3",
                packageName = "",
                resourceId = R.drawable.baldursgate3,
                position = 2
            ),
            TileData(
                id = "4",
                name =  "Eudic",
                packageName = "",
                resourceId = R.drawable.eudic,
                position = 3
            ),
            TileData(
                id = "5",
                name = "Orion",
                packageName = "",
                resourceId = R.drawable.orion,
                position = 4
            ),
            TileData(
                id = "6",
                name = "Item 6",
                packageName = "",
                resourceId = R.drawable.img,
                position = 5
            ),
            TileData(
                id = "7",
                name =  "R D R 2",
                packageName = "",
                resourceId = R.drawable.reddeadredemption,
                position = 6
            ),
            TileData(
               id = "8",
                name = "Steam",
                packageName = "",
                resourceId = R.drawable.steam,
                position = 7),
            TileData(
                id = "9",
                name = "Table Plus",
                packageName = "",
                resourceId = R.drawable.tableplus,
                position = 8),
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