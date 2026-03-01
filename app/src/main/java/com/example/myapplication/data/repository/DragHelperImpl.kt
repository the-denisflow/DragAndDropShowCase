package com.example.myapplication.data.repository

import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.TilesRepository
import javax.inject.Inject

class DragHelperImpl @Inject constructor(
    val tilesRepository: TilesRepository
): DragHelper {
    override fun reorderItems(from: Int, to: Int) {
        tilesRepository.reorderTiles(from, to)
    }
}