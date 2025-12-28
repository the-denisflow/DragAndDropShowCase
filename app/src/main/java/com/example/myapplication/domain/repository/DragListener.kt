package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.DragIndexState
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileIndex

interface DragListener {
    fun onDragStart(listBound: Map<TileIndex, TileBounds>, gridRowPerception: GridRowPerception)
    fun onDrag(x: Float, y: Float, indexTileBeingDragged: DragIndexState, offsetFromCenter: Pair<Float, Float>)
    fun onDragEnded()
}