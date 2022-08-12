package com.euzhene.instagramcollage.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.euzhene.instagramcollage.R
import com.euzhene.instagramcollage.utils.clickWithoutRipple
import com.euzhene.instagramcollage.utils.getAllImagesPaths
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GalleryReplaceMedia(
    shouldShowImages: Boolean,
    maxCountOfSelectedImages: Int,
    onImageReplace: (String, Int) -> Unit,
    onImageUndoReplace: (Int) -> Unit,
    onReplaceMedia: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .heightIn(min = 300.dp),
    ) {
        val paths = remember { mutableStateListOf<String>() }
        val listOfSelectedImages = remember { mutableStateListOf<Boolean>() }
        val listOfOrderOfSelectedImages = remember { mutableStateListOf<Int>() }

        var curCountOfSelectedImages by remember { mutableStateOf(0) }

        val context = LocalContext.current
        LaunchedEffect(key1 = shouldShowImages) {
            if (shouldShowImages) {
                CoroutineScope(Dispatchers.IO).launch {
                    paths.clear()
                    listOfSelectedImages.clear()
                    listOfOrderOfSelectedImages.clear()

                    paths.addAll(getAllImagesPaths(context))
                    listOfSelectedImages.addAll(BooleanArray(paths.size).toList())
                    listOfOrderOfSelectedImages.addAll(IntArray(paths.size).toList())

                    curCountOfSelectedImages = 0
                }

            }
        }
        Column {
            Text(
                text = LocalContext.current.getString(R.string.replace_media),
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(contentAlignment = Alignment.BottomCenter) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    items(paths.size) { i ->

                        GalleryImage(
                            selected = listOfSelectedImages[i],
                            path = paths[i],
                            onImageSelect = {
                                if (curCountOfSelectedImages == maxCountOfSelectedImages) return@GalleryImage

                                onImageReplace(paths[i], curCountOfSelectedImages)

                                listOfOrderOfSelectedImages[i] = ++curCountOfSelectedImages

                                listOfSelectedImages[i] = true
                            },
                            onImageDeselect = {
                                listOfSelectedImages[i] = false
                                curCountOfSelectedImages--
                                onImageUndoReplace(listOfOrderOfSelectedImages[i] - 1)
                                val list = listOfOrderOfSelectedImages.toList()
                                list.forEachIndexed { index, order ->
                                    if (list[i] < order) {
                                        listOfOrderOfSelectedImages[index]--
                                    }
                                }
                            },
                            markContentWhenSelected = {
                                RoundedDecorNumberBox(
                                    checked = listOfSelectedImages[i],
                                    number = listOfOrderOfSelectedImages[i]
                                )
                            })

                    }
                }
                ReplaceMediaButton(
                    onClick = onReplaceMedia,
                    from = curCountOfSelectedImages,
                    to = maxCountOfSelectedImages
                )
            }

        }


    }
}

@Composable
fun GalleryImportMedia(
    shouldShowImages: Boolean,
    onImageSelect: (String) -> Unit,
    onImageDeselect: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .heightIn(min = 300.dp),
    ) {
        val paths = remember { mutableStateListOf<String>() }
        val listOfSelectedImages = remember { mutableStateListOf<Boolean>() }

        val context = LocalContext.current
        LaunchedEffect(key1 = shouldShowImages) {
            if (shouldShowImages) {
                CoroutineScope(Dispatchers.IO).launch {
                    paths.clear()
                    listOfSelectedImages.clear()

                    paths.addAll(getAllImagesPaths(context))
                    listOfSelectedImages.addAll(BooleanArray(paths.size).toList())
                }

            }
        }
        Column {
            Text(
                text = LocalContext.current.getString(R.string.import_media),
                fontSize = MaterialTheme.typography.h5.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(contentAlignment = Alignment.BottomCenter) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                ) {
                    items(paths.size) { i ->
                        GalleryImage(
                            selected = listOfSelectedImages[i],
                            path = paths[i],
                            onImageSelect = {
                                onImageSelect(paths[i])
                                listOfSelectedImages[i] = true
                            },
                            onImageDeselect = {
                                onImageDeselect(paths[i])
                                listOfSelectedImages[i] = false
                            },
                            markContentWhenSelected = {
                                RoundedDecorCheckBox(checked = listOfSelectedImages[i])
                            })
                    }

                }
            }
        }

    }


}


@Composable
fun GalleryImage(
    selected: Boolean,
    path: String,
    onImageSelect: () -> Unit,
    onImageDeselect: () -> Unit,
    markContentWhenSelected: @Composable () -> Unit,
) {
    val borderStroke =
        if (selected) BorderStroke(12.dp, Color.LightGray) else BorderStroke(
            0.dp,
            Color.Transparent
        )
    val padding = if (selected) PaddingValues(12.dp) else PaddingValues(0.dp)
    Box(
        Modifier
            .clickWithoutRipple {
                if (!selected) onImageSelect()
                else onImageDeselect()
            }
            .size(LocalConfiguration.current.screenWidthDp.dp/4)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(path)
                .error(R.drawable.ic_trash)
                .placeholder(R.drawable.insagram)
                .build(),
            contentDescription = LocalContext.current.getString(R.string.image_from_gallery),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .border(borderStroke)
                .padding(padding),

            )
        markContentWhenSelected()
    }

}

@Composable
fun RoundedDecorCheckBox(checked: Boolean, modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape,
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color.White),
        modifier = modifier.padding(4.dp),
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier
                .size(20.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Black,
                uncheckedColor = Color.Transparent
            )
        )
    }
}

@Composable
fun RoundedDecorNumberBox(checked: Boolean, number: Int, modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape,
        color = if (checked) Color.Black else Color.Transparent,
        border = BorderStroke(1.dp, Color.White),
        modifier = modifier
            .padding(4.dp)
            .size(20.dp),
    ) {
        if (checked) {
            Text(
                text = number.toString(),
                modifier = Modifier.fillMaxSize(),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun ReplaceMediaButton(onClick: () -> Unit, from: Int, to: Int) {
    Button(
        onClick = onClick, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("${LocalContext.current.getString(R.string.replace_media)} $from/$to")
    }
}