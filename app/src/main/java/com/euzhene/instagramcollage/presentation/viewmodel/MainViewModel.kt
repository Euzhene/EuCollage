package com.euzhene.instagramcollage.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.euzhene.instagramcollage.domain.usecases.LoadPathsOfImagesUseCase
import com.euzhene.instagramcollage.domain.usecases.SavePathsOfImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadPathsOfImagesUseCase: LoadPathsOfImagesUseCase,
    private val savePathsOfImagesUseCase: SavePathsOfImagesUseCase,
) : ViewModel() {
    private var pathsInDB = emptyList<String?>()
    var paths = mutableStateListOf<String?>()

    private var _shouldFinishApp = mutableStateOf(false)
    val shouldFinishApp: State<Boolean> = _shouldFinishApp

    fun saveCollageAndExit() {
        viewModelScope.launch(Dispatchers.IO) {
            savePathsOfImagesUseCase(paths)
            finishApp()
        }
    }

    fun hasChanges(): Boolean {
        return pathsInDB != paths
    }

    fun finishApp() {
        _shouldFinishApp.value = true
    }

    fun loadCollage() {
        if (paths.isNotEmpty()) return

        viewModelScope.launch(Dispatchers.IO) {

            loadPathsOfImagesUseCase().collect {
                paths.addAll(it)

                pathsInDB = it
            }
        }
    }
}