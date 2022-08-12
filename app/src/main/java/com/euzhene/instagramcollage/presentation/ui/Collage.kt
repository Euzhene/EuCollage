package com.euzhene.instagramcollage.presentation.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.euzhene.instagramcollage.R
import com.euzhene.instagramcollage.utils.clickWithoutRipple
import org.burnoutcrew.reorderable.*

@Composable
fun Collage(
    images: MutableList<String?>,
    onAddRow: () -> Unit,
    onImageSelected: (Int) -> Unit,
    onImageDeselected: (Int) -> Unit,
    selectedImages: List<Int>,
) {
    Column {
        CollageFunctions(onAddRow = onAddRow)
        Spacer(modifier = Modifier.height(8.dp))
        CollageGrid(
            images = images,
            onImageSelected = onImageSelected,
            onImageDeselected = onImageDeselected,
            selectedImages = selectedImages
        )
    }

}

@Composable
fun CollageFunctions(onAddRow: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onAddRow,
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.LightGray
            )
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = LocalContext.current.getString(R.string.add_row)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = LocalContext.current.getString(R.string.add_row),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollageGrid(
    images: MutableList<String?>,
    onImageSelected: (Int) -> Unit,
    onImageDeselected: (Int) -> Unit,
    selectedImages: List<Int>
) {

    val state =
        rememberReorderableLazyGridState(onMove = { from: ItemPosition, to: ItemPosition ->
            images.add(to.index, images.removeAt(from.index))
        })

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .reorderable(state),
        state = state.gridState
    ) {
        items(images.size, { it }) { i: Int ->
            val imageSelected = selectedImages.find { it == i } != null

            val borderStroke = if (imageSelected) BorderStroke(
                3.dp,
                MaterialTheme.colors.onBackground
            ) else BorderStroke(0.dp, Color.Transparent)

            ReorderableItem(
                state = state,
                key = i,
                index = i,
                orientationLocked = false,
                defaultDraggingModifier = Modifier.animateItemPlacement()
            ) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 0.dp else 0.dp)
                Box(
                    modifier = Modifier
                        .clickWithoutRipple {
                            if (imageSelected) onImageDeselected(i)
                            else onImageSelected(i)
                        }
                        .detectReorderAfterLongPress(state)
                        .background(Color.LightGray.copy(0.4f))
                        .shadow(elevation.value)
                        .border(borderStroke)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(images[i])
                            .crossfade(true)
                            .build(),
                        contentDescription = LocalContext.current.getString(R.string.image_in_collage),
                        modifier = Modifier
                            .size(LocalConfiguration.current.screenWidthDp.dp/3),
                        contentScale = ContentScale.Crop
                    )

                    if (imageSelected) {
                        RoundedDecorCheckBox(
                            checked = imageSelected, modifier = Modifier.align(
                                Alignment.BottomEnd
                            )
                        )
                    }

                }
            }

        }
    }
}