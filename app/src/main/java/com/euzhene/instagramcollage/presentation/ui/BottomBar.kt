package com.euzhene.instagramcollage.presentation.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.euzhene.instagramcollage.R

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onImageAdd: () -> Unit,
    imageSelected: Boolean,
    onImageDelete: () -> Unit,
    onImageReplace: () -> Unit
) {
    Column(modifier = modifier) {
        Divider(
            Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.2f)
        )
        val size = 70.dp
        BottomAppBar(
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier.height(size)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (imageSelected) {
                    OptionsForSelectedImage(
                        onImageDelete = onImageDelete,
                        onImageReplace = onImageReplace
                    )
                } else {
                    AddRow(onClick = onImageAdd)
                }

            }
        }
    }

}

@Composable
fun OptionsForSelectedImage(onImageDelete: () -> Unit, onImageReplace: () -> Unit) {
    Option(
        onClick = onImageReplace,
        text = LocalContext.current.getString(R.string.replace),
        painterId = R.drawable.ic_replace,
        description = LocalContext.current.getString(R.string.replace_image_from_collage)
    )

    Option(
        onClick = onImageDelete,
        text = LocalContext.current.getString(R.string.delete),
        painterId = R.drawable.ic_trash,
        description = LocalContext.current.getString(R.string.delete_image_from_collage)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Option(
    onClick: () -> Unit,
    text: String,
    @DrawableRes painterId: Int,
    description: String
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = painterId),
                contentDescription = description,
                modifier = Modifier.fillMaxHeight(0.45f),
                contentScale = ContentScale.FillBounds

            )
            Text(text = text, color = Color.Black, fontSize = 14.sp)
        }
    }
}


@Composable
fun AddRow(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(7.dp),
        backgroundColor = MaterialTheme.colors.onBackground,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxHeight(0.8f)

    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = LocalContext.current.getString(R.string.add_row),
            tint = MaterialTheme.colors.background
        )
    }
}