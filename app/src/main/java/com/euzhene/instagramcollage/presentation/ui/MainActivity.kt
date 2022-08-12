package com.euzhene.instagramcollage.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.euzhene.instagramcollage.presentation.viewmodel.MainViewModel
import com.euzhene.instagramcollage.ui.theme.InstagramCollageTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val selectedImagesIndexes = mutableStateListOf<Int>()

    private var canShowGallery by mutableStateOf(false)
    private var shouldShowExitAlertDialog by mutableStateOf(false)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) canShowGallery = true
        }

    private var confirmSheetState by mutableStateOf(true)

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadCollage()

        setContent {
            if (viewModel.shouldFinishApp.value) {
                finish()
            }

            val sheetState = rememberModalBottomSheetState(
                ModalBottomSheetValue.Hidden,
                confirmStateChange = { confirmSheetState })
            val coroutineScope = rememberCoroutineScope()
            InstagramCollageTheme {
                ModalBottomSheetLayout(sheetContent = {
                    if (selectedImagesIndexes.isEmpty()) {

                        GalleryImportMedia(sheetState.isVisible, onImageSelect = {
                            viewModel.paths.add(0, it)
                        }, onImageDeselect = {
                            viewModel.paths.remove(it)
                        })
                    } else {
                        GalleryReplaceMedia(sheetState.isVisible,
                            maxCountOfSelectedImages = selectedImagesIndexes.size,
                            onImageReplace = { path: String, order: Int ->
                                viewModel.paths[selectedImagesIndexes[order]] = path
                            },
                            onImageUndoReplace = { order: Int ->
                                viewModel.paths[selectedImagesIndexes[order]] = null
                            }, onReplaceMedia = {
                                confirmSheetState = true
                                hideBottomSheet(sheetState, coroutineScope)
                                selectedImagesIndexes.clear()
                            })
                    }


                }, sheetState = sheetState, scrimColor = Color.Transparent) {

                    Scaffold(bottomBar = {

                        BottomBar(
                            onImageAdd = {
                                if (canShowGallery) {
                                    showBottomSheet(sheetState, coroutineScope)
                                }
                            },
                            onImageDelete = { deleteSelectedImages() },
                            imageSelected = selectedImagesIndexes.isNotEmpty(),
                            onImageReplace = {
                                if (canShowGallery) {
                                    showBottomSheet(sheetState, coroutineScope)
                                    confirmSheetState = false
                                }
                            }
                        )

                    }) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = it.calculateStartPadding(LayoutDirection.Ltr) + 4.dp,
                                    end = it.calculateEndPadding(LayoutDirection.Ltr) + 4.dp,
                                    bottom = it.calculateBottomPadding() + 4.dp,
                                    top = it.calculateTopPadding() + 4.dp,
                                )
                        ) {
                            Collage(
                                images = viewModel.paths,
                                onAddRow = {
                                    viewModel.paths.addAll(listOf(null, null, null))
                                },
                                onImageSelected = {
                                    selectedImagesIndexes.add(it)
                                },
                                onImageDeselected = {
                                    selectedImagesIndexes.remove(it)
                                },
                                selectedImages = selectedImagesIndexes,
                            )

                        }

                    }
                }
            }
            BackHandler {
                if (viewModel.hasChanges()) {
                    shouldShowExitAlertDialog = true
                } else {
                    viewModel.finishApp()
                }
            }

            if (shouldShowExitAlertDialog) {
                ExitAlertDialog(onSave = {
                    viewModel.saveCollageAndExit()
                    Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
                }, onExit = {
                    viewModel.finishApp()
                }, onDismiss = { shouldShowExitAlertDialog = false })
            }

        }
        checkGalleryPermission()


    }

    private fun checkGalleryPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            canShowGallery = true
        }

    }

    private fun deleteSelectedImages() {
        for (i in viewModel.paths.indices.reversed()) {
            val index = selectedImagesIndexes.find { it == i }
            if (index != null) {
                viewModel.paths.removeAt(index)
            }
        }
        selectedImagesIndexes.clear()

    }
}

@OptIn(ExperimentalMaterialApi::class)
fun showBottomSheet(sheetState: ModalBottomSheetState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        sheetState.show()
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun hideBottomSheet(sheetState: ModalBottomSheetState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        sheetState.hide()
    }
}