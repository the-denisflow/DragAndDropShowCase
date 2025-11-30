package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.model.TileStateData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TilesViewModel @Inject constructor() : ViewModel() {
    private val _tiles = MutableStateFlow(
        listOf(
            TileStateData("1", "Item 1"),
            TileStateData("2", "Item 2"),
            TileStateData("3", "Item 3"),
            TileStateData("4", "Item 4"),
            TileStateData("5", "Item 5"),
        )
    )
    val tiles = _tiles.asStateFlow()
}
