package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.TileStateData
import com.example.myapplication.domain.use_case.UpdateTilesList
import com.example.myapplication.shared.utils.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TilesViewModel @Inject constructor(
    private val updateTilesList: UpdateTilesList,
    private val logger: AppLogger
) : ViewModel() {
    private val TAG = "TilesViewModel"
    private val _tiles = MutableStateFlow(
     emptyList<TileStateData>()
    )
    val tiles = _tiles.asStateFlow()

    init {
        updateTilesListJob()
    }

    private fun updateTilesListJob() = viewModelScope.launch {
        logger.info(TAG, "updateTilesListJob called")
        updateTilesList().collect {
            _tiles.value = it
        }
    }
}
