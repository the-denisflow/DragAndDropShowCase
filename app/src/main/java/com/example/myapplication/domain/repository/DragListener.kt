package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.DragIndexState
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileBoundsMap

interface DragListener {
    fun onDragStart(listBound: TileBoundsMap, gridRowPerception: GridRowPerception)
    fun onDrag(x: Float, y: Float, indexTileBeingDragged: DragIndexState, offsetFromCenter: Pair<Float, Float>)
    fun onDragEnded()
}