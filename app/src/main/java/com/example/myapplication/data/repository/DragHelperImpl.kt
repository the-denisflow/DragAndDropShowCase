package com.example.myapplication.data.repository

import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.TilesRepository
import javax.inject.Inject

class DragHelperImpl @Inject constructor(
    val tilesRepository: TilesRepository
): DragHelper {
    override fun dragShadow(operation: String, from: Int, to: Int) {
        if(operation == "reorder") {
            tilesRepository.reorderTiles(from, to)
        }
    }
}