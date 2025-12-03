package com.example.myapplication.data.repository

import com.example.myapplication.domain.repository.DragHelper
import com.example.myapplication.domain.repository.TilesRepository
import javax.inject.Inject

class DragHelperImpl @Inject constructor(
    val tilesRepository: TilesRepository
): DragHelper {
    override fun dragShadow(x: Float, y: Float) {
        if (x < 1350) {
         tilesRepository.updateTiles()
        }
    }
}