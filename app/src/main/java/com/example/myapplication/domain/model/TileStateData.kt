package com.example.myapplication.domain.model

data class TileStateData(
    val id: String,
    val label: String,
    val isDragging: Boolean = false
)