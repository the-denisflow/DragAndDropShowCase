package com.example.myapplication.domain.model

sealed class DragIndexState {
    data class Dragging(val index: Int): DragIndexState()
    data object NotDragging: DragIndexState()
}