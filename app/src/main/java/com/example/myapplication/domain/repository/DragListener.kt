package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.DragIndexState
import com.example.myapplication.domain.model.GridRowPerception
import com.example.myapplication.domain.model.TileBounds
import com.example.myapplication.domain.model.TileBoundsMap

interface DragListener {
    fun onDragStart()
    fun onDrag(x: Float, y: Float, listBound: TileBoundsMap?, indexTileBeingDragged: DragIndexState, offsetFromCenter: Pair<Float, Float>)
    fun onDragEnded()
    fun initializeListPerception(gridRowPerception: GridRowPerception?)
}