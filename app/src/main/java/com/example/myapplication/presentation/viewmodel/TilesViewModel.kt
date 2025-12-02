package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.domain.use_case.UpdateTilesList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TilesViewModel @Inject constructor(
    private val updateTilesList: UpdateTilesList
) : ViewModel() {
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
